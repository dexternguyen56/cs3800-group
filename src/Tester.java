import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Tester extends Application {

    private int numWindows = 3;

    @Override
    public void start(Stage primaryStage) throws Exception {

        for (int i = 0; i < numWindows; i++) {
            Platform.runLater(() -> {
                Main ui = new Main();
                try {
                    ui.start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
	// Add path to VM args 
    public static void main(String[] args) {
        launch(args);
    }
}
