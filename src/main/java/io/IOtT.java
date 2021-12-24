package io;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class IOtT {

    public IOtT(){
        this.runnables = new ConcurrentHashMap<>();
    }
    private Loader loader;

    Map<String, HttpRunnable> runnables;

    ServerSocket serverSocket = null;
    Logger logger = Logger.getLogger("oLL");

    protected Boolean shutdown = false;

    public static void main(String[] args)  {
        IOtT iot = new IOtT();
        iot.await();
    }

    public void await() {
        loadJars();

        int port = 8080;
        try {
            serverSocket = new ServerSocket(port, 1, InetAddress.getByName("127.0.0.1"));
            logger.info("server running.........");
        }catch (IOException e) {
            e.printStackTrace(); System.exit(1);
        }

        while (!shutdown) {

            Socket clientSocket = null;

//            try {
//                clientSocket = this.serverSocket.accept();
//            } catch (IOException e) {
//                if(shutdown) {
//                    System.out.println("Server Stopped.") ;
//                    return;
//                }
//                throw new RuntimeException("Error accepting client connection", e);
//            }
            //thanks jankov
            //-> todo: reference
//            HttpRunnable runnable = null;
//            try {
//                runnable = new HttpRunnable(serverSocket.accept());
//                new Thread(runnable).run();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            new Thread(() -> {
//                try {
//                    HttpRunnable r = new HttpRunnable(serverSocket.accept());
//                    r.run();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
        }
    }

    protected void loadJars() {

        String projectPath = getProjectPath()
                .concat(File.separator)
                .concat("WEB-INF")
                .concat(File.separator)
                .concat("lib");

        File dependencyDir = new File(projectPath);
        File[] jars = dependencyDir.listFiles();
        if (jars != null) {
            URL[] jarUrls = new URL[jars.length];
            for (int q = 0; q < jars.length; q++) {
                try {
                    jarUrls[q] = jars[q].toURI().toURL();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            loader = new Loader(jarUrls);
            Thread.currentThread().setContextClassLoader(loader);
        }
    }

    protected String getProjectPath(){
        return new File("")
                .getAbsolutePath()
                .concat(File.separator)
                .concat("webapps")
                .concat(File.separator)
                .concat("z");
    }


    protected void loadJava(){

        String projectPath = getProjectPath()
                .concat(File.separator)
                .concat("WEB-INF")
                .concat(File.separator)
                .concat("classes");

        File folder = new File(projectPath);
        File[] folderFiles = folder.listFiles();
        traverseDispatch(folderFiles);

//        try {
//            ServletContextEvent sce = new ServletContextEvent(context);
//
//            for (Object obj : startupEvents) {
//                Method invoke = obj.getClass().getMethod("contextInitialized", ServletContextEvent.class);
//                invoke.invoke(obj, sce);
//            }
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }

    }

    protected void traverseDispatch(File[] folderFiles){
        for(File file : folderFiles){
            if(file.isDirectory()){
                File[] folder = file.listFiles();
                traverseDispatch(folder);
                continue;
            }

            try {
                if (file.getName().endsWith(".class")) {
                    URL url = getUrlPath(file);
                    loader.addFile(url);

//                    Class cls = loader.loadClass(getClassName(file));
//                    if(cls.isAnnotationPresent(WebListener.class)){
//                        Object obj = cls.getConstructor().newInstance();
//                        startupEvents.add(obj);
//                    }
                }
            }catch (Exception ex){}

        }
    }

    protected String getClassName(File file){
        String separator = System.getProperty("file.separator");
        String regex = "classes\\\\" + separator;
        String[] pathParts = file.getPath().split(regex);
        String name = pathParts[1]
                .replace("\\", ".")
                .replace("/",".")
                .replace(".class", "");
        return name;
    }

    protected URL getUrlPath(File file) throws MalformedURLException {
        String baseUrl = file.getAbsolutePath();
        String[] urlParts = baseUrl.split("classes");
        return new URL("file:" + urlParts[0] + "classes" + File.separator);
    }

}