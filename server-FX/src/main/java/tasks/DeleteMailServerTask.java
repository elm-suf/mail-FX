package tasks;

import logger.Logger;
import model.Mail;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Objects;

public class DeleteMailServerTask implements Runnable {
    private final Socket clientSocket;
    private Mail toDelete;
    private String username;

    public DeleteMailServerTask(Socket clientSocket, String username, Mail toDelete) {
        this.clientSocket = clientSocket;
        this.toDelete = toDelete;
        this.username = username;
    }

    @Override
    public void run() {
        File file = new File("mailfxserver/persistence/" + username);
        Logger.d("DeleteMailServerTask", String.format("Deleting mail w id[%d] from user {%s}", toDelete.id(), username));
        deleteFromDir(file);
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteFromDir(File file) {
        for (File f : Objects.requireNonNull(file.listFiles())) {
            if (f.isDirectory()) deleteFromDir(f);

            if (f.getName().replaceFirst("[.][^.]+$", "")
                    .equals(String.valueOf(toDelete.id()))) {
                try {
                    System.out.println("Found it; deleting status" + Files.deleteIfExists(f.toPath()));
                } catch (IOException e) {
                    Logger.e("DELETE", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }
}
