package io;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class HttpRunnable implements Runnable {

    int idx = 0;
    ServerSocket serverSocket;

    Map<String, Object> routes;
    Map<String, Boolean> sockets;

    public HttpRunnable(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        this.routes = new HashMap<>();
    }

    @Override
    public void run() {
        Socket clientSocket = null;
        try {
//            serverSocket.accept();
//            clientSocket = new Socket(InetAddress.getByName("127.0.0.1"),8080);
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

            output.write("HTTP/1.1 200 Ok\r\n".getBytes());
            output.write(("Content-Length: " + "Nasty!".length() + "\r\n").getBytes());
            output.write("Content-Type: text/html\r\n".getBytes());
            output.write("\r\n".getBytes());
            output.write("Nasty!".getBytes());
            output.write("\r\n\r\n".getBytes());
            output.flush();

            clientSocket.close();

        }catch (IOException ex) {
            ex.printStackTrace();
        }


    }
}
