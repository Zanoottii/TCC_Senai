package br.senai.tcc.nursecarework;

public class InfoClientes {
    private String nome, sobrenome, tipoServico, dataNasc;

    public String getNomeCliente() {
        return nome;
    }

    public String getSobrenomeCliente() {
        return sobrenome;
    }

    public String getTipoServicoCliente() {
        return tipoServico;
    }

    public String getDataNascCliente() {
        return dataNasc;
    }

    public void setNomeCliente(String nome) {
        this.nome = nome;
    }

    public void setSobrenomeCliente(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public void setTipoServicoCliente(String tipoServico) {
        this.tipoServico = tipoServico;
    }

    public void setDataNascCliente(String dataNasc) {
        this.dataNasc = dataNasc;
    }
}
