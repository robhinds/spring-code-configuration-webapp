package com.tmm.web.configuration;

import java.beans.PropertyVetoException;

import javax.persistence.EntityManagerFactory;

import org.hibernate.ejb.HibernatePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableTransactionManagement(mode=AdviceMode.ASPECTJ, proxyTargetClass=true)
@ComponentScan("com.tmm.web")
@PropertySource("classpath:META-INF/spring/database.properties")
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class ApplicationContextConfiguration {
	
	@Autowired Environment env;
	
    @Bean
    public PlatformTransactionManager txManager() throws PropertyVetoException {
    	JpaTransactionManager bean = new JpaTransactionManager();
    	bean.setEntityManagerFactory(entityManagerFactory());
        return bean;
    }
	
	@Bean 
	public CachingConfiguration cachingConfiguration(){
		return new CachingConfiguration();
	}

	
	@Bean
	public EntityManagerFactory entityManagerFactory() throws PropertyVetoException {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		emf.setDataSource(dataSource());
		emf.setPackagesToScan("com.tmm.web.domain","com.tmm.web.security");
		emf.setPersistenceProvider(new HibernatePersistence());
		emf.afterPropertiesSet();
		emf.setJpaDialect(new HibernateJpaDialect());
		return emf.getObject();
	}
	

	@Bean
	public ComboPooledDataSource dataSource() throws PropertyVetoException{
		ComboPooledDataSource bean = new ComboPooledDataSource();
		bean.setDriverClass(env.getProperty("database.driverClassName"));
		bean.setJdbcUrl(env.getProperty("database.url"));
		bean.setUser(env.getProperty("database.username"));
		bean.setPassword(env.getProperty("database.password"));
		return bean;
	}
}
