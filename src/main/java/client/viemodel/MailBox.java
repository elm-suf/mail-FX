package client.viemodel;

import client.tasks.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import model.Mail;

import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class MailBox {
    public static ObservableList<Mail> list = FXCollections.observableArrayList();
    private FilteredList<Mail> viewableMails = new FilteredList<>(list, e -> true);
    public String owner;

    public MailBox(String owner) {
        this.owner = owner;
    }


    public void init() {
        List<Mail> call = new GetAllCall(owner).call();
        list.setAll(call);
        list.sort(Comparator.comparing(Mail::getDate).reversed());
    }

    public ObservableList<Mail> getList() {
        return list;
    }

    public void delete(Mail selectedItem) {
        new DeleteMailTask(owner, selectedItem).run();
        list.remove(selectedItem);
    }

    public void send(Mail mail) {
        System.out.println("Send: " + mail);
        new SendMailTask(mail).run();
        mail.setCategory("sent");
        mail.setRead(true);
        list.add(mail);
    }

    public void setRead(Mail mail) {
        int i = list.indexOf(mail);
        mail.setRead(true);
        list.set(i, mail);
        new SetEmailReadTask(owner, mail).run();
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

    public void unfilter() {
        viewableMails.setPredicate(mail -> true);
    }

    /**
     * @return count of new Emails
     */
    public int update() {
//        viewableMails.setPredicate(m -> m.isRead() == false);
        if (list == null || list.isEmpty()) {
            init();
        } else {
            Date latest = list.stream()
                    .filter(el -> el.getCategory().equals("received"))
                    .map(Mail::getDate)
                    .max(Date::compareTo)
                    .orElse(new Date(0));
            System.out.println("UPDATE - Latest Date: " + latest);
            List<Mail> call = new UpdateCall(owner, latest).call();
            System.out.println("CallList " + call);
            if (call.size() > 0)
                list.addAll(call);

            return call.size();
        }
        return -1;
    }

    public boolean isOnline() {
        return new IsOnlineCall().call();
    }
}


