package com.example.discussion_app.entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Discussion {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_dsc")
    private int idDsc;
    @Basic
    @Column(name = "message")
    private String message;
    @Basic
    @Column(name = "dateMessage")
    private String dateMessage;

    @ManyToOne
    @JoinColumn(name = "idMembre", referencedColumnName = "id", nullable = false)
    private Membre membreByIdMembre;

    public int getIdDsc() {
        return idDsc;
    }

    public void setIdDsc(int idDsc) {
        this.idDsc = idDsc;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateMessage() {
        return dateMessage;
    }

    public void setDateMessage(String dateMessage) {
        this.dateMessage = dateMessage;
    }
    public Membre getMembreByIdMembre() {
        return membreByIdMembre;
    }

    public void setMembreByIdMembre(Membre membreByIdMembre) {
        this.membreByIdMembre = membreByIdMembre;
    }
}
