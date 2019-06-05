package TransportCompany;

import java.io.FileWriter;
import java.io.IOException;

public class ActionLogger implements Runnable {
    private String fileName;
    private String message;

    public ActionLogger(String fileName, String message) {
        this.fileName = fileName;
        this.message = message;
    }

    public void run(){
        try (FileWriter fw = new FileWriter(fileName + "/log.csv", true)) {
            fw.append(message);
            fw.append("\n");
            fw.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
