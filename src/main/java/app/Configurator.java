package app;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@PropertySource(name = "appProperties", value = "classpath:/config/default.properties")
public class Configurator {
    private static final Logger LOGGER = LoggerFactory.getLogger(Configurator.class);

//    @Value("db.driverclass")
    private String driverClass;
    
    @Value("${db.jdbcurl}")
    private String jdbcUrl;
    
    @Value("${db.user}")
    private String user;
    
    @Value("${db.password}")
    private String password;
    
    @Value("${db.hibernate.show_sql}")
    private String hibernateShowSql;
    
    @Value("${db.hibernate.dialect}")
    private String hibernateDialect;
    
    @PostConstruct
    public void init() {
        LOGGER.info("Initialized Configurator; user = " + user);
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
        ds.setDriverClass("com.mysql.jdbc.Driver");
        ds.setJdbcUrl(jdbcUrl);
        ds.setUser(user);
        ds.setPassword(password);
        
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
    
    
}
