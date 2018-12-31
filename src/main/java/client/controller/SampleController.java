package client.controller;

import client.viemodel.MailBox;
import com.jfoenix.controls.*;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Mail;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SampleController implements Initializable {
    @FXML
    private AnchorPane main_anchorpane;

    @FXML
    private AnchorPane left_anchorpane;

    @FXML
    private JFXListView<Mail> inbox_listview;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXButton new_mail_btn;

    @FXML
    private JFXButton show_mailbox_btn;

    @FXML
    private JFXButton show_inbox_btn;

    @FXML
    private JFXButton show_outbox_btn;

    @FXML
    private JFXButton show_deleted_btn;

    @FXML
    private JFXButton settings_btn;

    public static MailBox theMailBox;
        public static String owner = "hind@gmail.com";
//    public static String owner = "omar@gmail.com";
    public FilteredList<Mail> fList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            VBox sidePan = FXMLLoader.load(getClass().getResource("/view/drawer.fxml"));
            drawer.setSidePane(sidePan);
            hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> darwerToggle());
        } catch (IOException e) {
            e.printStackTrace();
        }
        drawer.setVisible(false);
        try {
            theMailBox = new MailBox(owner);
        } catch (IOException e) {
            theMailBox = new MailBox();
            promptNoConnection();
            e.printStackTrace();
        }
        fList = theMailBox.getViewableMails();
        inbox_listview.setItems(fList);


        show_inbox_btn.setOnMouseClicked(e -> theMailBox.filterReceived());
        show_outbox_btn.setOnMouseClicked(e -> theMailBox.filterSent());
        inbox_listview.setCellFactory(this::cellFactory);
        inbox_listview.setOnMouseClicked(event -> fill_all(inbox_listview.getSelectionModel().getSelectedItem()));

    }

    private void promptNoConnection() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Server Error");
        alert.setHeaderText("Server down");
        alert.setContentText("Ooops, there was an error!");

        alert.showAndWait();
    }


    private void fill_all(Mail selectedItem) {
        if (!selectedItem.isRead()) {
            selectedItem.setRead(true);
            inbox_listview.refresh();
            if (!selectedItem.getSender().equals(owner)) {
                theMailBox.setRead(selectedItem.id());
            }
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/read-mail.fxml"));

            ReadMailController controller = new ReadMailController(selectedItem);

            loader.setController(controller);

            Node load = loader.load();

            main_anchorpane.getChildren().setAll(load);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void darwerToggle() {
        drawer.toggle();
        if (drawer.isClosed() || drawer.isClosing()) {
            drawer.setVisible(false);
        } else {
            drawer.setVisible(true);
        } //todo look at this again please future wiser me
    }

    public void loadSendMail(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/write-mail.fxml"));
            loader.setController(new WriteMailController());
            Node load = loader.load();
            main_anchorpane.getChildren().setAll(load);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Node getCell(Mail item) {
        HBox mailfactory = null;
        String style;
        if (item.isRead()) {
            style = "-fx-font-weight: regular";
        } else {
            style = "-fx-font-weight: bold";
        }

        try {
            mailfactory = FXMLLoader.load(getClass().getResource("/view/mail-factory.fxml"));
            System.out.println(mailfactory);
            for (Node n : mailfactory.getChildren()) {
                if (n instanceof VBox) {
                    ObservableList<Node> children = ((VBox) n).getChildren();
                    for (Node ch : children) {

                        switch (ch.getId()) {
                            case "sender_txf":
                                ((JFXTextField) ch).setText(item.getSender());
                                ch.setStyle(style);
                                break;
                            case "subject_txf":
                                ((JFXTextField) ch).setText(item.getSubject());
                                ch.setStyle(style);
                                break;
                            case "date_txf":
                                ((JFXTextField) ch).setText(item.getDate().toString());
                                ch.setStyle(style);
                                break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mailfactory;
    }

    private ListCell<Mail> cellFactory(ListView<Mail> param) {
        ListCell<Mail> cell = new ListCell<Mail>() {
            @Override
            protected void updateItem(Mail item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    setGraphic(getCell(item));
                } else {
                    setText(null);
                    setGraphic(null);
                }
            }
        };
        return cell;
    }
}