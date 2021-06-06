package sample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.Unmarshaller;

public class Controller {
    ObservableList<String> inf1 = FXCollections.observableArrayList();
    String vibraniyelement;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuBar menubar;

    @FXML
    private TextField loginfield;

    @FXML
    private TextField namefield;

    @FXML
    private ListView<String> listview;

    @FXML
    private PasswordField passwordfield;

    @FXML
    private Button getpasbtn;

    @FXML
    private Button renamebtn;

    @FXML
    private Button obnovabtn;
    @FXML
    private TextField maskirovkaebat;


    @FXML
    private Button savebtn;

    @FXML
    private Button deletebtn;

    @FXML
    private CheckBox showpassword;

    @FXML
    void initialize() {

        dbHandler dbHandler = new dbHandler();
        MultipleSelectionModel<String> langsSelectionModel = listview.getSelectionModel();
        langsSelectionModel.selectedItemProperty().addListener(new ChangeListener<String>(){

            public void changed(ObservableValue<? extends String> changed, String oldValue, String newValue){

                vibraniyelement = newValue;
            }
        });

      getpasbtn.setOnAction(event -> {
          if(vibraniyelement.isEmpty()){
              showinfo("Выберите название пароля!");
              return;
          }
String pass = dbHandler.getpass(vibraniyelement);
System.out.println(pass);
          passwordfield.setText(readcode(pass));

      });


        listview.setEditable(true);

        maskirovkaebat.setVisible(false);

        savebtn.setOnAction(event -> {
if(namefield.getText().isEmpty()||loginfield.getText().isEmpty()||passwordfield.getText().isEmpty()){
    showinfo("Не все полня заполнены!");
return;
}
info.setNamef(namefield.getText());
info.setLoginf(loginfield.getText());
info.setPassf(creatkode(passwordfield.getText()));
            CompletableFuture.runAsync(()->dbHandler.addnewpas());


            showinfo("Пароль успешно добавлен!");




        });

        obnovabtn.setOnAction(event -> {
            listview.getItems().clear();

            inf1 = dbHandler.getkey();

            listview.getItems().addAll(inf1);
           inf1 = null;
        });

        showpassword.setOnAction(event -> {
            if(showpassword.isSelected()) {
                maskirovkaebat.setText(passwordfield.getText());
                passwordfield.setVisible(false);
                maskirovkaebat.setVisible(true);
            }
            else {
                maskirovkaebat.setText("");
                maskirovkaebat.setVisible(false);
                passwordfield.setVisible(true);
            }


        });
        deletebtn.setOnAction(event -> {
            if(namefield.getText().isEmpty()){
                showinfo("Поле названия не может быть пустым!");
                return;
            }

            dbHandler.deletepassword(namefield.getText());
            showinfo("Пароль успешно удален!");
        });

        renamebtn.setOnAction(event -> {

            if(namefield.getText().isEmpty()||passwordfield.getText().isEmpty()){
                showinfo("Поле названия или пароля не может быть пустым!");
                return;
            }

            dbHandler.renamepas(namefield.getText(), creatkode(passwordfield.getText()) );
            showinfo("Пароль успешно изменен!");


        });


    }

    public static void showinfo(String text){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("INFO");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();

    }
    public String readcode(String pass){
        String password = "privet";
        if (password == null) {
            throw new IllegalArgumentException("Run with -Dpassword=<password>");
        }


        byte[] salt = new String("57765675").getBytes();


        int iterationCount = 40000;

        int keyLength = 128;
        SecretKeySpec key = null;
        try {
            key = createSecretKey(password.toCharArray(),
                    salt, iterationCount, keyLength);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        String encryptedPassword = null;
        try {

            encryptedPassword = decrypt(pass, key);
        }
        catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
       catch (IOException e){

       }

        return  encryptedPassword;

    }

    public  String creatkode(String pass){
        String password = Passwords.PASSWORD_CODE;
        if (password == null) {
            throw new IllegalArgumentException("Run with -Dpassword=<password>");
        }


        byte[] salt = new String(Passwords.SALT_CODE).getBytes();


        int iterationCount = Passwords.ITER_COUNT;

        int keyLength = 128;
        SecretKeySpec key = null;
        try {
            key = createSecretKey(password.toCharArray(),
                    salt, iterationCount, keyLength);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }



        String encryptedPassword = null;
        try {

            encryptedPassword = encrypt(pass, key);
        }
             catch (GeneralSecurityException e) {
                e.printStackTrace();
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        return  encryptedPassword;

    }

    private static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = null;
        try {
            keyTmp = keyFactory.generateSecret(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }
    private static String encrypt(String property, SecretKeySpec key) throws GeneralSecurityException, UnsupportedEncodingException {
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes("UTF-8"));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static String decrypt(String string, SecretKeySpec key) throws GeneralSecurityException, IOException {
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }

    private static byte[] base64Decode(String property) throws IOException {
        return Base64.getDecoder().decode(property);
    }
}

