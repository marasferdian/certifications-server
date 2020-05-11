package com.ibm.certificationsserver.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration
@EnableTransactionManagement
@PropertySource("classpath:application.properties")
public class HibernateConfig {

    @Autowired
    private Environment environment;

    @Autowired
    private DataSource dataSource;    // It will automatically read database properties from application.properties and create DataSource object

    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean getSessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan(new String[]{"com.ibm.certificationsserver.persistence"});
        sessionFactory.setHibernateProperties(hibernateProperties());
        sessionFactory.setPackagesToScan("com.ibm.certificationsserver.model");
        return sessionFactory;
    }

    private Properties hibernateProperties() {                  // configure hibernate properties
        Properties properties = new Properties();
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.show_sql", false);
        properties.put("hibernate.format_sql", false);
        properties.put("hibernate.hbm2ddl.auto", "update");
        return properties;
    }

    @Bean(name = "transactionManager")                      // creating transaction manager factory
    public HibernateTransactionManager getTransactionManager(
            SessionFactory sessionFactory) {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(
                sessionFactory);
        return transactionManager;
    }
}
