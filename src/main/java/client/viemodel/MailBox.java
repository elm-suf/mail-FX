package client.viemodel;

import client.tasks.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import model.Mail;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


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


    public void update() {
        System.out.println("MBOX_Update");
//        viewableMails.setPredicate(m -> m.isRead() == false);
        System.out.println("MAIL_BOX update");
        if (list == null || list.isEmpty()) {
            init();
        } else {
            Mail latest = list.stream()
                    .filter(el -> el.getCategory().equals("received"))
                    .max(Comparator.comparing(Mail::getDate))
                    .orElseThrow(NoSuchElementException::new);
            System.out.println("Latest" + latest.getSubject());
            List<Mail> call = new UpdateCall(owner, latest).call();
            System.out.println("CallList " + call);
            if (call.size() > 0)
                for (Mail el : call) {
                    if (!el.isRead() && !list.contains(el)) list.add(0, el);
                }
        }
    }

    public boolean isOnline() {
        return new IsOnlineCall().call();
    }


}


