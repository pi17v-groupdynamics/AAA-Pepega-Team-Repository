package view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import view.logic.EncryptionFilesService;

import java.io.File;

public class EncryptionFilesController {

    @FXML
    private Button chooseFileBut;

    @FXML
    private TextField keyField;

    @FXML
    private HBox keyBox;

    @FXML
    private Label messageLabel;

    @FXML
    private ProgressBar progressEncryption;

    @FXML
    private Button startOrPauseBut;

    @FXML
    private ToggleButton modeEnBut;

    @FXML
    private ToggleButton modeDeBut;

    @FXML
    private ToggleGroup mode;

    @FXML
    private HBox modeBox;

    final KeyCombination plusKey = new KeyCodeCombination(KeyCode.P, KeyCombination.SHIFT_DOWN);

    final KeyCombination minusKey = new KeyCodeCombination(KeyCode.M, KeyCombination.SHIFT_DOWN);

    final KeyCombination modeEncKey = new KeyCodeCombination(KeyCode.E, KeyCombination.SHIFT_DOWN);

    final KeyCombination modeDecKey = new KeyCodeCombination(KeyCode.D, KeyCombination.SHIFT_DOWN);

    final KeyCombination openFileKey = new KeyCodeCombination(KeyCode.O, KeyCombination.SHIFT_DOWN);

    final KeyCombination startKey = new KeyCodeCombination(KeyCode.S, KeyCombination.SHIFT_DOWN);

    private Stage stage;

    private File dataFile, encryptedFile;

    private EncryptionFilesService enFileService;

    @FXML
    private void initialize(){
        keyField.setEditable(false);
        modeEnBut.setToggleGroup(mode);
        modeDeBut.setToggleGroup(mode);
    }

    @FXML
    private void handlechooseFile(){
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Choose File");
        File selectedFile = chooser.showOpenDialog(stage);
        if(selectedFile != null){

            if(modeEnBut.isSelected())
                dataFile = selectedFile;
            else
                encryptedFile = selectedFile;

            messageLabel.textProperty().unbind();
            messageLabel.setText("Файл выбран: " + selectedFile.getName());
        }

        progressEncryption.progressProperty().unbind();
        progressEncryption.setProgress(0);
    }

    @FXML
    private void keyPlus(){ keyField.setText("" + (Integer.parseUnsignedInt(keyField.getText()) + 1));}

    @FXML
    private void keySub(){
        int key = Integer.parseUnsignedInt(keyField.getText());
        if(key > 0){
            keyField.setText("" + (key - 1));
        }
    }

    @FXML
    private void playOrPause(){

        messageLabel.textProperty().unbind();
        if(modeEnBut.isSelected() &&  dataFile == null) {
            messageLabel.setText("Файл для шифрования не выбран.");
            return;
        }
        if (modeDeBut.isSelected() && encryptedFile == null) {
            messageLabel.setText("Зашифрованный файл не выбран.");
            return;
        }

        if(enFileService != null && enFileService.isRunning()) {
            enFileService.stop();
            messageLabel.setText("Процесс отменен.");
        }else {
            changeSorPButton();
            enFileService = new EncryptionFilesService(this);
            progressEncryption.progressProperty().bind(enFileService.progressProperty());
            messageLabel.textProperty().bind(enFileService.messageProperty());
            enFileService.start();
        }
    }

    public void setStage(Stage stage){ this.stage = stage; }

    public void setHotKey(){
        stage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if(plusKey.match(event)){
                keyPlus();
            } else if(minusKey.match(event)){
                keySub();
            } else if(modeEncKey.match(event)){
                modeEnBut.setSelected(true);
            } else if(modeDecKey.match(event)){
                modeDeBut.setSelected(true);
            } else if(openFileKey.match(event)){
                handlechooseFile();
            } else if(startKey.match(event)){
                playOrPause();
            }
        });
    }

    public File getDataFile(){ return dataFile; }

    public File getEncryptedFile() { return encryptedFile; }

    public TextField getKeyField(){ return keyField; }

    public ToggleButton getModeEnBut(){ return modeEnBut; }

    public void changeSorPButton(){
        if(startOrPauseBut.getText().equals("Начать")){
            startOrPauseBut.setText("Отмена");
            startOrPauseBut.setStyle("-fx-text-fill: red");
        }else {
            startOrPauseBut.setText("Начать");
            startOrPauseBut.setStyle("-fx-text-fill: green");
        }
    }

    public void closeFile(){
        dataFile = null;
        encryptedFile = null;
    }

    public void changeDisable(){
        if(keyBox.isDisable()){
            keyBox.setDisable(false);
            modeBox.setDisable(false);
            chooseFileBut.setDisable(false);
        } else {
            keyBox.setDisable(true);
            modeBox.setDisable(true);
            chooseFileBut.setDisable(true);
        }
    }

    public boolean isRunningEncruptedThread(){

        if(enFileService != null && enFileService.isRunning())
            return true;

        return false;
    }
}
