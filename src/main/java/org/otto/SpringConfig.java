package org.otto;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.inject.Inject;
import java.net.UnknownHostException;
import java.util.Locale;

@Configuration
@EnableWebMvc
public class SpringConfig {

    @Inject
    private String mongoUrl;

    @Inject
    private String mongoDbName;

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
    public DB mongoDb() throws MongoException, UnknownHostException {
        Mongo mongo = new Mongo(mongoUrl);
        return mongo.getDB(mongoDbName);
    }
}
