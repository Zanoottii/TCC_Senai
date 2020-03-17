package br.senai.tcc.nursecarework.Models;

public class InfoPacientes {
    private String nome, sobrenome, tipoServico, dataNasc;

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public String getTipoServico() {
        return tipoServico;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public void setTipoServico(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    public void setDataNasc(String dataNasc) {
        this.dataNasc = dataNasc;
    }
}
