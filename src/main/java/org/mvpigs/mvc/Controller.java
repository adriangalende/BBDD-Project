package org.mvpigs.mvc;


import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.mongodb.util.JSON;
import org.apache.commons.lang3.StringEscapeUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.mvpigs.dominio.Noticia;

import org.mvpigs.dominio.Usuario;
import org.mvpigs.repositorio.RepositorioNoticias;
import org.mvpigs.repositorio.UsuarioService;
import org.mvpigs.utiles.ConstantesAS;
import org.mvpigs.utiles.SocketConnection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.net.URLDecoder;
import java.util.Iterator;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@RestController
@RequestMapping(value="/",method = {RequestMethod.POST, RequestMethod.GET})
public class Controller {

    @Autowired
    RepositorioNoticias repositorioNoticias;

    @RequestMapping(value="/mapear" , method = RequestMethod.POST)
    @ResponseBody
    public Noticia obtenerNoticia(@RequestBody String data){

        Noticia noticia = new Noticia();
        String regex = null;
        Pattern pattern = null;
        Matcher matcher = null;
        try{
            String respuesta = SocketConnection.getURLSource(URLDecoder.decode(data.substring(0, data.length()-1), "UTF-8"));
            //Obtenemos el titulo de la noticia
            regex = ConstantesAS.TITULO;
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(respuesta);
            if(matcher.find()) {
                noticia.setTitulo(StringEscapeUtils.unescapeHtml4(matcher.group(1)));
            }
            //Obtenemos entradilla
            regex = ConstantesAS.ENTRADILLA;
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(respuesta);
            if(matcher.find()) {
                noticia.setEntradilla(StringEscapeUtils.unescapeHtml4(matcher.group(1)));
            }
            //Obtenemos el texto
            regex = ConstantesAS.TEXTO;

            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(respuesta);
            String articulo="";
            if(matcher.find()) {
                articulo=matcher.group(1);
                regex = ConstantesAS.TEXTOFILTRADO;
                pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(articulo);
                articulo = "";
                while(matcher.find()){
                    articulo += matcher.group();
                }
                noticia.setTexto(StringEscapeUtils.escapeHtml4(articulo.replaceAll("<[a-zA-Z\\/][^>]*>","")));

                //Obtenemos los tags
                regex = ConstantesAS.TAGS;
                pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(respuesta);
                if(matcher.find()) {
                    regex = ConstantesAS.TAGSFILTRADOS;
                    pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                    matcher = pattern.matcher(matcher.group(1));
                    while (matcher.find()) {
                        if (!matcher.group().contains("Etiquetado en")) {
                            noticia.getTags().add(matcher.group().split("</a>")[0].trim().substring(2));
                        }
                    }
                }

                //Obtenemos la categoria
                regex = ConstantesAS.CATEGORIA;
                pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
                matcher = pattern.matcher(respuesta);
                if(matcher.find()) {
                    noticia.setCategoria(matcher.group(3));
                }

            }
        }catch (Exception ex){

        }
        return noticia;
    }



    @RequestMapping(value = "/insertaNoticia", method = RequestMethod.POST)
    @ResponseBody
    public String insertarNoticia( @RequestBody String noticiaString) {
        String a = URLDecoder.decode(noticiaString);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = new JsonParser();
        Noticia noticia = new Noticia();
        noticia.setTitulo(jsonParser.parse(a.substring(0, a.length()-1)).getAsJsonObject().get("Titulo").getAsString());
        noticia.setEntradilla(jsonParser.parse(a.substring(0, a.length()-1)).getAsJsonObject().get("Entradilla").getAsString());
        noticia.setTexto(jsonParser.parse(a.substring(0, a.length()-1)).getAsJsonObject().get("Texto").getAsString());
        noticia.setFecha(jsonParser.parse(a.substring(0, a.length()-1)).getAsJsonObject().get("Fecha").getAsString());
        JsonArray jsonarray = jsonParser.parse(a.substring(0, a.length()-1)).getAsJsonObject().get("Tags").getAsJsonArray();
        Iterator iterator = jsonarray.iterator();
        while(iterator.hasNext()){
            noticia.getTags().add(iterator.next().toString());
        }
        //noticia.setCategoria(jsonParser.parse(a.substring(0, a.length()-1)).getAsJsonObject().get("Categoria").getAsString());

        if(repositorioNoticias.insert(noticia)){
            return "ok";
        } else {
            return "failed";
        }

    }

    @RequestMapping(value="/cargarNoticias", method = {RequestMethod.POST})
    @ResponseBody
    public List<Noticia> cargarNoticias(@RequestBody String filtro){
        return repositorioNoticias.cargar();
    }

    @RequestMapping(value="/cargarNoticia", method = {RequestMethod.POST})
    @ResponseBody
    public Noticia cargarNoticia(@RequestBody String id){
        return repositorioNoticias.cargarNoticia(id);
    }

    @RequestMapping(value="/eliminarNoticia", method = {RequestMethod.POST})
    @ResponseBody
    public void eliminarNoticia(@RequestBody String id){
        repositorioNoticias.eliminar(id);
    }

    @RequestMapping(value = "/editarNoticia", method = RequestMethod.POST)
    @ResponseBody
    public boolean editarNoticia( @RequestBody String noticiaString) {
        String a = URLDecoder.decode(noticiaString);
        ObjectMapper mapper = new ObjectMapper();
        JsonParser jsonParser = new JsonParser();
        Noticia noticia = new Noticia();
        noticia.setIdNoticia(jsonParser.parse(a.substring(0, a.length()-1)).getAsJsonObject().get("id").getAsInt());
        noticia.setTitulo(jsonParser.parse(a.substring(0, a.length()-1)).getAsJsonObject().get("Titulo").getAsString());
        noticia.setEntradilla(jsonParser.parse(a.substring(0, a.length()-1)).getAsJsonObject().get("Entradilla").getAsString());
        noticia.setTexto(jsonParser.parse(a.substring(0, a.length()-1)).getAsJsonObject().get("Texto").getAsString());
        noticia.setFecha(jsonParser.parse(a.substring(0, a.length()-1)).getAsJsonObject().get("Fecha").getAsString());
        noticia.setCategoria(jsonParser.parse(a.substring(0, a.length()-1)).getAsJsonObject().get("Categoria").getAsString());
        JsonArray jsonarray = jsonParser.parse(a.substring(0, a.length()-1)).getAsJsonObject().get("Tags").getAsJsonArray();
        Iterator iterator = jsonarray.iterator();
        while(iterator.hasNext()){
            noticia.getTags().add(iterator.next().toString());
        }
        //noticia.setCategoria(jsonParser.parse(a.substring(0, a.length()-1)).getAsJsonObject().get("Categoria").getAsString());

        if(repositorioNoticias.update(noticia)){
            return true;
        }
        return  false;

    }


    @RequestMapping(value = { "/login" }, method = RequestMethod.POST)
    @ResponseBody
    public String login(HttpSession session, String username, String password) throws Exception {
        UsuarioService usuarioService = new UsuarioService();
        Usuario member=usuarioService.authenticateUser(username, password);
        if(member!=null)
        {
            session.setAttribute("USUARIO", member);
        }else
        {
            throw new Exception("Invalid username or password");
        }
        return JSON.serialize("SUCCESS");
    }

}
