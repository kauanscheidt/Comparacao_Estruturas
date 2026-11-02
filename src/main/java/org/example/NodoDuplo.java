package org.example;

public class NodoDuplo {

    private int dado;
    private  NodoDuplo prox;
    private NodoDuplo ant;

    public NodoDuplo(int dado) {
        this.dado = dado;
        this.prox = null;
        this.ant = null;
    }

    public int getDado() {
        return dado;
    }

    public void setDado(int dado) {
        this.dado = dado;
    }

    public NodoDuplo getProx() {
        return prox;
    }

    public void setProx(NodoDuplo prox) {
        this.prox = prox;
    }

    public NodoDuplo getAnt() {
        return ant;
    }

    public void setAnt(NodoDuplo ant) {
        this.ant = ant;
    }

}