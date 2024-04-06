package com.example.pawnder.engine;
import java.util.LinkedList;

public class SimpleAnalysis {
    public static Move bestMove(Board board, int depth){
        LinkedList<Move> moves = board.getAllMoves();
        Move bestmove = new Move(0,0,0,0);
        if(board.toPlay == Board.Color.WHITE){
            double maxEval = -1000;
            for(Move move : moves){
                Board copy = board.copyBoard();
                copy.playMove(move);
                double moveEval = Minimax.alphaBetaPruning(copy, depth, -1000, 1000, Board.Color.BLACK);
                if(moveEval > maxEval){
                    bestmove = move;
                    maxEval = moveEval;
                }
            }
            return bestmove;
        }
        else{
            double minEval = 1000;
            for(Move move : moves){
                Board copy = board.copyBoard();
                copy.playMove(move);
                double moveEval = Minimax.alphaBetaPruning(copy, depth, -1000, 1000, Board.Color.WHITE);
                if(moveEval < minEval){
                    bestmove = move;
                    minEval = moveEval;
                }
            }
            return bestmove;
        }
    }
}
