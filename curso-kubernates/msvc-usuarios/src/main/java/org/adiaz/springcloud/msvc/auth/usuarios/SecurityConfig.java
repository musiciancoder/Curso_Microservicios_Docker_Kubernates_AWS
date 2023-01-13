package org.adiaz.springcloud.msvc.auth.usuarios;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

//Esta es la clase de configuracion de Spring Security para el cliente. En este caso msvc-usuarios es un cliente en el ambito de spring security
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //pk la autenticacion es con token, no con sesiones http. si fuera mvc sin apirest ahi seria always en vez de stateless
                .and()
                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/msvc-usuarios-client")) //esta es la pagina de login del cliente. Notar q msvc-usuarios-client es el client name dado en application.yml
                .oauth2Client(withDefaults()); //cuando importamos un metodo estatico se hace con la palabra reservada static

        return http.build();
    }


}
