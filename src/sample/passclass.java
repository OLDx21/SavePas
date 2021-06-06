package sample;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class passclass {
    int pipitki;



    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField passfield;

    @FXML
    private Button vxodttn;

    @FXML
    void initialize() {

        vxodttn.setDefaultButton(true);
     vxodttn.setOnAction(event -> {
         if(passfield.getText().equals(Passwords.PASSWORD_APP)){
             FXMLLoader loader = new FXMLLoader();
             vxodttn.getScene().getWindow().hide();
             loader.setLocation(getClass().getResource("sample.fxml"));
             try {
                 loader.load();
             } catch (IOException e) {
                 e.printStackTrace();
             }
             Parent root = loader.getRoot();


             Stage stage = new Stage();
             stage.setResizable(false);
             stage.setScene(new Scene(root));
             stage.show();
         }else {
             pipitki++;
             Controller.showinfo("Неверный пароль!");
             if(pipitki==5){
                 Passwords.PASSWORD_APP = randomparol();


                 CompletableFuture.runAsync(()->sendmail(Passwords.PASSWORD_APP));
                 Controller.showinfo("Вам на почту отправлен новый временный пароль!");
                 if (pipitki==15){
                     System.exit(1);
                 }
             }
         }
     });

    }
    public void sendmail(String pas) {
        try {
            final Properties properties = new Properties();
            properties.load(passclass.class.getClassLoader().getResourceAsStream("sample/email.properties"));
            Session mailsesion = Session.getDefaultInstance(properties);
            MimeMessage message = new MimeMessage(mailsesion);
            message.setFrom(new InternetAddress("maksumdem02@gmail.com"));
            message.addRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress("maksumdem02@gmail.com")));
            message.setSubject("WARNING");
            message.setText(pas);
            Transport tr = mailsesion.getTransport();
            tr.connect(null, Passwords.PASSWORD_EMAIL);
            tr.sendMessage(message, message.getAllRecipients());
            tr.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }
    public String randomparol(){

String ok = String.valueOf(Math.random()*10000);
return ok;
    }
}

