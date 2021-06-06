package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.Observable;

public class dbHandler extends  config {
    public Connection getdbConnection() throws ClassNotFoundException, SQLException {
Connection dbconnect;
String connectstring = "jdbc:mysql://"+dbHost+":"+dbPort+"/"+dbName+"?useUnicode=true&serverTimezone=UTC";
Class.forName("com.mysql.cj.jdbc.Driver");
      dbconnect = DriverManager.getConnection(connectstring,dbUser,dbPass);
      return dbconnect;

    }
    public  void addnewpas(){


        try {
            String insert = "insert into "+ Const.ID_NAME+"("+Const.NAME+","+Const.LOGIN+","+Const.PASSWORD+")"+"VALUES(?,?,?)";
            PreparedStatement prst = getdbConnection().prepareStatement(insert);
            prst.setString(1, info.getNamef());
            prst.setString(2, info.getLoginf());
            prst.setString(3, info.getPassf());
            prst.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public ObservableList<String> getkey(){
        ObservableList<String> inf1 = FXCollections.observableArrayList();
        String name;
        String select = "select * from " + Const.ID_NAME;

        try {
            PreparedStatement prst = getdbConnection().prepareStatement(select);
          ResultSet resultSet =  prst.executeQuery();
while (resultSet.next()){
  name  = resultSet.getString("name");
  inf1.add(name);

}
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
return inf1;
    }
    public void deletepassword (String text){


        try {
            String deleete = "delete from " + Const.ID_NAME + " where " + Const.NAME + " =?";
            PreparedStatement prst = getdbConnection().prepareStatement(deleete);
            prst.setString(1, text);
            prst.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public void renamepas(String text, String pass){

        try {
          String update = "update " + Const.ID_NAME + " set " + Const.PASSWORD + " =? " + " where " + Const.NAME + " =?";
          PreparedStatement prst = getdbConnection().prepareStatement(update);
          prst.setString(1, pass);
          prst.setString(2, text);
          prst.executeUpdate();
            System.out.println("ok");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    public String getpass(String name){

        String  passsw = "";

        String select = "select * from " + Const.ID_NAME + " where " + Const.NAME + " =?";

        try {
            PreparedStatement prst = getdbConnection().prepareStatement(select);
            prst.setString(1, name);
            ResultSet resultSet =  prst.executeQuery();
            if (resultSet.next()){
                 passsw  = resultSet.getString(Const.PASSWORD);


            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
      return  passsw;
    }

    }


