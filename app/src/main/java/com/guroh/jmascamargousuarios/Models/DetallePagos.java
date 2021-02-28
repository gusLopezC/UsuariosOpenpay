package com.guroh.jmascamargousuarios.Models;

public class DetallePagos {
    private String caja, referencia, fecha, monto;

    public DetallePagos(String caja, String referencia, String fecha, String monto) {
        this.caja = caja;
        this.referencia = referencia;
        this.fecha = fecha;
        this.monto = monto;
    }

    public String getCaja() {
        return caja;
    }

    public void setCaja(String caja) {
        this.caja = caja;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }
}
