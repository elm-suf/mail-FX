package client.tasks;

import model.Mail;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.sql.Time;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Callable;

import static client.controller.SampleController.theMailBox;

public class ListenForUpdates implements Runnable {


    private Socket socket;

    public ListenForUpdates(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected()) {
                Thread.sleep(2000);
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

                Mail mail = (Mail) in.readObject();

                theMailBox.add(mail);
//                in.close();
            }
        } catch (IOException | ClassNotFoundException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
