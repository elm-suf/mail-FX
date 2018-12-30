package client.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.Mail;

import javax.swing.text.html.ImageView;
import java.net.URL;
import java.util.ResourceBundle;


public class CellMailCOntroller implements Initializable {
    private Mail item;

    @FXML
    private ImageView imgv_propic;

    @FXML
    private JFXTextField txf_sender;

    @FXML
    private JFXTextField txf_subject;

    @FXML
    private JFXTextField txf_date;


    public CellMailCOntroller(Mail item) {
        this.item = item;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txf_subject.setText(item.getSubject());
        txf_sender.setText(item.getSender());
        txf_date.setText(item.getDate().toString());
    }
}
