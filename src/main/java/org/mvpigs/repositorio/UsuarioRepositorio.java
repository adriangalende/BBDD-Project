package org.mvpigs.repositorio;

import org.mvpigs.dominio.Usuario;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepositorio extends CrudRepository<Usuario, Object> {

    public Optional<Usuario> findUsuariosByUsernameAndPassword(String username, String password);

}
