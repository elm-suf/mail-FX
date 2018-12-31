package tasks;

import logger.Logger;
import model.Mail;
import model.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class WorkerRunnable implements Runnable {
    private static final String TAGW = "Worker";

    private Socket clientSocket;

    public WorkerRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void run() {
        Logger.i(TAGW, Thread.currentThread().toString());
        try {
            ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
//            ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
            Request request = (Request) input.readObject();
            System.out.println(request.getAction());
            Logger.i(TAGW, String.format("Got Request for %s", request.getAction()));
            long time = System.currentTimeMillis();

            switch (request.getAction()) {
                case "GET":
                    String username = (String) request.getRequestObject();
                    Logger.d("GET", "Get all messages for " + username);
                    new GetAllServerTask(clientSocket, username).run();
                    break;
                case "SEND":
                    Logger.d("SEND", "send message");
                    Mail toSend = (Mail) request.getRequestObject();
                    new SendMailServerTask(clientSocket, toSend).run();
                    break;
                case "SET_READ":
                    Logger.d("SET_READ", "setting update mail read status");
                    new SetMailRead(clientSocket, request).run();
                    break;
                case "DELETE":
                    Object req[] = (Object[]) request.getRequestObject();
                    String mailAddress = (String) req[0];
                    Mail delete = (Mail) req[1];
                    Logger.d("DELETE", "deleting mail w Id: " + delete.id());
                    new DeleteMailServerTask(clientSocket, mailAddress, delete).run();
                    break;
            }
//            input.close();
            System.out.println("Request processed: " + time);
        } catch (IOException | ClassNotFoundException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }
}
