package org.adiaz.springcloud.msvc.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name="msvc-cursos", url="localhost:8002") //cuando el otro servicio msvc-curso corra en local
//@FeignClient(name="msvc-cursos", url="msvc-cursos:8002") //cuando el otro servicio msvc-curso corra como containeren docker y se puedan comunicar. Notar que msvc-cursos es el --name en los comandos docker
//Antes de Kubernetes nos comunicamos por nombre del servicio y url
//@FeignClient(name="msvc-cursos", url="${msvc.cursos.url}") //msvc.cursos.url desde application.properties
//Con Kubernetes nos comunicamos solamente con el nombre del servicio. Debe coincidir con el nombre del servicio de kubernetes. Con la libreria agregada de loadbalancer, con esta linea ya se va a tener automaticamente un loadbalancer cuando haya coneccion mediante kubernetes
@FeignClient(name="msvc-cursos")
public interface CursoClienteRest {

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    void eliminarCursoUsuarioPorId(@PathVariable Long id);
}
