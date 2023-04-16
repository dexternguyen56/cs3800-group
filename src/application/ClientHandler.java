import java.io.BufferedReader;
import java.net.Socket;
import java.io.BufferedWriter;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

  public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;
  private String username;
  private Socket socket;

  
  public ClientHandler (Socket socket){
    try{
      this.socket = socket;
      this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.username = bufferedReader.readLine();
      clientHandlers.add(this);
      sendMessage(username + " has joined the chat");
      
    }
  }

  
}
