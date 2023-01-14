package org.adiaz.springcloud.msvc.auth.usuarios;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;

//Esta es la clase de configuracion de Spring Security para el cliente. En este caso msvc-usuarios es un cliente en el ambito de spring security
@EnableWebSecurity
public class SecurityConfig {

    //el servidor de recursos se comunicará tras bambalinas con el servidor de autorizacion.
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()

                //Esta parte corresponde al servidor de recursos:
                .antMatchers("/authorized").permitAll() //recordar q los antmatchers es para dar permisos a las rutas segun autorities, por lo q los agregó en la parte correspondiente al servidor de recursos. En esta linea estamos dando permiso a todos (administradores y usuarios) para q puedan acceder al metodo authorized de UsuarioController y obtener el codigo que está en ese metodo siempre y cuando el username y el password q ingresamos en la pagina de login sean los correctos.
                //en este caso tanto administradores como usuarios tendrán acceso a lo siguiente:
                .antMatchers(HttpMethod.GET,"/", "/{id]").hasAnyAuthority("SCOPE_read","SCOPE_write") //"/" es para todas,  "/{id]" es para detalle de uno. Con comas se pueden ir agregando varios endpoints, como en este caso
                .antMatchers(HttpMethod.POST,"/").hasAnyAuthority("SCOPE_write") //el SCOPE_write es como el admin, el SCOPE_read es como el user
                .antMatchers(HttpMethod.PUT,"/{id]").hasAnyAuthority("SCOPE_write")
                .antMatchers(HttpMethod.DELETE,"/{id]").hasAnyAuthority("SCOPE_write")
                .anyRequest().authenticated()

                //Esta parte corresponde al servidor de autorizacion (se encarga de la autenticacion, o sea del login):

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //pk la autenticacion es con token, no con sesiones http. si fuera mvc sin apirest ahi seria always en vez de stateless
                .and()
                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/msvc-usuarios-client")) //esta es la pagina de login del cliente. Notar q msvc-usuarios-client es el client name dado en application.yml
                .oauth2Client(withDefaults()) //cuando importamos un metodo estatico se hace con la palabra reservada static

                //Esta parte corresponde al servidor de recursos de nuevo:
                .oauth2ResourceServer().jwt(); //aca finalmente enviamos el jwt, ya q cada vez que solicitemos algun recurso que esté protegido debemos pasarle el token segun esta escrito en el application.yml.

        return http.build();
    }


}
