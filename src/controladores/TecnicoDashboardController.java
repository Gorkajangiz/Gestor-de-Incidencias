package controladores;

import gestor.GestorHistorico;
import gestor.GestorIncidencias;
import gestor.GestorUsuarios;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import modelo.*;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Optional;

public class TecnicoDashboardController {
    @FXML
    private Button btLogout;
    @FXML
    private Button btEspera;
    @FXML
    private Button btCerrar;
    @FXML
    private Button btReabrir;
    @FXML
    private Button btSolucionar;
    @FXML
    private Button btEditar;
    @FXML
    private CheckBox cbMostrarCerradas;
    @FXML
    private CheckBox cbMostrarEtiquetas;
    @FXML
    private TableView<Incidencia> tablaIncidencias;
    @FXML
    private TableColumn<Incidencia, String> colId;
    @FXML
    private TableColumn<Incidencia, String> colTitulo;
    @FXML
    private TableColumn<Incidencia, String> colPrioridad;
    @FXML
    private TableColumn<Incidencia, String> colEstado;
    @FXML
    private TableColumn<Incidencia, String> colFecha;
    @FXML
    private VBox vboxDetalles;
    @FXML
    private ScrollPane scrollDetalles;
    @FXML
    private RadioMenuItem rmiAlta;
    @FXML
    private RadioMenuItem rmiMedia;
    @FXML
    private RadioMenuItem rmiBaja;
    @FXML
    private RadioMenuItem rmiPendiente;
    @FXML
    private RadioMenuItem rmiEnProceso;
    @FXML
    private RadioMenuItem rmiEspera;
    @FXML
    private MenuItem miRestablecer;
    @FXML
    private MenuItem miInforme;
    @FXML
    private MenuItem miExpExcel;
    @FXML
    private MenuItem miImprimir;
    @FXML
    private MenuItem miManualDeUso;
    @FXML
    private MenuItem miAcercaDe;
    @FXML
    private MenuItem miSalir;
    @FXML
    private Label lblTecnicoNombre;
    @FXML
    private Label lblUltimaActualizacion;
    @FXML
    private Label lblContador;
    @FXML
    private Label lblBtSolucionar;
    @FXML
    private Label lblBtEspera;
    @FXML
    private Label lblBtCerrar;
    @FXML
    private Label lblBtEditar;
    @FXML
    private Label lblBtReabrir;

    private GestorIncidencias gestorIncidencias = new GestorIncidencias();
    private GestorUsuarios gestorUsuarios = new GestorUsuarios();
    private GestorHistorico gestorHistorico = new GestorHistorico();
    private String tecnicoEmail;
    private Usuario usuario;
    private ToggleGroup tgPrioridad;
    private ToggleGroup tgEstado;
    private Prioridad filtroPrioridad = null;
    private Estado filtroEstado = null;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private SimpleDateFormat formatAct = new SimpleDateFormat("HH:mm a");
    private int numIncidencias = 0;

    /**
     * Método utilizado por el login para asignar el tipo de usuario y su email a la nueva ventana.
     *
     * @param email Email del usuario / administrador / técnico a asignar.
     */
    public void setTecnicoEmail(String email) {
        this.tecnicoEmail = email;
        this.usuario = gestorUsuarios.getUsuarioEmail(email);
        lblTecnicoNombre.setText(usuario.getNombre() + " " + usuario.getApellidos());
        cargarIncidencias();
    }

