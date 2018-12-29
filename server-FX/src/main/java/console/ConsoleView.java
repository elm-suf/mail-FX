package console;

import server.MultiThreadedServer;
import logger.Log;
import logger.Logger;
import com.jfoenix.controls.JFXListView;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ConsoleView implements FxmlView<ConsoleViewModel>, Initializable {

    @FXML
    private JFXListView<Log> listview;


    @InjectViewModel
    private ConsoleViewModel viewModel;
    Logger logger;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MultiThreadedServer server = new MultiThreadedServer(6789);
        Thread thread = new Thread(server);
        logger = server.getLogger();
        listview.itemsProperty().bind(logger.listPropertyProperty());
        thread.start();

//

    }

    public void clearLoggerList(ActionEvent actionEvent) {
        logger.clear();
    }
}
