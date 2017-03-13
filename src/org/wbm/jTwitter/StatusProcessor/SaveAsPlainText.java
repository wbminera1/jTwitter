package org.wbm.jTwitter.StatusProcessor;

import twitter4j.Status;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
  */
public class SaveAsPlainText implements IStatusProcessor {
    private final File file;
    private FileWriter writer;

    public SaveAsPlainText(String filename) {
        file = new File(filename);
        //file.mkdirs();
        try {
            file.createNewFile();
            writer = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatus(Status status) {
        try {
            //writer.write(status.getText().toString() + "\n");
            writer.write(status.getUser().getName() + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
