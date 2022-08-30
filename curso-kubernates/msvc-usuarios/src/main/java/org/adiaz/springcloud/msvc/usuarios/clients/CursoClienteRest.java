package org.adiaz.springcloud.msvc.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name="msvc-cursos", url="localhost:8002") //cuando el otro servicio msvc-curso corra en local
@FeignClient(name="msvc-cursos", url="msvc-cursos:8002") //cuando el otro servicio msvc-curso corra como containeren docker y se puedan comunicar. Notar que msvc-cursos es el --name en los comandos docker
public interface CursoClienteRest {

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    void eliminarCursoUsuarioPorId(@PathVariable Long id);
}
