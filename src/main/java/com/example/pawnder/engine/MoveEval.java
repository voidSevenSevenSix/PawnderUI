package com.example.pawnder.engine;
public class MoveEval {
    private Move move;
    private double eval;

    public MoveEval(Move m, double e){
        move = m;
        eval = e;
    }

    public Move getMove(){
        return move;
    }

    public double getEval(){
        return eval;
    }

}
