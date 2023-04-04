import java.io.*; 
import java.net.*; 

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server listening on port 5000");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String request = inFromClient.readLine();
                System.out.println("Request: " + request);
                String time = TimeClient.getCurrentTime();
                OutputStream outToClient = clientSocket.getOutputStream();
                // Response message
                outToClient.write("HTTP/1.1 200 OK\r\n".getBytes());
                    outToClient.write("Content-Type: text/html\r\n\r\n".getBytes());
                    String gmtTime = convertTime(time, "GMT");
                    String estTime = convertTime(time, "EST");
                    String pstTime = convertTime(time, "PST");
                    
                    String html = "<html><body><h1>GMT: "+ gmtTime+ "</h1>"
                                + "<h1>EST: " + estTime + "</h1>"
                                + "<h1>PST: " + pstTime + "</h1>"
                                + "</body></html>";
                    
                    outToClient.write(html.getBytes());
                
                outToClient.flush();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
