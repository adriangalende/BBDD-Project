package org.mvpigs.repositorio;

import org.mvpigs.dominio.Noticia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RepositorioNoticias{
    @Autowired
    NoticiasRepositorio noticiasRepositorio;

    public Boolean insert(Noticia noticia){
        List<Noticia> noticias = noticiasRepositorio.findAll(new Sort(Sort.Direction.DESC, "_id"));
        int proximaId = (noticias != null)? noticias.get(0).getIdNoticia()+1:0;
        Optional<Noticia> existeNoticia = noticiasRepositorio.findOneByTitulo(noticia.getTitulo());
        if(existeNoticia.isPresent() && proximaId != 0){
            return false;
        } else {
            noticia.setIdNoticia(proximaId);
            noticiasRepositorio.save(noticia);
        }
        return true;
    }

    public List<Noticia> cargar(){
        return noticiasRepositorio.findAll(new Sort(Sort.Direction.DESC, "_id"));
    }

    public Noticia cargarNoticia(String id){
        Noticia noticia = noticiasRepositorio.findByIdNoticia(Integer.parseInt(id.split("=")[0]));
        if(noticia != null){
            return noticia;
        }
        return null;
    }

    public void eliminar(String id){
        Noticia noticia = noticiasRepositorio.findByIdNoticia(Integer.parseInt(id.split("=")[0]));
        if(noticia != null){
            noticiasRepositorio.delete(noticia);
        }
    }

    public boolean update(Noticia noticia){
        noticiasRepositorio.save(noticia);
        return true;
    }


}
