package client.controller;


import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IndexController implements Initializable {
    @FXML
    private JFXHamburger hamburger;

    @FXML
    private JFXDrawer drawer;

    @FXML
    private JFXListView<?> listview;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Initialize");

        try {
            VBox vBox = (VBox) FXMLLoader.load(getClass().getResource("../view/drawer.fxml"));
            drawer.setSidePane(vBox);

            for (Node node : vBox.getChildren()) {
                if (node.getAccessibleText() != null) {
                    node.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
                                switch (node.getAccessibleText()) {
                                    case "inbox":
                                        System.out.println("inbox acc text");
                                        break;
                                    case "new_mail":
                                        System.out.println("new mail acc text");
                                        break;
                                    case "draft":
                                        System.out.println("draft acc texrt");
                                        break;
                                    case "sent":
                                        System.out.println("sent acc text");
                                        break;
                                    case "settings":
                                        System.out.println("settings acc text");
                                        break;
                                    default:
                                        System.out.println("EEEEEEEEEEEELLLLLLLLSSSSEEEEEEEE");
                                        break;
                                }
                            }

                    );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> drawer.toggle());
    }



}
