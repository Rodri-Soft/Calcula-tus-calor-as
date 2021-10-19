package calculatuscalorías;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Antonio de Jesús Domínguez García
 */
public class CalculaTusCalorías extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLMain.fxml"));        
        Scene scene = new Scene(root);
        stage.getIcons().add(new Image("/calculatuscalorías/img/icons.jpg"));
        //stage.initStyle(StageStyle.UNDECORATED);
        //stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);        
    }
    
}
