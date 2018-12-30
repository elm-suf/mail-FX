package client.tasks;

import model.Mail;
import model.Request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SendMailTask implements Runnable{
    private final Mail mail;

    public SendMailTask( Mail mail){
        this.mail = mail;
    }

    @Override
    public void run() {
        Request request = new Request("SEND", mail);
        System.out.println("-----------------"+mail.id());
        try {
            Socket socket = new Socket("127.0.0.1", 6789);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(request);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
