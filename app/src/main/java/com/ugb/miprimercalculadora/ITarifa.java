package com.ugb.miprimercalculadora;

import java.util.ArrayList;

public interface ITarifa {
    public double calcularTotalAPagar(double metrosCubicos);
    public void inicilizarIntervalos();
    public void agregarIntervalos(Intervalo intervalo);
    public void imprimirIntervalos(ArrayList<Intervalo> intervalos);
}

