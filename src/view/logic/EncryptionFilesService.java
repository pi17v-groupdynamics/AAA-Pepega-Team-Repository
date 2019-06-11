package view.logic;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import view.EncryptionFilesController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class EncryptionFilesService extends Service {

    private EncryptionFilesController controller;

    private int step;
    private String[] messageTxt;
    private boolean stop, mode;
    private FileInputStream fis;
    private FileOutputStream fos;
    private File readFile, saveFile;

    public EncryptionFilesService(EncryptionFilesController controller){
        this.controller = controller;
        stop = false;
        this.step = Integer.parseInt(controller.getKeyField().getText());

        messageTxt = new String[2];

        this.mode = controller.getModeEnBut().isSelected();
        if(this.mode) {
            readFile = controller.getDataFile();
            messageTxt[0] = "Шифрование";
            messageTxt[1] = "encrypted";
        } else {
            readFile = controller.getEncryptedFile();
            step *= -1;
            messageTxt[0] = "Дешифрование";
            messageTxt[1] = "decrypted";
        }

        saveFile = createNewFile(readFile);


    }

    @Override
    protected Task createTask() {

        controller.changeDisable();

        return new Task<Void>(){
            @Override
            protected Void call() throws Exception {
                int actuallyRead, count, iter = 0;
                int size_100mb = 1024*1024*100;
                byte[] buffer;

                if(readFile.length() > size_100mb){
                    count = (int) (readFile.length() / size_100mb);
                    buffer = new byte[size_100mb];//100mb
                } else {
                    count = 1;
                    buffer = new byte[(int) readFile.length()];
                }

                saveFile.createNewFile();
                fis = new FileInputStream(readFile);
                fos = new FileOutputStream(saveFile);

                updateMessage(messageTxt[0] + " файла..");

                while (!stop && (actuallyRead = fis.read(buffer)) != -1) {
                    for (int i = 0; i < buffer.length; i++) {
                        buffer[i] -= step;
                    }

                    updateProgress(++iter, count);
                    fos.write(buffer);
                }

                updateMessage(messageTxt[0] + " завершено.");
                updateProgress(1,1);
                fis.close();
                fos.close();

                if(stop)
                    saveFile.delete();

                controller.changeDisable();
                controller.closeFile();
                Platform.runLater(() -> controller.changeSorPButton());
                return null;
            }
        };
    }

    private File createNewFile(File file){
        String filePath = file.getPath();
        String[] partPath = filePath.split("\\\\");
        String[] newName = partPath[partPath.length - 1].split("\\.");
        String newPath = "";
        String fileSeparator = System.getProperty("file.separator");

        for(int i = 0; i < partPath.length - 1; i++)
            newPath += partPath[i] + fileSeparator;
        newPath += newName[0] + " — " + messageTxt[1] + "." + newName[1];

        File newFile = new File(newPath);
        try {
            file.createNewFile();
        } catch (IOException e) {
            System.out.println("Файл с таким именем уже существует!");
        }

        return newFile;
    }

    public void stop(){ stop = true; }
}
