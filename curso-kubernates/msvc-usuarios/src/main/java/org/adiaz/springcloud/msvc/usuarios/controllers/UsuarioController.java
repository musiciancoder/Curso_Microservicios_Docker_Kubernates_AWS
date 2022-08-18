package org.adiaz.springcloud.msvc.usuarios.controllers;


import org.adiaz.springcloud.msvc.usuarios.models.entity.Usuario;
import org.adiaz.springcloud.msvc.usuarios.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
// @RequestMapping ("/api")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }

    @GetMapping("{id}") //lo pasa por url
    public ResponseEntity<?> detalle(@PathVariable Long id){
        Optional <Usuario> usuarioOptional = service.porId(id);
        if(usuarioOptional.isPresent()){
            return ResponseEntity.ok(usuarioOptional.get()); //ok es metodo estático, por eso no ocupa new ResponseEntity
        }
        //return null;
        return ResponseEntity.notFound().build();
    }



}
