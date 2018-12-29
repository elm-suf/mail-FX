import console.ConsoleView;
import console.ConsoleViewModel;
import de.saxsys.mvvmfx.FluentViewLoader;
import de.saxsys.mvvmfx.ViewTuple;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Server extends Application{

    public static void main(String... args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("SERVER-CONSOLE");

        ViewTuple<ConsoleView,ConsoleViewModel> tuple =
                FluentViewLoader.fxmlView(ConsoleView.class).load();

        Parent root = tuple.getView();
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }
}
