package view.logic;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import view.EncoderOverviewController;

import java.io.*;

public class FileThread extends Service {

    private File file;
    private TextArea areaP, areaR;
    private ProgressIndicator indicator;
    private int count;
    private int step;
    private boolean text,stop;
    private EncoderOverviewController controller;

    public FileThread(File file, EncoderOverviewController controller){
        this.file = file;
        this.controller = controller;
        this.areaP = controller.getPrimordialArea();
        this.areaR = controller.getResultArea();
        this.indicator = controller.getReadProgress();
        this.step = Integer.parseInt(controller.getKeyField().getText());
        if(!controller.getMode_encode_but().isSelected()) this.step *= -1;
        this.text = controller.getText_coding_but().isSelected();
        count = 0;
        stop = false;
    }

    /*@Override
    public void run(){
        if (read) {
            try {
                readFile(countLinesNew());
                read = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/

    public int countLinesNew() throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(file));
        try {
            byte[] c = new byte[1024];

            int readChars = is.read(c);
            if (readChars == -1) {
                // bail out if nothing to read
                return 0;
            }

            // make it easy for the optimizer to tune this loop
            int count = 0;
            while (readChars == 1024) {
                for (int i=0; i<1024;) {
                    if (c[i++] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            // count remaining characters
            while (readChars != -1) {
                for (int i=0; i<readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
                readChars = is.read(c);
            }

            return count == 0 ? 1 : count;
        } finally {
            is.close();
        }
    }

   /* private void readFile(int count) {

        };


    }*/

    private void showProgress(double x) {
        Platform.runLater(() -> indicator.setProgress(x));
    }

    private void showMessage(String mess, TextArea area) {
        Platform.runLater(() -> area.appendText(mess));
    }

    @Override
    protected Task createTask() {
        try {
            count = countLinesNew();
            areaP.setEditable(false);
            controller.changeRead();
            controller.changeProgressBox();
            controller.changeKeyBox();
            controller.changeModeBox();
            controller.changeTypeBox();
            controller.changeSaveBut();
            controller.changeOpenBut();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Task<Void>(){

            @Override
            protected Void call() throws Exception {
                try (BufferedReader fileOut = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf8"))) {
                    String line;
                    int iter = 1;
                    updateProgress(iter, count);

                    while (!stop && (line = fileOut.readLine()) != null) {
                        showMessage(line + "\n", areaP);
                        showMessage(Encryption.encryption(line, step, text) + "\n", areaR);
                        iter++;
                        updateProgress(iter , count);
                        Thread.sleep(50);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                controller.changeRead();
                controller.changeProgressBox();
                controller.changeKeyBox();
                controller.changeModeBox();
                controller.changeTypeBox();
                controller.changeSaveBut();
                controller.changeOpenBut();
                areaP.setEditable(true);

                return null;
            }
        };
    }

    public void stop(){
        this.stop = true;
    }
}