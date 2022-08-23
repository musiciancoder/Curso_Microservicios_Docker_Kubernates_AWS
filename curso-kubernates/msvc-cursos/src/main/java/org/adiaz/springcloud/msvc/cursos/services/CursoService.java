package org.adiaz.springcloud.msvc.cursos.services;

import org.adiaz.springcloud.msvc.cursos.models.Usuario;
import org.adiaz.springcloud.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listar();
    Optional<Curso> porId(Long id);
    Curso guardar(Curso usuario);
    void eliminar(Long id);

    //métodos que dependen de un Usuario usuario del microservicio msvc-usuarios
    Optional<Usuario>asignarUsuario(Usuario usuario, Long cursoId); //asignar usuario q ya existe en la bbdd de  msvc-usuarios
    Optional<Usuario>crearUsuario(Usuario usuario, Long cursoId);//crear usuario q ya existe en la bbdd de  msvc-usuarios
    Optional<Usuario>eliminarUsuario(Usuario usuario, Long cursoId);//desasignar usuario q ya existe en la bbdd de  msvc-usuarios
}
