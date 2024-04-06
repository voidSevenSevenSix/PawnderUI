package com.example.pawnder;

import com.example.pawnder.engine.Board;
import com.example.pawnder.engine.Move;
import com.example.pawnder.engine.MultiAnalysis;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class PawnderUIController {
    @FXML
    private GridPane board;
    @FXML
    private Text status;
    private Board internalBoard;

    private ArrayList<ImageView> boardSquares = new ArrayList<>();
    private ArrayList<Rectangle> rects = new ArrayList<>();

    private final Image whiteRook = new Image(PawnderApp.class.getResource("wr.png").toString());
    private final Image whiteKnight = new Image(PawnderApp.class.getResource("wn.png").toString());
    private final Image whiteBishop = new Image(PawnderApp.class.getResource("wb.png").toString());
    private final Image whiteQueen = new Image(PawnderApp.class.getResource("wq.png").toString());
    private final Image whiteKing = new Image(PawnderApp.class.getResource("wk.png").toString());
    private final Image whitePawn = new Image(PawnderApp.class.getResource("wp.png").toString());
    private final Image blackRook = new Image(PawnderApp.class.getResource("br.png").toString());
    private final Image blackKnight = new Image(PawnderApp.class.getResource("bn.png").toString());
    private final Image blackBishop = new Image(PawnderApp.class.getResource("bb.png").toString());
    private final Image blackQueen = new Image(PawnderApp.class.getResource("bq.png").toString());
    private final Image blackKing = new Image(PawnderApp.class.getResource("bk.png").toString());
    private final Image blackPawn = new Image(PawnderApp.class.getResource("bp.png").toString());

    public void initialize(){
        ArrayList<Object> list = new ArrayList<Object>(board.getChildren());
        int p = 0;
        for(Object n : list){
            if(n.getClass() == ImageView.class){
                ImageView iv = (ImageView)n;
                boardSquares.add(iv);
                final int m = p;
                iv.setOnMouseClicked((MouseEvent e)->{handleSquareClick(m);});
                p++;
            }
            if(n.getClass() == Rectangle.class){
                rects.add((Rectangle)n);
            }
        }
    }

    public void renderBoard(Board b){
        internalBoard = b;
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                int boardSquareIndex = 56-i*8+j;
                switch(b.board[i][j]){
                    case 'R':
                        boardSquares.get(boardSquareIndex).setImage(whiteRook);
                        break;
                    case 'N':
                        boardSquares.get(boardSquareIndex).setImage(whiteKnight);
                        break;
                    case 'B':
                        boardSquares.get(boardSquareIndex).setImage(whiteBishop);
                        break;
                    case 'Q':
                        boardSquares.get(boardSquareIndex).setImage(whiteQueen);
                        break;
                    case 'K':
                        boardSquares.get(boardSquareIndex).setImage(whiteKing);
                        break;
                    case 'P':
                        boardSquares.get(boardSquareIndex).setImage(whitePawn);
                        break;
                    case 'r':
                        boardSquares.get(boardSquareIndex).setImage(blackRook);
                        break;
                    case 'n':
                        boardSquares.get(boardSquareIndex).setImage(blackKnight);
                        break;
                    case 'b':
                        boardSquares.get(boardSquareIndex).setImage(blackBishop);
                        break;
                    case 'q':
                        boardSquares.get(boardSquareIndex).setImage(blackQueen);
                        break;
                    case 'k':
                        boardSquares.get(boardSquareIndex).setImage(blackKing);
                        break;
                    case 'p':
                        boardSquares.get(boardSquareIndex).setImage(blackPawn);
                        break;
                    default:
                        boardSquares.get(boardSquareIndex).setImage(null);
                        break;
                }
            }
        }
    }

    private Optional<Integer> prevClicked = Optional.empty();
    private int cLastOne = 0;
    private int cLastTwo = 0;
    private Paint cLastPOne;
    private Paint cLastPTwo;
    private Paint hLastPOne;

    public void handleSquareClick(int id){
        if(prevClicked.isEmpty()){
            prevClicked = Optional.of(id);
            hLastPOne = rects.get(id).getFill();
            rects.get(id).setFill(Color.rgb(100, 255, 100));
        }
        else{
            if(hLastPOne != null){
                rects.get(prevClicked.get()).setFill(hLastPOne);
            }
            if(id == prevClicked.get()){
                prevClicked = Optional.empty();
            }
            else{
                if(cLastPOne != null){
                    rects.get(cLastOne).setFill(cLastPOne);
                    rects.get(cLastTwo).setFill(cLastPTwo);
                }
                internalBoard.playMove(new Move(7-(int)(prevClicked.get()/8), prevClicked.get()%8, 7-(int)(id/8), id%8));
                prevClicked = Optional.empty();
                renderBoard(internalBoard);
                status.setText("Waiting for computer...");
                if(internalBoard.getAllMoves().isEmpty()){
                    status.setText("You win!");
                    return;
                }
                CompletableFuture.supplyAsync(() -> {
                    return MultiAnalysis.bestMove(internalBoard.copyBoard(), 3).getMove();
                }).thenAcceptAsync(bestMove -> {
                    Platform.runLater(() -> {
                        internalBoard.playMove(bestMove);
                        cLastOne = 56-bestMove.fromSquare.rank*8+bestMove.fromSquare.file;
                        cLastTwo = 56-bestMove.toSquare.rank*8+bestMove.toSquare.file;
                        cLastPOne = rects.get(cLastOne).getFill();
                        cLastPTwo = rects.get(cLastTwo).getFill();
                        rects.get(cLastOne).setFill(Color.rgb(50, 150, 50));
                        rects.get(cLastTwo).setFill(Color.rgb(100, 255, 100));
                        renderBoard(internalBoard);
                        if(internalBoard.getAllMoves().isEmpty()){
                            status.setText("You lose.");
                        }
                        else{
                            status.setText("Your turn.");
                        }
                    });
                });
            }
        }
    }
}