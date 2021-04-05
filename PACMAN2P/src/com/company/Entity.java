package com.company;

public class Entity {

    protected int Centity;
    protected int Lentity;

    public Entity(){
        Centity = 0;
        Lentity = 0;
    }

    public void setPosition(int C, int P){
        Centity = C;
        Lentity = P;
    }

    public void moveEntity(int x, int y){
        Centity+=x;
        Lentity+=y;
    }

    public int randomValue(int borneInf, int borneSup){ return (int)(Math.random() * ((borneSup - borneInf) + 1)) + borneInf; }

    public int getCentity(){ return Centity; }

    public int getLentity(){ return Lentity; }
}
