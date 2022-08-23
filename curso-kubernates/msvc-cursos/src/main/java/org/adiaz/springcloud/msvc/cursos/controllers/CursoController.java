package org.adiaz.springcloud.msvc.cursos.controllers;

import org.adiaz.springcloud.msvc.cursos.models.entity.Curso;
import org.adiaz.springcloud.msvc.cursos.services.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CursoController {

    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity<List<Curso>> listar(){ //ojo, dijo el profe que se podría haber hecho con genéricos tambien
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity <?> detalle (@PathVariable Long id){
        Optional<Curso> o = service.porId(id);
        if(o.isPresent()){
            return ResponseEntity.ok(o.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> crear(@Valid @RequestBody  Curso curso,BindingResult result){
        if (result.hasErrors()){
            return validar(result);
        }
        Curso cursoDb= service.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id){
        if (result.hasErrors()){
            return validar(result);
        }
        Optional <Curso> o = service.porId(id);
        if(o.isPresent()){
            Curso cursoDb= o.get();
            cursoDb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(cursoDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>eliminar(@PathVariable Long id){
        Optional <Curso> o = service.porId(id);
        if(o.isPresent()){
            service.eliminar(o.get().getId());
            return ResponseEntity.noContent().build();
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
