package com.example.pawnder.engine;
public class Pawnder {
    public static String analyze(String fen, int depth){
        if(Book.checkBook(fen).isPresent()){
            return Book.checkBook(fen).get() + "(0.0)";
        }
        MoveEval me = MultiAnalysis.bestMove(new Board(fen), depth);
        return Utils.moveToString(me.getMove()) + " (" + me.getEval() + ")";
    }
}
