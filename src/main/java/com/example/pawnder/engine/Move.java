package com.example.pawnder.engine;
public class Move {
    public Square fromSquare;
    public Square toSquare;

    public Move(Square fromSquare, Square toSquare){
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
    }

    public Move(int a, int b, int c, int d){
        this.fromSquare = new Square(a, b);
        this.toSquare = new Square(c, d);
    }

    @Override
    public String toString(){
        return "[" + fromSquare + "->" + toSquare + "]";
    }
}
