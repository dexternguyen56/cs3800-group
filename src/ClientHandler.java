import java.io.*;
import java.net.Socket;
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
      sendMessage("@Server: " +username + " has joined the chat");      
    } catch(IOException e) {
      System.out.print("Error");
    }
  }



  public void sendMessage(String message){
    for (ClientHandler clientHandler: clientHandlers){
      try{
        clientHandler.bufferedWriter.write(message);
        clientHandler.bufferedWriter.newLine();
        clientHandler.bufferedWriter.flush();
      }catch(IOException e){
        System.out.print("error");
      }
    }
  }



  @Override
  public void run() {
    String inputLine;
    try {
      while ((inputLine = bufferedReader.readLine()) != null) {
        sendMessage(username + ": " + inputLine);
      }

      if (inputLine.equals(".")){
        socket.close();
        clientHandlers.remove(this);
        sendMessage(username + " has left the chat");
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  
}
