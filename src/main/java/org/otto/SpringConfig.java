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

package org.otto;

import java.net.UnknownHostException;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Configuration
@EnableWebMvc
public class SpringConfig {

    @Inject
    private ServletContext servletContext;

    @Bean
    public FixedLocaleResolver fixedLocaleResolver() {
        FixedLocaleResolver resolver = new FixedLocaleResolver();
        resolver.setDefaultLocale(Locale.FRANCE);
        return resolver;
    }

    @Bean
    public UrlBasedViewResolver urlBasedViewResolver() {
        UrlBasedViewResolver resolver = new UrlBasedViewResolver();
        resolver.setViewClass(JstlView.class);
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public Mongo mongo() throws MongoException, UnknownHostException {
        String url = servletContext.getInitParameter("mongo/url");

        return new Mongo(url);
    }

    @Bean
    public DB mongoDb() throws MongoException, UnknownHostException {
        String dbName = servletContext.getInitParameter("mongo/dbName");
        String username = servletContext.getInitParameter("mongo/username");
        String password = servletContext.getInitParameter("mongo/password");

        DB db = mongo().getDB(dbName);

        if (org.apache.commons.lang.StringUtils.isNotEmpty(username)) {
            db.authenticate(username, password.toCharArray());
        }

        return db;
    }

    @Bean
    public Datastore dataStore() throws MongoException, UnknownHostException {
        String dbName = servletContext.getInitParameter("mongo/dbName");

        return morphia().createDatastore(mongo(), dbName);
    }

    @Bean
    public Morphia morphia() throws MongoException, UnknownHostException {
        return new Morphia();
    }
}
