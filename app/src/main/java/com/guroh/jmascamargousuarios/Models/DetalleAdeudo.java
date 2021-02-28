package com.guroh.jmascamargousuarios.Models;

public class DetalleAdeudo {
    private String conceptoAdeudo, descripcionConcepto, montoAdeudo, ivaAdeudo, subtotalAdeudo, totalAdeudo;

    public DetalleAdeudo(String conceptoAdeudo, String descripcionConcepto, String montoAdeudo, String ivaAdeudo, String subtotalAdeudo, String totalAdeudo) {
        this.conceptoAdeudo = conceptoAdeudo;
        this.descripcionConcepto = descripcionConcepto;
        this.montoAdeudo = montoAdeudo;
        this.ivaAdeudo = ivaAdeudo;
        this.subtotalAdeudo = subtotalAdeudo;
        this.totalAdeudo = totalAdeudo;
    }

    public String getConceptoAdeudo() {
        return conceptoAdeudo;
    }

    public void setConceptoAdeudo(String conceptoAdeudo) {
        this.conceptoAdeudo = conceptoAdeudo;
    }

    public String getDescripcionConcepto() {
        return descripcionConcepto;
    }

    public void setDescripcionConcepto(String descripcionConcepto) {
        this.descripcionConcepto = descripcionConcepto;
    }

    public String getMontoAdeudo() {
        return montoAdeudo;
    }

    public void setMontoAdeudo(String montoAdeudo) {
        this.montoAdeudo = montoAdeudo;
    }

    public String getIvaAdeudo() {
        return ivaAdeudo;
    }

    public void setIvaAdeudo(String ivaAdeudo) {
        this.ivaAdeudo = ivaAdeudo;
    }

    public String getSubtotalAdeudo() {
        return subtotalAdeudo;
    }

    public void setSubtotalAdeudo(String subtotalAdeudo) {
        this.subtotalAdeudo = subtotalAdeudo;
    }

    public String getTotalAdeudo() {
        return totalAdeudo;
    }

    public void setTotalAdeudo(String totalAdeudo) {
        this.totalAdeudo = totalAdeudo;
    }
}
