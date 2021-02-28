package com.guroh.jmascamargousuarios.Models;

public class DetalleCuentas {
    private String idusuario, cuenta, nombre, direccion, alias, saldo;

    public DetalleCuentas(String idusuario, String cuenta, String nombre, String direccion, String alias, String saldo) {
        this.idusuario = idusuario;
        this.cuenta = cuenta;
        this.nombre = nombre;
        this.direccion = direccion;
        this.alias = alias;
        this.saldo = saldo;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }
}