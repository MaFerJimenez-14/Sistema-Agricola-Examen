package com.finca.vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PantallaInicio {

    public void mostrar(Stage stage) {

        Label lblSistema   = new Label("Sistema Agricola");
        Label lblNombre    = new Label("Maria Alpizar");
        Label lblMateria   = new Label("Programacion II");
        Label lblAnio      = new Label("2026");
        Label separador1   = new Label("─────────────────────");
        Label separador2   = new Label("─────────────────────");
        Button btnEntrar   = new Button("Ingresar al sistema");

        lblSistema.getStyleClass().add("inicio-titulo");
        lblNombre.getStyleClass().add("inicio-subtitulo");
        lblMateria.getStyleClass().add("inicio-subtitulo");
        lblAnio.getStyleClass().add("inicio-anio");
        separador1.getStyleClass().add("inicio-separador");
        separador2.getStyleClass().add("inicio-separador");
        btnEntrar.getStyleClass().add("btn-inicio");

        btnEntrar.setOnAction(e -> {
            try {
                new VentanaPrincipal().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox layout = new VBox(20,
            lblSistema,
            separador1,
            lblNombre,
            lblMateria,
            lblAnio,
            separador2,
            btnEntrar
        );
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(60));
        layout.getStyleClass().add("fondo-inicio");

        Scene scene = new Scene(layout, 500, 400);
        scene.getStylesheets().add(getClass().getResource("estilos.css").toExternalForm());
        stage.setTitle("Sistema Agricola");
        stage.setScene(scene);
        stage.show();
    }
}