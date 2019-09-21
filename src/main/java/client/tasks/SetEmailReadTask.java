package client.tasks;

import model.Mail;
import model.Request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SetEmailReadTask implements Runnable {

    private final Mail mail;
    private final String  owner;

    public SetEmailReadTask(String owner, Mail mail) {
        this.owner = owner;
        this.mail = mail;
    }


    @Override
    public void run() {

        Object args[] = {owner,mail};

        Request request = new Request("SET_READ", args);
        System.out.println("********* setRead() " + mail);
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
