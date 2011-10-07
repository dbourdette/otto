/*
 * Copyright 2011 Damien Bourdette
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.dbourdette.otto.service.mail;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.github.dbourdette.otto.SpringConfig;
import com.google.code.morphia.Datastore;

/**
 * @author damien bourdette
 */
@Service
public class Mailer {
    @Inject
    private Datastore datastore;

    @Inject
    private SpringConfig springConfig;

    public void send(Mail mail) throws MessagingException, UnsupportedEncodingException {
        MailConfiguration configuration = findConfiguration();

        JavaMailSender javaMailSender = mailSender(configuration);

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
        helper.setSubject(mail.getSubject());
        helper.setFrom(configuration.getSender());

        for (String name : StringUtils.split(mail.getTo(), ",")) {
            helper.addTo(name);
        }

        helper.setText(mail.getHtml(), true);

        javaMailSender.send(mimeMessage);
    }

    public void saveConfiguration(MailConfiguration configuration) {
        datastore.delete(datastore.createQuery(MailConfiguration.class));
        datastore.save(configuration);
    }

    public MailConfiguration findConfiguration() {
        MailConfiguration configuration = datastore.find(MailConfiguration.class).limit(1).get();

        if (configuration == null) {
            return new MailConfiguration();
        }

        return configuration;
    }

    private JavaMailSender mailSender(MailConfiguration configuration) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(configuration.getSmtp());

        if (configuration.getPort() != 0) {
            sender.setPort(configuration.getPort());
        }

        if (StringUtils.isNotEmpty(configuration.getUser())) {
            sender.setUsername(configuration.getUser());
            sender.setPassword(configuration.getPassword());

            Properties javaMailProperties = new Properties();
            javaMailProperties.put("mail.smtp.auth", "true");
            sender.setJavaMailProperties(javaMailProperties);
        }

        return sender;
    }
}
