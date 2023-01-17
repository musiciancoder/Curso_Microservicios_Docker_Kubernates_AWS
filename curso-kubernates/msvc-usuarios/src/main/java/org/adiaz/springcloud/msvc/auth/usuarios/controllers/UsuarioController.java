/*
package org.adiaz.springcloud.msvc.usuarios.controllers;


import org.adiaz.springcloud.msvc.usuarios.models.entity.Usuario;
import org.adiaz.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
// @RequestMapping ("/api") //esto es opcional en la url
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    @GetMapping("/{id}") //lo pasa por url
    public ResponseEntity<?> detalle(@PathVariable Long id){
        Optional <Usuario> usuarioOptional = service.porId(id);
        if(usuarioOptional.isPresent()){
            return ResponseEntity.ok(usuarioOptional.get()); //ok es metodo estático, por eso no ocupa new ResponseEntity
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping
    public ResponseEntity <?> crear (@RequestBody Usuario usuario){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar (@RequestBody Usuario usuario, @PathVariable Long id){
        Optional <Usuario> o = service.porId(id);
        if(o.isPresent()){
            Usuario usuarioDB = o.get();
            usuarioDB.setNombre(usuario.getNombre());
            usuarioDB.setEmail(usuario.getPassword());
            usuarioDB.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuarioDB));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar (@PathVariable Long id) {
        Optional <Usuario> o = service.porId(id);
        if (o.isPresent()){
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
*/

package org.adiaz.springcloud.msvc.auth.usuarios.controllers;

import org.adiaz.springcloud.msvc.auth.usuarios.models.entity.Usuario;
import org.adiaz.springcloud.msvc.auth.usuarios.repositories.UsuarioRepository;
import org.adiaz.springcloud.msvc.auth.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class UsuarioController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioService service;

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Environment env;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/crash") //para que se pueda caer y probaren postman
    public void crash(){
        ((ConfigurableApplicationContext)context).close();
        }



        @GetMapping
        public ResponseEntity <?>listar() {
        Map<String,Object>body= new HashMap<>();
        body.put("users",service.listar());
        body.put("pod_info", env.getProperty("MY_POD_NAME")+": "+ env.getProperty("MY_POD_IP")); // para poder ver informacion de las variables de ambiente nombre del pod "MY_POD_NAME" e IP del pod "MY_POD_IP" seteadas en el deployment-usuarios.yaml.
        body.put("texto",env.getProperty("config.texto")); //para q al configurar en el configMap de kubernetes las variables de application.properties muestre este texto en postman
          //  return Collections.singletonMap("usuarios",service.listar()); //esta linea era antes de agregar las variables de ambiente de kubernetes
            return ResponseEntity.ok(body);
        }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.porId(id);
        if (usuarioOptional.isPresent()) {
            return ResponseEntity.ok(usuarioOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()){
            return validar(result); //profe dijo: "con esto el !usuario.getEmail().isEmpty() de las lineas de mas abajo no seria necesario, pero esa parte la voy a dejar igual"
        }
        if (!usuario.getEmail().isEmpty() && service.porEmail(usuario.getEmail()).isPresent()){ //verificar si hay un usuario con ese email
            return ResponseEntity.badRequest()
                    .body(Collections
                            .singletonMap("mensaje","Ya existe un usuario con ese email!"));
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); //encriptamos la contraseña y la devolvemos de nuevoy
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()){
            return validar(result);
        }
        if (!usuario.getEmail().isEmpty() && service.porEmail(usuario.getEmail()).isPresent()){ //verificar si hay un usuario con ese email
            return ResponseEntity.badRequest()
                    .body(Collections
                            .singletonMap("mensaje","Ya existe un usuario con ese email!"));
        }
        Optional<Usuario> o = service.porId(id);
        if (o.isPresent()) {
            Usuario usuarioDb = o.get();
            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
          //  usuarioDb.setPassword(usuario.getPassword()); //linea antes de encriptar acá abajo
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); //encriptamos la contraseña y la devolvemos de nuevoy

            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuarioDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Usuario> o = service.porId(id);
        if (o.isPresent()) {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    //para obtener el detalle de varios usuarios simultaneamente (no todos!) mediante los ids de usuarios (request "usuarios por curso en el postman")
    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?>obtenerAlumnosPorCurso(@RequestParam List <Long>ids){
        return ResponseEntity.ok(service.listarPorIds(ids));
    }


    //este metodo lo agregó Guzman en la parte "configurando OAUTH2 client securityconfig" y es la ruta que se mencionó en application.yml para el login
    @GetMapping("/authorized")
    public Map<String,Object>authorized(@RequestParam(name = "code") String code){ //lo de (name = "code")  es  resundante
        return Collections.singletonMap("code",code); //retorna el codigo de autorizacion en el json de la respuesta. Por supuesto esto es cuando todavía no tenemos el token ya q estamos queriendo obtener el token con este codigo de  autorizacion
    }

    //metodo springsecurity para login mediante email
    @GetMapping("/login")
    public ResponseEntity<?> loginByEmail(@RequestParam(name="email") String email){ //lo de (name="email") es reduntante
        Optional<Usuario> o = service.porEmail(email);
        if (o.isPresent()){
            return ResponseEntity.ok(o.get());
        }
        return ResponseEntity.notFound().build();

    }

    //metodo utilitario que podría ir en paquete utils
    private ResponseEntity<Map<String,String>> validar (BindingResult result){
        Map<String,String>errores = new HashMap<>();
        result.getFieldErrors(). //es una lista con los campos en donde hay errores
                forEach(fieldError -> {
                    errores.put(fieldError.getField(), "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage() );
                });
        return ResponseEntity.badRequest().body(errores);
    }



}

