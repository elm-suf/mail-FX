package client.tasks;

import com.google.gson.Gson;
import model.Mail;
import model.Request;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class GetAllCall implements Callable<List<Mail>> {
    FileOutputStream os;
    FileInputStream is;
    BufferedWriter bw;
    Gson gson;
    Socket socket;
    ObjectInputStream in;
    String owner;

    public GetAllCall(Socket socket, String owner) {
        this.socket = socket;
        this.owner = owner;
    }

    @Override
    public List<Mail> call() {
        List<Mail> allMail = new ArrayList<>();
        Request request = new Request("GET", owner);
        try {
            socket.setKeepAlive(true);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(request);
            out.flush();

            in = new ObjectInputStream(socket.getInputStream());

            model.Request response = (model.Request) in.readObject();

            //if user doesn't have any mail response == ERROR
            if (response.getAction().equals("OK")) {
                allMail = (List<Mail>) response.getRequestObject();
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return allMail;
    }

}
