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

public class UserDashboardController {
    @FXML
    private Button btLogout;
    @FXML
    private Button btReabrir;
    @FXML
    private Button btNueva;
    @FXML
    private Button btEditar;
    @FXML
    private Button btEliminar;
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
    private MenuItem miSalir;
    @FXML
    private MenuItem miInforme;
    @FXML
    private MenuItem miImprimir;
    @FXML
    private MenuItem miManualDeUso;
    @FXML
    private MenuItem miAcercaDe;
    @FXML
    private Label lblUsuarioNombre;
    @FXML
    private Label lblUltimaActualizacion;
    @FXML
    private Label lblContador;
    @FXML
    private Label lblBtReabrir;
    @FXML
    private Label lblBtEliminar;
    @FXML
    private Label lblBtEditar;
    @FXML
    private Label lblBtNueva;

    private GestorIncidencias gestorIncidencias = new GestorIncidencias();
    private GestorUsuarios gestorUsuarios = new GestorUsuarios();
    private String usuarioEmail;
    private Usuario usuario;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private SimpleDateFormat formatAct = new SimpleDateFormat("HH:mm a");
    private ToggleGroup tgPrioridad;
    private ToggleGroup tgEstado;
    private Prioridad filtroPrioridad = null;
    private Estado filtroEstado = null;
    private int numIncidencias = 0;

