package app;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@PropertySource(name = "appProperties", value = "classpath:/config/default.properties")
public class Configurator {
    private static final Logger LOGGER = LoggerFactory.getLogger(Configurator.class);

    @Value("${db.driverclass}")
    private String driverClass;
    
    @Value("${db.jdbcurl}")
    private String jdbcUrl;
    
    @Value("${db.user}")
    private String dbUser;
    
    @Value("${db.password}")
    private String dbPassword;
    
    @Value("${db.hibernate.show_sql}")
    private String hibernateShowSql;
    
    @Value("${db.hibernate.dialect}")
    private String hibernateDialect;
    
    @Value("amq.brokerurl")
    private String amqBrokerUrl;
    
    @Value("amq.user")
    private String amqUser;
    
    @Value("amq.password")
    private String amqPassword;
    
    @PostConstruct
    public void init() {
        LOGGER.info("Initialized Configurator");
    }

    @Profile("dev")
    @Configuration
    public static class DevProperties {
        @PostConstruct
        public void init() {
            LOGGER.info("Initialized devProperties");
        }
    }

    @Profile("docker")
    @Configuration
    public static class DockerProperties {
        @PostConstruct
        public void init() {
            LOGGER.info("Initialized dockerProperties");
        }
    }

    @Bean
    public DataSource dataSource()
            throws PropertyVetoException {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        ds.setDriverClass(driverClass);
        ds.setJdbcUrl(jdbcUrl);
        ds.setUser(dbUser);
        ds.setPassword(dbPassword);
        
        ds.setInitialPoolSize(3);
        ds.setMinPoolSize(6);
        ds.setMaxPoolSize(25);
        ds.setAcquireIncrement(3);
        ds.setMaxStatements(0);
        
        ds.setAcquireRetryAttempts(30);
        ds.setAcquireRetryDelay(1000);
        
        ds.setBreakAfterAcquireFailure(false);
        
        ds.setMaxIdleTime(180);
        
        ds.setMaxConnectionAge(10);
        
        ds.setCheckoutTimeout(5000);
        
        ds.setIdleConnectionTestPeriod(14400);
        
        ds.setTestConnectionOnCheckout(true);
        ds.setPreferredTestQuery("SELECT 1");
        ds.setTestConnectionOnCheckin(true);
        
        return ds;
    }
    
    @Bean
    public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setPackagesToScan("app");
        factoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", "validate");
        jpaProperties.put("hibernate.show_sql", hibernateShowSql);
        jpaProperties.put("hibernate.dialect", hibernateDialect);
        jpaProperties.put("hibernate.format_sql", "false");
        factoryBean.setJpaProperties(jpaProperties);
        
        return (EntityManagerFactory)factoryBean;
    }

    @Bean
    public TransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        
        return (TransactionManager) transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:sql/changelog.xml");
        
        return liquibase;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL(amqBrokerUrl);
        connectionFactory.setUserName(amqUser);
        connectionFactory.setPassword(amqPassword);
        
        return connectionFactory;
    }
    
}
