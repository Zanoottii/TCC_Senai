package br.senai.tcc.nursecarework.Models;

import android.graphics.Bitmap;

public class Usuario {

    private static Usuario usuario;

    private String email;
    private String uid;
    private Pacientes paciente;
    private Bitmap foto;

    private Usuario() {}

    public static Usuario getInstance() {
        if (usuario == null)
            usuario = new Usuario();
        return usuario;
    }

    public static void clearInstance() {
        usuario = null;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public static void setUsuario(Usuario usuario) {
        Usuario.usuario = usuario;
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

    public Pacientes getPaciente() {
        return paciente;
    }

    public void setPaciente(Pacientes paciente) {
        this.paciente = paciente;
    }

    public Bitmap getFoto() {
        return foto;
    }

    public void setFoto(Bitmap foto) {
        this.foto = foto;
    }
}
