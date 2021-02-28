package com.guroh.jmascamargousuarios.Models;

public class DetalleRecibo {
    private String conceptoPago, descripcionConcepto, montoPago;

    public DetalleRecibo(String conceptoPago, String descripcionConcepto, String montoPago) {
        this.conceptoPago = conceptoPago;
        this.descripcionConcepto = descripcionConcepto;
        this.montoPago = montoPago;
    }

    public String getConceptoPago() {
        return conceptoPago;
    }

    public void setConceptoPago(String conceptoPago) {
        this.conceptoPago = conceptoPago;
    }

    public String getDescripcionConcepto() {
        return descripcionConcepto;
    }

    public void setDescripcionConcepto(String descripcionConcepto) {
        this.descripcionConcepto = descripcionConcepto;
    }

    public String getMontoPago() {
        return montoPago;
    }

    public void setMontoPago(String montoPago) {
        this.montoPago = montoPago;
    }
}
