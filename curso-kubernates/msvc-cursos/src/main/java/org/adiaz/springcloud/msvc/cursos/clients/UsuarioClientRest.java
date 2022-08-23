package org.adiaz.springcloud.msvc.cursos.clients;

import org.adiaz.springcloud.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

//Este es un cliente http con feign. Se podría hacer con http rest template también. Acá ya está listo para ser inyectado por dependencias en el servicio (CursoServiceImpl)

@FeignClient(name="msvc-usuarios",url="localhost:8001") //quiere decir q vamos a consumir msvc-usuarios acá
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    Usuario detalle (@PathVariable Long id); //del método "detalle" del controlador de msvc-usuarios

    @PostMapping("/")
    Usuario crear (@RequestBody Usuario usuario);
}
