import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import view.EncoderOverviewController;

import java.io.IOException;

public class Encoder extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage){
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Encoder");
        this.primaryStage.getIcons().add(new Image("file:resources/images/encoder_icon.png"));
        this.primaryStage.setResizable(false);

        showEncoderOverview();
    }

    private void showEncoderOverview(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/EncoderView.fxml"));
            AnchorPane encoderOverview = (AnchorPane) loader.load();

            EncoderOverviewController controller = loader.getController();
            controller.setPrimaryStage(this.primaryStage);

            Scene scene = new Scene(encoderOverview);
            primaryStage.setScene(scene);

            controller.setHotKey();
            controller.setCloseCheck();
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}