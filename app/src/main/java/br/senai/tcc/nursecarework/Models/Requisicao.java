package br.senai.tcc.nursecarework.Models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.List;

@IgnoreExtraProperties
public class Requisicao implements Serializable {

    private long datahora;
    private String id, cooperativa, enfermeiro, paciente, endereco, latitude, longitude;
    private List<String> servico;
    private int estado;

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public int getEstado() {
        return estado;
    }

    @Exclude
    public void setEstado(int estado) {
        this.estado = estado;
    }

    public long getDatahora() {
        return datahora;
    }

    public void setDatahora(long datahora) {
        this.datahora = datahora;
    }

    public String getCooperativa() {
        return cooperativa;
    }

    public void setCooperativa(String cooperativa) {
        this.cooperativa = cooperativa;
    }

    public String getEnfermeiro() {
        return enfermeiro;
    }

    public void setEnfermeiro(String enfermeiro) {
        this.enfermeiro = enfermeiro;
    }

    public String getPaciente() {
        return paciente;
    }

    public void setPaciente(String paciente) {
        this.paciente = paciente;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public List<String> getServico() {
        return servico;
    }

    public void setServico(List<String> servico) {
        this.servico = servico;
    }
}
