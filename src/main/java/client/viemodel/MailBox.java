package client.viemodel;

import client.tasks.DeleteMailTask;
import client.tasks.GetAllCall;
import client.tasks.SendMailTask;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import model.Mail;

import java.util.List;


public class MailBox {
    public static ObservableList<Mail> list = FXCollections.observableArrayList();
    private FilteredList<Mail> viewableMails = new FilteredList<>(list, e -> true);
    public String owner;

    public MailBox(String owner) {
        List<Mail> call = new GetAllCall(owner).call();
        list.setAll(call);
        this.owner = owner;
    }

    public void add(String user, Mail mail) {
//        file = new File("persistent/" + user + ".txt");
////
////        try {
////            os = new FileOutputStream(file, true);
////            bw = new BufferedWriter(new OutputStreamWriter(os));
////
////            gson = new GsonBuilder().create();
////            String temp = gson.toJson(mail);
////            bw.append(temp + "\n");
////            bw.flush();
////        } catch (IOException e) {
////            e.printStackTrace();
////
////        } finally {
////            try {
////                assert os != null;
////                os.close();
////                assert bw != null;
////                bw.close();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////        }

    }

    public ObservableList<Mail> getList() {
        return list;
    }

    public void delete(Mail selectedItem) {
        new DeleteMailTask(owner, selectedItem).run();
        list.remove(selectedItem);
    }

    public void send(Mail mail) {

        System.out.println(mail);
        new SendMailTask(mail).run();
        mail.setCategory("sent");
        list.add(mail);
    }

    public ObservableList<Mail> getReceived() {
        return list.filtered(m -> m.getCategory().equals("sent"));
    }

    public FilteredList<Mail> getViewableMails() {
        return viewableMails;
    }

    public void filterReceived() {
        viewableMails.setPredicate(mail -> mail.getCategory().equals("received"));
    }

    public void filterSent() {
        viewableMails.setPredicate(mail -> mail.getCategory().equals("sent"));
    }

}


