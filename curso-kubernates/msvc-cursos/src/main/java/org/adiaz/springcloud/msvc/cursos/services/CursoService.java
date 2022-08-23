package org.adiaz.springcloud.msvc.cursos.services;

import org.adiaz.springcloud.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listar();
    Optional<Curso> porId(Long id);
    Curso guardar(Curso usuario);
    void eliminar(Long id);
}
