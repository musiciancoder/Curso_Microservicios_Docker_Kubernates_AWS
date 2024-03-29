package org.adiaz.springcloud.msvc.auth.cursos.clients;

import org.adiaz.springcloud.msvc.auth.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Este es un cliente http con feign. Se podría hacer con http rest template también. Acá ya está listo para ser inyectado por dependencias en el servicio (CursoServiceImpl)

//@FeignClient(name="msvc-usuarios",url="localhost:8001") //quiere decir q vamos a consumir msvc-usuarios acá. Esto es cuando probamos en local
//@FeignClient(name="msvc-usuarios", url="msvc-usuarios:8001") //quiere decir q vamos a consumir msvc-usuarios acá. Esto es cuando probamos en docker desde un contenedor con un microservicio a otro contenedor con el otro microservicio
//Antes de Kubernetes nos comunicamos por nombre del servicio y url
//@FeignClient(name="msvc-usuarios", url="${msvc.usuarios.url}") //msvc.usuarios.url lo obtiene del application properties
//Con Kubernetes nos comunicamos solamente con el nombre del servicio. Debe coincidir con el nombre del servicio de kubernetes. Con la libreria agregada de loadbalancer, con esta linea ya se va a tener automaticamente un loadbalancer cuando haya coneccion mediante kubernetes
@FeignClient(name="msvc-usuarios")
public interface UsuarioClientRest {

    //Nota: los métodos detalle, crear, etc. los saca todos del controlador msvc-usuarios UsuarioController

    @GetMapping("/{id}")
    Usuario detalle (@PathVariable Long id); //del método "detalle" del controlador de msvc-usuarios

    @PostMapping("/")
    Usuario crear (@RequestBody Usuario usuario);

    @GetMapping("/usuarios-por-curso")
    List<Usuario> obtenerAlumnosPorCurso(@RequestParam Iterable<Long> ids
    , @RequestHeader(value = "Authorization", required = true) String token); // esta linea la agregó en seccion " Propagar token JWT en msvc cursos". Es para pasarle el token como Autorizacion en los headers en postman.
}
