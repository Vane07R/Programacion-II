package com.ugb.miprimercalculadora;

public class Intervalo {
    private double limiteInferior;
    private double limiteSuperior;
    private double valor;
    private double exceso;

    public Intervalo(){

    }

    public Intervalo(double limiteInferior, double limiteSuperior, double valor) {
        this.limiteInferior = limiteInferior;
        this.limiteSuperior = limiteSuperior;
        this.valor = valor;
    }

    public double getLimiteInferior() {
        return limiteInferior;
    }

    public double getLimiteSuperior() {
        return limiteSuperior;
    }

    public double getValor() {
        return valor;
    }

    public double getExceso() {
        return exceso;
    }

    public void setLimiteInferior(double limiteInferior) {
        this.limiteInferior = limiteInferior;
    }

    public void setLimiteSuperior(double limiteSuperior) {
        this.limiteSuperior = limiteSuperior;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setExceso(double exceso) {
        this.exceso = exceso;
    }
}
