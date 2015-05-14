package pop3Client_Prac7;

/**
 * Creators for this Controller.
 * @author Vivian Laura-Lee Venter u13238435
 * @author Jason Richard Evans u13032608
 */

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    public TextField txtUsername;
    public TextField txtPassword;
    public TextField txtServer;
    public Button btnServer;
    public Label lblError;
    public Button btnListEmails;
    public Button btnLogin;
    public Button btnDeleteEmails;
    public Button btnQuit;
    public FlowPane emailPane;
    public VBox vbchecks = new VBox();
    public Label lblErrorDel;


    PopClient client;
    LinkedList<Pop3Server.Email> emailList = new LinkedList<>();
    LinkedList<CheckBox> list = new LinkedList<>();

    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public Controller() {
        System.out.println(ANSI_YELLOW+ "Controller creating Client..." + ANSI_WHITE);
        client = new PopClient();
        System.out.println(ANSI_YELLOW + "Client created"  + ANSI_WHITE);
    }

    public void Login(ActionEvent actionEvent) throws IOException {
        Boolean loggedIn;
        System.out.println(ANSI_YELLOW + "Logging In User" + txtUsername.getText() + " : " + txtPassword.getText() + ANSI_WHITE);
        loggedIn = client.getMyServer().login(txtUsername.getText(), txtPassword.getText());

        if (loggedIn == true) {
            Parent child2 = FXMLLoader.load(getClass().getResource("pop3Page.fxml"));
            Scene scene3 = new Scene(child2, 1000, 400);
            Stage s = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            s.setScene(scene3);
            s.centerOnScreen();
            s.show();
        }
        else {
            txtUsername.setStyle("-fx-border-color: red");
            txtPassword.setStyle("-fx-border-color: red");
            lblError.setText("Username or Password incorrect!\nCould not authorize the user!");
        }
    }


    public void getServer(ActionEvent actionEvent) throws IOException {
        System.out.println(ANSI_YELLOW + "Setting Server" + ANSI_WHITE);
        client.setServerName(txtServer.getText());
        System.out.println(ANSI_YELLOW + "Server " + txtServer.getText() + " has been set..." + ANSI_WHITE);
        connectServer();

        if (actionEvent.getSource() == btnServer) {
            Parent child1 = FXMLLoader.load(getClass().getResource("loginPage.fxml"));
            Scene scene2 = new Scene(child1, 327, 240);
            Stage s = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            s.setScene(scene2);
            s.show();
        }
    }

    public void connectServer() {
        client.connectToServer();
    }


    public void listEmails(ActionEvent actionEvent) throws IOException {
        System.out.println(ANSI_YELLOW + "Request to list emails" + ANSI_WHITE);
        emailList = client.getMyServer().listEmails();

        String sender, subject, date, tempDate = " ";
        int nr;
        Double size;

        vbchecks.setSpacing(10);
        vbchecks.setPadding(new Insets(20.0));

        for(int i=0; i < emailList.size(); i++) {
            CheckBox chb = new CheckBox();
            chb.setId("chb" + i);
            sender = emailList.get(i).getSender();
            subject = emailList.get(i).getSubject();
            nr = emailList.get(i).getEmailNR();
            date = emailList.get(i).getDate();
            size = emailList.get(i).getSize();

            DecimalFormat df = new DecimalFormat("#.##");

            if (subject.length() >= 35) {
                subject = subject.substring(0,30) + "...";
            }
            else {
                int diff = 35-subject.length();

                for (int k = 0; k < diff; k++) {
                    subject += " ";
                }
            }

            if (sender.length() >= 50) {
                sender = sender.substring(0,45) + "...";
                System.out.println(ANSI_YELLOW + "Sender: " + sender + " : " + sender.length() + ANSI_WHITE);
            }
            else {
                int diff = 50-sender.length();

                for (int k = 0; k < diff; k++) {
                    sender += " ";
                }
            }

            tempDate = date.substring(0, (date.length()-5));
            date = tempDate;

            chb.setText(nr + "\t\t" + sender + "\t\t\t" + subject + "\t\t\t" + "" + date + "\t\t\t" + df.format(size) + "KB");

            list.add(chb);
            System.out.println(ANSI_YELLOW + chb.toString() + ANSI_WHITE);
            vbchecks.getChildren().add(chb);
        }

        emailPane.setHgap(20.0);
        emailPane.getChildren().setAll(vbchecks);

    }

    public void DeleteEmails(ActionEvent actionEvent) throws IOException {
        lblErrorDel.setText("");

        LinkedList<Integer> deleteEmails = new LinkedList<>();
        System.out.println(ANSI_YELLOW + "Request to delete emails" + ANSI_WHITE);

        int selected = 0, index;

        for (int k = 0; k < list.size(); k++) {
            if (list.get(k).isSelected()) {
                System.out.println(ANSI_YELLOW + list.get(k).getText() + ANSI_WHITE);

                index = list.get(k).getText().indexOf("\t");
                deleteEmails.add(selected,Integer.parseInt(list.get(k).getText().substring(0, index)));
                list.get(k).setSelected(false);

                selected++;
            }
        }

        if (selected == 0) {
            lblErrorDel.setText("Error, No emails were selected!");
        } else {

            for (int i = 0; i < deleteEmails.size(); i++) {
                client.getMyServer().deleteEmails(deleteEmails.get(i));
            }

            int s = emailList.size();
            vbchecks.getChildren().remove(0, s);
            vbchecks.getChildren().clear();
            emailPane.getChildren().clear();
        }
    }

    public void QUIT(ActionEvent actionEvent) throws IOException {
        client.getMyServer().quit();
        System.out.println(ANSI_YELLOW + "Exiting from application" + ANSI_WHITE);
        System.exit(1);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void onChangeUsername(Event event) {
        lblError.setText("");
        txtUsername.setStyle("-fx-border-color: black");
        txtPassword.setStyle("-fx-border-color: black");
    }

    public void onChangePassword(Event event) {
        lblError.setText("");
        txtUsername.setStyle("-fx-border-color: black");
        txtPassword.setStyle("-fx-border-color: black");
    }

}
