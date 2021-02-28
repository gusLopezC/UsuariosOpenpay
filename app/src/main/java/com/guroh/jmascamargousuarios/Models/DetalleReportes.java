package com.guroh.jmascamargousuarios.Models;

public class DetalleReportes {
    private String idReporte, detalleReporte, fechaReporte, respuestaReporte, fechaRespuestaReporte, estatusReporte;

    public DetalleReportes(String idReporte, String detalleReporte, String fechaReporte, String respuestaReporte, String fechaRespuestaReporte, String estatusReporte) {
        this.idReporte = idReporte;
        this.detalleReporte = detalleReporte;
        this.fechaReporte = fechaReporte;
        this.respuestaReporte = respuestaReporte;
        this.fechaRespuestaReporte = fechaRespuestaReporte;
        this.estatusReporte = estatusReporte;
    }

    public String getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(String idReporte) {
        this.idReporte = idReporte;
    }

    public String getDetalleReporte() {
        return detalleReporte;
    }

    public void setDetalleReporte(String detalleReporte) {
        this.detalleReporte = detalleReporte;
    }

    public String getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(String fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public String getRespuestaReporte() {
        return respuestaReporte;
    }

    public void setRespuestaReporte(String respuestaReporte) {
        this.respuestaReporte = respuestaReporte;
    }

    public String getFechaRespuestaReporte() {
        return fechaRespuestaReporte;
    }

    public void setFechaRespuestaReporte(String fechaRespuestaReporte) {
        this.fechaRespuestaReporte = fechaRespuestaReporte;
    }

    public String getEstatusReporte() {
        return estatusReporte;
    }

    public void setEstatusReporte(String estatusReporte) {
        this.estatusReporte = estatusReporte;
    }
}
