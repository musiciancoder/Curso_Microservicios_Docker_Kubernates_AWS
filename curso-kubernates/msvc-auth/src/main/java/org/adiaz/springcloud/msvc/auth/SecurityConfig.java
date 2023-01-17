package org.adiaz.springcloud.msvc.auth;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;


/*Esta clase la copió y pegó directamente de la documentacion . En la documentacion hay info detallada sobre cada metodo de esta clase
https://docs.spring.io/spring-authorization-server/docs/current/reference/html/getting-started.html
 */
@Configuration
public class SecurityConfig {

    @Autowired
    private Environment env; //para configurar variables de entorno

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public static BCryptPasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1) //este es el orden de los filtros
//    Este es el filtro para manejo de errores y excepciones. Cuando el usuario no esta autenticado redirige a la pagina d login
    //Ademas configura nuestra pagina de entrada ("/login", en nuestro caso)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        http
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(
                                new LoginUrlAuthenticationEntryPoint("/login"))
                );

        return http.build();
    }

//    esto es para dar autorizacion a las rutas. ademas se configur el formulario de login
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )
                // Form login handles the redirect to the login page from the
                // authorization server filter chain
                .formLogin(Customizer.withDefaults()).csrf().disable();

        return http.build();
    }

    //este metodo reemplaza metodo q esta comentado mas abajo
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

//    esto es un usuario de ejemplo. Al hardcodearlo lo crea en memoria, no en base de datos. Esto debemos cambiarlo al implementarlo para q agarre los usuarios ni siquiera de la bbdd sino a msvc-usuarios. Lo comentó en la sección "Configurando BCrypt Password Encoder en el servidor de autorización"
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails userDetails = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("12345")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(userDetails);
//    }

//    Este es el registro de cliente, donde se registra un cliente con angular o con react, por ejemplo (el cliente en nuestro caso no es frontend, sino msvc-usuarios)
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
               // .clientId("messaging-client") //esto significa q no esta encriptado
                .clientId("usuarios-client") //el nombre de usuario del cliente frontend es "usuarios-client", el nombre es msvc-usuarios-client. Deben ir configurados con los mismos nombre y nombre de usuario en el application.yaml del cliente (en nuestro caso msvc-usuarios) para q se puedan comunicar
                //.clientSecret("{noop}12345") //esto significa q no esta encriptado
                .clientSecret(passwordEncoder().encode("12345"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                // el servidor de autorizacion redirige a esta ruta para login
                //.redirectUri("http://127.0.0.1:8001/login/oauth2/code/msvc-usuarios-client") //acá hay q utilizar la ip de nuestra maquina, q siempre es 127.0.0.1., ya q primero va a probar en local con minikube. Despues tenemos q cambiar esta ip por una variable de ambiente env q contenga la ip y puerto (loadbalancers) del servicio de usuarios de kubernetes. 8001 es el puerto de msvc-usuarios.
                //				.redirectUri("http://127.0.0.1:8001/authorized") //en el controlador va implementado un metodo para "authorized"
//aca cambio las lineas de arriba por las variables de ambiente establecidas en archivo application.yml
                .redirectUri(env.getProperty("LB_USUARIOS_URI") + "/login/oauth2/code/msvc-usuarios-client")
                .redirectUri(env.getProperty("LB_USUARIOS_URI") + "/authorized")
                .scope(OidcScopes.OPENID)
                .scope("read")
                .scope("write")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    //generar llave publica y privada con la firma del token y lo guarda en el servidor de autorizacion. Aca se firma, basicamente.
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    @Bean
    public ProviderSettings providerSettings() {
        return ProviderSettings.builder().build();
    }

}


