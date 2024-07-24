package com.devteria.gateway.configuration;

import com.devteria.gateway.repository.IdentityClient;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfiguration {

    @Bean
    WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080/identity") // chỉ định URL cơ sở mà WebClient sẽ sử dụng cho tất cả các yêu cầu HTTP
                .build();
    }

    // Cấu hình CORS truoc duoc dat trong identity-service nhung gio dua ra api-gateway lam common
    @Bean
    CorsWebFilter corsWebFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }


    // Tạo một client dịch vụ sử dụng WebClient để giao tiếp với dịch vụ bên ngoài, với sự hỗ trợ của HttpServiceProxyFactory.
    @Bean
    IdentityClient identityClient(WebClient webClient) {
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient)).build();

        return httpServiceProxyFactory.createClient(IdentityClient.class); // tạo một proxy client cho IdentityClient
    }
}
