/*
package org.adiaz.springcloud.msvc.usuarios.services;

import org.adiaz.springcloud.msvc.usuarios.models.entity.Usuario;
import org.adiaz.springcloud.msvc.usuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//TODO Comparar patron de diseño con el de otros proyectos
@Service
public class UsuarioServiceImpl implements UsuarioService { //Clase DAO que implementa interfaz anterior

    @Autowired
    private UsuarioRepository repository;

    @Override
    @Transactional(readOnly = true) //por si hay algun error en proceso de lectura
    public List<Usuario> listar() {
        return (List<Usuario>) repository.findAll();
    }

    @Transactional(readOnly = true) //TODO Dedicarle tiempo a transactional
    @Override
    public Optional<Usuario> porId(Long id) {
        return Optional.empty();
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return repository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) { //debe devolver un status 204
        repository.deleteById(id);
    }
}
*/

package org.adiaz.springcloud.msvc.usuarios.services;

import org.adiaz.springcloud.msvc.usuarios.models.entity.Usuario;
import org.adiaz.springcloud.msvc.usuarios.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    @Autowired
    private UsuarioRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return (List<Usuario>) repository.findAll();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Usuario> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return repository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<Usuario> porEmail(String email) {
        return repository.findByEmail(email);
    }
}

