package com.finca.vista;

import com.finca.controlador.GestorAgrario;
import com.finca.modelo.*;
import com.finca.reporte.GeneradorInforme;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.List;

public class VentanaPrincipal extends Application {

    private GestorAgrario gestor = new GestorAgrario();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Sistema de Gestion Agraria");
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().addAll(
            crearTabParcelas(),
            crearTabCultivos(),
            crearTabResponsables(),
            crearTabLabores(),
            crearTabConsultas(),
            crearTabInforme()
        );
        Scene scene = new Scene(tabPane, 1000, 700);
        try {
            scene.getStylesheets().add(getClass().getResource("estilos.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS no encontrado.");
        }
        stage.setScene(scene);
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.show();
    }
    // TAB PARCELAS
    private Tab crearTabParcelas() {
        Tab tab = new Tab("Parcelas");

        TextField txtCodigo    = campo("Codigo");
        TextField txtNombre    = campo("Nombre");
        TextField txtUbicacion = campo("Ubicacion");
        TextField txtArea      = campo("Area (m2)");
        TextField txtSuelo     = campo("Tipo de suelo");
        ComboBox<String> cboEstado = new ComboBox<>();
        cboEstado.getItems().addAll("Disponible", "En produccion", "En descanso");
        cboEstado.setPromptText("Seleccione estado");
        cboEstado.setMaxWidth(Double.MAX_VALUE);
        cboEstado.getStyleClass().add("combo-box");

        Button btnGuardar   = boton("Guardar",   "btn-verde");
        Button btnModificar = boton("Modificar", "btn-amarillo");
        Button btnEliminar  = boton("Eliminar",  "btn-rojo");
        Button btnLimpiar   = boton("Limpiar",   "btn-gris");
        Label lblMsg = new Label();

        TableView<Parcela> tabla = new TableView<>();
        agregarColumna(tabla, "Codigo", "codigo", 80);
        agregarColumna(tabla, "Nombre", "nombre", 130);
        agregarColumna(tabla, "Ubicacion", "ubicacion", 130);
        agregarColumna(tabla, "Area (m2)", "area", 80);
        agregarColumna(tabla, "Tipo Suelo", "tipoSuelo", 100);
        agregarColumna(tabla, "Estado", "estado", 100);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        cargarParcelas(tabla);

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                txtCodigo.setText(sel.getCodigo()); txtCodigo.setDisable(true);
                txtNombre.setText(sel.getNombre());
                txtUbicacion.setText(sel.getUbicacion());
                txtArea.setText(String.valueOf(sel.getArea()));
                txtSuelo.setText(sel.getTipoSuelo());
                cboEstado.setValue(sel.getEstado());
            }
        });

        btnGuardar.setOnAction(e -> {
            try {
                Parcela p = new Parcela(
                    txtCodigo.getText().trim(), txtNombre.getText().trim(),
                    txtUbicacion.getText().trim(), Double.parseDouble(txtArea.getText().trim()),
                    txtSuelo.getText().trim(), cboEstado.getValue() != null ? cboEstado.getValue() : ""
                );
                gestor.registrarParcela(p);
                exito(lblMsg, "Parcela registrada correctamente.");
                limpiarParcelas(txtCodigo, txtNombre, txtUbicacion, txtArea, txtSuelo, cboEstado, tabla);
                cargarParcelas(tabla);
            } catch (NumberFormatException ex) {
                error(lblMsg, "El area debe ser un numero valido.");
            } catch (Exception ex) { error(lblMsg, ex.getMessage()); }
        });

        btnModificar.setOnAction(e -> {
            if (tabla.getSelectionModel().getSelectedItem() == null) {
                error(lblMsg, "Seleccione una parcela para modificar."); return;
            }
            try {
                Parcela p = new Parcela(
                    txtCodigo.getText().trim(), txtNombre.getText().trim(),
                    txtUbicacion.getText().trim(), Double.parseDouble(txtArea.getText().trim()),
                    txtSuelo.getText().trim(), cboEstado.getValue() != null ? cboEstado.getValue() : ""
                );
                gestor.actualizarParcela(p);
                exito(lblMsg, "Parcela modificada correctamente.");
                limpiarParcelas(txtCodigo, txtNombre, txtUbicacion, txtArea, txtSuelo, cboEstado, tabla);
                cargarParcelas(tabla);
            } catch (Exception ex) { error(lblMsg, ex.getMessage()); }
        });

        btnEliminar.setOnAction(e -> {
            Parcela sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) { error(lblMsg, "Seleccione una parcela para eliminar."); return; }
            if (confirmar("Eliminar la parcela " + sel.getNombre() + "?")) {
                try {
                    gestor.eliminarParcela(sel.getCodigo());
                    exito(lblMsg, "Parcela eliminada.");
                    limpiarParcelas(txtCodigo, txtNombre, txtUbicacion, txtArea, txtSuelo, cboEstado, tabla);
                    cargarParcelas(tabla);
                } catch (Exception ex) { error(lblMsg, ex.getMessage()); }
            }
        });

        btnLimpiar.setOnAction(e -> {
            limpiarParcelas(txtCodigo, txtNombre, txtUbicacion, txtArea, txtSuelo, cboEstado, tabla);
            lblMsg.setText("");
        });

        GridPane grid = crearGrid(
            "Codigo:", txtCodigo, "Nombre:", txtNombre,
            "Ubicacion:", txtUbicacion, "Area (m2):", txtArea,
            "Tipo de suelo:", txtSuelo, "Estado:", cboEstado
        );

        HBox botones = new HBox(10, btnGuardar, btnModificar, btnEliminar, btnLimpiar);
        botones.setAlignment(Pos.CENTER_LEFT);

        VBox panelIzquierdo = new VBox(10,
            encabezado("Gestion de Parcelas", "Registre, edite y elimine parcelas"),
            tarjeta(grid), botones, lblMsg
        );
        panelIzquierdo.setPadding(new Insets(15));
        panelIzquierdo.setMinWidth(380);
        panelIzquierdo.setMaxWidth(420);

        Label lblRegistros = new Label("Registros existentes:");
        lblRegistros.getStyleClass().add("label-seccion");
        VBox panelDerecho = new VBox(10, lblRegistros, tabla);
        panelDerecho.setPadding(new Insets(15));
        VBox.setVgrow(tabla, Priority.ALWAYS);
        HBox.setHgrow(panelDerecho, Priority.ALWAYS);

        HBox layoutPrincipal = new HBox(10, panelIzquierdo, panelDerecho);
        layoutPrincipal.getStyleClass().add("fondo");

        tab.setContent(layoutPrincipal);
        return tab;
    }
    // TAB CULTIVOS
    private Tab crearTabCultivos() {
        Tab tab = new Tab("Cultivos");

        TextField txtCodigo   = campo("Codigo");
        TextField txtNombre   = campo("Nombre");
        TextField txtVariedad = campo("Variedad");
        TextField txtFecha    = campo("Fecha siembra (YYYY-MM-DD)");
        TextField txtExtra    = campo("Dias ciclo / Anos produccion");
        ComboBox<String> cboTipo = new ComboBox<>();
        cboTipo.getItems().addAll("Anual", "Perenne");
        cboTipo.setPromptText("Tipo de cultivo");
        cboTipo.setMaxWidth(Double.MAX_VALUE);
        cboTipo.getStyleClass().add("combo-box");
        cboTipo.setOnAction(e -> txtExtra.setPromptText(
            "Anual".equals(cboTipo.getValue()) ? "Duracion ciclo (dias)" : "Anos de produccion"));

        Button btnGuardar   = boton("Guardar",   "btn-verde");
        Button btnModificar = boton("Modificar", "btn-amarillo");
        Button btnEliminar  = boton("Eliminar",  "btn-rojo");
        Button btnLimpiar   = boton("Limpiar",   "btn-gris");
        Label lblMsg = new Label();

        TableView<Cultivo> tabla = new TableView<>();
        agregarColumna(tabla, "Codigo", "codigo", 80);
        agregarColumna(tabla, "Nombre", "nombre", 120);
        agregarColumna(tabla, "Variedad", "variedad", 100);
        agregarColumna(tabla, "Fecha Siembra", "fechaSiembra", 110);
        agregarColumna(tabla, "Tipo", "tipoCultivo", 80);
        TableColumn<Cultivo, String> colDesc = new TableColumn<>("Descripcion");
        colDesc.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue().obtenerDescripcion()));
        tabla.getColumns().add(colDesc);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        cargarCultivos(tabla);

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                txtCodigo.setText(sel.getCodigo()); txtCodigo.setDisable(true);
                txtNombre.setText(sel.getNombre());
                txtVariedad.setText(sel.getVariedad());
                txtFecha.setText(sel.getFechaSiembra());
                cboTipo.setValue(sel.getTipoCultivo());
                if (sel instanceof CultivoAnual)
                    txtExtra.setText(String.valueOf(((CultivoAnual) sel).getDuracionCicloDias()));
                else if (sel instanceof CultivoPerenne)
                    txtExtra.setText(String.valueOf(((CultivoPerenne) sel).getAniosProduccion()));
            }
        });

        btnGuardar.setOnAction(e -> {
            try {
                String tipo = cboTipo.getValue();
                if (tipo == null) { error(lblMsg, "Seleccione el tipo de cultivo."); return; }
                Cultivo c = "Anual".equals(tipo)
                    ? new CultivoAnual(txtCodigo.getText().trim(), txtNombre.getText().trim(),
                        txtVariedad.getText().trim(), txtFecha.getText().trim(),
                        Integer.parseInt(txtExtra.getText().trim()))
                    : new CultivoPerenne(txtCodigo.getText().trim(), txtNombre.getText().trim(),
                        txtVariedad.getText().trim(), txtFecha.getText().trim(),
                        Integer.parseInt(txtExtra.getText().trim()));
                gestor.registrarCultivo(c);
                exito(lblMsg, "Cultivo registrado correctamente.");
                limpiarCultivos(txtCodigo, txtNombre, txtVariedad, txtFecha, txtExtra, cboTipo, tabla);
                cargarCultivos(tabla);
            } catch (NumberFormatException ex) {
                error(lblMsg, "El valor numerico no es valido.");
            } catch (Exception ex) { error(lblMsg, ex.getMessage()); }
        });

        btnModificar.setOnAction(e -> {
            if (tabla.getSelectionModel().getSelectedItem() == null) {
                error(lblMsg, "Seleccione un cultivo para modificar."); return;
            }
            try {
                String tipo = cboTipo.getValue();
                if (tipo == null) { error(lblMsg, "Seleccione el tipo."); return; }
                Cultivo c = "Anual".equals(tipo)
                    ? new CultivoAnual(txtCodigo.getText().trim(), txtNombre.getText().trim(),
                        txtVariedad.getText().trim(), txtFecha.getText().trim(),
                        Integer.parseInt(txtExtra.getText().trim()))
                    : new CultivoPerenne(txtCodigo.getText().trim(), txtNombre.getText().trim(),
                        txtVariedad.getText().trim(), txtFecha.getText().trim(),
                        Integer.parseInt(txtExtra.getText().trim()));
                gestor.actualizarCultivo(c);
                exito(lblMsg, "Cultivo modificado correctamente.");
                limpiarCultivos(txtCodigo, txtNombre, txtVariedad, txtFecha, txtExtra, cboTipo, tabla);
                cargarCultivos(tabla);
            } catch (Exception ex) { error(lblMsg, ex.getMessage()); }
        });

        btnEliminar.setOnAction(e -> {
            Cultivo sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) { error(lblMsg, "Seleccione un cultivo para eliminar."); return; }
            if (confirmar("Eliminar el cultivo " + sel.getNombre() + "?")) {
                try {
                    gestor.eliminarCultivo(sel.getCodigo());
                    exito(lblMsg, "Cultivo eliminado.");
                    limpiarCultivos(txtCodigo, txtNombre, txtVariedad, txtFecha, txtExtra, cboTipo, tabla);
                    cargarCultivos(tabla);
                } catch (Exception ex) { error(lblMsg, ex.getMessage()); }
            }
        });

        btnLimpiar.setOnAction(e -> {
            limpiarCultivos(txtCodigo, txtNombre, txtVariedad, txtFecha, txtExtra, cboTipo, tabla);
            lblMsg.setText("");
        });

        GridPane grid = crearGrid(
            "Codigo:", txtCodigo, "Nombre:", txtNombre,
            "Variedad:", txtVariedad, "Fecha siembra:", txtFecha,
            "Tipo:", cboTipo, "Dias ciclo / Anos prod.:", txtExtra
        );

        HBox botones = new HBox(10, btnGuardar, btnModificar, btnEliminar, btnLimpiar);

        VBox panelIzquierdo = new VBox(10,
            encabezado("Gestion de Cultivos", "Administre cultivos anuales y perennes"),
            tarjeta(grid), botones, lblMsg
        );
        panelIzquierdo.setPadding(new Insets(15));
        panelIzquierdo.setMinWidth(380);
        panelIzquierdo.setMaxWidth(420);

        Label lblRegistros = new Label("Registros existentes:");
        lblRegistros.getStyleClass().add("label-seccion");
        VBox panelDerecho = new VBox(10, lblRegistros, tabla);
        panelDerecho.setPadding(new Insets(15));
        VBox.setVgrow(tabla, Priority.ALWAYS);
        HBox.setHgrow(panelDerecho, Priority.ALWAYS);

        HBox layoutPrincipal = new HBox(10, panelIzquierdo, panelDerecho);
        layoutPrincipal.getStyleClass().add("fondo");

        tab.setContent(layoutPrincipal);
        return tab;
    }
    // TAB RESPONSABLES
    private Tab crearTabResponsables() {
        Tab tab = new Tab("Responsables");

        TextField txtId           = campo("Identificacion");
        TextField txtNombre       = campo("Nombre completo");
        TextField txtCorreo       = campo("Correo electronico");
        TextField txtTel          = campo("Telefono");
        TextField txtFinca        = campo("Nombre de finca o asociacion");
        TextField txtEspecialidad = campo("Especialidad tecnica");

        ComboBox<String> cboTipo = new ComboBox<>();
        cboTipo.getItems().addAll("Productor", "Tecnico Agricola");
        cboTipo.setPromptText("Tipo de responsable");
        cboTipo.setMaxWidth(Double.MAX_VALUE);
        cboTipo.getStyleClass().add("combo-box");
        cboTipo.setOnAction(e -> {
            boolean esProductor = "Productor".equals(cboTipo.getValue());
            txtFinca.setDisable(!esProductor);
            txtEspecialidad.setDisable(esProductor);
        });
        txtFinca.setDisable(true);
        txtEspecialidad.setDisable(true);

        Button btnGuardar   = boton("Guardar",   "btn-verde");
        Button btnModificar = boton("Modificar", "btn-amarillo");
        Button btnEliminar  = boton("Eliminar",  "btn-rojo");
        Button btnLimpiar   = boton("Limpiar",   "btn-gris");
        Label lblMsg = new Label();

        TableView<Responsable> tabla = new TableView<>();
        agregarColumna(tabla, "Identificacion", "identificacion", 100);
        agregarColumna(tabla, "Nombre", "nombreCompleto", 150);
        agregarColumna(tabla, "Correo", "correo", 150);
        agregarColumna(tabla, "Telefono", "telefono", 90);
        agregarColumna(tabla, "Tipo", "tipoResponsable", 100);
        TableColumn<Responsable, String> colRol = new TableColumn<>("Rol / Detalle");
        colRol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue().obtenerRol()));
        tabla.getColumns().add(colRol);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        cargarResponsables(tabla);

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                txtId.setText(sel.getIdentificacion()); txtId.setDisable(true);
                txtNombre.setText(sel.getNombreCompleto());
                txtCorreo.setText(sel.getCorreo());
                txtTel.setText(sel.getTelefono());
                cboTipo.setValue(sel.getTipoResponsable());
                if (sel instanceof Productor) {
                    txtFinca.setText(((Productor) sel).getNombreFincaAsociacion());
                    txtFinca.setDisable(false);
                    txtEspecialidad.clear(); txtEspecialidad.setDisable(true);
                } else if (sel instanceof TecnicoAgricola) {
                    txtEspecialidad.setText(((TecnicoAgricola) sel).getEspecialidadTecnica());
                    txtEspecialidad.setDisable(false);
                    txtFinca.clear(); txtFinca.setDisable(true);
                }
            }
        });

        btnGuardar.setOnAction(e -> {
            try {
                String tipo = cboTipo.getValue();
                if (tipo == null) { error(lblMsg, "Seleccione el tipo de responsable."); return; }
                Responsable r = "Productor".equals(tipo)
                    ? new Productor(txtId.getText().trim(), txtNombre.getText().trim(),
                        txtCorreo.getText().trim(), txtTel.getText().trim(),
                        txtFinca.getText().trim())
                    : new TecnicoAgricola(txtId.getText().trim(), txtNombre.getText().trim(),
                        txtCorreo.getText().trim(), txtTel.getText().trim(),
                        txtEspecialidad.getText().trim());
                gestor.registrarResponsable(r);
                exito(lblMsg, "Responsable registrado correctamente.");
                limpiarResponsables(txtId, txtNombre, txtCorreo, txtTel, txtFinca, txtEspecialidad, cboTipo, tabla);
                cargarResponsables(tabla);
            } catch (Exception ex) { error(lblMsg, ex.getMessage()); }
        });

        btnModificar.setOnAction(e -> {
            if (tabla.getSelectionModel().getSelectedItem() == null) {
                error(lblMsg, "Seleccione un responsable para modificar."); return;
            }
            try {
                String tipo = cboTipo.getValue();
                if (tipo == null) { error(lblMsg, "Seleccione el tipo."); return; }
                Responsable r = "Productor".equals(tipo)
                    ? new Productor(txtId.getText().trim(), txtNombre.getText().trim(),
                        txtCorreo.getText().trim(), txtTel.getText().trim(),
                        txtFinca.getText().trim())
                    : new TecnicoAgricola(txtId.getText().trim(), txtNombre.getText().trim(),
                        txtCorreo.getText().trim(), txtTel.getText().trim(),
                        txtEspecialidad.getText().trim());
                gestor.actualizarResponsable(r);
                exito(lblMsg, "Responsable modificado correctamente.");
                limpiarResponsables(txtId, txtNombre, txtCorreo, txtTel, txtFinca, txtEspecialidad, cboTipo, tabla);
                cargarResponsables(tabla);
            } catch (Exception ex) { error(lblMsg, ex.getMessage()); }
        });

        btnEliminar.setOnAction(e -> {
            Responsable sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) { error(lblMsg, "Seleccione un responsable para eliminar."); return; }
            if (confirmar("Eliminar a " + sel.getNombreCompleto() + "?")) {
                try {
                    gestor.eliminarResponsable(sel.getIdentificacion());
                    exito(lblMsg, "Responsable eliminado.");
                    limpiarResponsables(txtId, txtNombre, txtCorreo, txtTel, txtFinca, txtEspecialidad, cboTipo, tabla);
                    cargarResponsables(tabla);
                } catch (Exception ex) { error(lblMsg, ex.getMessage()); }
            }
        });

        btnLimpiar.setOnAction(e -> {
            limpiarResponsables(txtId, txtNombre, txtCorreo, txtTel, txtFinca, txtEspecialidad, cboTipo, tabla);
            lblMsg.setText("");
        });

        GridPane grid = crearGrid(
            "Identificacion:", txtId,
            "Nombre completo:", txtNombre,
            "Correo:", txtCorreo,
            "Telefono:", txtTel,
            "Tipo:", cboTipo,
            "Finca / Asociacion:", txtFinca,
            "Especialidad tecnica:", txtEspecialidad
        );

        HBox botones = new HBox(10, btnGuardar, btnModificar, btnEliminar, btnLimpiar);

        VBox panelIzquierdo = new VBox(10,
            encabezado("Gestion de Responsables", "Productores y tecnicos agricolas"),
            tarjeta(grid), botones, lblMsg
        );
        panelIzquierdo.setPadding(new Insets(15));
        panelIzquierdo.setMinWidth(380);
        panelIzquierdo.setMaxWidth(420);

        Label lblRegistros = new Label("Registros existentes:");
        lblRegistros.getStyleClass().add("label-seccion");
        VBox panelDerecho = new VBox(10, lblRegistros, tabla);
        panelDerecho.setPadding(new Insets(15));
        VBox.setVgrow(tabla, Priority.ALWAYS);
        HBox.setHgrow(panelDerecho, Priority.ALWAYS);

        HBox layoutPrincipal = new HBox(10, panelIzquierdo, panelDerecho);
        layoutPrincipal.getStyleClass().add("fondo");

        tab.setContent(layoutPrincipal);
        return tab;
    }
    // TAB LABORES
    private Tab crearTabLabores() {
        Tab tab = new Tab("Labores Agricolas");

        TextField txtCodigo = campo("Codigo");
        TextField txtFecha  = campo("Fecha (YYYY-MM-DD)");
        TextField txtDesc   = campo("Descripcion");
        TextField txtCosto  = campo("Costo estimado");

        ComboBox<Parcela>     cboParcela     = new ComboBox<>();
        ComboBox<Cultivo>     cboCultivo     = new ComboBox<>();
        ComboBox<Responsable> cboResponsable = new ComboBox<>();
        ComboBox<String>      cboTipoLabor   = new ComboBox<>();
        cboTipoLabor.getItems().addAll("Siembra","Fertilizacion","Riego",
            "Control de plagas","Cosecha","Mantenimiento","Otro");
        cboTipoLabor.setPromptText("Tipo de labor");
        cboTipoLabor.setMaxWidth(Double.MAX_VALUE);
        cboParcela.setMaxWidth(Double.MAX_VALUE);
        cboCultivo.setMaxWidth(Double.MAX_VALUE);
        cboResponsable.setMaxWidth(Double.MAX_VALUE);

        try {
            cboParcela.setItems(FXCollections.observableArrayList(gestor.listarParcelas()));
            cboCultivo.setItems(FXCollections.observableArrayList(gestor.listarCultivos()));
            cboResponsable.setItems(FXCollections.observableArrayList(gestor.listarResponsables()));
        } catch (SQLException ex) { mostrarAlerta(ex.getMessage()); }

        Button btnGuardar   = boton("Guardar",         "btn-verde");
        Button btnModificar = boton("Modificar",       "btn-amarillo");
        Button btnEliminar  = boton("Eliminar",        "btn-rojo");
        Button btnLimpiar   = boton("Limpiar",         "btn-gris");
        Button btnRecargar  = boton("Recargar listas", "btn-gris");
        Label lblMsg = new Label();

        TableView<LaborAgricola> tabla = new TableView<>();
        agregarColumna(tabla, "Codigo", "codigo", 80);
        agregarColumna(tabla, "Fecha", "fecha", 100);
        TableColumn<LaborAgricola, String> colParcela = new TableColumn<>("Parcela");
        colParcela.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue().getParcela().getNombre()));
        TableColumn<LaborAgricola, String> colCultivo = new TableColumn<>("Cultivo");
        colCultivo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue().getCultivo().getNombre()));
        TableColumn<LaborAgricola, String> colResp = new TableColumn<>("Responsable");
        colResp.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue().getResponsable().getNombreCompleto()));
        agregarColumna(tabla, "Tipo Labor", "tipoLabor", 100);
        agregarColumna(tabla, "Costo", "costoEstimado", 80);
        tabla.getColumns().addAll(colParcela, colCultivo, colResp);
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(tabla, Priority.ALWAYS);
        cargarLabores(tabla);

        tabla.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                txtCodigo.setText(sel.getCodigo()); txtCodigo.setDisable(true);
                txtFecha.setText(sel.getFecha());
                txtDesc.setText(sel.getDescripcion());
                txtCosto.setText(String.valueOf(sel.getCostoEstimado()));
                cboTipoLabor.setValue(sel.getTipoLabor());
                cboParcela.getItems().stream()
                    .filter(p -> p.getCodigo().equals(sel.getParcela().getCodigo()))
                    .findFirst().ifPresent(cboParcela::setValue);
                cboCultivo.getItems().stream()
                    .filter(c -> c.getCodigo().equals(sel.getCultivo().getCodigo()))
                    .findFirst().ifPresent(cboCultivo::setValue);
                cboResponsable.getItems().stream()
                    .filter(r -> r.getIdentificacion().equals(sel.getResponsable().getIdentificacion()))
                    .findFirst().ifPresent(cboResponsable::setValue);
            }
        });

        btnRecargar.setOnAction(e -> {
            try {
                cboParcela.setItems(FXCollections.observableArrayList(gestor.listarParcelas()));
                cboCultivo.setItems(FXCollections.observableArrayList(gestor.listarCultivos()));
                cboResponsable.setItems(FXCollections.observableArrayList(gestor.listarResponsables()));
            } catch (SQLException ex) { mostrarAlerta(ex.getMessage()); }
        });

        btnGuardar.setOnAction(e -> {
            try {
                LaborAgricola l = new LaborAgricola(
                    txtCodigo.getText().trim(), cboParcela.getValue(), cboCultivo.getValue(),
                    cboResponsable.getValue(), txtFecha.getText().trim(),
                    cboTipoLabor.getValue() != null ? cboTipoLabor.getValue() : "",
                    txtDesc.getText().trim(), Double.parseDouble(txtCosto.getText().trim())
                );
                gestor.registrarLabor(l);
                exito(lblMsg, "Labor registrada correctamente.");
                limpiarLabores(txtCodigo, txtFecha, txtDesc, txtCosto,
                    cboParcela, cboCultivo, cboResponsable, cboTipoLabor, tabla);
                cargarLabores(tabla);
            } catch (NumberFormatException ex) {
                error(lblMsg, "El costo debe ser un numero valido.");
            } catch (Exception ex) { error(lblMsg, ex.getMessage()); }
        });

        btnModificar.setOnAction(e -> {
            if (tabla.getSelectionModel().getSelectedItem() == null) {
                error(lblMsg, "Seleccione una labor para modificar."); return;
            }
            try {
                LaborAgricola l = new LaborAgricola(
                    txtCodigo.getText().trim(), cboParcela.getValue(), cboCultivo.getValue(),
                    cboResponsable.getValue(), txtFecha.getText().trim(),
                    cboTipoLabor.getValue() != null ? cboTipoLabor.getValue() : "",
                    txtDesc.getText().trim(), Double.parseDouble(txtCosto.getText().trim())
                );
                gestor.actualizarLabor(l);
                exito(lblMsg, "Labor modificada correctamente.");
                limpiarLabores(txtCodigo, txtFecha, txtDesc, txtCosto,
                    cboParcela, cboCultivo, cboResponsable, cboTipoLabor, tabla);
                cargarLabores(tabla);
            } catch (Exception ex) { error(lblMsg, ex.getMessage()); }
        });

        btnEliminar.setOnAction(e -> {
            LaborAgricola sel = tabla.getSelectionModel().getSelectedItem();
            if (sel == null) { error(lblMsg, "Seleccione una labor para eliminar."); return; }
            if (confirmar("Eliminar la labor " + sel.getCodigo() + "?")) {
                try {
                    gestor.eliminarLabor(sel.getCodigo());
                    exito(lblMsg, "Labor eliminada.");
                    limpiarLabores(txtCodigo, txtFecha, txtDesc, txtCosto,
                        cboParcela, cboCultivo, cboResponsable, cboTipoLabor, tabla);
                    cargarLabores(tabla);
                } catch (Exception ex) { error(lblMsg, ex.getMessage()); }
            }
        });

        btnLimpiar.setOnAction(e -> {
            limpiarLabores(txtCodigo, txtFecha, txtDesc, txtCosto,
                cboParcela, cboCultivo, cboResponsable, cboTipoLabor, tabla);
            lblMsg.setText("");
        });

        GridPane grid = crearGrid(
            "Codigo:", txtCodigo, "Parcela:", cboParcela,
            "Cultivo:", cboCultivo, "Responsable:", cboResponsable,
            "Fecha:", txtFecha, "Tipo de labor:", cboTipoLabor,
            "Descripcion:", txtDesc, "Costo estimado:", txtCosto
        );

        HBox botones = new HBox(10, btnGuardar, btnModificar, btnEliminar, btnLimpiar, btnRecargar);

        VBox panelIzquierdo = new VBox(10,
            encabezado("Gestion de Labores Agricolas", "Registre y administre las labores"),
            tarjeta(grid), botones, lblMsg
        );
        panelIzquierdo.setPadding(new Insets(15));
        panelIzquierdo.setMinWidth(380);
        panelIzquierdo.setMaxWidth(420);

        Label lblRegistros = new Label("Registros existentes:");
        lblRegistros.getStyleClass().add("label-seccion");
        VBox panelDerecho = new VBox(10, lblRegistros, tabla);
        panelDerecho.setPadding(new Insets(15));
        VBox.setVgrow(tabla, Priority.ALWAYS);
        HBox.setHgrow(panelDerecho, Priority.ALWAYS);

        HBox layoutPrincipal = new HBox(10, panelIzquierdo, panelDerecho);
        layoutPrincipal.getStyleClass().add("fondo");

        tab.setContent(layoutPrincipal);
        return tab;
    }
    // TAB CONSULTAS
    private Tab crearTabConsultas() {
        Tab tab = new Tab("Consultas");

        TableView<Parcela> tablaParcelas = new TableView<>();
        agregarColumna(tablaParcelas, "Codigo", "codigo", 80);
        agregarColumna(tablaParcelas, "Nombre", "nombre", 130);
        agregarColumna(tablaParcelas, "Ubicacion", "ubicacion", 130);
        agregarColumna(tablaParcelas, "Area", "area", 80);
        agregarColumna(tablaParcelas, "Tipo Suelo", "tipoSuelo", 100);
        agregarColumna(tablaParcelas, "Estado", "estado", 100);
        tablaParcelas.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableView<Cultivo> tablaCultivos = new TableView<>();
        agregarColumna(tablaCultivos, "Codigo", "codigo", 80);
        agregarColumna(tablaCultivos, "Nombre", "nombre", 120);
        agregarColumna(tablaCultivos, "Variedad", "variedad", 100);
        agregarColumna(tablaCultivos, "Fecha Siembra", "fechaSiembra", 110);
        agregarColumna(tablaCultivos, "Tipo", "tipoCultivo", 80);
        TableColumn<Cultivo, String> colDescC = new TableColumn<>("Descripcion");
        colDescC.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue().obtenerDescripcion()));
        tablaCultivos.getColumns().add(colDescC);
        tablaCultivos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableView<Responsable> tablaResp = new TableView<>();
        agregarColumna(tablaResp, "Identificacion", "identificacion", 100);
        agregarColumna(tablaResp, "Nombre", "nombreCompleto", 150);
        agregarColumna(tablaResp, "Correo", "correo", 150);
        agregarColumna(tablaResp, "Telefono", "telefono", 90);
        agregarColumna(tablaResp, "Tipo", "tipoResponsable", 100);
        TableColumn<Responsable, String> colRolR = new TableColumn<>("Rol");
        colRolR.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue().obtenerRol()));
        tablaResp.getColumns().add(colRolR);
        tablaResp.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        TableView<LaborAgricola> tablaLabores = new TableView<>();
        agregarColumna(tablaLabores, "Codigo", "codigo", 80);
        agregarColumna(tablaLabores, "Fecha", "fecha", 100);
        TableColumn<LaborAgricola, String> colP = new TableColumn<>("Parcela");
        colP.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue().getParcela().getNombre()));
        TableColumn<LaborAgricola, String> colC = new TableColumn<>("Cultivo");
        colC.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue().getCultivo().getNombre()));
        TableColumn<LaborAgricola, String> colR = new TableColumn<>("Responsable");
        colR.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
            c.getValue().getResponsable().getNombreCompleto()));
        agregarColumna(tablaLabores, "Tipo", "tipoLabor", 100);
        agregarColumna(tablaLabores, "Costo", "costoEstimado", 80);
        tablaLabores.getColumns().addAll(colP, colC, colR);
        tablaLabores.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        Label lblSeccion = new Label("Seleccione que desea consultar:");
        lblSeccion.getStyleClass().add("label-seccion");
        StackPane panelTabla = new StackPane();
        VBox.setVgrow(panelTabla, Priority.ALWAYS);

        Button btnParcelas     = boton("Parcelas",     "btn-verde");
        Button btnCultivos     = boton("Cultivos",     "btn-verde");
        Button btnResponsables = boton("Responsables", "btn-verde");
        Button btnLabores      = boton("Labores",      "btn-verde");
        Label lblMsg = new Label();

        btnParcelas.setOnAction(e -> {
            try {
                tablaParcelas.setItems(FXCollections.observableArrayList(gestor.listarParcelas()));
                tablaParcelas.setPrefHeight(400);
                panelTabla.getChildren().setAll(tablaParcelas);
                lblSeccion.setText("Parcelas registradas:");
                exito(lblMsg, tablaParcelas.getItems().size() + " parcelas encontradas.");
            } catch (SQLException ex) { error(lblMsg, ex.getMessage()); }
        });

        btnCultivos.setOnAction(e -> {
            try {
                tablaCultivos.setItems(FXCollections.observableArrayList(gestor.listarCultivos()));
                tablaCultivos.setPrefHeight(400);
                panelTabla.getChildren().setAll(tablaCultivos);
                lblSeccion.setText("Cultivos registrados:");
                exito(lblMsg, tablaCultivos.getItems().size() + " cultivos encontrados.");
            } catch (SQLException ex) { error(lblMsg, ex.getMessage()); }
        });

        btnResponsables.setOnAction(e -> {
            try {
                tablaResp.setItems(FXCollections.observableArrayList(gestor.listarResponsables()));
                tablaResp.setPrefHeight(400);
                panelTabla.getChildren().setAll(tablaResp);
                lblSeccion.setText("Responsables registrados:");
                exito(lblMsg, tablaResp.getItems().size() + " responsables encontrados.");
            } catch (SQLException ex) { error(lblMsg, ex.getMessage()); }
        });

        btnLabores.setOnAction(e -> {
            try {
                tablaLabores.setItems(FXCollections.observableArrayList(gestor.listarLabores()));
                tablaLabores.setPrefHeight(400);
                panelTabla.getChildren().setAll(tablaLabores);
                lblSeccion.setText("Labores agricolas registradas:");
                exito(lblMsg, tablaLabores.getItems().size() + " labores encontradas.");
            } catch (SQLException ex) { error(lblMsg, ex.getMessage()); }
        });

        HBox botones = new HBox(10, btnParcelas, btnCultivos, btnResponsables, btnLabores);
        botones.setAlignment(Pos.CENTER_LEFT);

        VBox layout = new VBox(12,
            encabezado("Consulta de Registros", "Visualice la informacion almacenada en el sistema"),
            botones, lblMsg, lblSeccion, panelTabla
        );
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("fondo");
        VBox.setVgrow(panelTabla, Priority.ALWAYS);
        tab.setContent(layout);
        return tab;
    }
    // TAB INFORME
    private Tab crearTabInforme() {
        Tab tab = new Tab("Informe");

        Label lblInfo = new Label("Genera un informe con todas las labores agricolas registradas.");
        lblInfo.setWrapText(true);
        lblInfo.getStyleClass().add("label-seccion");

        Button btnTXT = boton("Generar informe TXT", "btn-verde");
        Label lblMsg  = new Label();

        TextArea areaInforme = new TextArea();
        areaInforme.setEditable(false);
        areaInforme.setWrapText(true);
        areaInforme.setPrefHeight(380);
        areaInforme.setPromptText("El contenido del informe aparecera aqui al generarlo...");
        areaInforme.getStyleClass().add("area-informe");
        VBox.setVgrow(areaInforme, Priority.ALWAYS);

        btnTXT.setOnAction(e -> {
            try {
                List<LaborAgricola> labores = gestor.listarLabores();
                if (labores.isEmpty()) {
                    error(lblMsg, "No hay labores registradas para exportar.");
                    areaInforme.clear();
                    return;
                }
                GeneradorInforme.generarTXT(labores, "informe_labores_agricolas.txt");
                StringBuilder sb = new StringBuilder();
                sb.append("========================================\n");
                sb.append("   INFORME DE LABORES AGRICOLAS\n");
                sb.append("========================================\n\n");
                for (LaborAgricola l : labores) {
                    sb.append("Codigo:          ").append(l.getCodigo()).append("\n");
                    sb.append("Fecha:           ").append(l.getFecha()).append("\n");
                    sb.append("Parcela:         ").append(l.getParcela().getNombre()).append("\n");
                    sb.append("Cultivo:         ").append(l.getCultivo().getNombre()).append("\n");
                    sb.append("Responsable:     ").append(l.getResponsable().getNombreCompleto()).append("\n");
                    sb.append("Tipo de labor:   ").append(l.getTipoLabor()).append("\n");
                    sb.append("Costo estimado:  ").append(l.getCostoEstimado()).append("\n");
                    sb.append("----------------------------------------\n");
                }
                areaInforme.setText(sb.toString());
                exito(lblMsg, "Informe generado con " + labores.size() + " registros. Guardado como informe_labores_agricolas.txt");
            } catch (Exception ex) {
                error(lblMsg, "Error: " + ex.getMessage());
            }
        });

        VBox tarjetaInfo = new VBox(8,
            new Label("El informe incluye:"),
            new Label("  - Codigo de la labor"),
            new Label("  - Fecha de realizacion"),
            new Label("  - Nombre de la parcela"),
            new Label("  - Nombre del cultivo"),
            new Label("  - Nombre del responsable"),
            new Label("  - Tipo de labor"),
            new Label("  - Costo estimado")
        );
        tarjetaInfo.getStyleClass().add("tarjeta-info");

        VBox layout = new VBox(12,
            encabezado("Generacion de Informe", "Exporte las labores agricolas a un archivo TXT"),
            lblInfo, tarjetaInfo, btnTXT, lblMsg, areaInforme
        );
        layout.setPadding(new Insets(20));
        layout.getStyleClass().add("fondo");
        tab.setContent(layout);
        return tab;
    }
    // METODOS DE CARGA
    private void cargarParcelas(TableView<Parcela> tabla) {
        try { tabla.setItems(FXCollections.observableArrayList(gestor.listarParcelas())); }
        catch (SQLException e) { mostrarAlerta(e.getMessage()); }
    }

    private void cargarCultivos(TableView<Cultivo> tabla) {
        try { tabla.setItems(FXCollections.observableArrayList(gestor.listarCultivos())); }
        catch (SQLException e) { mostrarAlerta(e.getMessage()); }
    }

    private void cargarResponsables(TableView<Responsable> tabla) {
        try { tabla.setItems(FXCollections.observableArrayList(gestor.listarResponsables())); }
        catch (SQLException e) { mostrarAlerta(e.getMessage()); }
    }

    private void cargarLabores(TableView<LaborAgricola> tabla) {
        try { tabla.setItems(FXCollections.observableArrayList(gestor.listarLabores())); }
        catch (SQLException e) { mostrarAlerta(e.getMessage()); }
    }
    // METODOS DE LIMPIEZA
    private void limpiarParcelas(TextField cod, TextField nom, TextField ubi,
            TextField area, TextField suelo, ComboBox<String> cbo, TableView<?> tabla) {
        cod.clear(); cod.setDisable(false); nom.clear(); ubi.clear();
        area.clear(); suelo.clear(); cbo.setValue(null);
        tabla.getSelectionModel().clearSelection();
    }

    private void limpiarCultivos(TextField cod, TextField nom, TextField var,
            TextField fecha, TextField extra, ComboBox<String> cbo, TableView<?> tabla) {
        cod.clear(); cod.setDisable(false); nom.clear(); var.clear();
        fecha.clear(); extra.clear(); cbo.setValue(null);
        tabla.getSelectionModel().clearSelection();
    }

    private void limpiarResponsables(TextField id, TextField nom, TextField correo,
            TextField tel, TextField finca, TextField esp, ComboBox<String> cbo, TableView<?> tabla) {
        id.clear(); id.setDisable(false); nom.clear(); correo.clear(); tel.clear();
        finca.clear(); esp.clear(); cbo.setValue(null);
        finca.setDisable(true); esp.setDisable(true);
        tabla.getSelectionModel().clearSelection();
    }

    private void limpiarLabores(TextField cod, TextField fecha, TextField desc,
            TextField costo, ComboBox<Parcela> cboPar, ComboBox<Cultivo> cboCul,
            ComboBox<Responsable> cboRes, ComboBox<String> cboTipo, TableView<?> tabla) {
        cod.clear(); cod.setDisable(false); fecha.clear(); desc.clear(); costo.clear();
        cboPar.setValue(null); cboCul.setValue(null); cboRes.setValue(null); cboTipo.setValue(null);
        tabla.getSelectionModel().clearSelection();
    }
    // COMPONENTES VISUALES
    private VBox encabezado(String titulo, String subtitulo) {
        Label lblTitulo = new Label(titulo);
        lblTitulo.getStyleClass().add("encabezado-titulo");
        Label lblSub = new Label(subtitulo);
        lblSub.getStyleClass().add("encabezado-subtitulo");
        VBox box = new VBox(4, lblTitulo, lblSub);
        box.getStyleClass().add("encabezado");
        return box;
    }

    private VBox tarjeta(javafx.scene.Node contenido) {
        VBox card = new VBox(contenido);
        card.getStyleClass().add("tarjeta");
        return card;
    }

    private TextField campo(String prompt) {
        TextField tf = new TextField();
        tf.setPromptText(prompt);
        tf.getStyleClass().add("campo");
        return tf;
    }

    private Button boton(String texto, String estilo) {
        Button b = new Button(texto);
        b.getStyleClass().add(estilo);
        return b;
    }

    private <T> void agregarColumna(TableView<T> tabla, String titulo, String propiedad, double ancho) {
        TableColumn<T, String> col = new TableColumn<>(titulo);
        col.setCellValueFactory(new PropertyValueFactory<>(propiedad));
        col.setPrefWidth(ancho);
        tabla.getColumns().add(col);
    }

    private GridPane crearGrid(Object... pares) {
        GridPane grid = new GridPane();
        grid.setHgap(12); grid.setVgap(10);
        grid.setPadding(new Insets(5));
        for (int i = 0; i < pares.length; i += 2) {
            Label lbl = new Label(pares[i].toString());
            lbl.getStyleClass().add("label-titulo");
            grid.add(lbl, 0, i / 2);
            javafx.scene.Node nodo = (javafx.scene.Node) pares[i + 1];
            GridPane.setHgrow(nodo, Priority.ALWAYS);
            nodo.setStyle("-fx-min-width: 240px;");
            grid.add(nodo, 1, i / 2);
        }
        return grid;
    }

    private void exito(Label lbl, String msg) {
        lbl.getStyleClass().removeAll("label-error", "label-exito");
        lbl.getStyleClass().add("label-exito");
        lbl.setText("Exito: " + msg);
    }

    private void error(Label lbl, String msg) {
        lbl.getStyleClass().removeAll("label-error", "label-exito");
        lbl.getStyleClass().add("label-error");
        lbl.setText("Error: " + msg);
    }

    private boolean confirmar(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, mensaje, ButtonType.YES, ButtonType.NO);
        alert.setTitle("Confirmacion");
        alert.setHeaderText(null);
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}