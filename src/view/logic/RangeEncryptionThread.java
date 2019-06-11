package view.logic;

import javafx.application.Platform;
import javafx.concurrent.Task;
import view.EncoderOverviewController;

import javafx.concurrent.Service;

import java.io.FileWriter;

public class RangeEncryptionThread extends Service {

    private EncoderOverviewController controller;
    private boolean stop;

    public RangeEncryptionThread(EncoderOverviewController controller){
        this.controller = controller;
        stop = false;
    }


    public void stop(){
        stop = true;
    }

    @Override
    protected Task createTask() {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int left_key = Integer.parseInt(controller.getLeftKeyField().getText());
                int right_key = Integer.parseInt(controller.getRightKeyField().getText());
                String primordialStr = controller.getPrimordialArea().getText();
                String[] primordialText = primordialStr.split("\n");
                int cout = primordialText.length * (right_key - left_key);
                int iter = 0;
                boolean mode = controller.getText_coding_but().isSelected();

                FileWriter fin = new FileWriter(controller.getFileRangeSave());

                updateMessage("Обработка данных и запись в файл");
                updateProgress(++iter, cout);
                while (!stop){
                    for(int i = left_key; i <= right_key; i++){

                        fin.write(System.getProperty("line.separator") + "key: " + i + System.getProperty("line.separator") + System.getProperty("line.separator"));

                        for (String str: primordialText){
                            fin.write(Encryption.encryption(str, i, mode) + System.getProperty("line.separator"));
                            updateProgress(++iter, cout);
                        }
                    }

                    stop = true;
                }

                updateMessage("Обработка завершена.");
                fin.close();
                Platform.runLater(() -> controller.changeRangeMode());
                controller.closeFileRangeSave();
                return null;
            }
        };
    }
}
