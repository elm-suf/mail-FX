package client.controller;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import model.Mail;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static client.controller.SampleController.theMailBox;
import static javafx.scene.paint.Color.RED;

public class ReadMailController implements Initializable {

    @FXML
    private JFXTextField sender_txf;

    @FXML
    private JFXTextField receivers_txf;

    @FXML
    private JFXTextField subject_txf;

    @FXML
    private JFXTextArea message_txa;

    @FXML
    private StackPane root_read_mail;

    Mail mail;


    public ReadMailController(Mail mail) {
        this.mail = mail;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sender_txf.setText(mail.getSender());
        receivers_txf.setText(mail.getReceivers().toString());
        subject_txf.setText(mail.getSubject());
        message_txa.setText(mail.getMessage());
    }


    @FXML
    void delete(MouseEvent event) {
        JFXDialogLayout dialogLayout = new JFXDialogLayout();
        JFXButton button_yep = new JFXButton("Yep!");
        JFXButton button_nope = new JFXButton("Nope!");
        button_nope.setButtonType(JFXButton.ButtonType.FLAT);
        button_yep.setButtonType(JFXButton.ButtonType.RAISED);
        button_yep.setRipplerFill(Paint.valueOf(String.valueOf(Color.GREEN)));
        button_nope.setRipplerFill(Paint.valueOf(String.valueOf(RED)));

        button_nope.setStyle("-fx-background-color : #D63E3C;-fx-border-color: #d69b3a;-fx-text-fill: #F5F4F6;");
        button_yep.setStyle("-fx-background-color: #30D1B2;-fx-border-color: #3c4ed1;-fx-text-fill: #F5F4F6;");

        dialogLayout.setHeading(new Text("Are you sure you want to delete this message?"));
        dialogLayout.setActions(button_nope, button_yep);

        JFXDialog dialog = new JFXDialog(root_read_mail, dialogLayout, JFXDialog.DialogTransition.CENTER);

        button_yep.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            theMailBox.delete(mail);
            clearFields();
            dialog.close();
        });
        button_nope.setOnMouseClicked(e -> dialog.close());

        dialog.show();
    }

    private void clearFields() {
        root_read_mail.setVisible(false);
//
//        sender_txf.clear();
//        receivers_txf.clear();
//        message_txa.clear();
//        subject_txf.clear();
    }

    @FXML
    void forward(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/write-mail.fxml"));
            Mail forwardMail = new Mail();
            forwardMail.setSender(SampleController.owner);
            forwardMail.setSubject(String.format("FW: %s", mail.getSubject()));
            forwardMail.setMessage("---------- Forwarded message ---------\n" +
                    "From: " + mail.getSender() + " \n\n" + mail.getMessage() +
                    "\n\n------Sent from Mail-FX-------");
            forwardMail.setReceivers(null);

            WriteMailController controller = new WriteMailController(forwardMail);
            fxmlLoader.setController(controller);
            Node node = fxmlLoader.load();
            root_read_mail.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void reply(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/write-mail.fxml"));
            Mail replyMail = new Mail();

            replyMail.setReceivers(Arrays.asList(mail.getSender()));
            replyMail.setSender(SampleController.owner);


            replyMail.setSubject(String.format("RE: %s", mail.getSubject()));

            replyMail.setMessage("\n\n\nRE - Sent from Mail-FX");

            WriteMailController controller = new WriteMailController(replyMail);

            fxmlLoader.setController(controller);
            Node node = fxmlLoader.load();
            root_read_mail.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void replyAll(MouseEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/write-mail.fxml"));
            Mail replyAllMail = new Mail();

            List<String> receivers = mail.getReceivers();
            receivers.add(mail.getSender());
            replyAllMail.setSender(SampleController.owner);

            replyAllMail.setReceivers(receivers);

            replyAllMail.setSubject(String.format("RE_ALL: %s", mail.getSubject()));

            replyAllMail.setMessage("\n\n\nRE_ALL - Sent from Mail-FX");

            WriteMailController controller = new WriteMailController(replyAllMail);

            fxmlLoader.setController(controller);
            Node node = fxmlLoader.load();
            root_read_mail.getChildren().setAll(node);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}