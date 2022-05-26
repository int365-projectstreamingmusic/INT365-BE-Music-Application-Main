package naturegecko.jingjok.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ApplicationCorsConfig implements WebMvcConfigurer {

	@Value("#{'${application.origin.method}'.split(',')}")
	private String[] allowedMethods;
	@Value("#{'${application.origin.host}'.split(',')}")
	private String[] allowedDomain;

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**").allowedOrigins(allowedDomain).allowedMethods(allowedMethods);
	}

}
