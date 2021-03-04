package com.ugb.miprimercalculadora;

public class CalculadoraTest {
    public static void main(String[] args) {
        Tarifa tarifa=new Tarifa();
        tarifa.inicilizarIntervalos();
        tarifa.imprimirIntervalos(tarifa.getIntervalos());
    }
}
