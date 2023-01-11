/*
package org.adiaz.springcloud.msvc.usuarios.models.entity;

import javax.persistence.*;

//NOTA: En este proyecto se crea en mysql la BD de antemano manualmente, pero sin tablas. Las tablas se generan al correr el proyecto por primera vez
@Entity
@Table(name="usuarios") //opcional, si no se pone la tabla toma el nombre de la clase automaticamente
public class Usuario {

    @Id //llave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) //autoincremental
    private Long id;
    private String nombre;

    @Column(unique = true) //@Column nos permitirá definir aspectos muy importantes sobre las columnas de la base de datos de la base de datos como lo es el nombre, la longitud, constrains, etc
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
*/

package org.adiaz.springcloud.msvc.auth.usuarios.models.entity;

import javax.persistence.*;
import javax.validation.constraints.*;


@Entity
@Table(name="usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

  //  @NotEmpty //solo para strings, para los demas tipos se ocupa @NotNull
    @NotBlank
    private String nombre;

    @Column(unique = true)
    @Email
    @NotBlank
    private String email;

    @NotBlank
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

