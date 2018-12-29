package tasks;

import logger.Logger;
import com.google.gson.Gson;
import model.Mail;
import model.Request;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GetAllServerTask implements Runnable {
    private final Socket clientSocket;
    private final String username;
    String TAG;

    public GetAllServerTask(Socket clientSocket, String username) {
        this.clientSocket = clientSocket;
        this.username = username;
        TAG = clientSocket.toString();
    }

    @Override
    public void run() {
        Logger.d(TAG, "Getting all emails for " + username);
        System.out.println(File.listRoots().toString());
        System.out.println(System.getProperty("user.dir"));
        List<File> list = listf("mailfxserver/persistence/" + username);

        Request request = new Request();
        List<Mail> listOfUserMail;
        if (list == null || list.isEmpty()){
            request.setAction("ERROR");
        } else {
            listOfUserMail = getListOfUserMail(list);
            request = new Request("OK", listOfUserMail);
        }

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

    public static List<File> listf(String directoryName) {
        File directory = new File(directoryName);
        List<File> resultList = new ArrayList<>();
        File[] fList = directory.listFiles();
        if (fList == null)
            return null;
        for (File file : fList) {
            if (file.isFile()) {
                resultList.add(file);
            } else if (file.isDirectory()) {
                resultList.addAll(listf(file.getAbsolutePath()));
            }
        }

        return resultList;
    }

    public static List<Mail> getListOfUserMail(List<File> list){
        BufferedReader br;
        Gson gson = new Gson();
        List<Mail> result = new ArrayList<>();

        for (File f : list) {
            try {
                br = new BufferedReader(new FileReader(f));
                Mail obj = gson.fromJson(br, Mail.class);
                result.add(obj);
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
