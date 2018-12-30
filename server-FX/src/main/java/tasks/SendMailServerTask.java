package tasks;

import logger.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Mail;

import java.io.*;
import java.net.Socket;

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
        toSend.setCategory("received");
        file = new File("mailfxserver/persistence/" + receiver + "/received/" + toSend.id() + ".json");
        file.getParentFile().mkdirs();
        addToPes(toSend);
    }

    private void addToSent(Mail toSend, String sender) {
        toSend.setCategory("sent");
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