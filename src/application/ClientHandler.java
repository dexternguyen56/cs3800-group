import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler {

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
      
    } catch(Exception e) {
      System.out.print("ERror");
    }
  }

   public void sendMessage(String message){
    for (ClientHandler clientHandler: clientHandlers){
      try{
        clientHandler.bufferedWriter.write(message);
        clientHandler.bufferedWriter.newLine();
        clienthandler.bufferedWriter.flush();
      }except(IOException e){
        System.out.print("error");
      }
    }
  }
  
}
