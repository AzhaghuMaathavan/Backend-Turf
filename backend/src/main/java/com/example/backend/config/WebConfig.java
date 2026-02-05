package com.example.backend.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Deprecated/unused: kept to avoid breaking imports; CORS is configured in {@link CorsConfig}.
 */
public class WebConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		// no-op
	}
}
