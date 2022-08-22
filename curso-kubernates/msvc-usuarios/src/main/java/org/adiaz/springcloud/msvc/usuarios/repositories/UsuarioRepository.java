/*
package org.adiaz.springcloud.msvc.usuarios.repositories;

import org.adiaz.springcloud.msvc.usuarios.models.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioRepository extends CrudRepository<Usuario,Long> {
}
*/

package org.adiaz.springcloud.msvc.usuarios.repositories;

import org.adiaz.springcloud.msvc.usuarios.models.entity.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario>findByEmail(String email); //para validar que no haya un usuario con un email repetido al crear o editar un usuario
}
