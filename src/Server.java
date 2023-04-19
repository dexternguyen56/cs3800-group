import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Server {
  private ServerSocket serverSocket;

  TimeHeap messageHeap = new TimeHeap();

  // Creating synchronized hashmap
  Map<String, ClientSocket> synchronizedMap = Collections.synchronizedMap(new HashMap<String, ClientSocket>());

  public Server(ServerSocket serverSocket) {
    this.serverSocket = serverSocket;
  }

  public void startServer() {
    try {
      while (!serverSocket.isClosed()) {
        Socket socket = serverSocket.accept();
        System.out.println("A new user has joined");

        ClientSocket ClientSocket = new ClientSocket(socket, messageHeap, synchronizedMap);
        Thread thread = new Thread(ClientSocket);
        thread.start();

      }
    } catch (IOException e) {

    }
  }

  public void broadCast(String msg) {
    // Iterate over the entries and send the msg to all user
    for (Map.Entry<String, ClientSocket> entry : synchronizedMap.entrySet()) {
      String user = entry.getKey();
      ClientSocket client = entry.getValue();
      client.sendMessage(msg);
      
    }
  }

  public void processResponse() {

    synchronized (messageHeap) {
      while (!messageHeap.isEmpty()) {
        String request = messageHeap.removeFromQueue();

        String fields[] = request.split(",");
        String tag = fields[0];
        String msg = fields[1];
        String time = fields[2];
        String username = fields[3];

        

        if (tag.equals("message")) {
          String response = msg.contains("@Server") ? msg : username + ": " + msg;
          broadCast(Utility.formmatPayload("message", response, time));

        } else if (tag.equals("disconnect")) {
          String leftTime = Utility.getCurrentTime();
          synchronizedMap.get(username).sendMessage(Utility.formmatPayload("disconnect", "@Server: Goodbye!", leftTime));
          synchronizedMap.get(username).close();
          synchronizedMap.remove(username);
          broadCast(Utility.formmatPayload("message", "@Server: " + username + " has left the chat!", leftTime));

        }

      }
    }
    try {
      Thread.sleep(100); // wait for 100 milliseconds before checking the list again
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }


  public void closeServer() {
    try {
      if (serverSocket != null) {
        serverSocket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
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

    } catch (IOException e) {
      System.out.print("Hey");
    }
  }
}
