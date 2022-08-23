package org.adiaz.springcloud.msvc.cursos.repositories;

import org.adiaz.springcloud.msvc.cursos.models.entity.Curso;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Curso,Long> { //Long es el tipo de la llave id que se seteó en clase Curso



}
