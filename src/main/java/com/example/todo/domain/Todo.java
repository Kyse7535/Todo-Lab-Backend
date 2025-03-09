package com.example.todo.domain;

final public class Todo {

    private String id;
    private String texte;
    private String dateLimite;
    private String auteurId;
    private Boolean isCompleted = false;

    public Todo() {
    }

    private Todo(String id, String texte, String date, String user) {
        this.id = id;
        this.texte = texte;
        this.dateLimite = date;
        this.auteurId = user;
    }

    public static Todo creerTodo(String id, String texte, String dateLimite, String user) {
        return new Todo(id, texte, dateLimite, user);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTexte() {
        return texte;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public String getDateLimite() {
        return dateLimite;
    }

    public void setDateLimite(String dateLimite) {
        this.dateLimite = dateLimite;
    }

    public String getAuteurId() {
        return auteurId;
    }

    public void setAuteurId(String auteurId) {
        this.auteurId = auteurId;
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

}
