package br.senai.tcc.nursecarework.models;

import android.graphics.Bitmap;

public class Usuario {

    private static Usuario usuario;

    private String email;
    private String uid;
    private Enfermeiro enfermeiro;
    private Cooperativa cooperativa;
    private Bitmap foto;

    private Usuario() {
    }

    public static Usuario getInstance() {
        if (usuario == null)
            usuario = new Usuario();
        return usuario;
    }

    public static void clearInstance() {
        usuario = null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Enfermeiro getEnfermeiro() {
        return enfermeiro;
    }

    public void setEnfermeiro(Enfermeiro enfermeiro) {
        this.enfermeiro = enfermeiro;
    }

    public Cooperativa getCooperativa() {
        return cooperativa;
    }

    public void setCooperativa(Cooperativa cooperativa) {
        this.cooperativa = cooperativa;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }
}
