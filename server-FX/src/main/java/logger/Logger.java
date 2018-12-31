package logger;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.function.Predicate;


public class Logger {
    private static ObservableList<Log> logBook = FXCollections.observableArrayList();
    private FilteredList<Log> viewableLogs = new FilteredList<>(logBook, e -> false);
    private ListProperty<Log> listProperty = new SimpleListProperty<>(viewableLogs);

    Predicate<Log> info = log -> log.getLevel().equals("INFO");
    Predicate<Log> debug = log -> log.getLevel().equals("DEBUG");
    Predicate<Log> error = log -> log.getLevel().equals("ERROR");


    //    public ListProperty<Log> listPropertyProperty() {
//        return listProperty;
//    }
    public static void d(String tag, String message) {
        Platform.runLater(() -> logBook.add(0, new Log("DEBUG", tag, message)));
//        logBook.add(new Log("DEBUG", tag, message));
        System.out.printf("LOG: tag=[%s] mess[%s] %n", tag, message);
    }

    public static void i(String tag, String message) {
        Platform.runLater(() -> logBook.add(0, new Log("INFO", tag, message)));
//        logBook.add(new Log("INFO", tag, message));
        System.out.printf("LOG: tag=[%s] mess[%s] %n", tag, message);
    }

    public static void e(String tag, String message) {
        Platform.runLater(() -> logBook.add(0, new Log("ERROR", tag, message)));
//        logBook.add(new Log("ERROR", tag, message));
        System.out.printf("LOG: tag=[%s] mess[%s] %n", tag, message);
    }

    public void clear() {
        logBook.remove(0, logBook.size());
    }

    public ObservableList<Log> getListProperty() {
        return listProperty.get();
    }

    public ListProperty<Log> listPropertyProperty() {
        return listProperty;
    }

    public void filterInfoON() {
        viewableLogs.setPredicate(info.or(viewableLogs.getPredicate()));
    }

    public void filterErrorON() {
        viewableLogs.setPredicate(error.or(viewableLogs.getPredicate()));
    }

    public void filterDebugON() {
        viewableLogs.setPredicate(debug.or(viewableLogs.getPredicate()));
    }

    public void filterInfoOFF() {
        viewableLogs.setPredicate(info.negate().and(viewableLogs.getPredicate()));
    }

    public void filterDebugOFF() {
        viewableLogs.setPredicate(debug.negate().and(viewableLogs.getPredicate()));

    }

    public void filterErrorOFF() {
        viewableLogs.setPredicate(error.negate().and(viewableLogs.getPredicate()));
    }
}
