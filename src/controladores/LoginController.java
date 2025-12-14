package controladores;

import dao.LoginDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.io.InputStream;

public class LoginController {
    @FXML
    TextField tfEmail;
    @FXML
    PasswordField pfPassword;
    @FXML
    Button loginBoton;
    @FXML
    Label lbErrorLogin;

    LoginDAO loginDAO = new LoginDAO();

    @FXML
    public void initialize() {
        loginBoton.setOnAction(e -> {
            login();
        });
        pfPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                login();
            }
        });
        pfPassword.setOnMouseClicked(event -> {
            lbErrorLogin.setVisible(false);
        });
        tfEmail.setOnMouseClicked(event -> {
            lbErrorLogin.setVisible(false);
        });
    }

    /**
     * Método para enviar los datos del usuario al gestor y registrar una entrada en el login.
     */
    private void login() {
        String email = tfEmail.getText();
        String contraseña = pfPassword.getText();
        try {
            int resultado = loginDAO.verificarLogin(email, contraseña);
            switch (resultado) {
                case 0:
                    abrirVentana("user");
                    loginDAO.registrarLogin(email);
                    break;
                case 1:
                    abrirVentana("tecnico");
                    loginDAO.registrarLogin(email);
                    break;
                case 2:
                    abrirVentana("admin");
                    loginDAO.registrarLogin(email);
                    break;
                case 3:
                    lbErrorLogin.setText("Datos incorrectos");
                    lbErrorLogin.setVisible(true);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para redirigir al usuario a la ventana con los datos respectivos que ha introducido.
     *
     * @param ventana Nombre de la ventana para localizar cual debe abrir.
     */
    private void abrirVentana(String ventana) {
        try {
            String ventanaPath = "/vista/" + ventana + "_dashboard.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ventanaPath));
            Parent root = loader.load();
            switch (ventana) {
                case "user":
                    UserDashboardController controllerUser = loader.getController();
                    controllerUser.setUsuarioEmail(tfEmail.getText());
                    break;
                case "tecnico":
                    TecnicoDashboardController controllerTecnico = loader.getController();
                    controllerTecnico.setTecnicoEmail(tfEmail.getText());
                    break;
                case "admin":
                    AdminDashboardController controllerAdmin = loader.getController();
                    controllerAdmin.setAdminEmail(tfEmail.getText());
                    break;
            }
            int ancho = 1200;
            int alto = 720;
            if ("admin".equals(ventana)) {
                ancho = 1800;
                alto = 900;
            }
            if ("tecnico".equals(ventana)) {
                ancho = 1500;
                alto = 900;
            }
            Stage ventanaActual = (Stage) loginBoton.getScene().getWindow();
            Scene escena = new Scene(root, ancho, alto);
            ventanaActual.setScene(escena);
            ventanaActual.setTitle("Gestor de incidencias");
            ventanaActual.centerOnScreen();
            InputStream is = getClass().getResourceAsStream("../recursos/pfp.png");
            ventanaActual.getIcons().add(new Image(is));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}