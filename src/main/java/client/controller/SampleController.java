package client.controller;

import com.jfoenix.controls.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.Mail;
import client.viemodel.MailBox;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SampleController implements Initializable {
    @FXML
    private AnchorPane main_anchorpane;

    @FXML
    private AnchorPane connection;

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

    @FXML
    private Label left_title_label;
    @FXML
    private Label mode_label;

    protected static MailBox theMailBox;

    //    =====================================================================================================
//    "omar@gmail.com", "me@me.com", "mario@lone.com"
//   public static String owner = "me@me.com";
    public static String owner = "omar@gmail.com";
//    public static String owner = "mario@lone.com";
    public FilteredList<Mail> fList;
    private SimpleStringProperty modeProperty;
    private boolean online = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
        System.out.println("data");
        initView();
        System.out.println("view");
        initListeners();
        System.out.println("listeners");
        scheduleUpdates();
        System.out.println("ups");
    }


    ScheduledFuture<?> scheduledUpdateFuture;

    public void scheduleUpdates() {
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);
        Runnable task1 = () -> {
            System.out.println("task");
            if (online) {
                theMailBox.update();
            }
        };

        scheduledUpdateFuture = ses.scheduleAtFixedRate(task1, 1, 5, TimeUnit.SECONDS);
    }

    private void initData() {
        theMailBox = new MailBox(owner);
        online = theMailBox.isOnline();
        if (online) {
            fList = theMailBox.getViewableMails();
            fList.sort(Comparator.comparing(Mail::getDate));
            connection.setVisible(false);
        } else {
            System.out.println("Waiting for Connection");
            connection.setVisible(true);
        }
    }

    private void initListeners() {
        inbox_listview.setOnMouseClicked(event -> fill_all(inbox_listview.getSelectionModel().getSelectedItem()));
        show_inbox_btn.setOnMouseClicked(e -> showInbox());
        show_outbox_btn.setOnMouseClicked(e -> showOutbox());

        show_mailbox_btn.setOnMouseClicked(e -> showAll());
    }

    private void showAll() {
        modeProperty.setValue("Unread Messages");
        fList.setPredicate(elem -> !elem.isRead());
        theMailBox.update();
    }

    private void showInbox() {
//        theMailBox.filterReceived();
        modeProperty.setValue("Inbox");
        fList.setPredicate(elem -> !elem.getSender().equals(owner));
    }

    private void showOutbox() {
//        theMailBox.filterSent();
        modeProperty.setValue("Outbox");
        fList.setPredicate(elem -> elem.getSender().equals(owner));
    }

    private void initView() {
        left_title_label.setText(theMailBox.owner);
        this.modeProperty = new SimpleStringProperty("Inbox");
        mode_label.textProperty().bind(modeProperty);

        try {
            VBox sidePan = FXMLLoader.load(getClass().getResource("/view/drawer.fxml"));
            drawer.setSidePane(sidePan);
            hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> darwerToggle());
        } catch (IOException e) {
            System.out.println("SidePane error");
        }
        drawer.setVisible(false);


        inbox_listview.setItems(fList);

        inbox_listview.setCellFactory((param) -> {
            ListCell<Mail> cell = new ListCell<Mail>() {
                @Override
                protected void updateItem(Mail item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item != null) {
                        setGraphic(getCell(item));
                    } else {
                        setGraphic(null);
                    }
                }
            };
            return cell;
        });

    }


    private void fill_all(Mail selectedItem) {
        if (!selectedItem.isRead()) {
            System.out.println("not read");
            selectedItem.setRead(true);
            theMailBox.setRead(selectedItem);
            inbox_listview.getSelectionModel().getSelectedItem().setRead(true);
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/read-mail.fxml"));

            ReadMailController controller = new ReadMailController(selectedItem);

            loader.setController(controller);

            Node load = loader.load();

            main_anchorpane.getChildren().setAll(load);
        } catch (IOException e) {
            System.out.println("ReadMail Pane ERROR");
            ;
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
        if (!item.isRead()) {
            style = "-fx-font-weight: regular";
        } else {
            style = "-fx-font-weight: bold";
        }
        boolean isOwner = item.getSender().equals(theMailBox.owner);

        try {
            mailfactory = FXMLLoader.load(getClass().getResource("/view/mail-factory.fxml"));
            if (isOwner)
                mailfactory.setStyle("-fx-background-color: #806567");
            else if (item.isRead())
                mailfactory.setStyle("-fx-background-color: #608080");
            else
                mailfactory.setStyle("-fx-background-color: #ff4b6f");

            for (Node n : mailfactory.getChildren()) {
                if (n instanceof VBox) {
                    ObservableList<Node> children = ((VBox) n).getChildren();


                    for (Node ch : children) {
                        switch (ch.getId()) {
                            case "sender_txf":
                                if (isOwner)
                                    ((JFXTextField) ch).setText("To: " + item.getReceivers());
                                else
                                    ((JFXTextField) ch).setText("From: " + item.getSender());
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
                            case "root":
//                                if (isOwner) {
//                                    ((HBox) ch).setBackground(new Background(new BackgroundFill(Color.rgb(10, 10, 20), CornerRadii.EMPTY, Insets.EMPTY)));
//                                }
//                                if (item.isRead())
//                                    ch.setStyle("-fx-background-color: gray");
//                                else
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mailfactory;
    }
}