package view.logic;

import javafx.application.Platform;
import view.EncoderOverviewController;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SaveInFileThread extends Thread{

    private boolean stop;

    private File file;

    private EncoderOverviewController controller;

    public SaveInFileThread(File file, EncoderOverviewController controller){
        this.controller = controller;
        stop = false;
        this.file = file;
    }

    @Override
    public void run(){
        save();
    }

    private void save() {
        try {
            FileWriter fin = new FileWriter(file);

            String textInStr = controller.getResultArea().getText();
            String[] text = textInStr.split("\n");

            for (String str : text) {
                fin.write(str + System.getProperty("line.separator"));
            }

            fin.close();

            Platform.runLater(() -> controller.showAlert(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
