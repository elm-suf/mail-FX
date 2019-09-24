package tasks;

import logger.Logger;
import com.google.gson.Gson;
import model.Mail;
import model.Request;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class GetNewServerTask implements Runnable {
    private final Socket clientSocket;
    private final String username;
    String TAG;
    private Date last;

    public GetNewServerTask(Socket clientSocket, String username, Date last) {
        this.clientSocket = clientSocket;
        this.username = username;
        TAG = clientSocket.toString();
        this.last = last;
    }

    @Override
    public void run() {
        Logger.d(TAG, "Getting new emails for " + username);
        System.out.println(Arrays.toString(File.listRoots()));
        System.out.println(System.getProperty("user.dir"));
        List<File> list = listf("mailfxserver/persistence/" + username + "/received");

        Request request = new Request();
        List<Mail> listOfUserMail;
        listOfUserMail = getListOfUserMail(list);
        if (listOfUserMail == null || listOfUserMail.isEmpty()) {
            System.out.println("0 new Emails");
            request.setAction("ERROR");
        } else {
            System.out.println("NEWWWW" + listOfUserMail.size());
            request.setAction("OK");
        }

        request.setRequestObject(listOfUserMail);

        try {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());

            out.writeObject(request);
            out.flush();

            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<File> listf(String directoryName) {
        File directory = new File(directoryName);
        List<File> resultList = new ArrayList<>();
        File[] fList = directory.listFiles();
        if (fList == null)
            return resultList;
        for (File file : fList) {
            if (file.isFile()) {
                resultList.add(file);
            } else if (file.isDirectory()) {
                resultList.addAll(listf(file.getAbsolutePath()));
            }
        }
        return resultList;
    }

    public List<Mail> getListOfUserMail(List<File> list) {
        BufferedReader br;
        Gson gson = new Gson();
        List<Mail> newEmails = new ArrayList<>();

        for (File f : list) {
            try {
                br = new BufferedReader(new FileReader(f));
                Mail item = gson.fromJson(br, Mail.class);
                if (item.getDate().compareTo(last) > 0) {
                    newEmails.add(item);
                    System.out.println("item" + item);
                    System.out.println("newEmails" + newEmails);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
                return newEmails;
            }
        }
        return newEmails;
    }
}
