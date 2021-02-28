package com.guroh.jmascamargousuarios.Models;

public class DetalleLecturas {
    private String periodo, lanterior, lactual, consumo;

    public DetalleLecturas(String periodo, String lanterior, String lactual, String consumo) {
        this.periodo = periodo;
        this.lanterior = lanterior;
        this.lactual = lactual;
        this.consumo = consumo;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public String getLanterior() {
        return lanterior;
    }

    public void setLanterior(String lanterior) {
        this.lanterior = lanterior;
    }

    public String getLactual() {
        return lactual;
    }

    public void setLactual(String lactual) {
        this.lactual = lactual;
    }

    public String getConsumo() {
        return consumo;
    }

    public void setConsumo(String consumo) {
        this.consumo = consumo;
    }
}
