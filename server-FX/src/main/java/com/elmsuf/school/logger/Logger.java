package com.elmsuf.school.logger;

import javafx.application.Platform;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Logger {
    private static ObservableList<Log> logBook = FXCollections.observableArrayList();
    private ListProperty<Log> listProperty = new SimpleListProperty<>(logBook);

    //    public ListProperty<Log> listPropertyProperty() {
//        return listProperty;
//    }
    public static void d(String tag, String message)
    {
        Platform.runLater(()->logBook.add(0,new Log("DEBUG",tag,message)));
//        logBook.add(new Log("DEBUG", tag, message));
        System.out.printf("LOG: tag=[%s] mess[%s] %n", tag, message);
    }

    public static void i(String tag, String message) {
        Platform.runLater(()->logBook.add(0,new Log("INFO",tag,message)));
//        logBook.add(new Log("INFO", tag, message));
        System.out.printf("LOG: tag=[%s] mess[%s] %n", tag, message);
    }

    public static void e(String tag, String message){
        Platform.runLater(()->logBook.add(0,new Log("ERROR",tag,message)));
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
}
