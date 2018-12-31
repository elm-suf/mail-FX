package client.tasks;

import model.Request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SetEmailReadTask implements Runnable {

    private String owner;
    private final int  id;

    public SetEmailReadTask(String owner, int id){
        this.owner = owner;
        this.id = id;
    }


    @Override
    public void run() {
        Object[] params = {owner, id};

        Request request = new Request("SET_READ", params);
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
