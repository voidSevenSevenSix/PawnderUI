package com.example.pawnder.engine;
import java.util.LinkedList;

public class Minimax {
    public static double alphaBetaPruning(Board board, int depth, double alpha, double beta, Board.Color maximizingPlayer){
        if(depth == 0){
            return board.evaluation();
        }
        LinkedList<Move> moves = board.getAllMoves();   
        if(maximizingPlayer == Board.Color.WHITE){
            double maxEval = -200;
            for(Move move : moves){
                board.playMove(move);
                if(board.isLegal()){
                    Board copy = board.copyBoard();
                    double eval = alphaBetaPruning(copy, depth - 1, alpha, beta, Board.Color.BLACK);
                    board.unplayMove(); 
                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);
                    if (beta <= alpha)
                        break;
                }
                else{
                    board.unplayMove();
                }
            }
            return maxEval;
        }
        else{
            double minEval = 200;
            for(Move move : moves){
                board.playMove(move);
                if(board.isLegal()){
                    Board copy = board.copyBoard();
                    double eval = alphaBetaPruning(copy, depth - 1, alpha, beta, Board.Color.WHITE);
                    board.unplayMove(); 
                    minEval = Math.min(minEval, eval);
                    alpha = Math.min(alpha, eval);
                    if (beta <= alpha)
                        break;
                }
                else{
                    board.unplayMove();
                }
            }
            return minEval;
        }
    }
}
