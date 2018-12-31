package tasks;

import com.google.gson.Gson;
import model.Mail;
import model.Request;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;

public class SetMailRead implements Runnable {
    private Socket clientSocket;
    private Request request;

    public SetMailRead(Socket clientSocket, Request request) {
        this.clientSocket = clientSocket;
        this.request = request;
    }

    @Override
    public void run() {
        Object[] params = (Object[]) request.getRequestObject();
        String username = (String) params[0];
        int updateId = (int) params[1];
        File file = new File(String.format("mailfxserver/persistence/%s//%s", username, updateId));

        updateMail(file);
    }

    public static void updateMail(File file) {
        try {
            Gson gson = new Gson();
            String mailSting = FileUtils.readFileToString(file, Charset.defaultCharset());
            Mail mail = gson.fromJson(mailSting, Mail.class);
            mail.setRead(true);
            String toJson = gson.toJson(mail);

            FileUtils.writeStringToFile(file,toJson);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        File file = new File(String.format("mailfxserver/persistence/%s/sent/%s.json", "omar@gmail.com","-568045173"));
        System.out.println(FileUtils.readFileToString(file));

        updateMail(file);
        System.out.println("####################################\n\n");
        System.out.println(FileUtils.readFileToString(file));
    }
}