    @FXML
    public void initialize() {
        Timeline timelineActualizaciones = new Timeline(new KeyFrame(Duration.seconds(60), event -> {
            cargarIncidencias();
        }));
        timelineActualizaciones.setCycleCount(Timeline.INDEFINITE);
        timelineActualizaciones.play();
        Timeline timelineReloj = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            lblUltimaActualizacion.setText("Actualizado: " + formatAct.format(System.currentTimeMillis()));
        }));
        timelineReloj.setCycleCount(Timeline.INDEFINITE);
        timelineReloj.play();

        colId.setCellValueFactory(new PropertyValueFactory<>("idIncidencia"));
        colTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colPrioridad.setCellValueFactory(cellData -> {
            Incidencia incidencia = cellData.getValue();
            return new SimpleStringProperty(incidencia.getPrioridad().name());
        });
        colEstado.setCellValueFactory(cellData -> {
            Incidencia incidencia = cellData.getValue();
            String retorno = (incidencia.getEstado().equals(Estado.EN_PROCESO) ? "EN PROCESO" : incidencia.getEstado().name());
            return new SimpleStringProperty(retorno);
        });
        colFecha.setCellValueFactory(cellData -> {
            Incidencia incidencia = cellData.getValue();
            String fecha = (incidencia.getFechaAlta() != null) ? sdf.format(incidencia.getFechaAlta()) : "";
            return new SimpleStringProperty(fecha);
        });
        colPrioridad.setCellFactory(column -> new TableCell<Incidencia, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "ALTA":
                            setStyle("-fx-text-fill: #ff4444;");
                            break;
                        case "MEDIA":
                            setStyle("-fx-text-fill: #ff9900;");
                            break;
                        case "BAJA":
                            setStyle("-fx-text-fill: #44aa44;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
        tablaIncidencias.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaIncidencias.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                mostrarDetallesIncidencia(newValue);
            }
            actualizarBotones(newValue);
        });
        cbMostrarCerradas.setSelected(false);
        cbMostrarCerradas.selectedProperty().addListener((observable, oldValue, newValue) -> {
            cargarIncidencias();
        });
        actualizarBotones(null);
        Platform.runLater(() -> {
            Stage stage = (Stage) btLogout.getScene().getWindow();
            stage.centerOnScreen();
            stage.setOnCloseRequest(event -> {
                event.consume();
                try {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Cerrar aplicación");
                    alert.setHeaderText("¿Estás seguro de que quieres salir?");
                    alert.setContentText("Se registrará el cierre de sesión automáticamente.");
                    ButtonType btnAceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
                    ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
                    alert.getButtonTypes().setAll(btnAceptar, btnCancelar);
                    Optional<ButtonType> resultado = alert.showAndWait();
                    if (resultado.isPresent() && resultado.get() == btnAceptar) {
                        gestorUsuarios.registrarLogout(tecnicoEmail);
                        stage.close();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });
        tgPrioridad = new ToggleGroup();
        tgEstado = new ToggleGroup();
        rmiAlta.setToggleGroup(tgPrioridad);
        rmiMedia.setToggleGroup(tgPrioridad);
        rmiBaja.setToggleGroup(tgPrioridad);
        rmiPendiente.setToggleGroup(tgEstado);
        rmiEnProceso.setToggleGroup(tgEstado);
        rmiEspera.setToggleGroup(tgEstado);
        tgPrioridad.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                filtroPrioridad = null;
            } else if (newValue == rmiAlta) {
                filtroPrioridad = Prioridad.ALTA;
            } else if (newValue == rmiMedia) {
                filtroPrioridad = Prioridad.MEDIA;
            } else if (newValue == rmiBaja) {
                filtroPrioridad = Prioridad.BAJA;
            }
            filtroEstado = null;
            cargarIncidencias();
        });
        tgEstado.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                filtroEstado = null;
            } else if (newValue == rmiPendiente) {
                filtroEstado = Estado.PENDIENTE;
            } else if (newValue == rmiEnProceso) {
                filtroEstado = Estado.EN_PROCESO;
            } else if (newValue == rmiEspera) {
                filtroEstado = Estado.ESPERA;
            }
            filtroPrioridad = null;
            cargarIncidencias();
        });
        miRestablecer.setOnAction(event -> {
            filtroPrioridad = null;
            filtroEstado = null;
            rmiAlta.setSelected(false);
            rmiMedia.setSelected(false);
            rmiBaja.setSelected(false);
            rmiPendiente.setSelected(false);
            rmiEnProceso.setSelected(false);
            rmiEspera.setSelected(false);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Filtros");
            alert.setHeaderText("Se han restablecido los filtros");
            alert.showAndWait();
            cargarIncidencias();
        });
        miSalir.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Salir");
            alert.setHeaderText("¿Estás seguro de que quieres salir?");
            alert.setContentText("Se cerrará sesión automáticamente.");
            ButtonType btnAceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(btnAceptar, btnCancelar);
            Optional<ButtonType> resultado = alert.showAndWait();
            if (resultado.isPresent() && resultado.get() == btnAceptar) {
                logout();
                System.exit(0);
            }
        });
        miInforme.setOnAction(event -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Guardar Reporte");
                fileChooser.setInitialFileName("incidencias.txt");
                Stage stage = (Stage) tablaIncidencias.getScene().getWindow();
                File archivo = fileChooser.showSaveDialog(stage);
                if (archivo != null) {
                    FileWriter writer = new FileWriter(archivo);
                    writer.write("Reporte de incidencias \n");
                    writer.write("---------------------------------------------------\n");
                    writer.write("ID | Título | Descripción | Fecha Alta | Fecha Fin\n");
                    writer.write("---------------------------------------------------\n");
                    for (Incidencia inc : tablaIncidencias.getItems()) {
                        writer.write("--------------Incidencia " + inc.getIdIncidencia() + "--------------" + "\n");
                        writer.write(inc.getTitulo() + "\n");
                        writer.write(inc.getDescripcion() + "\n");
                        writer.write(inc.getFechaAlta() + "\n");
                        writer.write((inc.getFechaFin() != null ? sdf.format(inc.getFechaFin()) : "No se ha cerrado aún"));
                    }
                    writer.write("\n\nTotal incidencias: " + tablaIncidencias.getItems().size() + "\n");
                    writer.close();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Reporte Generado");
                    alert.setHeaderText("Se ha generado el reporte");
                    alert.setContentText("Reporte guardado en:\n" + archivo.getAbsolutePath());
                    alert.showAndWait();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error al generar reporte");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
        miExpExcel.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Exportar en Excel");
            alert.setHeaderText("Aún no se permite exportar en Excel");
            alert.setContentText("Por favor, espera a futuras actualizaciones");
            alert.showAndWait();
        });
        miImprimir.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Imprimir");
            alert.setHeaderText("Aún no se permite imprimir");
            alert.setContentText("Por favor, espera a futuras actualizaciones");
            alert.showAndWait();
        });
        miManualDeUso.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Manual de uso");
            alert.setHeaderText("Manual no disponible");
            alert.setContentText("Por favor, contacte a un administrador para saber más");
            alert.showAndWait();
        });
        miAcercaDe.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Acerca de");
            alert.setHeaderText("Sección no disponible");
            alert.setContentText("Por favor, contacte a un administrador para saber más");
            alert.showAndWait();
        });
        cbMostrarEtiquetas.setOnAction(event -> {
            if (cbMostrarEtiquetas.isSelected()) {
                lblBtSolucionar.setText("Solucionar");
                lblBtEditar.setText("Editar");
                lblBtEspera.setText("Poner en espera");
                lblBtReabrir.setText("Reabrir");
                lblBtCerrar.setText("Cerrar");
            } else {
                lblBtSolucionar.setText("");
                lblBtEditar.setText("");
                lblBtEspera.setText("");
                lblBtReabrir.setText("");
                lblBtCerrar.setText("");
            }
        });
    }

    /**
     * Método para registrar el cierre de sesión del usuario en la base de datos. Se dispara automáticamente al cerrar
     * la aplicación.
     */
    @FXML
    private void logout() {
        try {
            gestorUsuarios.registrarLogout(tecnicoEmail);
            Stage stage = (Stage) btLogout.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/vista/login.fxml"));
            stage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método interno para cargar incidencias. Se ejecuta cuando se inicia y se refresca la aplicación, carga una lista
     * entera de todas las incidencias y las muestra en pantalla
     */
    private void cargarIncidencias() {
        try {
            ArrayList<Incidencia> todasIncidencias = gestorIncidencias.getAllIncidencias();
            ArrayList<Incidencia> activas = new ArrayList<>();
            ArrayList<Incidencia> inactivas = new ArrayList<>();
            if (filtroPrioridad != null) {
                for (Incidencia incidencia : todasIncidencias) {
                    if (incidencia.getPrioridad() == filtroPrioridad) {
                        if (!incidencia.getEstado().equals(Estado.CERRADA)) {
                            activas.add(incidencia);
                        } else {
                            inactivas.add(incidencia);
                        }
                    }
                }
            } else if (filtroEstado != null) {
                for (Incidencia incidencia : todasIncidencias) {
                    if (incidencia.getEstado() == filtroEstado) {
                        if (!incidencia.getEstado().equals(Estado.CERRADA)) {
                            activas.add(incidencia);
                        } else {
                            inactivas.add(incidencia);
                        }
                    }
                }
            } else {
                for (Incidencia incidencia : todasIncidencias) {
                    if (!incidencia.getEstado().equals(Estado.CERRADA)) {
                        activas.add(incidencia);
                    } else {
                        inactivas.add(incidencia);
                    }
                }
            }
            ArrayList<Incidencia> listaFiltrada = new ArrayList<>();
            listaFiltrada.addAll(activas);
            if (cbMostrarCerradas.isSelected()) {
                listaFiltrada.addAll(inactivas);
            }
            numIncidencias = listaFiltrada.size();
            lblContador.setText(String.valueOf(numIncidencias));
            lblUltimaActualizacion.setText("Actualizado: " + formatAct.format(System.currentTimeMillis()));
            ObservableList<Incidencia> incidenciasObservable = FXCollections.observableArrayList(listaFiltrada);
            tablaIncidencias.setItems(incidenciasObservable);
            actualizarBotones(tablaIncidencias.getSelectionModel().getSelectedItem());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Método para solucionar incidencias en la base de datos, solo si estan en Pendiente (Estado = En proceso)
     * Se registra en el histórico.
     */
    @FXML
    public void solucionarIncidencia() {
        Incidencia seleccionada = tablaIncidencias.getSelectionModel().getSelectedItem();
        if (seleccionada == null || usuario == null) return;

        boolean respuesta = gestorIncidencias.solucionarIncidencias(seleccionada, usuario);
        if (respuesta) {
            HistoricoResolucion historico = new HistoricoResolucion();
            historico.setIncidencia(seleccionada);
            historico.setEstadoPrevio(seleccionada.getEstado());
            historico.setEstadoNuevo(Estado.EN_PROCESO);
            historico.setPrioridadPrevia(seleccionada.getPrioridad());
            historico.setPrioridadNueva(seleccionada.getPrioridad());
            historico.setComentario("Solución iniciada");
            historico.setTecnico(usuario);
            gestorHistorico.registrarHistorico(historico);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Solución");
            alert.setHeaderText("Solución iniciada");
            alert.setContentText("Inicidencia en proceso. Se puede poner en espera o cerrar.");
            cargarIncidencias();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Ha ocurrido un error al solucionar la incidencia");
            alert.setContentText("Ha ocurrido un error al solucionar la incidencia, por favor contacte a un administrador");
            alert.showAndWait();
        }
    }

    /**
     * Método para poner en espera incidencias en la base de datos, solo si estan En proceso (Estado = En espera)
     * Se registra en el histórico.
     */
    @FXML
    public void ponerEnEspera() {
        Incidencia seleccionada = tablaIncidencias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;

        String comentario = pedirComentario("Poner en espera", "¿Por qué pones esta incidencia en espera?:");
        if (comentario == null) return;

        boolean respuesta = gestorIncidencias.ponerEnEspera(seleccionada);
        if (respuesta) {
            HistoricoResolucion historico = new HistoricoResolucion();
            historico.setIncidencia(seleccionada);
            historico.setEstadoPrevio(seleccionada.getEstado());
            historico.setEstadoNuevo(Estado.ESPERA);
            historico.setPrioridadPrevia(seleccionada.getPrioridad());
            historico.setPrioridadNueva(seleccionada.getPrioridad());
            historico.setComentario(comentario);
            historico.setTecnico(usuario);
            gestorHistorico.registrarHistorico(historico);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Espera");
            alert.setHeaderText("Incidencia en espera");
            alert.setContentText("Cuando se concluya, se debe cerrar");
            alert.showAndWait();
            cargarIncidencias();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Ha ocurrido un error al poner en espera la incidencia");
            alert.setContentText("Ha ocurrido un error al poner en espera la incidencia, por favor contacte a un administrador");
            alert.showAndWait();
        }
    }

    /**
     * Método para cerrar incidencias en la base de datos, solo si estan En proceso o En espera (Estado = Cerrada)
     * Se registra en el histórico.
     */
    @FXML
    public void cerrarIncidencia() {
        Incidencia seleccionada = tablaIncidencias.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cerrar");
        alert.setHeaderText("¿Has terminado con la incidencia?");
        alert.setContentText("Se podrá reabrir más adelante");
        alert.showAndWait();
        String comentario = pedirComentario("Cerrar incidencia", "Explica cómo resolviste esta incidencia:");
        if (comentario == null) return;

        boolean respuesta = gestorIncidencias.cerrarIncidencia(seleccionada);
        if (respuesta) {
            HistoricoResolucion historico = new HistoricoResolucion();
            historico.setIncidencia(seleccionada);
            historico.setEstadoPrevio(seleccionada.getEstado());
            historico.setEstadoNuevo(Estado.CERRADA);
            historico.setPrioridadPrevia(seleccionada.getPrioridad());
            historico.setPrioridadNueva(seleccionada.getPrioridad());
            historico.setComentario(comentario);
            historico.setTecnico(usuario);
            gestorHistorico.registrarHistorico(historico);
            Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
            alertInfo.setTitle("Cerrada");
            alertInfo.setHeaderText("Incidencia cerrada");
            alertInfo.setContentText("Se ha cerrado la incidencia, se puede reabrir");
            alertInfo.showAndWait();
            cargarIncidencias();
        } else {
            Alert alertError = new Alert(Alert.AlertType.ERROR);
            alertError.setTitle("Error");
            alertError.setHeaderText("Ha ocurrido un error al cerrar la incidencia");
            alertError.setContentText("Ha ocurrido un error al cerrar la incidencia, por favor contacte a un administrador");
            alertError.showAndWait();
        }
    }

    /**
     * Método para reabrir incidencias en la base de datos, solo si estan cerradas. Reabre la incidencia asignandole la
     * ID de la incidencia anterior y creando una nueva con una ID propia y los mismos datos.
     * Se registra en el histórico.
     */
    @FXML
    public void reabrirIncidencia() {
        Incidencia seleccionada = tablaIncidencias.getSelectionModel().getSelectedItem();
        if (seleccionada == null || usuario == null) return;

        if (!seleccionada.getEstado().equals(Estado.CERRADA)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al reabrir la incidencia");
            alert.setContentText("No puedes reabrir una incidencia que no está cerrada.");
            alert.showAndWait();
            return;
        }

        String comentario = pedirComentario("Reabrir incidencia", "¿Por qué reabres esta incidencia?");
        if (comentario == null) return;

        boolean respuesta = gestorIncidencias.reabrir(seleccionada);
        if (respuesta) {
            HistoricoResolucion historico = new HistoricoResolucion();
            historico.setIncidencia(seleccionada);
            historico.setEstadoPrevio(seleccionada.getEstado());
            historico.setEstadoNuevo(Estado.PENDIENTE);
            historico.setPrioridadPrevia(seleccionada.getPrioridad());
            historico.setPrioridadNueva(seleccionada.getPrioridad());
            historico.setComentario(comentario);
            historico.setTecnico(usuario);
            gestorHistorico.registrarHistorico(historico);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reabrir incidencia");
            alert.setHeaderText("Se ha reabierto la incidencia correctamente");
            alert.showAndWait();
            cargarIncidencias();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Ha ocurrido un error al reabrir la incidencia");
            alert.setContentText("Ha ocurrido un error al reabrir la incidencia, por favor contacte a un administrador");
            alert.showAndWait();
        }
    }

    /**
     * Método para editar la incidencia, únicamente permite cambiar su prioridad y se registra en el histórico
     */
    @FXML
    private void editarIncidencia() {
        Incidencia incidencia = tablaIncidencias.getSelectionModel().getSelectedItem();
        if (incidencia == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Incidencia no seleccionada");
            alert.setContentText("Seleccione un incidencia, por favor.");
            alert.showAndWait();
            return;
        }
        try {
            Prioridad prioridadAnterior = incidencia.getPrioridad();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/nuevaIncidencia.fxml"));
            Parent root = loader.load();
            NuevaIncidenciaController controller = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Editar Incidencia");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(btLogout.getScene().getWindow());
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("../recursos/pfp.png")));
            dialogStage.setScene(new Scene(root));
            controller.setDialogStage(dialogStage);
            controller.cargarDatosIncidencia(incidencia, usuario);
            dialogStage.showAndWait();
            if (controller.isGuardado()) {
                String nuevoTitulo = controller.getIncidencia().getTitulo();
                String nuevaDescripcion = controller.getIncidencia().getDescripcion();
                Prioridad nuevaPrioridad = controller.getIncidencia().getPrioridad();
                if (gestorIncidencias.modificar(incidencia, nuevoTitulo, nuevaDescripcion, nuevaPrioridad)) {
                    if (!prioridadAnterior.equals(nuevaPrioridad)) {
                        String comentario = pedirComentario("Cambio de prioridad", "¿Por qué cambiaste la prioridad de " + prioridadAnterior + " a " + nuevaPrioridad + "?");
                        if (comentario != null) {
                            HistoricoResolucion historico = new HistoricoResolucion();
                            historico.setIncidencia(incidencia);
                            historico.setEstadoPrevio(incidencia.getEstado());
                            historico.setEstadoNuevo(incidencia.getEstado());
                            historico.setPrioridadPrevia(prioridadAnterior);
                            historico.setPrioridadNueva(nuevaPrioridad);
                            historico.setComentario(comentario);
                            historico.setTecnico(usuario);
                            gestorHistorico.registrarHistorico(historico);
                        }
                    }
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Incidencia editada");
                    alert.setHeaderText("Se ha editado la incidencia correctamente");
                    alert.showAndWait();
                    cargarIncidencias();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Ha ocurrido un error al editar la incidencia");
                    alert.setContentText("Ha ocurrido un error al editar la incidencia, por favor contacte a un administrador");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método interno para mostrar una ventana en pantalla que pide un comentario al usuario.
     *
     * @param titulo Título de la ventana a abrir
     * @param header Frase principal de la ventana a abrir
     * @return String comentario
     */
    private String pedirComentario(String titulo, String header) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(titulo);
        dialog.setHeaderText(header);
        dialog.setContentText("Comentario (obligatorio):");

        while (true) {
            Optional<String> resultado = dialog.showAndWait();
            if (!resultado.isPresent()) {
                return null;
            }
            String comentario = resultado.get();
            if (!comentario.isEmpty()) {
                return comentario;
            }
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("El comentario es obligatorio");
            alert.setContentText("Por favor, escribe un comentario.");
            alert.showAndWait();
        }
    }

    /**
     * Método interno para mostrar detalles de una incidencia seleccionada en la tabla derecha.
     *
     * @param incidencia Incidencia recogida por la ventana al pulsar en una de la lista
     */
    private void mostrarDetallesIncidencia(Incidencia incidencia) {
        vboxDetalles.getChildren().clear();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        ArrayList<HBox> filas = new ArrayList<>();

        filas.add(crearFila("ID:", String.valueOf(incidencia.getIdIncidencia()), "valor-normal"));
        filas.add(crearFila("Título:", incidencia.getTitulo(), "valor-destacado"));
        filas.add(crearFila("Descripción:", incidencia.getDescripcion(), "valor-descripcion"));

        if (incidencia.getAutor() != null) {
            filas.add(crearFila("Autor:", incidencia.getAutor().getNombre() + " " + incidencia.getAutor().getApellidos(), "valor-normal"));
            filas.add(crearFila("Email autor:", incidencia.getAutor().getEmail(), "valor-normal"));
        }

        filas.add(crearFila("Fecha alta:", sdf.format(incidencia.getFechaAlta()), "valor-normal"));

        if (incidencia.getFechaInicio() != null) {
            filas.add(crearFila("Fecha inicio:", sdf.format(incidencia.getFechaInicio()), "valor-normal"));
        } else {
            filas.add(crearFila("Fecha inicio:", "No se ha iniciado", "valor-normal"));
        }

        if (incidencia.getFechaFin() != null) {
            filas.add(crearFila("Fecha fin:", sdf.format(incidencia.getFechaFin()), "valor-normal"));
        } else {
            filas.add(crearFila("Fecha fin:", "No finalizada", "valor-normal"));
        }

        filas.add(crearFila("Estado:", (incidencia.getEstado().equals(Estado.EN_PROCESO) ? "EN PROCESO" : incidencia.getEstado().name()), "valor-normal"));
        if (incidencia.getPrioridad().equals(Prioridad.ALTA))
            filas.add(crearFila("Prioridad:", incidencia.getPrioridad().name(), "valor-alta"));
        if (incidencia.getPrioridad().equals(Prioridad.MEDIA))
            filas.add(crearFila("Prioridad:", incidencia.getPrioridad().name(), "valor-media"));
        if (incidencia.getPrioridad().equals(Prioridad.BAJA))
            filas.add(crearFila("Prioridad:", incidencia.getPrioridad().name(), "valor-baja"));
        filas.add(crearFila("Técnico asignado:", incidencia.getTecnico() != null ? incidencia.getTecnico().getNombre() + " " + incidencia.getTecnico().getApellidos() : "No tiene técnico asignado", "valor-normal"));
        filas.add(crearFila("ID Incidencia previa:", incidencia.getIncidenciaAnterior() != null ? String.valueOf(incidencia.getIncidenciaAnterior().getIdIncidencia()) : "No se ha reabierto", "valor-normal"));

        GestorHistorico gestorHistorico = new GestorHistorico();
        ArrayList<HistoricoResolucion> listaHistorico = gestorHistorico.getHistoricoByIncidencia(incidencia.getIdIncidencia());
        if (!listaHistorico.isEmpty()) {
            for (int i = 0; i < listaHistorico.size(); i++) {
                HistoricoResolucion hr = listaHistorico.get(i);
                HBox contenedorCambio = new HBox();
                Label lblCambio = new Label("Cambio #" + (i + 1));
                lblCambio.setStyle("-fx-font-weight: bold; -fx-font-size: 17px; -fx-text-fill: #2c3e50; -fx-padding: 10 0 5 0;");
                contenedorCambio.getChildren().add(lblCambio);
                filas.add(contenedorCambio);
                filas.add(crearFila("ID Histórico:", String.valueOf(hr.getIdHistorico()), "valor-estado"));
                filas.add(crearFila("ID Incidencia:", String.valueOf(incidencia.getIdIncidencia()), "valor-estado"));
                filas.add(crearFila("Fecha de la edición:", sdf.format(hr.getFechaCambio()), "valor-estado"));
                filas.add(crearFila("Estado:", (hr.getEstadoPrevio().equals(Estado.EN_PROCESO) ? "EN PROCESO" : hr.getEstadoPrevio().name()) + " → " + (hr.getEstadoNuevo().equals(Estado.EN_PROCESO) ? "EN PROCESO" : hr.getEstadoNuevo().name()), "valor-estado"));
                if (incidencia.getPrioridad().equals(Prioridad.ALTA))
                    filas.add(crearFila("Prioridad:", hr.getPrioridadPrevia().name() + " → " + hr.getPrioridadNueva().name(), "valor-alta"));
                if (incidencia.getPrioridad().equals(Prioridad.MEDIA))
                    filas.add(crearFila("Prioridad:", hr.getPrioridadPrevia().name() + " → " + hr.getPrioridadNueva().name(), "valor-media"));
                if (incidencia.getPrioridad().equals(Prioridad.BAJA))
                    filas.add(crearFila("Prioridad:", hr.getPrioridadPrevia().name() + " → " + hr.getPrioridadNueva().name(), "valor-baja"));
                filas.add(crearFila("Comentario:", hr.getComentario(), "valor-descripcion"));
                if (hr.getTecnico() != null) {
                    filas.add(crearFila("Técnico responsable:", hr.getTecnico().getNombre() + " " + hr.getTecnico().getApellidos(), "valor-normal"));
                }
            }
        } else {
            filas.add(crearFila("Histórico:", "No hay cambios registrados", "valor-na"));
        }

        for (HBox fila : filas) {
            vboxDetalles.getChildren().add(fila);
        }

        Platform.runLater(() -> scrollDetalles.setVvalue(0));
        actualizarBotones(incidencia);
    }

    /**
     * Método interno para generar las filas de la tabla de la derecha, usado por mostrarDetallesIncidencia(Inidencia inc)
     *
     * @param etiqueta    Título de la columna (Nombre: )
     * @param valor       Contenido principal de la columna (Jorge)
     * @param estiloValor Estilos añadidos a la columna
     * @return HBox fila entera
     */
    private HBox crearFila(String etiqueta, String valor, String estiloValor) {
        HBox fila = new HBox();
        fila.getStyleClass().add("fila-detalle");
        Label lblEtiqueta = new Label(etiqueta);
        lblEtiqueta.getStyleClass().add("etiqueta-detalle");
        Label lblValor = new Label(valor);
        lblValor.getStyleClass().add(estiloValor);
        HBox.setHgrow(lblValor, Priority.ALWAYS);
        fila.getChildren().addAll(lblEtiqueta, lblValor);
        return fila;
    }

    /**
     * Método interno para habilitar y deshabilitar los botones en base al tipo de incidencia que este seleccionada y lo
     * que se pueda hacer con ella
     *
     * @param incidenciaSeleccionada Incidencia seleccionada en la lista
     */
    private void actualizarBotones(Incidencia incidenciaSeleccionada) {
        boolean seleccionada = incidenciaSeleccionada != null;

        if (!seleccionada) {
            btEditar.setDisable(true);
            btReabrir.setDisable(true);
            btSolucionar.setDisable(true);
            btEspera.setDisable(true);
            btCerrar.setDisable(true);
            return;
        }

        Estado estado = incidenciaSeleccionada.getEstado();
        boolean activa = incidenciaSeleccionada.isActivo();

        if (estado.equals(Estado.CERRADA)) {
            btEditar.setDisable(true);
            btReabrir.setDisable(false);
            btSolucionar.setDisable(true);
            btEspera.setDisable(true);
            btCerrar.setDisable(true);
        } else {
            btEditar.setDisable(false);
            btReabrir.setDisable(true);

            if (estado.equals(Estado.PENDIENTE)) {
                btSolucionar.setDisable(false);
                btEspera.setDisable(true);
                btCerrar.setDisable(true);
            } else if (estado.equals(Estado.EN_PROCESO)) {
                btSolucionar.setDisable(true);
                btEspera.setDisable(false);
                btCerrar.setDisable(false);
            } else if (estado.equals(Estado.ESPERA)) {
                btSolucionar.setDisable(true);
                btEspera.setDisable(true);
                btCerrar.setDisable(false);
            }
        }
    }
}