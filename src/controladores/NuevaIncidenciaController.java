package controladores;

import gestor.GestorIncidencias;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import modelo.Incidencia;
import modelo.Perfil;
import modelo.Prioridad;
import modelo.Usuario;

public class NuevaIncidenciaController {
    @FXML
    private TextField tituloField;
    @FXML
    private TextArea descripcionArea;
    @FXML
    private ComboBox<Prioridad> prioridadCombo;
    @FXML
    private Button btGuardar;
    @FXML
    private Button btCancelar;
    @FXML
    private Label lblTituloDialogo;

    private Stage dialogStage;
    private boolean guardado = false;
    private GestorIncidencias gestorIncidencias;
    private boolean editando = false;
    private Incidencia incidenciaEditando;

    @FXML
    public void initialize() {
        prioridadCombo.getItems().setAll(Prioridad.values());
        prioridadCombo.setValue(Prioridad.MEDIA);
        gestorIncidencias = new GestorIncidencias();
    }

    /**
     * Método para guardar los datos de la ventana recién abierta.
     */
    @FXML
    private void guardar() {
        if (tituloField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("El titulo es obligatorio");
            alert.setContentText("Introduce el titulo, por favor.");
            alert.showAndWait();
            return;
        }
        if (descripcionArea.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("La descripción es obligatoria");
            alert.setContentText("Introduce una descripción, por favor.");
            alert.showAndWait();
            return;
        }
        guardado = true;
        dialogStage.close();
    }

    /**
     * Método para cancelar la ventana.
     */
    @FXML
    private void cancelar() {
        guardado = false;
        dialogStage.close();
    }

    /**
     * Método para alterar la ventana y ajustarla a los requerimientos de añadir una nueva incidencia, reciclandola.
     */
    public void configurarNueva() {
        editando = false;
        lblTituloDialogo.setText("Nueva Incidencia");
        dialogStage.setTitle("Nueva Incidencia");
        tituloField.clear();
        descripcionArea.clear();
        prioridadCombo.setValue(Prioridad.MEDIA);
        btGuardar.setText("Crear");
    }

    /**
     * Método para alterar la ventana y ajustarla a los requerimientos de editar una incidencia, reciclandola.
     *
     * @param incidencia La incidencia seleccionada en la lista
     */
    public void configurarEdicion(Incidencia incidencia) {
        editando = true;
        incidenciaEditando = incidencia;
        lblTituloDialogo.setText("Editar Incidencia");
        dialogStage.setTitle("Editar Incidencia");
        tituloField.setText(incidencia.getTitulo());
        descripcionArea.setText(incidencia.getDescripcion());
        prioridadCombo.setValue(incidencia.getPrioridad());
        btGuardar.setText("Guardar Cambios");
    }

    /**
     * Un limpiador privado de campos para la ventana que también la prepara para ser usada en configurarNueva
     */
    public void limpiador() {
        tituloField.clear();
        descripcionArea.clear();
        prioridadCombo.setValue(Prioridad.MEDIA);
        lblTituloDialogo.setText("Nueva Incidencia");
    }

    /**
     * Setter del dialogStage
     *
     * @param dialogStage DialogStage a settear
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Getter de incidencias personalizado para dividir entre las incidencias editadas y las nuevas.
     *
     * @return
     */
    public Incidencia getIncidencia() {
        if (guardado) {
            if (editando) {
                incidenciaEditando.setTitulo(tituloField.getText());
                incidenciaEditando.setDescripcion(descripcionArea.getText());
                incidenciaEditando.setPrioridad(prioridadCombo.getValue());
                return incidenciaEditando;
            } else {
                Incidencia nuevaIncidencia = new Incidencia();
                nuevaIncidencia.setTitulo(tituloField.getText());
                nuevaIncidencia.setDescripcion(descripcionArea.getText());
                nuevaIncidencia.setPrioridad(prioridadCombo.getValue());
                return nuevaIncidencia;
            }
        }
        return null;
    }

    /**
     * Un método para cargar los datos en la incidencia y poder editarla.
     *
     * @param incidencia Incidencia seleccionada de la que se sacan los datos
     * @param usuario    Usuario respectivo enlazado a la incidencia
     */
    public void cargarDatosIncidencia(Incidencia incidencia, Usuario usuario) {
        btGuardar.setText("Guardar cambios");
        if (usuario.getPerfil().equals(Perfil.USUARIO)) {
            tituloField.setText(incidencia.getTitulo());
            descripcionArea.setText(incidencia.getDescripcion());
            prioridadCombo.setValue(incidencia.getPrioridad());
            lblTituloDialogo.setText("Editar Incidencia");
        } else {
            tituloField.setText(incidencia.getTitulo());
            descripcionArea.setText(incidencia.getDescripcion());
            prioridadCombo.setValue(incidencia.getPrioridad());
            tituloField.setDisable(true);
            descripcionArea.setDisable(true);
            lblTituloDialogo.setText("Editar Incidencia");
        }
    }

    public boolean isGuardado() {
        return guardado;
    }

    public void setGuardado(boolean guardado) {
        this.guardado = guardado;
    }

    public boolean isEditando() {
        return editando;
    }

    public void setEditando(boolean editando) {
        this.editando = editando;
    }
}