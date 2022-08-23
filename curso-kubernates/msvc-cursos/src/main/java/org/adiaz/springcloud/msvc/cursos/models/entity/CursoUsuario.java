package org.adiaz.springcloud.msvc.cursos.models.entity;

import javax.persistence.*;

@Entity
@Table(name="cursos_usuarios") //relaciona con el otro servicio usuarios
public class CursoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="usuario_id", unique = true) //con esto un usuario no puede estar repetido en un curso
    private Long usuarioId; // ambos servicios se relacionarán con usuarioId

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Override
    public boolean equals(Object obj) { //sobrescribió equals para q usuarioId sea el unico criterio para decidir si dos objetos CursoUsuario son iguales
        if (this ==obj) { //si la instancia actual es igual a lo que se esta pasando por argumento
            return true;
        }
        if(!(obj instanceof CursoUsuario)) {
            return false;
        }
        CursoUsuario o = (CursoUsuario) obj; //casting
        return this.usuarioId !=null && this.usuarioId.equals(o.usuarioId);
    }
}
