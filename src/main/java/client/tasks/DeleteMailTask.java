package client.tasks;

import model.Mail;
import model.Request;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class DeleteMailTask implements Runnable{


    private final String owner;
    private Mail toDelete;

    public DeleteMailTask(String owner, Mail toDelete) {
        this.owner = owner;
        this.toDelete = toDelete;
    }

    @Override
    public void run() {
        Object req[] = {owner,toDelete};
        System.out.println("Deleting mail w code = " + toDelete.hashCode());
        Request request = new Request("DELETE", req);
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
