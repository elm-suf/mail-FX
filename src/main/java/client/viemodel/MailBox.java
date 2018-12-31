package client.viemodel;

import client.tasks.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import model.Mail;

import java.io.IOException;
import java.net.Socket;
import java.util.Comparator;
import java.util.List;


public class MailBox {
    public static ObservableList<Mail> list = FXCollections.observableArrayList();
    private FilteredList<Mail> viewableMails = new FilteredList<>(list, e -> true);
    public String owner;
    public Socket mySocket;

    public MailBox(){}

    public MailBox(String owner) throws IOException {
        this.owner = owner;
        this.init();
    }

    private void init() throws IOException {
            mySocket = new Socket("127.0.0.1", 6789);
            mySocket.setKeepAlive(true);
            new Thread(new ListenForUpdates(mySocket)).start();

        List<Mail> call = new GetAllCall(mySocket, owner).call();
        list.setAll(call);
        list.sort(Comparator.comparing(Mail::getDate));
    }

    public void add(Mail mail) {
        list.add( mail);
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
        mail.setRead(true);
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

    public void setRead(int id) {
        new SetEmailReadTask(owner, id).run();
    }
}


