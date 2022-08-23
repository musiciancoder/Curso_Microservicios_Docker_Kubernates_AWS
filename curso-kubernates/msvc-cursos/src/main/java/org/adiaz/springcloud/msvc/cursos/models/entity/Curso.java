package org.adiaz.springcloud.msvc.cursos.models.entity;

import org.adiaz.springcloud.msvc.cursos.models.Usuario;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name= "cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nombre;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true) //cascade= si se elimina un curso se elimina la lista de usuarios inscritos en él
    @JoinColumn(name = "curso_id") //con esto se crea la llave foránea "curso_id" en la tabla cursos_usuarios. OJO, que llave foránea es una llave de otra tabla, NO de otro servicio, por lo que acá esta bien hecho
    private List<CursoUsuario> cursoUsuarios = new ArrayList<>(); //lista de usuarios inscritos en el curso

    @Transient //con esto decimos q no es una entidad
    private List<Usuario> usuarios;

    public Curso() {
        cursoUsuarios = new ArrayList<>();
        usuarios=new ArrayList<>();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void addCursoUsuario(CursoUsuario cursoUsuario){ //agregar un usuario a un curso
        cursoUsuarios.add(cursoUsuario);
    }

    public void removeCursoUsuario(CursoUsuario cursoUsuario){ //remover un usuario de un curso (lo hace por usuarioId para encontrarlo)
        cursoUsuarios.remove(cursoUsuario);
    }

    public List<CursoUsuario> getCursoUsuarios() {
        return cursoUsuarios;
    }

    public void setCursoUsuarios(List<CursoUsuario> cursoUsuarios) {
        this.cursoUsuarios = cursoUsuarios;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
