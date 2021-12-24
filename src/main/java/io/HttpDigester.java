package io;

import java.io.*;

public class HttpDigester {

    final int BYTE_SIZED = 1024 * 9;

    byte[] bytes;
    InputStream input;
    ByteArrayOutputStream bao;

    public HttpDigester(InputStream input){
        this.input = input;
        this.bao = this.copy(input);
        this.bytes = new byte[this.BYTE_SIZED];
    }

    public HttpDigester digest() throws IOException {
        /**
         * 
         */

        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        Boolean isCookie = false;
        while (!(line = br.readLine()).equals("\r")) {
            if(isCookie) {
                break;
            }
            requestBuilder.append(line + "\r\n");
            System.out.println(line);
            if (line.contains("Cookie:")) isCookie = true;
        }

        System.out.println("z..");


        return this;
    }

    public ByteArrayOutputStream copy(InputStream inputStream) throws IOException {
        int bytesRead;
        do {
            bytesRead = inputStream.read(bytes);
            if(bytesRead != -1)
                bao.write(bytes, 0, bytesRead);
        }while(inputStream.available() > 0);
        return bao;
    }

}
