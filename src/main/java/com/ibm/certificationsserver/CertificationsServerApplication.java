package com.ibm.certificationsserver;

import com.ibm.certificationsserver.util.GenerateExcelUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@SpringBootApplication(exclude=HibernateJpaAutoConfiguration.class)
public class CertificationsServerApplication {
	@Bean
	public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);
		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
		databasePopulator.addScript(new ClassPathResource("schema.sql"));
		dataSourceInitializer.setDatabasePopulator(databasePopulator);
		dataSourceInitializer.setEnabled(true);
		return dataSourceInitializer;
	}

	public static void main(String[] args) {
		GenerateExcelUtils.setCached(false);
		SpringApplication.run(CertificationsServerApplication.class, args);
	}

}
