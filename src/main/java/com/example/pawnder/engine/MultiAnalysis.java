package com.example.pawnder.engine;
import java.util.LinkedList;
import java.util.concurrent.*;

public class MultiAnalysis {
    public static MoveEval bestMove(Board board, int depth) {
        ExecutorService executor = Executors.newFixedThreadPool(4);
        LinkedList<Move> moves = board.getAllMoves();
        LinkedList<Future<MoveEval>> futures = new LinkedList<>();
        for (Move move : moves) {
            Future<MoveEval> future = executor.submit(
                    () -> {
                        Board copy = board.copyBoard();
                        copy.playMove(move);
                        double moveEval = Minimax.alphaBetaPruning(copy, depth, -1000, 1000, copy.toPlay);
                        return new MoveEval(move, moveEval);
                    });
            futures.add(future);
        }
        if(board.toPlay == Board.Color.WHITE){
            double maxEval = -1000;
            Move bestMove = new Move(0,0,0,0);
            for(Future<MoveEval> moveEvalFuture : futures){
                try{
                    MoveEval moveEval = moveEvalFuture.get();
                    if(moveEval.getEval() > maxEval){
                        maxEval = moveEval.getEval();
                        bestMove = moveEval.getMove();
                    }
                }
                catch(Exception e){
                    System.out.println("Exception 1-1");
                }
            }
            executor.shutdown();
            return new MoveEval(bestMove, maxEval);
        }
        else{
            double minEval = 1000;
            Move bestMove = new Move(0,0,0,0);
            for(Future<MoveEval> moveEvalFuture : futures){
                try{
                    MoveEval moveEval = moveEvalFuture.get();
                    if(moveEval.getEval() < minEval){
                        minEval = moveEval.getEval();
                        bestMove = moveEval.getMove();
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            executor.shutdown();
            return new MoveEval(bestMove, minEval);
        }
    }
}
