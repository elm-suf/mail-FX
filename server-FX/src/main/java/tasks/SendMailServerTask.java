package tasks;

import logger.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Mail;

import java.io.*;
import java.net.Socket;

import static server.MultiThreadedServer.mapUserSocket;

public class SendMailServerTask implements Runnable {
    private final Socket clientSocket;
    private final Mail toSend;
    File file;

    public SendMailServerTask(Socket clientSocket, Mail mail) {
        this.clientSocket = clientSocket;
        this.toSend = mail;
    }

    @Override
    public void run() {
        Logger.d("SEND_TASK", Thread.currentThread().toString());
        addToSent(toSend, toSend.getSender());
        Logger.d("SEND_TASK", "mail sent");

        for (String receiver : toSend.getReceivers()) {
            addToReceived(toSend, receiver);
            Logger.d("SEND_TASK", "mail received");
        }
    }

    private void addToReceived(Mail toSend, String receiver) {
        toSend.setCategory("received");toSend.setRead(false);
        file = new File("mailfxserver/persistence/" + receiver + "/received/" + toSend.id() + ".json");
        file.getParentFile().mkdirs();
        addToPes(toSend);
        notifyUser(receiver);
    }

    private void notifyUser(String receiver) {
        if (mapUserSocket.containsKey(receiver)){
            new NotifyUserNewMailReceivedTask(mapUserSocket.get(receiver), toSend).run();
        }
    }

    private void addToSent(Mail toSend, String sender) {
        toSend.setCategory("sent");
        toSend.setRead(true);
        file = new File("mailfxserver/persistence/" + sender + "/sent/" + toSend.id() + ".json");
        file.getParentFile().mkdirs();
        addToPes(toSend);
    }

    private void addToPes(Mail toSend) {
        FileOutputStream os;
        BufferedWriter bw;
        try {
            os = new FileOutputStream(file, false);
            bw = new BufferedWriter(new OutputStreamWriter(os));
            Gson gson = new GsonBuilder().create();
            String temp = gson.toJson(toSend);
            System.out.println("adding " + temp);
            System.out.println("To file " + file.getName());
            bw.write(temp);
            bw.flush();
            os.close();
            bw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}