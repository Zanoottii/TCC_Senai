package br.senai.tcc.nursecarework;

public class OpcoesServicos {
    private String id;
    private String descricao;
    private boolean check;

    public OpcoesServicos(String id, String descricao) {
        this.id = id;
        this.descricao = descricao;
        this.check = false;
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isChecked() {
        return check;
    }

    public void change() {
        this.check = !this.check;
    }
}
