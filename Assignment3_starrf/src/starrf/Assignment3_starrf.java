package starrf;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Assignment3_starrf extends Application
{
    ///////////////////////////////////////////////////////////////////////////
    // entry-point of JavaFX application
    @Override
    public void start(Stage stage) throws Exception
    {
        Parent root = FXMLLoader.load(getClass().getResource("MainForm.fxml"));
        Scene scene = new Scene(root);
        //my title
        stage.setTitle("The Grid");
        stage.setScene(scene);
        stage.show();
    }

    ///////////////////////////////////////////////////////////////////////////
    public static void main(String[] args)
    {
        launch(args);
    }
}