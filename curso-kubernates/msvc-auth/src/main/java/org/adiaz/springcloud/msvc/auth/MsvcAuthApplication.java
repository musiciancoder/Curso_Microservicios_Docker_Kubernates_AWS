package org.adiaz.springcloud.msvc.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient //para poder usar spring cloud kubernetes
//para este caso no se ocupó Feign, sino spring reactive web
@SpringBootApplication
public class MsvcAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsvcAuthApplication.class, args);
    }

}
