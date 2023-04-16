import java.io.*;
import java.net.*;
import java.util.Scanner;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.application.Platform;

public class Client {
    public static final String PORT_NUMBER = "1234";
    public static final String HOST_NAME = "localhost";
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private boolean set;
	private Main ui ;

    public Client(String host, Integer port){
        try{
            this.socket = new Socket(host, port);;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = "default-name";
            set = true;

			// UI
			ui = new Main();
			Platform.startup(() -> {
				ui.start(new Stage());
			});
	
        }
        catch(IOException e){
			System.out.println("Unable to connect to server at " + host + ":" + port);
            e.printStackTrace();
            close(socket, bufferedReader, bufferedWriter);
        }
    }

    // public void sendMessage(String message, String tag) {
    //     try {
    //         // bufferedWriter.write(username);
    //         bufferedWriter.newLine();
    //         bufferedWriter.flush();
    //         while(socket.isConnected()){
    //             bufferedWriter.write(message);
    //             bufferedWriter.newLine();
    //             bufferedWriter.flush();
    //         }
    //     }
    //     catch(IOException e) {
    //         close(socket, bufferedReader, bufferedWriter);
    //     }
    // }

    public void sendMessage() {
        try {
            // bufferedWriter.write(username);
            // bufferedWriter.newLine();
            // bufferedWriter.flush();
            while(socket.isConnected()){
	
				if(ui.getMessage().length() > 0)
				{
					System.out.println("message from UI: "+ ui.getMessage());
					bufferedWriter.write(ui.getMessage());
					bufferedWriter.newLine();
					bufferedWriter.flush();
                    ui.setMessage("");

				}
   
            }
        }
        catch(IOException e) {
            close(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenToMessage() {
        String msgFromGroupChat;
        try {
            while(socket.isConnected()) {
                if(bufferedReader.ready()) {
                    msgFromGroupChat = bufferedReader.readLine();
					ui.updateChatBox(msgFromGroupChat);
                }
            }
        }
        catch(IOException e) {
            close(socket, bufferedReader, bufferedWriter);
        }
    }

    public void start() {
        Thread sendMessageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                sendMessage();
            }
        });  
		

		Thread listenToMessageThread = new Thread(new Runnable() {
            @Override
            public void run() {
                listenToMessage();
            }
        }); 

   		sendMessageThread.start();
        listenToMessageThread.start();

		// sendMessage();
		// listenToMessage();

    }

    public void close(Socket socket, BufferedReader reader, BufferedWriter writer){
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

    public static void main(String[] args) throws IOException{

		Client client = new Client("localhost", 1234);
        client.start();


    }
}
