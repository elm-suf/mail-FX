package console;

import de.saxsys.mvvmfx.ViewModel;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class ConsoleViewModel implements ViewModel {
    private ObservableList<String> logger = FXCollections.observableArrayList();
    private ListProperty<String> listProperty = new SimpleListProperty<String>(logger);

    public ListProperty<String> listPropertyProperty() {
        logger.addAll("mari", "gianno", "manni");
        return listProperty;
    }

    //        return listProperty.get();
    //    public ObservableList<Logger> getListProperty() {
    //
    //    }
    //        this.logger = logger;
//    public void setLogger(ObservableList<Logger> logger) {

//    }
}
