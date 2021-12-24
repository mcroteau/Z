package io;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class Loader extends URLClassLoader {
    public Loader(URL[] urls) {
        super(urls);
    }
    public void addFile (URL url){
        addURL (url);
    }
}
