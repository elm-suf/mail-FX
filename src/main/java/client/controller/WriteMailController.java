package client.controller;

import client.tasks.SendMailTask;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXChipView;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import model.Mail;
import model.Request;
import org.controlsfx.control.Notifications;

import javax.management.Notification;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.BiFunction;

import static client.controller.SampleController.theMailBox;

public class WriteMailController implements Initializable {
    private Mail mail;

    public WriteMailController() {
    }

    public WriteMailController(Mail mail) {
        this.mail = mail;
    }

    @FXML
    private JFXButton discard_btn;

    @FXML
    private JFXButton send_btn;

    @FXML
    private JFXTextField sender_txf;

    @FXML
    private JFXChipView<String> send_to_chips;

    @FXML
    private JFXTextField subject_txf;

    @FXML
    private JFXTextArea message_txa;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sender_txf.setText(SampleController.owner);
        send_to_chips.getSuggestions().addAll("omar@gmail.com", "me@me.com", "mario@lone.com");

        if (mail != null) {
            message_txa.setText(mail.getMessage());
            subject_txf.setText(mail.getSubject());
            if (mail.getReceivers() != null && !mail.getReceivers().isEmpty()) {
                send_to_chips.getChips().setAll(mail.getReceivers());
            }
        }

    }


    public void discardMail(MouseEvent mouseEvent) {
        System.out.println("Discard mail now");
    }

    public void sendMail(MouseEvent mouseEvent) {
        System.out.println("sending email...");

        String sender = sender_txf.getText();
        String message = message_txa.getText();
        String subject = subject_txf.getText();
        List<String> receivers = new ArrayList<>(send_to_chips.getChips());
        Mail mail = new Mail(sender, receivers, subject, message, new Date(), false);

        theMailBox.send(mail);
        Notifications.create().title("Send Email").text("Email sent correctly").showInformation();
    }

}
