package org.adiaz.springcloud.msvc.auth.cursos.services;

import org.adiaz.springcloud.msvc.auth.cursos.models.Usuario;
import org.adiaz.springcloud.msvc.auth.cursos.models.entity.Curso;
import org.adiaz.springcloud.msvc.auth.cursos.models.entity.CursoUsuario;
import org.adiaz.springcloud.msvc.auth.cursos.repositories.CursoRepository;
import org.adiaz.springcloud.msvc.auth.cursos.clients.UsuarioClientRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository repository;

    @Autowired
    private UsuarioClientRest client; //iny por dep. del cliente http

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Curso> porIdConUsuarios(Long id) { //id es id del curso
        Optional<Curso> o = repository.findById(id);
        if (o.isPresent()) {
            Curso curso = o.get();
            if (!curso.getCursoUsuarios().isEmpty()) { //si la lista de usuarios de ese curso no esta vacia
                List<Long> ids = curso.getCursoUsuarios() // lista de usuarios de ese curso
                        .stream()
                        .map(cu -> cu.getUsuarioId()) //pasa de cursoUsuario a idUsuario
                        .collect(Collectors.toList());// obtenemos una lista de usuarioId del curso a partir del id del curso
                List<Usuario> usuarios = client.obtenerAlumnosPorCurso(ids); //obtiene finalmente los usuarios completos a partir de sus ids
                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return repository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void eliminarCursoUsuarioPorId(Long id) {
        repository.eliminarCursoUsuarioPorId(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) { //ya existe en la bbdd de msvc-usuario
        Optional<Curso> o = repository.findById(cursoId);//encuentra el curso si existe
        if (o.isPresent()) {
            Usuario usuarioMsvc = client.detalle(usuario.getId());
            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario(); //se debe crear un CursoUsuario porque lo q se guarda en esta bbdd es un CursoUsuario, no un Usuario tipo dto
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) { //no existe en la bbdd de msvc-usuario
        Optional<Curso> o = repository.findById(cursoId);//encuentra el curso si existe
        if (o.isPresent()) {
            Usuario usuarioNuevoMsvc = client.crear(usuario); //crea un usuario q no existe en la bbdd de msvc-usuario
            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioNuevoMsvc.getId());
            curso.addCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioNuevoMsvc);
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = repository.findById(cursoId);//encuentra el curso si existe
        if (o.isPresent()) {
            Usuario usuarioMsvc = client.detalle(usuario.getId());
            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
            curso.removeCursoUsuario(cursoUsuario);
            repository.save(curso);
            return Optional.of(usuarioMsvc);
        }
        return Optional.empty();
    }
}
