package org.adiaz.springcloud.msvc.usuarios.models.entity;

import javax.persistence.*;

@Entity
@Table(name="usuarios") //opcional, si no se pone la tabla toma el nombre de la clase automaticamente
public class Usuario {

    @Id //llave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincremental
    private Long id;
    private String nombre;

    @Column(unique = true) //funciona solo para el caso que nosotros creamos la tabla al correr la aplicacion, pero no cuando esta creada de antemano en la bbdd
    private String email;

    private String password;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
