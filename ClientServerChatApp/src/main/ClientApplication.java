package src.chatapp.main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ClientApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("\\resources\\files\\ClientView.fxml"));
        ClientController client_controller = new ClientController("127.0.0.1");
        fxmlLoader.setController(client_controller);
        fxmlLoader.setLocation(getClass().getResource("ClientView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(),721, 425);
        stage.setTitle("Client App");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}