package com.example.pawnder.engine;
public class Square {
    public int rank;
    public int file;

    public Square(int rank, int file){
        this.rank = rank;
        this.file = file;
    }

    @Override
    public String toString(){
        return "[" + rank + "," + file + "]";
    }
}
