package com.example.unlight.config;

import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
	@Bean
	public DataSource dataSource() {
		return DataSourceBuilder.create()
				.url("jdbc:mysql://localhost:3306/spring?useSSL=false&serverTimezone=Asia/Taipei&useLegacyDatetimeCode=false&allowPublicKeyRetrieval=true")
				.username("root")
				.password("12345678")
				.build();
	}
}
