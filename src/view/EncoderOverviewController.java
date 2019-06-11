package view;

import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.logic.EncryptionThread;
import view.logic.FileThread;
import view.logic.RangeEncryptionThread;
import view.logic.SaveInFileThread;

import java.io.*;
import java.nio.file.Files;

public class EncoderOverviewController {

    @FXML
    private TextArea primordialArea;

    @FXML
    private TextArea resultArea;

    @FXML
    private TextField keyField;

    @FXML
    private ToggleButton text_coding_but;

    @FXML
    private ToggleGroup type_of_coding;

    @FXML
    private ToggleGroup type_of_file;

    @FXML
    private ToggleButton binary_coding_but;

    @FXML
    private ToggleButton mode_encode_but;

    @FXML
    private ToggleButton mode_decode_but;

    @FXML
    private ProgressIndicator readProgress;

    @FXML
    private HBox readProgressBox;

    @FXML
    private HBox keyBox;

    @FXML
    private HBox typeBox;

    @FXML
    private HBox rangeBox;

    @FXML
    private TextField leftKeyField;

    @FXML
    private TextField rightKeyField;

    @FXML
    private Button openFileRangeBut;

    @FXML
    private Button controlRangeBut;

    @FXML
    private ProgressBar progressRange;

    @FXML
    private HBox modeBox;

    @FXML
    private Button saveBut;

    @FXML
    private Button openBut;

    @FXML
    private Label messageLabel;

    @FXML
    private ImageView imageView;

    @FXML
    private Button minusBut;

    @FXML
    private Button plusBut;

    private File fileRangeSave;
    private Stage primaryStage;
    private EncryptionThread enT;
    private FileThread readFileThread;
    private SaveInFileThread saveThread;
    private RangeEncryptionThread rangeThread;
    private boolean read = true;
    private Stage fileStage, helpStage;

    final KeyCombination plusKey = new KeyCodeCombination(KeyCode.P, KeyCombination.SHIFT_DOWN);

    final KeyCombination minusKey = new KeyCodeCombination(KeyCode.M, KeyCombination.SHIFT_DOWN);

    final KeyCombination openKey = new KeyCodeCombination(KeyCode.O, KeyCombination.SHIFT_DOWN);

    final KeyCombination saveKey = new KeyCodeCombination(KeyCode.S, KeyCombination.SHIFT_DOWN);

