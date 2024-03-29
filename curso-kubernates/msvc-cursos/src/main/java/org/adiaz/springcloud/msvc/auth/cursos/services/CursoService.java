package org.adiaz.springcloud.msvc.auth.cursos.services;

import org.adiaz.springcloud.msvc.auth.cursos.models.Usuario;
import org.adiaz.springcloud.msvc.auth.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listar();
    Optional<Curso> porId(Long id);
    Optional<Curso> porIdConUsuarios(Long id, String token);
    Curso guardar(Curso usuario);
    void eliminar(Long id);

    void eliminarCursoUsuarioPorId(Long id);

    //métodos que dependen de un Usuario usuario del microservicio msvc-usuarios
    Optional<Usuario>asignarUsuario(Usuario usuario, Long cursoId); //asignar usuario q ya existe en la bbdd de  msvc-usuarios
    Optional<Usuario>crearUsuario(Usuario usuario, Long cursoId);//crear usuario q ya existe en la bbdd de  msvc-usuarios
    Optional<Usuario>eliminarUsuario(Usuario usuario, Long cursoId);//desasignar usuario q ya existe en la bbdd de  msvc-usuarios
}
