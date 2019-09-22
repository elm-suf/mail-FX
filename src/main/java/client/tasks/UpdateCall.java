package client.tasks;

import com.google.gson.Gson;
import model.Mail;
import model.Request;

import java.io.*;
import java.net.Socket;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class UpdateCall implements Callable<List<Mail>> {
    FileOutputStream os;
    FileInputStream is;
    BufferedWriter bw;
    Gson gson;
    Socket socket;
    ObjectInputStream in;

    private String owner;
    private Mail mail;

    public UpdateCall(String owner, Mail lastReceived) {
        this.owner = owner;
        mail = lastReceived;
    }

    @Override
    public List<Mail> call() {
        List<Mail> newMail = new ArrayList<>();

        Object args[] = {owner, mail};
        Request request = new Request("GET_NEW", args);

        try {
            socket = new Socket("127.0.0.1", 6789);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(request);
            out.flush();

            in = new ObjectInputStream(socket.getInputStream());

            model.Request response = (model.Request) in.readObject();

            //if user doesn't have any mail response == ERROR
            System.out.println("Latest> " + mail.getSubject() + "\ndate >   " + mail.getDate().getTime());
            System.out.println(response.getAction() + "¬" + response.getRequestObject());
            newMail = (List<Mail>) response.getRequestObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newMail;
    }
}
