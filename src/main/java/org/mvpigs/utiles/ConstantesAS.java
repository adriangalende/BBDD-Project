package org.mvpigs.utiles;

public class  ConstantesAS {
    public static final String FECHA="\"<time datetime=\\\"(.*?)\\\">(.*?)\"";
    public static final String TITULO="<h1 class=\"titular-articulo\" itemprop=\"headline\">(.*?)<\\/h1>";
    public static final String ENTRADILLA="<h2 class=\"cont-entradilla-art\" itemprop=\"description\">(.*?)<\\/h2>";
    public static final String TEXTO="<div class=\"int-articulo\" itemprop=\"articleBody\">(.*?)<\\/p><\\/div>";
    public static final String TEXTOFILTRADO="<p>(.*?)<\\/p>";
    public static final String TAGS="<ul class=\"list-art-tags cf\"><li>(.*?)<\\/a><\\/li><\\/ul>";
    public static final String TAGSFILTRADOS="\">(.*?)<\\/a>";
    public static final String CATEGORIA="<p class=\"txt-art-recomienda txt-rec\">(.*?)<span(.*?)>(.*?)<\\/span><\\/a><\\/p>";
}
