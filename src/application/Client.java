package application;
import java.io.*; 
import java.net.*;
import java.util.Scanner; 
public class Client {
    public static final String PORT_NUMBER = "1234";
    public static final String HOST_NAME = "Hi";
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private boolean set;

    public Client(Socket socket, String username){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
            set = true;
        }
        catch(IOException e){
            close(socket, bufferedReader, bufferedWriter);
        }
    }
    //Send a json to server with tag and message
    public void sendMessage(String message, String tag) {
    	try {
    		bufferedWriter.write(username);
    		bufferedWriter.newLine();
    		bufferedWriter.flush();
    		Scanner scan = new Scanner(System.in);
    		
    	}
    	catch(IOException e) {
    		close(socket, bufferedReader, bufferedWriter);
    	}
    }
	public void sendMessage() {
    	try {
    		bufferedWriter.write(username);
    		bufferedWriter.newLine();
    		bufferedWriter.flush();
    		Scanner scan = new Scanner(System.in);
    		while(socket.isConnected()){
				String messageToSend = scan.nextLine();
				bufferedWriter.write(username + ": " + messageToSend);
				bufferedWriter.newLine();
				bufferedWriter.flush();
			}
    	}
    	catch(IOException e) {
    		close(socket, bufferedReader, bufferedWriter);
    	}
    }

    public void listenToMessage() {
    	new Thread(new Runnable() {
    		@Override
    		public void run() {
    			String msgFromGroupChat;
    
    		 while(socket.isConnected()) {
    			 	try {
    			 		msgFromGroupChat = bufferedReader.readLine();
    			 		//Makes UI update to have the chat
						System.out.println(msgFromGroupChat);
    			 	}
    			 	catch(IOException e) {
    			 		close(socket, bufferedReader, bufferedWriter);
    			 	}
    		 }
    		}
    	}).start();
    }
    public void broadcastMessage() {
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
			Scanner scan = new Scanner(System.in);
			System.out.println("Enter Username: ");
			String username = scan.nextLine();
			Socket socket = new Socket("localhost", 1234);
			Client client = new Client(socket, username);
			client.listenToMessage();
			client.sendMessage();
		}
    }