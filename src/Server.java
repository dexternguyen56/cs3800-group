import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import java.util.List;
import java.util.Map;


public class Server {
  private ServerSocket serverSocket;



  List<String> synchronizedList = Collections.synchronizedList(new ArrayList<String>());

  // Creating synchronized hashmap
  Map<String, ClientHandler> synchronizedMap = Collections.synchronizedMap(new HashMap<String, ClientHandler>());


  public Server(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  public void startServer() {
    try {
      while (!serverSocket.isClosed()) {
        Socket socket = serverSocket.accept();
        System.out.println("A new user has joined");

        ClientHandler clientHandler = new ClientHandler(socket, synchronizedList, synchronizedMap);
        Thread thread = new Thread(clientHandler);
        thread.start();

      }
    } catch(IOException e) {
      
    }
  }


  public void broadCast(String msg){
    // Iterate over the entries
    for (Map.Entry<String, ClientHandler> entry : synchronizedMap.entrySet()) {
      String name = entry.getKey();
      ClientHandler client = entry.getValue();
      client.sendMessage(msg);
      // ...
    }
  }



  public void processResponse(){

      synchronized(synchronizedList) {
        while(!synchronizedList.isEmpty()) {
            String request = synchronizedList.remove(0);

            String fields[] = request.split(",");
            String tag = fields[0];
            String msg = fields[1];
            String time = fields[2];
            String username = fields[3];

            if (tag.equals("message") ){
              String response = msg.contains("@Server") ? msg : username +": " + msg;
              broadCast(payload("message", response, time));

            }
            else if(tag.equals("disconnect")){
              String leftTime = getTime();
              synchronizedMap.get(username).sendMessage(payload("disconnect", "@Server: Goodbye!" , leftTime));
              synchronizedMap.get(username).close();
              synchronizedMap.remove(username);
              broadCast(payload("message","@Server: " + username + " has left the chat!", leftTime));
             
            }

        }
    }
    try {
        Thread.sleep(100); // wait for 100 milliseconds before checking the list again
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
  }

  public String payload(String tag, String msg,  String time ){
    String []response = {tag,msg,time};
    return String.join(",", response);
    
}



  public void closeServer() {
    try {
      if (serverSocket != null) {
        serverSocket.close();
      }
    } catch(IOException e) {
      e.printStackTrace();
    }
  }

  public String getTime() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
      "[hh:mm:ss a]"
    );

    return "["+ now.format(formatter) +"] ";
  }

  public static void main(String[] args) {
    try {
      // Create the server object
      ServerSocket serverSocket = new ServerSocket(1234);
      Server server = new Server(serverSocket);

      // Create a new thread to run the startServer() method
      Thread serverThread = new Thread(() -> {
          server.startServer();
      });

      // Start the server thread
      serverThread.start();

      // Create a new thread to run the processResponse() method
      Thread processThread = new Thread(() -> {
          while (!serverSocket.isClosed()) {
              server.processResponse();
          }
      });

      // Start the process thread
      processThread.start();


    } catch(IOException e) {
      System.out.print("Hey");
    }
  }
}
