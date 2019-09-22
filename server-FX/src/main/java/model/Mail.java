package model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Mail implements Serializable {
    private String sender;
    //todo list of receiver --> mail can have multiple receiver
    private List<String> receivers;
    private String subject;
    private String message;
    private Date date;
    private String category;
    private int id;
    private boolean isRead;

    public Mail() {
    }

    public Mail(String sender, List<String> receivers, String subject, String message, Date date, Boolean isRead) {
        this();
        this.sender = sender;
        this.receivers = receivers;
        this.subject = subject;
        this.message = message;
        this.date = date;
        this.id = hashCode();
        this.isRead = isRead;
    }

    public Mail(String sender, List<String> receivers, String subject, String message, Date date, String category, Boolean isRead) {
        this.sender = sender;
        this.receivers = receivers;
        this.subject = subject;
        this.message = message;
        this.date = date;
        this.category = category;
        this.id = hashCode();
        this.isRead = isRead;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Mail{ cat=" + category +
                " sender='" + sender + '\'' +
                ", receivers=" + receivers +
                ", subject='" + subject + '\'' +
                ", message='" + message + '\'' +
                ", isRead='" + isRead + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(sender, receivers, subject, message, date);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int id() {
        return this.id;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

}
