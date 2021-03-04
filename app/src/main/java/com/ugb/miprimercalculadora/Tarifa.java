package com.ugb.miprimercalculadora;

import java.util.ArrayList;

public class Tarifa implements ITarifa{
    private double metrosCubicos;
    private ArrayList<Intervalo> intervalos;

    public Tarifa() {
    }

    public Tarifa(double metrosCubicos, ArrayList<Intervalo> intervalos) {
        this.metrosCubicos = metrosCubicos;
        this.intervalos = intervalos;
    }

    public double getMetrosCubicos() {
        return metrosCubicos;
    }

    public void setMetrosCubicos(double metrosCubicos) {
        this.metrosCubicos = metrosCubicos;
    }

    public ArrayList<Intervalo> getIntervalos() {
        return intervalos;
    }

    public void setIntervalos(ArrayList<Intervalo> intervalos) {
        this.intervalos = intervalos;
    }

    @Override
    public double calcularTotalAPagar(double metrosCubicos) {
        double exceso=0;
        double tarifa=0;
        double totalaPagar=0;
        for (int i = this.intervalos.size()-1; i >= 0; i--) {
            Intervalo intervaloActual=this.intervalos.get(i);
            if(i==0){
                totalaPagar=totalaPagar+intervaloActual.getValor();
                break;
            }
            if (metrosCubicos>=intervaloActual.getLimiteInferior()&&metrosCubicos<=intervaloActual.getLimiteSuperior()){
                Intervalo intervaloAnterior =this.intervalos.get(i-1);
                exceso=metrosCubicos-intervaloAnterior.getLimiteSuperior();
                tarifa=exceso*intervaloActual.getValor();
                metrosCubicos=intervaloAnterior.getLimiteSuperior();
                totalaPagar=totalaPagar+tarifa;
            }
        }
        return totalaPagar;
    }

    @Override
    public void inicilizarIntervalos() {
        this.intervalos=new ArrayList<Intervalo>();
        Intervalo intervalo1=new Intervalo(1,18,6);
        Intervalo intervalo2=new Intervalo(19,28,0.45);
        Intervalo intervalo3=new Intervalo();
        intervalo3.setLimiteInferior(29);
        intervalo3.setLimiteSuperior(Double.MAX_VALUE);
        intervalo3.setValor(0.65);
        this.intervalos.add(intervalo1);
        this.intervalos.add(intervalo2);
        this.intervalos.add(intervalo3);
        //Intervalo intervalo4=new Intervalo(101,1000,0.15);
        //this.intervalos.add(intervalo4);

    }

    @Override
    public void agregarIntervalos(Intervalo intervalo) {

    }

    @Override
    public void imprimirIntervalos(ArrayList<Intervalo> intervalos) {
        for (Intervalo intervalo:intervalos ) {
            System.out.println(intervalo.getLimiteInferior());
            System.out.println(intervalo.getLimiteSuperior());
            System.out.println(intervalo.getValor());
        }
    }
}
