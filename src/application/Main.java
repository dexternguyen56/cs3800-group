package application;



import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private TextArea chatArea;
    private ArrayList<String> chatMessages = new ArrayList<>();
    private boolean promptTextFlag = false;
    TextField messageField; 
    Button sendButton ;
    String userName = "Client";
    String promptText = "Enter your message here...";

    @Override
    public void start(Stage primaryStage) {
        try {




            // Create the UI components
            BorderPane root = new BorderPane();
            chatArea = new TextArea();
            chatArea.setEditable(false);
            chatArea.getStyleClass().add("chat-area");

            messageField = new TextField();
            messageField.getStyleClass().add("message-field");
            messageField.setPromptText(promptText);

            // Create send Button
            sendButton = new Button("Send");
            sendButton.getStyleClass().add("send-button");


            chatArea.appendText("@Server: Enter your username\n");

            // Display the example chat messages in the chat area
            // TODO: Render all current chatMessages
            for (String message : chatMessages) {
                chatArea.appendText(message);
            }




            // Create the vertical layout for the chat box + Horizontal box
            VBox chatBox = new VBox(chatArea, new HBox(messageField, sendButton));
            chatBox.getStyleClass().add("chat-box");

            // Set margin for the send button
            HBox.setMargin(sendButton, new Insets(0, 0, 0, 10));

            // Add the chat box to the root
            root.getStyleClass().add("root");
            root.setCenter(chatBox);

            // Create the Scene and set it as the content of the primary Stage
            Scene scene = new Scene(root, 535, 250);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.setTitle(userName);
            primaryStage.show();

            // Event handler for messageField to handle the Enter key
            messageField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    sendMessage(messageField.getText());
                    messageField.clear();
                }
            });

            // Event handler for sendButton 
            sendButton.setOnAction(event -> {
                sendMessage(messageField.getText());
                messageField.clear();
            });



            // TODO: Create a thread to hand incoming messages from server -> updateUI
            //updateChatBox(msg);



        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void sendMessage(String message) {
    	if (message.length() == 0) {
    		return;
    	}



    	// TODO: Send the message to the server

    	// NOTE: Remove this. Demo ONLY
    	updateChatBox(message);


    	// Sign off
    	if (message.equals(".")) {
            signOff();
       	}

    }

    private void updateChatBox(String message){
    	  // NOTE: Update the prompt message
        if (promptTextFlag){
        	promptText = "Enter your message here...\n";
        	messageField.setPromptText(promptText);
        }

        chatMessages.add(userName + ": " + message + "\n");
        chatArea.appendText(userName + ": " +  message + "\n");

    }

    private void signOff() {
    	messageField.setDisable(true);
    	sendButton.setDisable(true);

    	// TODO: send a sign off request and wait for server response 
    	chatArea.appendText("@Server: Goodbye!\n");

    }

    public static void main(String[] args) {
        launch(args);
    }
}