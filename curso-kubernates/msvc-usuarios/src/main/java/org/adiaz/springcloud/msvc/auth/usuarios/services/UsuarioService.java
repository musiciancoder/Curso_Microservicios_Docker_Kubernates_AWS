/*
package org.adiaz.springcloud.msvc.usuarios.services;

import org.adiaz.springcloud.msvc.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService { //esta es una interfaz DAO
    List<Usuario>listar();
    Optional<Usuario> porId(Long id);
    Usuario guardar (Usuario usuario);
    void eliminar (Long id);
}
*/


package org.adiaz.springcloud.msvc.auth.usuarios.services;

import org.adiaz.springcloud.msvc.auth.usuarios.models.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> listar();
    Optional<Usuario> porId(Long id);
    Usuario guardar(Usuario usuario);
    void eliminar(Long id);
    List<Usuario>listarPorIds(Iterable<Long>ids);// video Msvc usuarios obtener alumnos por ids

    Optional<Usuario>porEmail(String email);
}

