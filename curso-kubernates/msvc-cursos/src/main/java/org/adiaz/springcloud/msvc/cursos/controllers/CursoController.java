package org.adiaz.springcloud.msvc.cursos.controllers;

import feign.FeignException;
import org.adiaz.springcloud.msvc.cursos.models.Usuario;
import org.adiaz.springcloud.msvc.cursos.models.entity.Curso;
import org.adiaz.springcloud.msvc.cursos.services.CursoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
public class CursoController {

    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity<List<Curso>> listar() { //ojo, dijo el profe que se podría haber hecho con genéricos tambien
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}") //id del curso
    public ResponseEntity<?> detalle(@PathVariable Long id) {
      //  Optional<Curso> o = service.porId(id); //con esto obtiene todos los usuarios, pero antes de que implementaramos porIdConUsuarios en el servicio
       Optional<Curso> o = service.porIdConUsuarios(id);
        if (o.isPresent()) {
            return ResponseEntity.ok(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Curso cursoDb = service.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validar(result);
        }
        Optional<Curso> o = service.porId(id);
        if (o.isPresent()) {
            Curso cursoDb = o.get();
            cursoDb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(cursoDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Curso> o = service.porId(id);
        if (o.isPresent()) {
            service.eliminar(o.get().getId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    //metodo utilitario que podría ir en paquete utils
    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors(). //es una lista con los campos en donde hay errores
                forEach(fieldError -> {
            errores.put(fieldError.getField(), "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

    //asigna un usuario existente en el msvc-usuario
    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> o;
        try {
            o = service.asignarUsuario(usuario, cursoId); //comunicar con el servicio msvc-usuario
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No existe el usuario por " +
                            "el id o error en la comunicacion: " + e.getMessage()));
        }
        if (o.isPresent()) { //buscar el curso
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    //crea un usuario no existente en el msvc-usuario
    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> o;
        try {
            o = service.crearUsuario(usuario, cursoId); //comunicar con el servicio msvc-usuario
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No se pudo crear el usuario " +
                            "o error en la comunicacion: " + e.getMessage()));
        }
        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    //desasigna un usuario existente en el msvc-usuario
    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> o;
        try {
            o = service.eliminarUsuario(usuario, cursoId); //comunicar con el servicio msvc-usuario
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("mensaje", "No se pudo desasignar el usuario por " +
                            "el id o error en la comunicacion: " + e.getMessage()));
        }
        if (o.isPresent()) { //buscar el curso
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }
        return ResponseEntity.notFound().build();
    }
}
