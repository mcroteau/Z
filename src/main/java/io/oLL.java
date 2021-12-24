package io;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

// Read the full article https://dev.to/mateuszjarzyna/build-your-own-http-server-in-java-in-less-than-one-hour-only-get-method-2k02
public class oLL {

    public static final int DEFAULT_BUFFER_SIZE = 8192;

    public static void main( String[] args ) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(3001, 4000)) {
            while (true) {
                try (Socket client = serverSocket.accept()) {
                    if(client.isConnected())
                        handleClient(client);
                }
            }
        }
//        ServerSocket serverSocket = new ServerSocket(3001, 3000);
//        while (true) {
//            System.out.print(".");
//            Socket socket = serverSocket.accept();
//            SocketHandler h = new SocketHandler(socket);
//            h.run();
//        }
    }

    private static void handleClient(Socket client) throws IOException {
        InputStream inputStream = client.getInputStream();
        String start = "\n...";
        String end = "\n...";
        List<InputStream> streams = Arrays.asList(
                            new ByteArrayInputStream(start.getBytes()),
                            inputStream,
                            new ByteArrayInputStream(end.getBytes()));

        InputStream story = new SequenceInputStream(Collections.enumeration(streams));
        BufferedReader br = new BufferedReader(new InputStreamReader(story));

//
//        String request = "";
//        int ch;
//        while ((ch = br.read()) > -1) {
//            char c = (char)ch;
//            System.out.println(c);
//            request += c;
//        }
//
//        System.out.println(request);

        while(!br.ready()){
            try {
                Thread.sleep(10);
            }catch(Exception ex){}
        }

        try{

            byte[] buff = new byte[1024 * 9];

            ByteArrayOutputStream bao = new ByteArrayOutputStream();

            int bytesRead;
            String dataString = "";
            do {
                bytesRead = inputStream.read(buff);
                bao.write(bytesRead);
                dataString += new String(buff, 0, bytesRead);
            }while(inputStream.available() > 0);

            System.out.println("MESSAGE: " + dataString);

        }catch (Exception e) {
            e.printStackTrace();
        }

        OutputStream clientOutput = client.getOutputStream();
        clientOutput.write(("HTTP/1.1 \r\n" + "200 OK").getBytes());
        clientOutput.write(("ContentType: " + "text/html" + "\r\n").getBytes());
        clientOutput.write("\r\n".getBytes());
        clientOutput.write("Hello World!".getBytes());
        clientOutput.write("\r\n\r\n".getBytes());
        clientOutput.flush();
        if(client.isConnected())
            client.close();



//        sendResponse(client, "200 OK", "text/html", Files.readAllBytes(Paths.get("webapp", "upload.html")));
//

//        StringBuilder requestBuilder = new StringBuilder();
//        String line;
//        while (!(line = br.readLine()).isBlank()) {
//            requestBuilder.append(line + "\r\n");
//        }
//
//        String request = requestBuilder.toString();
//        String[] requestsLines = request.split("\r\n");
//        String[] requestLine = requestsLines[0].split(" ");
//        String method = requestLine[0];
//        String path = requestLine[1];
//        String version = requestLine[2];
//        String host = requestsLines[1].split(" ")[1];
//
//        List<String> headers = new ArrayList<>();
//        for (int h = 2; h < requestsLines.length; h++) {
//            String header = requestsLines[h];
//            headers.add(header);
//        }
//
//        String accessLog = String.format("Client %s, method %s, path %s, version %s, host %s, headers %s",
//                client.toString(), method, path, version, host, headers.toString());
//        System.out.println(accessLog);
//
//
//        Path filePath = getFilePath(path);
//        if (Files.exists(filePath)) {
//            // file exist
//            String contentType = guessContentType(filePath);
//            sendResponse(client, "200 OK", contentType, Files.readAllBytes(filePath));
//        } else {
//            // 404
//            byte[] notFoundContent = "<h1>Not found :(</h1>".getBytes();
//            sendResponse(client, "404 Not Found", "text/html", notFoundContent);
//        }
    }

    protected static void keepReading(String line, BufferedReader br) throws IOException {
        line = br.readLine();
        System.out.println(line);
        line = br.readLine();
        System.out.println(line);
        line = br.readLine();
        System.out.println(line);
    }


    private static String convertInputStreamToString(InputStream is) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }

        // Java 1.1
        return result.toString(StandardCharsets.UTF_8.name());

        // Java 10
//         return result.toString(StandardCharsets.UTF_8);

    }

    private static void sendResponse(Socket client, String status, String contentType, byte[] content) throws IOException {
        OutputStream clientOutput = client.getOutputStream();
        clientOutput.write(("HTTP/1.1 \r\n" + status).getBytes());
        clientOutput.write(("ContentType: " + contentType + "\r\n").getBytes());
        clientOutput.write("\r\n".getBytes());
        clientOutput.write("Hello World!".getBytes());
        clientOutput.write("\r\n\r\n".getBytes());
        clientOutput.flush();
        client.close();
    }

    private static Path getFilePath(String path) {
        if ("/".equals(path)) {
            path = "/index.html";
        }

        return Paths.get("/webapp", path);
    }

    private static String guessContentType(Path filePath) throws IOException {
        return Files.probeContentType(filePath);
    }

}