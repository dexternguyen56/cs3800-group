package application;
import java.io.*; 
import java.net.*; 
public class Client {
    public static final String PORT_NUMBER = "80";
    public static final String HOST_NAME = "Hi";
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedReader bufferedWriter;
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
    		bufferedWriter.write(username);
    		bufferedWriter.newLine();
    		bufferedWriter.flush();
    		//Send message to UI
    		
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
    	
    }