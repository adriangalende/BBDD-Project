package org.mvpigs.repositorio;

import org.mvpigs.dominio.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UsuarioService {
    @Autowired
    UsuarioRepositorio usuarioRepositorio;
    public Usuario authenticateUser(String user, String password){
        Optional<Usuario> usuario = usuarioRepositorio.findUsuariosByUsernameAndPassword(user,password);
        if(usuario.isPresent()){
            return (Usuario) usuario.get();
        }
        return null;
    }
}
