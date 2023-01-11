/*
package org.adiaz.springcloud.msvc.usuarios.repositories;

import org.adiaz.springcloud.msvc.usuarios.models.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario,Long> {
}
*/

package org.adiaz.springcloud.msvc.auth.usuarios.repositories;

import org.adiaz.springcloud.msvc.auth.usuarios.models.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    //Forma uno: Spring reconoce automaticamente que se trata del email por la nomenclatura del metodo findByEmail
    Optional<Usuario>findByEmail(String email); //para validar que no haya un usuario con un email repetido al crear o editar un usuario

    //Forma dos: Con Query
    @Query("select u from Usuario u where u.email=?1")
    Optional<Usuario>porEmail(String email);

    //Forma tres: Con boolean (habria q cambiar el service y el controller)
    //boolean existsByEmail(String email);
}
