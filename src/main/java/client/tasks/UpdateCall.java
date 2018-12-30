package client.tasks;

import model.Mail;

import java.sql.Time;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class UpdateCall implements Callable<List<Mail>> {
    private String owner;

    public UpdateCall(String owner) {
        this.owner = owner;
    }

    @Override
    public List<Mail> call() {
        return null;
    }
//    private Time lastUpdate;
//
//    public UpdateCall(Time time) {
//        lastUpdate = time;
//    }
//
}
