package org.adiaz.springcloud.msvc.auth.cursos.repositories;

import org.adiaz.springcloud.msvc.auth.cursos.models.entity.Curso;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Curso,Long> { //Long es el tipo de la llave id que se seteó en clase Curso

    @Modifying
    @Query("delete from CursoUsuario cu where cu.usuarioId=?1")
    void eliminarCursoUsuarioPorId(Long id);


}
