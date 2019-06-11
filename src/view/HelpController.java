package view;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;

public class HelpController {

    Stage stage;

    @FXML
    private TextArea helpArea;

    @FXML
    private void initialize(){
        File file = new File("resources/source/readme.txt");
        try {
            String line;
            BufferedReader fileOut = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf8"));

            while ((line = fileOut.readLine()) != null){
                helpArea.appendText(line + "\n");
            }

            fileOut.close();
            file = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        helpArea.positionCaret(0);
    }

    public void setStage(Stage stage){ this.stage = stage; }
}
