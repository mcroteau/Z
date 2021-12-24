package io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class SocketHandler extends Thread{

    Socket clientSock;
    public SocketHandler(Socket sock){
        clientSock = sock;
    }

    public void run(){
        try {

            InputStream input = clientSock.getInputStream();
            OutputStream output = clientSock.getOutputStream();
            DataInputStream dataInput = new DataInputStream(input);
            Scanner scanner = new Scanner(dataInput);
            while(scanner.hasNext()){
                System.out.println(scanner.nextLine());
            }

            output.write("HTTP/1.1 200 Ok\r\n".getBytes());
            output.write(("Content-Length: " + "Ok".length() + "\r\n").getBytes());
            output.write("Content-Type: text/html\r\n".getBytes());
            output.write("\r\n".getBytes());
            output.write("Ok".getBytes());
            output.write("\r\n\r\n".getBytes());
            output.flush();

        }catch(Exception ex){
            ex.printStackTrace();
        }


        try {
            if(clientSock.isConnected())
                clientSock.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}