package org.mvpigs.dominio;

import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.annotation.Generated;
import java.util.ArrayList;

import java.util.List;

@Document(collection = "noticias")
public class Noticia {
    @Id
    @Field("_id")
    @JsonProperty("_id")
    private int idNoticia;
    @Field("Fecha")
    @JsonProperty("Fecha")
    private String fecha;
    @Field("Titulo")
    @JsonProperty("Titulo")
    private String titulo;
    @Field("Entradilla")
    @JsonProperty("Entradilla")
    private String entradilla;
    @Field("Texto")
    @JsonProperty("Texto")
    private String texto;
    @Field("Tags")
    @JsonProperty("Tags")
    private List<String> tags;
    @Field("Usuario")
    @JsonProperty("Usuario")
    String usuario;
    @Field("Categoria")
    @JsonProperty("Categoria")
    String categoria;

    public int getIdNoticia() {
        return idNoticia;
    }

    public void setIdNoticia(int idNoticia) {
        this.idNoticia = idNoticia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEntradilla() {
        return entradilla;
    }

    public void setEntradilla(String entradilla) {
        this.entradilla = entradilla;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public List<String> getTags() {
        if(tags == null){
            tags = new ArrayList<>();
        }
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        usuario = usuario;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
