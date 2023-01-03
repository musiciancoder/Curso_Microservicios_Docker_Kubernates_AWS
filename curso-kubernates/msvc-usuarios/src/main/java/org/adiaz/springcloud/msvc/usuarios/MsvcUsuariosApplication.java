
package org.adiaz.springcloud.msvc.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient //para poder usar spring cloud kubernetes
@EnableFeignClients //esta anotacion permite conectar microservicios mediante cliente feign
@SpringBootApplication
public class MsvcUsuariosApplication {

	public MsvcUsuariosApplication() {
	}

	public static void main(String[] args) {
		SpringApplication.run(MsvcUsuariosApplication.class, args);
	}

}

