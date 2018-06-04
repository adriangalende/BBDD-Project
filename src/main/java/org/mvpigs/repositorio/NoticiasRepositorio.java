package org.mvpigs.repositorio;

import org.mvpigs.dominio.Noticia;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface NoticiasRepositorio extends MongoRepository<Noticia, Integer>{

    public Optional<Noticia> findOneByTitulo(String titulo);
    public List<Noticia> findByTitulo(String titulo);
    public List<Noticia> findAll(Sort sort);
    public Noticia findByIdNoticia(Integer idNoticia);
    public Boolean existsByIdNoticia(Integer id);

}
