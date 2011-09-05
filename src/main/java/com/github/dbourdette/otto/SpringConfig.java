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

package com.github.dbourdette.otto;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.github.dbourdette.otto.service.user.User;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

/**
 * @author damien bourdette
 * @version \$Revision$
 */
@Configuration
@EnableWebMvc
public class SpringConfig {

    public static final String DEFAULT_DB_URL = "localhost";

    public static final String DEFAULT_DB_NAME = "otto";

    public static final String DEFAULT_SECURITY_USERNAME = "letme";

    public static final String DEFAULT_SECURITY_PASSWORD = "in";

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
        Mongo mongo = new Mongo(getMongoServerAdresses());

        mongo.slaveOk();

        return mongo;
    }

    @Bean
    public DB mongoDb() throws MongoException, UnknownHostException {
        String username = getMongoUsername();

        DB db = mongo().getDB(getMongoDbName());

        if (StringUtils.isNotEmpty(username)) {
            db.authenticate(username, getMongoPassword().toCharArray());
        }

        return db;
    }

    @Bean
    public Datastore dataStore() throws MongoException, UnknownHostException {
        return morphia().createDatastore(mongo(), getMongoDbName());
    }

    @Bean
    public Morphia morphia() throws MongoException, UnknownHostException {
        Morphia morphia = new Morphia();

        morphia.map(User.class);

        return morphia;
    }

    public List<ServerAddress> getMongoServerAdresses() throws UnknownHostException {
        List<ServerAddress> addresses = new ArrayList<ServerAddress>();

        for (String url : StringUtils.split(getMongoUrl(), ",")) {
            addresses.add(new ServerAddress(url));
        }

        return addresses;
    }

    public String getMongoUrl() {
        return getInitParameter("mongo/url", DEFAULT_DB_URL);
    }

    public String getMongoDbName() {
        return getInitParameter("mongo/dbName", DEFAULT_DB_NAME);
    }

    public String getMongoUsername() {
        return getInitParameter("mongo/username", "");
    }

    public String getMongoPassword() {
        return getInitParameter("mongo/password", "");
    }

    public String getSecurityDefaultUsername() {
        return getInitParameter("security/default.username", DEFAULT_SECURITY_USERNAME);
    }

    public String getSecurityDefaultPassword() {
        return getInitParameter("security/default.password", DEFAULT_SECURITY_PASSWORD);
    }

    public String getSecurityLdapProviderUrl() {
        return getInitParameter("security/ldap.providerUrl", "");
    }

    public String getSecurityLdapUserDn() {
        return getInitParameter("security/ldap.userDn", "");
    }

    public String getSecurityLdapPassword() {
        return getInitParameter("security/ldap.password", "");
    }

    public String getSecurityLdapSearchBase() {
        return getInitParameter("security/ldap.searchBase", "");
    }

    public String getSecurityLdapSearchFilter() {
        return getInitParameter("security/ldap.searchFilter", "");
    }

    public String getInitParameter(String name, String defaultValue) {
        String value = servletContext.getInitParameter(name);

        if (StringUtils.isEmpty(value)) {
            return defaultValue;
        } else {
            return value;
        }
    }

}
