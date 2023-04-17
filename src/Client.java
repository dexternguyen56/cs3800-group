import java.io.*;
import java.net.*;
import java.util.Scanner;

import javafx.stage.Stage;
import javafx.application.Application;
import javafx.application.Platform;

public class Client {


    public static void main(String[] args) throws IOException{

		// Client client = new Client("localhost", 1234);
        // client.start();

		for (int i = 0; i < 3; i++) {
			Main ui = new Main();

			Platform.startup(() -> {
           		ui.start(new Stage());
        	});
			
		}


    }
}
