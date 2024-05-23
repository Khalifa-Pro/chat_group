package com.example.discussion_app.entities;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Membre {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "dateAdhesion")
    private String dateAdhesion;
    @OneToMany(mappedBy = "membreByIdMembre")
    private Collection<Discussion> discussionsById;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDateAdhesion() {
        return dateAdhesion;
    }

    public void setDateAdhesion(String dateAdhesion) {
        this.dateAdhesion = dateAdhesion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Membre membre = (Membre) o;
        return id == membre.id && Objects.equals(username, membre.username) && Objects.equals(dateAdhesion, membre.dateAdhesion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, dateAdhesion);
    }

    public Collection<Discussion> getDiscussionsById() {
        return discussionsById;
    }

    public void setDiscussionsById(Collection<Discussion> discussionsById) {
        this.discussionsById = discussionsById;
    }
}
