package org.adiaz.springcloud.msvc.auth;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @LoadBalanced //A.Guzman: "con esto configuramos balanceo de carga entre servidor de autorizacion y clientes usando webclient en vez de feign, similar a como balanceamos la carga entre msvc-usuarios y msvc-cursos usando feign "
    @Bean
    WebClient webClient() {
        return WebClient.builder().build();
    }
}
