package io;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HttpRunnableGross implements Runnable {

    int idx = 0;
    ServerSocket serverSocket;

    Map<String, Object> routes;
    Map<String, Boolean> sockets;

    public HttpRunnableGross(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.routes = new HashMap<>();
    }

    @Override
    public void run() {
        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("concurrent." + clientSocket.hashCode());

        idx++;
        InputStream socketInput = null;
        InputStream headersInput= null;
        InputStream clientInput = null;
        OutputStream output = null;

        try {

            output = clientSocket.getOutputStream();
            socketInput  = clientSocket.getInputStream();
//40460134
            System.out.println("si " + socketInput);

            StringBuilder payload = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(socketInput));
            while(br.ready()){
                payload.append((char) br.read());
            }
            System.out.println("Payload data is: "+payload.toString());



//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            socketInput.transferTo(baos);

// Code simulating the copy
// You could alternatively use NIO
// And please, unlike me, do something about the Exceptions :D
//            byte[] buffer = new byte[512];
//            int len;
//            System.out.println("here...");
//            while ((len = socketInput.read(buffer)) > -1 ) {
//                baos.write(buffer, 0, len);
//            }
//            baos.flush();
//
//            headersInput = new ByteArrayInputStream(baos.toByteArray());
//            clientInput = new ByteArrayInputStream(baos.toByteArray());

            long time = System.currentTimeMillis();
            System.out.println("\n\n\n**************************************");

//            byte[] bytes = input.readNBytes(1);
//            System.out.println(bytes.length);

//            input.read(buffer);
//            String z = new String(input.readAllBytes());
//            System.out.println("z " + z);



//            StringBuilder requestBuilder = new StringBuilder();
//            String line;
//            while (!(line = br.readLine()).equals("")) {
//                requestBuilder.append(line + "\r\n");
//                System.out.println(line);
//            }

//            String request = requestBuilder.toString();
//            String[] requestsLines = request.split("\r\n");
//            String[] requestLine = requestsLines[0].split(" ");
//            String method = requestLine[0];
//            String path = requestLine[1];
//            String version = requestLine[2];
//            String host = requestsLines[1].split(" ")[1];
//            String[] contentLengthArray = requestsLines[3].split(":");
//            Integer contentLength = Integer.valueOf(contentLengthArray[1].trim());
//
//            List<String> headers = new ArrayList<>();
//            for (int h = 2; h < requestsLines.length; h++) {
//                String header = requestsLines[h];
//                headers.add(header);
//            }

//            System.out.println("c -> " +contentLength);

//            BufferedReader br2 = new BufferedReader(new InputStreamReader(clientInput));
//            StringBuilder payload = new StringBuilder();

//            int rc = clientInput.read(buffer);
//            while(rc != -1)
//            {
//                rc = clientInput.read(buffer);
//                System.out.println(rc);
////                payload.append((char) br2.read());
//            }


            HttpRequest req = new HttpRequest();
            HttpResponse resp = new HttpResponse();


            SimpleDateFormat sdf = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss z");
            Date date = new Date();
            String dateFormat = sdf.format(date);

            if ("test".startsWith("/z")) {

                String route = "test".replaceFirst("/z", "");
                System.out.println("route: " + route);
                for (Map.Entry<String, Object> entry : routes.entrySet()) {
                    System.out.println(entry.getKey());
                }

                Object servlet = routes.get(route);

                System.out.println("serve ::: " + servlet);

                Object[] parameters = {req, resp};

                //                    Method init = servlet.getClass().getDeclaredMethod("init", ServletConfig.class);
                //                    init.setAccessible(true);
                //                    init.invoke(servlet, config);

                //                    Method action = servlet.getClass().getDeclaredMethod("doGet", HttpServletRequest.class, HttpServletResponse.class);
                //                    action.setAccessible(true);

                //                    Object html = action.invoke(servlet, parameters);
                String html = "Hello World!";

                output.write("HTTP/1.1 200 OK\r\n".getBytes());
                output.write(("Date: " + dateFormat + "\r\n").getBytes());
                output.write(("Content-Length: " + html.length() + "\r\n").getBytes());
                output.write("Content-Type: text/html\r\n".getBytes());
                output.write("\r\n".getBytes());
                output.write(html.getBytes());
                output.write("\r\n\r\n".getBytes());
                output.flush();
                output.close();
            } else {
                output.write("HTTP/1.1 404 Not Found\r\n".getBytes());
                output.write(("Date: " + dateFormat + "\r\n").getBytes());
                output.write(("Content-Length: " + "404".length() + "\r\n").getBytes());
                output.write("Content-Type: text/html\r\n".getBytes());
                output.write("\r\n".getBytes());
                output.write("404".getBytes());
                output.write("\r\n\r\n".getBytes());
                output.flush();
                output.close();
            }


            System.out.println("**********************************\n\n\n");

        } catch (Exception e) {
            e.printStackTrace();
            try {
                output.write("HTTP/1.1 404 Not Found\r\n".getBytes());
                output.write(("Content-Length: " + "404".length() + "\r\n").getBytes());
                output.write("Content-Type: text/html\r\n".getBytes());
                output.write("\r\n".getBytes());
                output.write("404".getBytes());
                output.write("\r\n\r\n".getBytes());
                output.flush();
                output.close();
            } catch (Exception ex) {
                System.out.println("We have a problem?");
            }
        }

        try {
//            clientInput.close();
//        headersInput.close();
            socketInput.close();
            clientSocket.close();
        }catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}
