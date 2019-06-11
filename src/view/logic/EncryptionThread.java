package view.logic;

import javafx.application.Platform;
import view.EncoderOverviewController;

import java.io.UnsupportedEncodingException;

public class EncryptionThread extends Thread{

    private boolean stop;
    private EncoderOverviewController controller;

    public EncryptionThread(EncoderOverviewController controller){
        stop = false;
        this.controller = controller;
    }

    @Override
    public void run(){
        controller.getResultArea().setText("");
        String text = controller.getPrimordialArea().getText();
        String[] primordialText = text.split("\n");
        String resultStr;
        String str = "";
        boolean mode;
        int i = 0, step;

        try {
            mode = controller.getText_coding_but().isSelected();
            step = Integer.parseInt(controller.getKeyField().getText());
            if(!controller.getMode_encode_but().isSelected()) step *= -1;
            while (!stop && primordialText.length > i){
                resultStr = Encryption.encryption(primordialText[i++], step, mode);
                String finalResultStr = resultStr;
                Platform.runLater(() -> controller.getResultArea().appendText(finalResultStr + "\n"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void setStop(){
        this.stop = true;
    }
}
