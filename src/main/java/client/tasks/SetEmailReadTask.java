package client.tasks;

import model.Mail;
import model.Request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SetEmailReadTask implements Runnable {

    private final int  id;

    public SetEmailReadTask(int id){
        this.id = id;
    }


    @Override
    public void run() {

        Request request = new Request("SET_READ", id);
        System.out.println("********* setRead() " + id);
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
