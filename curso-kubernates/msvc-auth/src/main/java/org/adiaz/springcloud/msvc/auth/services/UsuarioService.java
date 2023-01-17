package org.adiaz.springcloud.msvc.auth.services;

import org.adiaz.springcloud.msvc.auth.models.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private WebClient client;

    private Logger log = LoggerFactory.getLogger(UsuarioService.class);

    //metodo para hacer login por email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        try {
         Usuario usuario =   client.get()
                    .uri("http://msvc-usuarios", uri -> uri.queryParam("email", email).build())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(Usuario.class)
                    .block(); //con esto lo pasamos al tipo Usuario

            log.info("Usuario login:" + usuario.getEmail());
            log.info("Usuario login:" + usuario.getNombre());
            log.info("Usuario login:" + usuario.getPassword());

            return new User(email, usuario.getPassword() //el password por debajo se compara con el password enviado en el formulario login, es automatico, lo hace spring security
                    , true, true, true, true,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")))  ;     //SimpleGrantedAuthority es una clase de spring security que representa un rol o autoridad. eS SINLETON PORQUE ES UNO SOLO Y NO UNA LISTA
        } catch (RuntimeException e) {
            String error = "Error en el login, no existe el usuario " + email + " en el sistema";
            log.error(error);
            log.error(e.getMessage());
            throw new UsernameNotFoundException(error);

        }
    }
}
