package com.example.demo.task1;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {

	@Bean
	public DataSource dataSource() {
		HikariDataSource dataSource = new HikariDataSource();
		dataSource.setJdbcUrl("jdbc:oracle:thin:@localhost:1521:orcl");
		dataSource.setUsername("your_username");
		dataSource.setPassword("your_password");
		dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
		dataSource.setMaximumPoolSize(20);
		return dataSource;
	}

}
