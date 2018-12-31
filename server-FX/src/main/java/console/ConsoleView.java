package console;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleButton;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import logger.Log;
import logger.Logger;
import server.MultiThreadedServer;

import java.net.URL;
import java.util.ResourceBundle;

public class ConsoleView implements FxmlView<ConsoleViewModel>, Initializable {

    @FXML
    JFXToggleButton toggle_info;
    @FXML
    JFXToggleButton toggle_debug;
    @FXML
    JFXToggleButton toggle_error;
    @FXML
    private JFXListView<Log> listview;


    @InjectViewModel
    private ConsoleViewModel viewModel;
    Logger logger;
    public static MultiThreadedServer server;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        server = new MultiThreadedServer(6789);
        Thread thread = new Thread(server);
        logger = server.getLogger();
        listview.itemsProperty().bind(logger.listPropertyProperty());
        listview.setCellFactory(this::cellFactory);

        toggle_info.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (toggle_info.isSelected()){
                logger.filterInfoON();
            } else {
                logger.filterInfoOFF();
            }
        });

        toggle_error.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (observable.getValue()){
                System.out.println("Error on");
                logger.filterErrorON();
            } else {
                logger.filterErrorOFF();
            }
        });
        toggle_debug.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (observable.getValue()){
                logger.filterDebugON();
            } else {
                logger.filterDebugOFF();
            }
        });

        thread.start();
    }

    private ListCell<Log> cellFactory(ListView<Log> logListView) {
        ListCell<Log> cell = new ListCell<Log>() {
            @Override
            protected void updateItem(Log item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    setGraphic(getCell(item));
                } else {
                    setText(null);
                    setGraphic(null);
                }
            }

            private Node getCell(Log item) {
                HBox cell = new HBox();
                Label label_tag = new Label();
                Label label_tag_desc = new Label();
                Label label_message = new Label();

                String labelStyle = "-fx-pref-height: 40; -fx-pref-width: 72; -fx-max-width: 100; -fx-alignment: center; -fx-font-weight: bold;";


                label_tag.setText(item.getLevel().toUpperCase());
                label_tag_desc.setText(item.getTag());
                label_message.setText(item.getMessage());

                if (item.getLevel().equals("ERROR")) {
                    labelStyle = labelStyle.concat("-fx-background-color: #B93636;");// -fx-text-fill: #d63e3c");
                } else if (item.getLevel().equals("DEBUG")) {
                    labelStyle = labelStyle.concat("-fx-background-color: #AB5318;");//-fx-text-fill: #F37821;");
                } else {
                    labelStyle = labelStyle.concat("-fx-background-color: #959E48; "); //-fx-text-fill: #B0BB51;
                }
                System.out.println(labelStyle);
                label_tag_desc.setStyle("-fx-pref-height: 40; -fx-pref-width: 96;-fx-max-width: 120; -fx-alignment: center");
                label_message.setStyle("-fx-pref-height: 40; -fx-pref-width: 200; -fx-max-width: 1000; -fx-alignment: center");
                label_tag.setStyle(labelStyle);

                cell.getChildren().addAll(label_tag, label_tag_desc, label_message);
                cell.setStyle("-fx-padding: 4; -fx-pref-width: 400; -fx-max-width: 1200");
                return cell;
            }
        };
        return cell;
    }


    public void clearLoggerList(ActionEvent actionEvent) {
        logger.clear();
    }
}
