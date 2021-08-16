package com.company;

public class Entity {

    protected int CEntity;
    protected int LEntity;

    public Entity(){
        CEntity = 0;
        LEntity = 0;
    }

    public void setPosition(int C, int P){
        CEntity = C;
        LEntity = P;
    }

    public void moveEntity(int x, int y){
        CEntity +=x;
        LEntity +=y;
    }

    public int randomValue(int borneInf, int borneSup){ return (int)(Math.random() * ((borneSup - borneInf) + 1)) + borneInf; }

    public int getCEntity(){ return CEntity; }

    public int getLEntity(){ return LEntity; }
}