    /**
     * Método utilizado por el login para asignar el tipo de usuario y su email a la nueva ventana.
     *
     * @param email Email del usuario / administrador / técnico a asignar.
     */
    public void setUsuarioEmail(String email) {
        this.usuarioEmail = email;
        this.usuario = gestorUsuarios.getUsuarioEmail(email);
        lblUsuarioNombre.setText(usuario.getNombre() + " " + usuario.getApellidos());
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
            actualizarBotones(tablaIncidencias.getSelectionModel().getSelectedItem());
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
                        gestorUsuarios.registrarLogout(usuarioEmail);
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
                stage.getIcons().add(new Image(getClass().getResourceAsStream("../recursos/pfp.png")));
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
        miImprimir.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Imprimir");
            alert.setHeaderText("Aún no se permite imprimir");
            alert.setContentText("Por favor, espera a futuras actualizaciones");
            alert.showAndWait();
        });
        miManualDeUso.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Manual de uso");
            alert.setHeaderText("Manual no disponible");
            alert.setContentText("Por favor, contacte a un administrador para saber más");
            alert.showAndWait();
        });
        miAcercaDe.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Acerca de");
            alert.setHeaderText("Sección no disponible");
            alert.setContentText("Por favor, contacte a un administrador para saber más");
            alert.showAndWait();
        });
        cbMostrarEtiquetas.setOnAction(event -> {
            if (cbMostrarEtiquetas.isSelected()) {
                lblBtNueva.setText("Nueva");
                lblBtEditar.setText("Editar");
                lblBtEliminar.setText("Eliminar");
                lblBtReabrir.setText("Reabrir");
            } else {
                lblBtNueva.setText("");
                lblBtEditar.setText("");
                lblBtEliminar.setText("");
                lblBtReabrir.setText("");
            }

        });
    }

    /**
     * Método para registrar el cierre de sesión del usuario en la base de datos. Se dispara automáticamente al cerrar
     * la aplicación.
     */
    @FXML
    public void logout() {
        try {
            gestorUsuarios.registrarLogout(usuarioEmail);
            Stage stage = (Stage) btLogout.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/vista/login.fxml"));
            stage.setScene(new Scene(root, 800, 600));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Método interno para cargar incidencias. Se ejecuta cuando se inicia y se refresca la aplicación, carga una lista
     * entera de todas las incidencias y las muestra en pantalla
     */
    public void cargarIncidencias() {
        try {
            ArrayList<Incidencia> todasIncidencias = gestorIncidencias.getAllIncidencias();
            ArrayList<Incidencia> activas = new ArrayList<>();
            ArrayList<Incidencia> inactivas = new ArrayList<>();
            int userId = gestorUsuarios.getUsuarioEmail(usuarioEmail).getIdUsuario();
            if (filtroPrioridad != null) {
                for (Incidencia incidencia : todasIncidencias) {
                    if (incidencia.getAutor() != null && incidencia.getAutor().getIdUsuario() == userId) {
                        if (incidencia.getPrioridad() == filtroPrioridad) {
                            if (!incidencia.getEstado().equals(Estado.CERRADA)) {
                                activas.add(incidencia);
                            } else {
                                inactivas.add(incidencia);
                            }
                        }
                    }
                }
            } else if (filtroEstado != null) {
                for (Incidencia incidencia : todasIncidencias) {
                    if (incidencia.getAutor() != null && incidencia.getAutor().getIdUsuario() == userId) {
                        if (incidencia.getEstado() == filtroEstado) {
                            if (!incidencia.getEstado().equals(Estado.CERRADA)) {
                                activas.add(incidencia);
                            } else {
                                inactivas.add(incidencia);
                            }
                        }
                    }
                }
            } else {
                for (Incidencia incidencia : todasIncidencias) {
                    if (incidencia.getAutor() != null && incidencia.getAutor().getIdUsuario() == userId) {
                        if (!incidencia.getEstado().equals(Estado.CERRADA)) {
                            activas.add(incidencia);
                        } else {
                            inactivas.add(incidencia);
                        }
                    }
                }
            }
            ArrayList<Incidencia> listaFiltrada = new ArrayList<>();
            listaFiltrada.addAll(activas);
            if (cbMostrarCerradas.isSelected()) {
                listaFiltrada.addAll(inactivas);
            }
            numIncidencias = listaFiltrada.size();
            ObservableList<Incidencia> incidenciasObservable = FXCollections.observableArrayList(listaFiltrada);
            tablaIncidencias.setItems(incidenciasObservable);
            actualizarBotones(tablaIncidencias.getSelectionModel().getSelectedItem());
            lblUltimaActualizacion.setText("Actualizado: " + formatAct.format(System.currentTimeMillis()));
            lblContador.setText(String.valueOf(numIncidencias));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Método para añadir nuevas incidencias a la base de datos.
     */
    @FXML
    public void nuevaIncidencia() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/nuevaIncidencia.fxml"));
            Parent root = loader.load();
            NuevaIncidenciaController controller = loader.getController();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Nueva Incidencia");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(btLogout.getScene().getWindow());
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("../recursos/pfp.png")));
            dialogStage.setScene(new Scene(root));
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

            if (controller.isGuardado()) {
                Incidencia nuevaIncidencia = controller.getIncidencia();
                if (gestorIncidencias.alta(nuevaIncidencia.getTitulo(), nuevaIncidencia.getDescripcion(), usuario, nuevaIncidencia.getPrioridad())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Incidencia guardada");
                    alert.setHeaderText("Se ha creado la nueva incidencia");
                    alert.showAndWait();
                    cargarIncidencias();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al crear");
                    alert.setHeaderText("Error al crear incidencia");
                    alert.setContentText("Ha ocurrido un error al crear la incidencia.");
                    alert.showAndWait();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Método para editar la incidencia en la base de datos.
     */
    @FXML
    public void editarIncidencia() {
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
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Incidencia editada");
                    alert.setHeaderText("Se ha editado la incidencia correctamente");
                    alert.showAndWait();
                    cargarIncidencias();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error al editar");
                    alert.setHeaderText("Error al editar incidencia");
                    alert.setContentText("Ha ocurrido un error al editar la incidencia.");
                    alert.showAndWait();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Método para eliminar incidencias de la base de datos.
     */
    @FXML
    public void eliminarIncidencia() {
        Incidencia seleccionada = tablaIncidencias.getSelectionModel().getSelectedItem();
        if (seleccionada.getEstado().equals(Estado.CERRADA)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error al eliminar incidencia");
            alert.setContentText("No puedes eliminar una incidencia cerrada.");
            alert.showAndWait();
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar baja");
        confirmacion.setHeaderText("¿Quieres dar de baja esta incidencia?");
        confirmacion.setContentText("Se podrá reabrir más adelante");
        Optional<ButtonType> resultado = confirmacion.showAndWait();
        String comentario = pedirComentario("Eliminar incidencia", "¿Por qué eliminas esta incidencia?");
        if (comentario == null) return;
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean eliminado = gestorIncidencias.eliminar(seleccionada);
            if (eliminado) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Incidencia eliminada");
                alert.setHeaderText("La incidencia se ha eliminado correctamente");
                alert.showAndWait();
                cargarIncidencias();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error al eliminar");
                alert.setHeaderText("Error al eliminar incidencia");
                alert.setContentText("Ha ocurrido un error al eliminar la incidencia.");
                alert.showAndWait();
            }
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
            alert.setContentText("No puedes reabrir una incidencia que no esta cerrada.");
            alert.showAndWait();
            return;
        }
        String comentario = pedirComentario("Reabrir incidencia", "¿Por qué reabres esta incidencia?");
        if (comentario == null) return;
        boolean respuesta = gestorIncidencias.reabrir(seleccionada);
        if (respuesta) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reabrir incidencia");
            alert.setHeaderText("Se ha reabierto la incidencia correctamente");
            alert.showAndWait();
            cargarIncidencias();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al reabrir incidencia");
            alert.setHeaderText("Error al reabrir incidencia");
            alert.setContentText("Ha ocurrido un error reabrir incidencia.");
            alert.showAndWait();
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
            alert.setTitle("Comentario vacío");
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
        ArrayList<HBox> filas = new ArrayList<>();
        filas.add(crearFila("ID:", String.valueOf(incidencia.getIdIncidencia()), "valor-normal"));
        filas.add(crearFila("Título:", incidencia.getTitulo(), "valor-normal"));
        filas.add(crearFila("Descripción:", incidencia.getDescripcion(), "valor-descripcion"));
        filas.add(crearFila("Fecha:", sdf.format(incidencia.getFechaAlta()), "valor-normal"));
        filas.add(crearFila("Estado:", (incidencia.getEstado().equals(Estado.EN_PROCESO) ? "EN PROCESO" : incidencia.getEstado().name()), "valor-normal"));
        if (incidencia.getPrioridad().equals(Prioridad.ALTA))
            filas.add(crearFila("Prioridad:", incidencia.getPrioridad().name(), "valor-alta"));
        if (incidencia.getPrioridad().equals(Prioridad.MEDIA))
            filas.add(crearFila("Prioridad:", incidencia.getPrioridad().name(), "valor-media"));
        if (incidencia.getPrioridad().equals(Prioridad.BAJA))
            filas.add(crearFila("Prioridad:", incidencia.getPrioridad().name(), "valor-baja"));
        filas.add(crearFila("Técnico asignado:", incidencia.getTecnico() != null ? incidencia.getTecnico().getNombre() + " " + incidencia.getTecnico().getApellidos() : "No tiene técnico asignado", "valor-normal"));

        GestorHistorico gestorHistorico = new GestorHistorico();
        ArrayList<HistoricoResolucion> listaHistorico = gestorHistorico.getHistoricoByIncidencia(incidencia.getIdIncidencia());
        if (!listaHistorico.isEmpty()) {
            HistoricoResolucion hr = listaHistorico.get(listaHistorico.size() - 1);
            HBox contenedorCambio = new HBox();
            Label lblCambio = new Label("Actualización:");
            lblCambio.setStyle("-fx-font-weight: bold; -fx-font-size: 17px; -fx-text-fill: #2c3e50; -fx-padding: 10 0 5 0;");
            contenedorCambio.getChildren().add(lblCambio);
            filas.add(contenedorCambio);
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
        } else {
            filas.add(crearFila("Histórico:", "No hay cambios registrados", "valor-na"));
        }

        for (HBox fila : filas) {
            vboxDetalles.getChildren().add(fila);
        }

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
        lblValor.setWrapText(true);
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
            btEliminar.setDisable(true);
            return;
        }
        if (incidenciaSeleccionada.getEstado().equals(Estado.CERRADA)) {
            btEditar.setDisable(true);
            btReabrir.setDisable(false);
            btEliminar.setDisable(true);
        }
        if (!incidenciaSeleccionada.getEstado().equals(Estado.CERRADA)) {
            btEditar.setDisable(false);
            btReabrir.setDisable(true);
            btEliminar.setDisable(false);
        }
    }
}