    final KeyCombination openRageKey = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);

    final KeyCombination goKey = new KeyCodeCombination(KeyCode.G, KeyCombination.SHIFT_DOWN);

    final KeyCombination modeEnKey = new KeyCodeCombination(KeyCode.E, KeyCombination.SHIFT_DOWN);

    final KeyCombination modeDeKey = new KeyCodeCombination(KeyCode.D, KeyCombination.SHIFT_DOWN);

    final KeyCombination modeTextKey = new KeyCodeCombination(KeyCode.T, KeyCombination.SHIFT_DOWN);

    final KeyCombination modeBinaryKey = new KeyCodeCombination(KeyCode.B, KeyCombination.SHIFT_DOWN);

    final KeyCombination openFileEncKey = new KeyCodeCombination(KeyCode.F, KeyCombination.SHIFT_DOWN);

    final KeyCombination openHelpKey = new KeyCodeCombination(KeyCode.H, KeyCombination.SHIFT_DOWN);

    final KeyCombination cancelKey = new KeyCodeCombination(KeyCode.C, KeyCombination.SHIFT_DOWN);

    String numberMatcher;

    EncryptionFilesController controller;

    @FXML
    private void initialize(){
        keyField.setEditable(false);

        leftKeyField.textProperty().addListener( (obs,s,t1) -> controlInput(s, t1, leftKeyField) );
        rightKeyField.textProperty().addListener( (obs,s,t1) -> controlInput(s, t1, rightKeyField) );

        primordialArea.setFont(Font.font(12));
        primordialArea.setWrapText(true);

        resultArea.setFont(Font.font(12));
        resultArea.setWrapText(true);
        resultArea.setEditable(false);

       primordialArea.textProperty().addListener((ChangeListener<String>)
                (observable, oldValue, newValue) -> {
                    if(read) encryption();
                }
        );

        text_coding_but.setToggleGroup(type_of_coding);
        binary_coding_but.setToggleGroup(type_of_coding);

        mode_decode_but.setToggleGroup(type_of_file);
        mode_encode_but.setToggleGroup(type_of_file);

        imageView.setImage(new Image("file:resources/images/switch.png"));
    }

    /**
     * Вызывается, когда пользователь кликнул по кнопке 'Выбрать изображение'
     * */
    @FXML
    private void handleOpenFileText() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Text File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(this.primaryStage);
        if(selectedFile != null) {
            primordialArea.setText("");
            readFileThread = new FileThread(selectedFile, this);
            readProgress.progressProperty().bind(readFileThread.progressProperty());
            readFileThread.start();
        }
    }

    @FXML
    private void handleSaveFileText() throws IOException{
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Text File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(this.primaryStage);
        if(selectedFile != null){
            saveThread = new SaveInFileThread(selectedFile, this);
            saveThread.start();
        }
    }

    private void controlInput(String s, String t1, TextField field){
        numberMatcher = "^-?\\d+$";

        if (!t1.isEmpty()) {
            if (!t1.matches(numberMatcher)) {
                field.setText(s);
            } else {
                try {
                    // тут можете парсить строку как захотите
                    int value = Integer.parseInt(t1);
                    field.setText("" + value);
                } catch (NumberFormatException e) {
                    field.setText(s);
                }
            }
        }
    }

    private void setTextInArea(String[] text, boolean result){
        if(result){
            for (String str: text)
                resultArea.appendText(str + "\n");
        }
        else {
            for (String str : text)
                primordialArea.appendText(str + "\n");
        }
    }

    private void encryption(){
        if (enT != null && enT.isAlive())
            enT.setStop();
        else{
            enT = new EncryptionThread(this);
            enT.start();
        }
    }

    public void setPrimaryStage(Stage primaryStage){ this.primaryStage = primaryStage; }

    public void setHotKey(){
        primaryStage.getScene().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if(plusKey.match(event)){
                addStep();
            } else if(minusKey.match(event)){
                subStep();
            } else if(openKey.match(event)){
                try {
                    handleOpenFileText();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(saveKey.match(event)){
                try {
                    handleSaveFileText();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(openRageKey.match(event)){
                handleChooseFileRange();
            } else if(goKey.match(event)){
                handleStartRange();
            } else if(modeEnKey.match(event)){
                mode_encode_but.setSelected(true);
                checkMode();
            } else if(modeDeKey.match(event)){
                mode_decode_but.setSelected(true);
                checkMode();
            } else if(modeTextKey.match(event)){
                text_coding_but.setSelected(true);
                checkMode();
            } else if(modeBinaryKey.match(event)){
                binary_coding_but.setSelected(true);
                checkMode();
            } else if(openFileEncKey.match(event)){
                handleOpenWindowFileEncode();
            } else if(openHelpKey.match(event)){

            } else if(cancelKey.match(event)){
                stopRead();
            }
        });
    }

    public void setCloseCheck(){

        primaryStage.setOnCloseRequest(windowEvent -> {
            String message = "";

            if (saveThread != null && saveThread.isAlive()){
                message += "Сохранение еще не завершено!\n";
            }
            if (readFileThread != null && readFileThread.isRunning()){
                message += "Чтение файла еще не завершено!\n";
            }
            if (rangeThread != null && rangeThread.isRunning()){
                message += "Работу по диапазону не завершена!\n";
            }
            if (controller != null && controller.isRunningEncruptedThread()){
                message += "Шифрование файла не завершено!\n";
            }

            if (message.length() != 0){

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setResizable(false);
                alert.setTitle("Invalid close");
                alert.setHeaderText("Не все процессы завершили свой работу");
                alert.setContentText(message);
                alert.showAndWait();

                windowEvent.consume();
            } else {
                if(fileStage != null)
                    fileStage.close();
                if(helpStage != null)
                    helpStage.close();
            }
        });
    }

    @FXML
    private void addStep(){
        keyField.setText("" + (Integer.parseInt(keyField.getText() ) + 1));
        encryption();
    }

    @FXML
    private void subStep(){
        if(Integer.parseInt(keyField.getText()) > 0) {
            keyField.setText("" + (Integer.parseInt(keyField.getText()) - 1));
            encryption();
        }
    }

    @FXML
    private void stopRead(){
        readFileThread.stop();
    }

    @FXML
    private void handleChooseFileRange(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Range in Text File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text", "*.txt"));
        File selectedFile = fileChooser.showOpenDialog(this.primaryStage);
        if(selectedFile != null) {
            fileRangeSave = selectedFile;
            messageLabel.textProperty().unbind();
            progressRange.progressProperty().unbind();

            messageLabel.setText("Файл выбран.");
            progressRange.setProgress(0);
        }
    }

    @FXML
    private void handleStartRange(){
        if(rangeThread != null && rangeThread.isRunning()){
            rangeThread.stop();
        }
        else {
            messageLabel.textProperty().unbind();

            if(leftKeyField.getText().isEmpty() ||
                    rightKeyField.getText().isEmpty() ||
                    Integer.parseInt(leftKeyField.getText()) >= Integer.parseInt(rightKeyField.getText()))
            {

                messageLabel.setText("Неверный интервал!");
                return;
            }

            if(fileRangeSave == null){
                messageLabel.setText("Файл не выбран!");
                return;
            }

            changeRangeMode();
            rangeThread = new RangeEncryptionThread(this);
            messageLabel.textProperty().bind(rangeThread.messageProperty());
            progressRange.progressProperty().bind(rangeThread.progressProperty());
            rangeThread.start();
        }
    }

    @FXML
    private void handleOpenWindowFileEncode(){
        if(fileStage != null)
            fileStage.show();
        else {
            fileStage = new Stage();
            fileStage.setTitle("Encryption Files");
            fileStage.getIcons().add(new Image("file:resources/images/encoder_icon.png"));
            fileStage.setResizable(false);

            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("EncryptionFilesOverview.fxml"));
                AnchorPane encryption_files = (AnchorPane) loader.load();

                controller = loader.getController();
                controller.setStage(fileStage);

                Scene scene = new Scene(encryption_files);
                fileStage.setScene(scene);

                controller.setHotKey();
                fileStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleOpenHelp(){
        if(helpStage != null){
            helpStage.show();
        }else {
            helpStage = new Stage();
            helpStage.setTitle("Help");
            helpStage.getIcons().add(new Image("file:resources/images/encoder_icon.png"));
            helpStage.setResizable(false);

            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("helpOverview.fxml"));
                AnchorPane help = (AnchorPane) loader.load();

                HelpController controller = loader.getController();
                controller.setStage(helpStage);

                Scene scene = new Scene(help);
                helpStage.setScene(scene);
                helpStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void checkMode(){
        if(!mode_encode_but.isSelected() && !mode_decode_but.isSelected())
            mode_encode_but.setSelected(true);
        if(!text_coding_but.isSelected() && !binary_coding_but.isSelected())
            text_coding_but.setSelected(true);
        encryption();
    }

    public void changeProgressBox(){
        if(readProgressBox.isVisible())
            readProgressBox.setVisible(false);
        else readProgressBox.setVisible(true);
    }

    public void changeKeyBox(){
        if(keyBox.isDisable())
            keyBox.setDisable(false);
        else keyBox.setDisable(true);
    }

    public void changeModeBox(){
        if(modeBox.isDisable())
            modeBox.setDisable(false);
        else modeBox.setDisable(true);
    }

    public void changeRead(){
        if(read)
            read = false;
        else
            read = true;
    }

    public void changeTypeBox(){
        if(typeBox.isDisable())
            typeBox.setDisable(false);
        else typeBox.setDisable(true);
    }


    public void changeSaveBut(){
        if(saveBut.isDisable())
            saveBut.setDisable(false);
        else saveBut.setDisable(true);
    }

    public void changeOpenBut(){
        if(openBut.isDisable())
            openBut.setDisable(false);
        else openBut.setDisable(true);
    }

    public ProgressIndicator getReadProgress(){ return readProgress; }

    public TextArea getPrimordialArea(){ return primordialArea; }

    public TextArea getResultArea(){ return resultArea; }

    public TextField getKeyField(){ return keyField; }

    public ToggleButton getText_coding_but(){ return text_coding_but; }

    public TextField getLeftKeyField(){ return leftKeyField; }

    public TextField getRightKeyField(){ return rightKeyField; }

    public void changeRangeMode(){
        if(rangeBox.isDisable()) {
            rangeBox.setDisable(false);
            openFileRangeBut.setDisable(false);
            controlRangeBut.setStyle("-fx-text-fill: green");
            controlRangeBut.setText("Начать");
        } else {
            rangeBox.setDisable(true);
            openFileRangeBut.setDisable(true);
            controlRangeBut.setStyle("-fx-text-fill: red");
            controlRangeBut.setText("Отмена");
        }
    }

    public ToggleButton getMode_encode_but(){ return mode_encode_but; }

    public File getFileRangeSave(){ return fileRangeSave; }

    public void closeFileRangeSave(){ fileRangeSave = null; }

    public Label getMessageLabel(){ return messageLabel; }

    public void showAlert(File file){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setResizable(false);
        alert.setTitle("Save complete");
        alert.setHeaderText("Текст сохранен");
        alert.setContentText("Текст успешно сохранен в файл: " + file.getName());
        alert.showAndWait();
    }

}