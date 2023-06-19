package com.nsdl.beckn.lm.audit.config;

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
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
@EnableJpaRepositories(basePackages = "com.nsdl.beckn.lm.audit.db.dehliarchaltmp", entityManagerFactoryRef = "apiAuditArchvlTmpDehliEntityManagerFactory", transactionManagerRef = "apiAuditArchvlTmpDehliTransactionManager")
public class ApiAuditArchalTmpDehliDataSourceConfiguration {

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
	@ConfigurationProperties("spring.datasource.api-audit-archvl-tmp-dehli")
	public DataSourceProperties apiAuditArchvlTmpDehliDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean
	@ConfigurationProperties("spring.datasource.api-audit-archvl-tmp-dehli.configuration")
	public DataSource apiAuditArchvlTmpDehliDataSource() {
		return apiAuditArchvlTmpDehliDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
	}

	@Bean(name = "apiAuditArchvlTmpDehliEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean apiAuditArchvlTmpDehliEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		Map<String, Object> properties = new HashMap<>();
		properties.put("spring.jpa.hibernate.ddl-auto", this.ddlAuto);
		properties.put("spring.jpa.show-sql", this.showSql);
		properties.put("spring.jpa.properties.hibernate.format_sql", this.formatSql);
		properties.put("logging.level.org.hibernate.type", this.logType);
		properties.put("spring.jpa.properties.hibernate.connection.isolation", this.connectIsolation);
		properties.put("hibernate.hbm2ddl.auto", "update");
		return builder.dataSource(apiAuditArchvlTmpDehliDataSource()).packages("com.nsdl.beckn.lm.audit.db.dehliarchaltmp")
				.properties(properties).build();
	}

	@Bean
	public PlatformTransactionManager apiAuditArchvlTmpDehliTransactionManager(
			final @Qualifier("apiAuditArchvlTmpDehliEntityManagerFactory") LocalContainerEntityManagerFactoryBean apiAuditArchvlTmpDehliEntityManagerFactory) {
		return new JpaTransactionManager(apiAuditArchvlTmpDehliEntityManagerFactory.getObject());
	}

}