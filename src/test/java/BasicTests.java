
import org.apache.commons.lang3.StringEscapeUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mvpigs.App;
import org.mvpigs.dominio.Noticia;
import org.mvpigs.repositorio.NoticiasRepositorio;
import org.mvpigs.utiles.SocketConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={SocketConnection.class, App.class})
public class BasicTests {
    @Autowired
    NoticiasRepositorio noticiasRepositorio;

    @Test
    public void testContenidoURL() throws IOException {
        String result = "";
        String articulo="";
        Noticia noticia = new Noticia();

        try{
            result = SocketConnection.getURLSource("https://as.com/futbol/2018/05/27/champions/1527399775_166994.html");
        } catch (IOException ioex){
            throw ioex;
        }

        String data1 = result;

        //FECHA
        //<time datetime="(.*?)">(.*?)
        String regex = "<time datetime=\"(.*?)\">(.*?)";

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(data1);
        if(matcher.find()) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(matcher.group(1));
                String fechaFormateada = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date.getTime());
                noticia.setFecha(date.toString());
            }catch (ParseException pex){

            }
        }

        //TITULO
        //<h1 class="titular-articulo" itemprop="headline">(.*?)<\/h1>
        //"<h1 class=\"titular-articulo\" itemprop=\"headline\">(.|\n)*?<\\/h1>"
        regex = "<h1 class=\"titular-articulo\" itemprop=\"headline\">(.*?)<\\/h1>";

        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(data1);
        if(matcher.find()) {
            noticia.setTitulo(StringEscapeUtils.unescapeHtml4(matcher.group(1)));
        }

        //ENTRADILLA
        regex = "<h2 class=\"cont-entradilla-art\" itemprop=\"description\">(.*?)<\\/h2>";

        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(data1);
        if(matcher.find()) {
            noticia.setEntradilla(StringEscapeUtils.unescapeHtml4(matcher.group(1)));
        }

        //TEXTO
        regex = "<div class=\"int-articulo\" itemprop=\"articleBody\">(.*?)<\\/p><\\/div>";

        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(data1);
        if(matcher.find()) {
            articulo=matcher.group(1);
            regex = "<p>(.*?)<\\/p>";
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(articulo);
            articulo = "";
            while(matcher.find()){
                articulo += matcher.group();
            }
            noticia.setTexto(articulo);

        }

        //TAGS
        regex = "<ul class=\"list-art-tags cf\"><li>(.*?)<\\/a><\\/li><\\/ul>";
        pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        matcher = pattern.matcher(data1);
        if(matcher.find()){
            regex = "\">(.*?)<\\/a>";
            pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
            matcher = pattern.matcher(matcher.group(1));
            while(matcher.find()){
                if(!matcher.group().contains("Etiquetado en")){
//                    noticia.getTags().add();
//                    noticia.getTags().join(matcher.group().split("</a>")[0]);
                }
            }
        }
    }


}
