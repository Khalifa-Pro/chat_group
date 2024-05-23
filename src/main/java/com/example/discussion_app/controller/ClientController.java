package com.example.discussion_app.controller;

import com.example.discussion_app.entities.Discussion;
import com.example.discussion_app.entities.Membre;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientController {

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("default");
    EntityManager em = emf.createEntityManager();
    EntityTransaction trans = em.getTransaction();

    @FXML
    private TextArea txtDiscussion;

    @FXML
    private TextArea txtMessage;

    @FXML
    private TextField usernameTxt;

    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private boolean clientIdentified = false;

    public String automaticDate(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy : hh'h' mm'min' ss's' ");
        String formatDate = sdf.format(date);
        return formatDate;
    }

    @FXML
    public void initialize() {
        txtMessage.setDisable(true);
        try {
            socket = new Socket("localhost", 1234);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.identifier();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.listenForMessage();
    }

    int idMembre;

    @FXML
    void identifier() {
        String username = usernameTxt.getText().trim();
        if (!username.isEmpty()) {
            this.username = username;
            clientIdentified = true;
            Membre membre = new Membre();
            membre.setUsername(username);
            membre.setDateAdhesion(automaticDate());
            try {
                trans.begin();
                em.persist(membre);
                trans.commit();
                idMembre = membre.getId();
            }catch (Exception e){
                if (trans.isActive()){
                    trans.rollback();
                }
                e.printStackTrace();
            }
            showAlert("Nouveau client", "Vous avez été identifié!");
            usernameTxt.setDisable(true);
            txtMessage.setDisable(false);
        } else {
            showAlert("Erreur", "Veuillez saisir un nom d'utilisateur.");

        }
    }

    /*
    @FXML
    void send() {
         txtMessage.setDisable(false);
         if (!clientIdentified) {
            showAlert("Erreur", "Vous devez d'abord vous identifier.");
            return;
        }
         //trans.begin();
         try {
             trans.begin();
             Membre membre = em.find(Membre.class,idMembre);
             Discussion discussion = new Discussion();
             discussion.setMembreByIdMembre(membre);
             discussion.setMessage(txtMessage.getText());
             discussion.setDateMessage(automaticDate());
             em.persist(discussion);
             trans.commit();
         }catch (Exception e){
             if (trans.isActive()){
                 trans.rollback();
                 e.printStackTrace();
             }
         }
        listenForMessage();
        sendMessage(txtMessage.getText());
        txtMessage.clear();
    }
     */

    @FXML
    void send() {
        txtMessage.setDisable(false);
        if (!clientIdentified) {
            showAlert("Erreur", "Vous devez d'abord vous identifier.");
            return;
        }

        try {
            trans.begin();
            Membre membre = em.find(Membre.class,idMembre);
            Discussion discussion = new Discussion();
            discussion.setMembreByIdMembre(membre);
            discussion.setMessage(txtMessage.getText());
            discussion.setDateMessage(automaticDate());
            em.persist(discussion);
            trans.commit();
            // Afficher le message dans la zone de discussion
            displayMessage(username + " : " + txtMessage.getText());
            sendMessage(txtMessage.getText());
        } catch (Exception e) {
            if (trans.isActive()) {
                trans.rollback();
                e.printStackTrace();
            }
        }

        listenForMessage();
        txtMessage.clear();
    }

    // Méthode pour afficher un message dans la zone de discussion
    public void displayMessage(String message) {
        Platform.runLater(() -> txtDiscussion.appendText(message + "\n"));
    }

    public void sendMessage(String message) {
        try {

            String messageToSend = username + " : " + message;
            bufferedWriter.write(messageToSend);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listenForMessage() {
        new Thread(() -> {
            try {
                String msgFromGroupChat;
                while ((msgFromGroupChat = bufferedReader.readLine()) != null) {
                    final String message = msgFromGroupChat;
                    Platform.runLater(() -> txtDiscussion.appendText(message + "\n"));
                }
            } catch (IOException e) {
                closeEverything(socket,bufferedReader,bufferedWriter);
                e.printStackTrace();
            }
        }).start();
    }

    public void showAlert(String title, String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
