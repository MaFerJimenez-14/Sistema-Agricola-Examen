package una.ac.cr.sistemaagricola;

import com.finca.vista.PantallaInicio;
import javafx.application.Application;
import javafx.stage.Stage;

public class SistemaAgricola extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        new PantallaInicio().mostrar(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}