package pop3Client_Prac7;

/**
 * Creators for this Main.
 * @author Vivian Laura-Lee Venter u13238435
 * @author Jason Richard Evans u13032608
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public Scene scene1;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("serverPage.fxml"));

        primaryStage.setTitle("Pop3 Client");

        scene1 = new Scene(root, 320, 200);

        primaryStage.setScene(scene1);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
