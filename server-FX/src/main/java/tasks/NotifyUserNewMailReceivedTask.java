package tasks;

import logger.Logger;
import model.Mail;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NotifyUserNewMailReceivedTask implements Runnable {
    private final Socket socket;
    private Mail mail;

    public NotifyUserNewMailReceivedTask(Socket socket, Mail mail) {
        Logger.d("NOTIFY", "User Notified on socket " + socket);
        this.socket = socket;
        this.mail = mail;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());

            out.writeObject(mail);
            out.flush();
//            out.close();
        } catch (IOException e) {
            Logger.e("NOTIFY", e.getMessage());
            e.printStackTrace();
        }
    }
}
