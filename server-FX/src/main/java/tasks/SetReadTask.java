package tasks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logger.Logger;
import model.Mail;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Objects;

public class SetReadTask implements Runnable {
    private final Socket clientSocket;
    private Mail mail;
    private String username;
    File resourceDirectory;

    public SetReadTask(Socket clientSocket, String username, Mail mail) {
        this.clientSocket = clientSocket;
        this.mail = mail;
        this.username = username;
    }

    @Override
    public void run() {
        System.out.println("SetReadTask");
        resourceDirectory = new File("mailfxserver/persistence/" + username);
        Logger.d("SetReadTask", String.format("mail {id: %d, user: %s}", mail.id(), username));
        deleteFromDir(resourceDirectory);
        mail.setRead(true);
        addToPersistence(mail);
    }

    private void deleteFromDir(File file) {
        for (File f : Objects.requireNonNull(file.listFiles())) {
            if (f.isDirectory()) deleteFromDir(f);

            if (f.getName().replaceFirst("[.][^.]+$", "")
                    .equals(String.valueOf(mail.id()))) {
                try {
                    System.out.println("Found it; deleting status" + Files.deleteIfExists(f.toPath()));
                } catch (IOException e) {
                    Logger.e("UPDATE", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private void addToPersistence(Mail toSend) {
        File path = new File("mailfxserver/persistence/" + username.concat("/"+mail.getCategory()+"/" + toSend.id()).concat(".json"));
        path.getParentFile().mkdirs();

        FileOutputStream os;
        BufferedWriter bw;
        try {
            os = new FileOutputStream(path, false);
            bw = new BufferedWriter(new OutputStreamWriter(os));
            Gson gson = new GsonBuilder().create();
            String temp = gson.toJson(toSend);
            System.out.println("adding " + temp);
            System.out.println("To file " + path.getName());
            bw.write(temp);
            bw.flush();
            os.close();
            bw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
