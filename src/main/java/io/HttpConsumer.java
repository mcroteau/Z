package io;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpConsumer implements Runnable {

    ServerSocket serverSocket;

    public HttpConsumer(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void run(){
        try (Socket client = serverSocket.accept()) {
            if(client.isConnected()){
                InputStream inputStream = client.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                while(!br.ready()){
                    Thread.sleep(10);
                }
                new HttpDigester(inputStream).digest();

                OutputStream clientOutput = client.getOutputStream();
                clientOutput.write(("HTTP/1.1 \r\n" + "200 OK").getBytes());
                clientOutput.write(("ContentType: " + "text/html" + "\r\n").getBytes());
                clientOutput.write("\r\n".getBytes());
                clientOutput.write("Hello World!".getBytes());
                clientOutput.write("\r\n\r\n".getBytes());
                clientOutput.flush();
                if(client.isConnected())
                    client.close();

            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
