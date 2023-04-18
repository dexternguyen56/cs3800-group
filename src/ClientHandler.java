import java.io.*;
import java.net.Socket;
import java.sql.Time;
import java.util.List;
import java.util.Map;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ClientHandler implements Runnable {

  // public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;
  private String username;

  private Socket socket;
  private TimeHeap timeHeap;
  private Map userMap;
  private String request;

  
  public ClientHandler (Socket socket, TimeHeap timeHeap,  Map userName){
    try{
      this.socket = socket;
      this.timeHeap = timeHeap;
      this.userMap = userName;

      this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

      sendMessage(payload("username", "@Server: Enter your username" , getTime()));
          
    } catch(IOException e) {
      close();
    }
  }

  public void setUsername(String name){
    username = name;
  }


  public void sendMessage(String message){
      try{
        bufferedWriter.write(message);
        bufferedWriter.newLine();
        bufferedWriter.flush();
      }catch(IOException e){
        close();
      }   
  }

  public void listenToMessage() {
    String msgFromClient = ""; 
    try {
        while (socket.isConnected()) {
            if (bufferedReader.ready()) {

                msgFromClient = bufferedReader.readLine();
                System.out.println("Request: " +msgFromClient);

                if (username != null){
                  timeHeap.addToQueue(LocalDateTime.now(), (msgFromClient + "," + username));;
                }
                else{
                  processUsername(msgFromClient); 
                }
            }
        }
    } catch (IOException e) {
        close();
    }
  }

  public void processUsername(String request){
    String fields[] = request.split(",");

    String tag = fields[0];
    String msg = fields[1];
    String time = fields[2];

    if(tag.equals("username")){
      
      if(!userMap.containsKey(msg)){
        username = msg;
        userMap.put(username, this);

        sendMessage(payload("username", msg , time));
        timeHeap.addToQueue(LocalDateTime.now(), payload("message", "@Server: " + username + " has join the chat!", time) +  "," + username);;


      }
      else{
        sendMessage(payload("username", "@Server: Enter a different username" , getTime()));
      }

    }

  }


  public String getTime() {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
      "[hh:mm:ss a]"
    );

    return "["+ now.format(formatter) +"] ";
  }

  


public void close(){
  try {
      
      if(bufferedReader != null) {
          bufferedReader.close();
      }
      if(bufferedWriter != null) {
          bufferedWriter.close();
      }
      if(socket != null) {
          socket.close();
      }
  }
  catch(IOException e) {
      e.printStackTrace();
  }
}

  public String payload(String tag, String msg,  String time ){
    String []response = {tag,msg,time};
    return String.join(",", response);

  }


  @Override
  public void run() {
    listenToMessage();
  }

  
}
