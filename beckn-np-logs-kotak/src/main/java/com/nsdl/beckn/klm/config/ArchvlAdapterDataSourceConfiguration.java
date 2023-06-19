package com.nsdl.beckn.klm.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.nsdl.beckn.klm.dao.archvl", entityManagerFactoryRef = "archvlAdapterEntityManagerFactory", transactionManagerRef = "archvlAdapterTransactionManager")
public class ArchvlAdapterDataSourceConfiguration {
	@Value("${spring.jpa.hibernate.ddl-auto}")
	String ddlAuto;

	@Value("${spring.jpa.show-sql}")
	Boolean showSql;

	@Value("${spring.jpa.properties.hibernate.format_sql}")
	Boolean formatSql;

	@Value("${spring.jpa.properties.hibernate.connection.isolation}")
	Integer connectIsolation;

	@Value("${logging.level.org.hibernate.type}")
	String logType;

	@Bean
//	@Primary
	@ConfigurationProperties("spring.datasource.archvl.adapter")
	public DataSourceProperties archivlAdapterDataSourceProperties() {
		return new DataSourceProperties();
	}
	@Bean
	@ConfigurationProperties("spring.datasource.archvl.adapter.configuration")
	public DataSource archivekotakadapterDataSource() {
		return archivlAdapterDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}
	@Bean(name = "archvlAdapterEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean archvlAdapterEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		Map<String, Object> properties = new HashMap<>();
		properties.put("spring.jpa.hibernate.ddl-auto", this.ddlAuto);
		properties.put("spring.jpa.show-sql", this.showSql);
		properties.put("spring.jpa.properties.hibernate.format_sql", this.formatSql);
		properties.put("logging.level.org.hibernate.type", this.logType);
		properties.put("spring.jpa.properties.hibernate.connection.isolation", this.connectIsolation);

		return builder.dataSource(archivekotakadapterDataSource()).packages("com.nsdl.beckn.klm.model.archvl")
				.properties(properties).build();
	}
	@Bean
	public PlatformTransactionManager archvlAdapterTransactionManager(
			final @Qualifier("archvlAdapterEntityManagerFactory") LocalContainerEntityManagerFactoryBean adapterEntityManagerFactory) {
		return new JpaTransactionManager(adapterEntityManagerFactory.getObject());
	}


}
