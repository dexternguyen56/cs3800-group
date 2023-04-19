import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Map;

public class ClientSocket implements Runnable {

  private BufferedReader bufferedReader;
  private BufferedWriter bufferedWriter;
  private String username;

  private Socket socket;
  private TimeHeap msgList;
  private Map userMap;

  public ClientSocket(Socket socket, TimeHeap msgList, Map userName) {
    try {
      this.socket = socket;
      this.msgList = msgList;
      this.userMap = userName;

      // Initialize bufferedWriter to send messages to the client
      this.bufferedWriter =
        new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
      // Initialize bufferedReader to read messages from the client
      this.bufferedReader =
        new BufferedReader(new InputStreamReader(socket.getInputStream()));

      // Send initial username request to the client
      sendMessage(
        Utility.formmatPayload(
          "username",
          "@Server: Enter your username",
          Utility.getCurrentTime()
        )
      );
    } catch (IOException e) {
      close();
    }
  }

  public void setUsername(String name) {
    username = name;
  }

  public synchronized void sendMessage(String message) {
    try {
      // Send message to the client
      bufferedWriter.write(message);
      bufferedWriter.newLine();
      bufferedWriter.flush();
    } catch (IOException e) {
      close();
    }
  }

  public void listenToMessage() {
    String msgFromClient = "";
    try {
      while (socket.isConnected()) {
        if (bufferedReader.ready()) {
          msgFromClient = bufferedReader.readLine();
          System.out.println("Request: " + msgFromClient);

          String fields[] = msgFromClient.split(",");

          String tag = fields[0];
          String msg = fields[1];
          String time = fields[2];

          LocalDateTime convertedTime = Utility.stringToLocalDateTime(time);

          if (username != null) {
            msgList.addToQueue(convertedTime, msgFromClient + "," + username);
          } else {
            processUsername(msgFromClient);
          }
        }
      }
    } catch (IOException e) {
      close();
    }
  }

  public void processUsername(String request) {
    String fields[] = request.split(",");

    String tag = fields[0];
    String msg = fields[1];
    String time = fields[2];

    if (tag.equals("username")) {
      if (!userMap.containsKey(msg)) {
        username = msg;
        userMap.put(username, this);

        // Send confirmation of username to the client
        sendMessage(Utility.formmatPayload("username", msg, time));
        // Add a join message to the message queue
        msgList.addToQueue(
          LocalDateTime.now(),
          Utility.formmatPayload(
            "message",
            "@Server: " + username + " has joined the chat!",
            time
          ) +
          "," +
          username
        );
      } else {
        // Send error message for duplicate username
        sendMessage(
          Utility.formmatPayload(
            "username",
            "@Server: Enter a different username",
            Utility.getCurrentTime()
          )
        );
      }
    }
  }

  public void close() {
    try {
      if (bufferedReader != null) {
        bufferedReader.close();
      }
      if (bufferedWriter != null) {
        bufferedWriter.close();
      }
      if (socket != null) {
        socket.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    listenToMessage();
  }
}
