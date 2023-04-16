package application;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
  private ServerSocket serverSocket;

  public Server(ServerSocket serverSocket){
    this.serverSocket = serverSocket;
  }

  public void startServer(){
    try{
      while (!serverSocket.isClosed()){
        Socket socket = serverSocket.accept();
        System.out.println("A new user has joined");
        ClientHandler clientHandler = new ClientHandler(socket);
      }
    } catch(IOException e){
      
    }
  }

  public void closeServer(){
    try {
      if (serverSocket != null){
        serverSocket.close();
      }
    } catch(IOException e){
      e.printStackTrace();
    }
  }
  public static void main(String [] args){
    try{
      ServerSocket serverSocket = new ServerSocket(1234);
      Server server = new Server(serverSocket);
      server.startServer();
    } catch(IOException e){
      System.out.print("Hey");
    }
  }
}
