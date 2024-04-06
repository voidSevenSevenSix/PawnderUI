package com.example.pawnder;

import com.example.pawnder.engine.Board;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PawnderApp extends Application {
    private static final FXMLLoader gameViewLoader = new FXMLLoader(PawnderApp.class.getResource("chess.fxml"));
    private static Scene gameViewScene;
    private static Stage stage;

    @Override
    public void start(Stage s) throws IOException {
        stage = s;
        gameViewScene = new Scene(gameViewLoader.load(), 800, 800);
        stage.setTitle("PawnderUI");
        showGameView();
    }

    public void showGameView(){
        stage.setScene(gameViewScene);
        stage.show();
        PawnderUIController p = gameViewLoader.getController();
        p.renderBoard(new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
    }

    public static void main(String[] args) {
        launch();
    }
}