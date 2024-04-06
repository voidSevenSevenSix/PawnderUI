package com.example.pawnder.engine;

import java.util.LinkedList;

public class Board {
    public char[][] board = new char[8][8];
    public char[][] prevBoard = new char[8][8];
    public boolean havePrevBoard = false;

    public enum Color {
        WHITE,
        BLACK,
        EMPTY
    }

    public Color toPlay;
    public boolean whiteCastlesLong = false;
    public boolean whiteCastlesShort = false;
    public boolean blackCastlesLong = false;
    public boolean blackCastlesShort = false;

    public Board copyBoard(){
        Board b = new Board(board);
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                b.prevBoard[i][j] = prevBoard[i][j];
            }
        }
        b.havePrevBoard = havePrevBoard;
        b.toPlay = toPlay;
        b.whiteCastlesLong = whiteCastlesLong;
        b.whiteCastlesShort = whiteCastlesShort;
        b.blackCastlesLong = blackCastlesLong;
        b.blackCastlesShort = blackCastlesShort;
        return b;
    }

    public Board(String fen) {
        int rank = 7;
        int file = 0;
        int section = 0;
        for (int i = 0; i < fen.length(); i++) {
            char c = fen.charAt(i);
            if (c == ' ') {
                section++;
            } else {
                if (section == 0) {
                    if (c == '/') {
                        rank--;
                        file = 0;
                    } else if (Character.isDigit(c)) {
                        int emptySpaces = c - '0';
                        for (int j = 0; j < emptySpaces; j++) {
                            board[rank][j + file] = '-';
                        }
                        file += emptySpaces;
                    } else {
                        board[rank][file] = c;
                        file++;
                    }
                } else if (section == 1) {
                    if (c == 'w') {
                        toPlay = Color.WHITE;
                    } else {
                        toPlay = Color.BLACK;
                    }
                } else if (section == 2) {
                    switch (c) {
                        case 'K':
                            whiteCastlesShort = true;
                            break;
                        case 'Q':
                            whiteCastlesLong = true;
                            break;
                        case 'k':
                            blackCastlesShort = true;
                            break;
                        case 'q':
                            blackCastlesLong = true;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    public Board(char[][] b) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = b[i][j];
            }
        }
    }

    @Override
    public String toString() {
        String output = "";
        output += "To play: " + toPlay + "\n";
        output += "Castling (KQkq): " + whiteCastlesShort + whiteCastlesLong + blackCastlesShort + blackCastlesLong
                + "\n";
        output += "Board: \n";
        for (int i = 7; i > -1; i--) {
            for (int j = 0; j < 8; j++) {
                output += board[i][j];
            }
            output += "\n";
        }
        return output;
    }

    public Color colorOf(char p) {
        if (p == '-') {
            return Color.EMPTY;
        } else if (p == 'R' || p == 'B' || p == 'N' || p == 'Q' || p == 'K' || p == 'P') {
            return Color.WHITE;
        }
        return Color.BLACK;
    }

    public void playMove(Move move) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                prevBoard[i][j] = board[i][j];
            }
        }
        board[move.toSquare.rank][move.toSquare.file] = board[move.fromSquare.rank][move.fromSquare.file];
        board[move.fromSquare.rank][move.fromSquare.file] = '-';
        if(toPlay == Color.WHITE && move.toSquare.rank == 7 && board[move.toSquare.rank][move.toSquare.file] == 'P'){
            board[move.toSquare.rank][move.toSquare.file] = 'Q';
        }
        if(toPlay == Color.BLACK && move.toSquare.rank == 0 && board[move.toSquare.rank][move.toSquare.file] == 'p'){
            board[move.toSquare.rank][move.toSquare.file] = 'q';
        }
        if(toPlay == Color.WHITE && move.toSquare.rank == 0 && move.toSquare.file == 6 && move.fromSquare.rank == 0 && move.fromSquare.file == 4 && board[0][6] == 'K' && board[0][7] == 'R' && whiteCastlesShort){
            board[0][7] = '-';
            board[0][5] = 'R';
            whiteCastlesShort = false;
        }
        if(toPlay == Color.WHITE && move.toSquare.rank == 0 && move.toSquare.file == 2 && move.fromSquare.rank == 0 && move.fromSquare.file == 4 && board[0][2] == 'K' && board[0][0] == 'R' && whiteCastlesLong){
            board[0][0] = '-';
            board[0][3] = 'R';
            whiteCastlesLong = false;
        }
        if(toPlay == Color.BLACK && move.toSquare.rank == 7 && move.toSquare.file == 6 && move.fromSquare.rank == 7 && move.fromSquare.file == 4 && board[7][6] == 'k' && board[7][7] == 'r' && blackCastlesShort){
            board[7][7] = '-';
            board[7][5] = 'r';
            blackCastlesShort = false;
        }
        if(toPlay == Color.BLACK && move.toSquare.rank == 7 && move.toSquare.file == 2 && move.fromSquare.rank == 7 && move.fromSquare.file == 4 && board[7][2] == 'K' && board[7][0] == 'r' && blackCastlesLong){
            board[7][0] = '-';
            board[7][3] = 'r';
            blackCastlesLong = false;
        }
        if(board[move.toSquare.rank][move.toSquare.file] == 'K'){
            whiteCastlesLong = false;
            whiteCastlesShort = false;
        }
        if(board[move.toSquare.rank][move.toSquare.file] == 'k'){
            blackCastlesLong = false;
            blackCastlesShort = false;
        }
        if(board[0][0] != 'R'){
            whiteCastlesLong = false;
        }
        if(board[0][7] != 'R'){
            whiteCastlesShort = false;
        }
        if(board[7][0] != 'r'){
            blackCastlesLong = false;
        }if(board[7][7] != 'R'){
            blackCastlesLong = false;
        }


        toPlay = toPlay == Color.WHITE ? Color.BLACK : Color.WHITE;
        havePrevBoard = true;
    }

    public void unplayMove() {
        if (!havePrevBoard)
            return;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                board[i][j] = prevBoard[i][j];
            }
        }
        toPlay = toPlay == Color.WHITE ? Color.BLACK : Color.WHITE;
        havePrevBoard = false;
    }

    public LinkedList<Move> pawnMoves(Square square) {
        LinkedList<Move> moves = new LinkedList<>();
        int rank = square.rank;
        int file = square.file;
        if (colorOf(board[rank][file]) == Color.WHITE) {
            if (colorOf(board[rank + 1][file]) == Color.EMPTY) {
                moves.add(new Move(new Square(rank, file), new Square(rank + 1, file)));
                if (rank == 1 && colorOf(board[rank + 2][file]) == Color.EMPTY) {
                    moves.add(new Move(new Square(rank, file), new Square(rank + 2, file)));
                }
            }
            if (file > 0) {
                if (colorOf(board[rank + 1][file - 1]) == Color.BLACK) {
                    moves.add(new Move(new Square(rank, file), new Square(rank + 1, file - 1)));
                }
            }
            if (file < 7) {
                if (colorOf(board[rank + 1][file + 1]) == Color.BLACK) {
                    moves.add(new Move(new Square(rank, file), new Square(rank + 1, file + 1)));
                }
            }
        } else {
            if (colorOf(board[rank - 1][file]) == Color.EMPTY) {
                moves.add(new Move(new Square(rank, file), new Square(rank - 1, file)));
                if (rank == 6 && colorOf(board[rank - 2][file]) == Color.EMPTY) {
                    moves.add(new Move(new Square(rank, file), new Square(rank - 2, file)));
                }
            }
            if (file > 0) {
                if (colorOf(board[rank - 1][file - 1]) == Color.WHITE) {
                    moves.add(new Move(new Square(rank, file), new Square(rank - 1, file - 1)));
                }
            }
            if (file < 7) {
                if (colorOf(board[rank - 1][file + 1]) == Color.WHITE) {
                    moves.add(new Move(new Square(rank, file), new Square(rank - 1, file + 1)));
                }
            }
        }
        return moves;
    }

    public LinkedList<Move> knightMoves(Square square) {
        LinkedList<Move> moves = new LinkedList<>();
        int rank = square.rank;
        int file = square.file;
        Color color = colorOf(board[rank][file]);
        if (rank + 1 < 8) {
            if (file + 2 < 8) {
                if (colorOf(board[rank + 1][file + 2]) != color) {
                    moves.add(new Move(new Square(rank, file), new Square(rank + 1, file + 2)));
                }
            }
            if (file - 2 > -1) {
                if (colorOf(board[rank + 1][file - 2]) != color) {
                    moves.add(new Move(new Square(rank, file), new Square(rank + 1, file - 2)));
                }
            }
        }
        if (rank - 1 > -1) {
            if (file + 2 < 8) {
                if (colorOf(board[rank - 1][file + 2]) != color) {
                    moves.add(new Move(new Square(rank, file), new Square(rank - 1, file + 2)));
                }
            }
            if (file - 2 > -1) {
                if (colorOf(board[rank - 1][file - 2]) != color) {
                    moves.add(new Move(new Square(rank, file), new Square(rank - 1, file - 2)));
                }
            }
        }
        if (file + 1 < 8) {
            if (rank + 2 < 8) {
                if (colorOf(board[rank + 2][file + 1]) != color) {
                    moves.add(new Move(new Square(rank, file), new Square(rank + 2, file + 1)));
                }
            }
            if (rank - 2 > -1) {
                if (colorOf(board[rank - 2][file + 1]) != color) {
                    moves.add(new Move(new Square(rank, file), new Square(rank - 2, file + 1)));
                }
            }
        }
        if (file - 1 > -1) {
            if (rank + 2 < 8) {
                if (colorOf(board[rank + 2][file - 1]) != color) {
                    moves.add(new Move(new Square(rank, file), new Square(rank + 2, file - 1)));
                }
            }
            if (rank - 2 > -1) {
                if (colorOf(board[rank - 2][file - 1]) != color) {
                    moves.add(new Move(new Square(rank, file), new Square(rank - 2, file - 1)));
                }
            }
        }
        return moves;
    }

    public LinkedList<Move> bishopMoves(Square square) {
        LinkedList<Move> moves = new LinkedList<>();
        int rank = square.rank;
        int file = square.file;
        Color color = colorOf(board[rank][file]);
        char p;
        for (int i = 1; (rank + i < 8 && file + i < 8); i++) {
            p = board[rank + i][file + i];
            if (colorOf(p) == color) {
                break;
            } else if (colorOf(p) != Color.EMPTY) {
                moves.add(new Move(new Square(rank, file), new Square(rank + i, file + i)));
                break;
            } else {
                moves.add(new Move(new Square(rank, file), new Square(rank + i, file + i)));
            }
        }
        for (int i = 1; (rank + i < 8 && file - i > -1); i++) {
            p = board[rank + i][file - i];
            if (colorOf(p) == color) {
                break;
            } else if (colorOf(p) != Color.EMPTY) {
                moves.add(new Move(new Square(rank, file), new Square(rank + i, file - i)));
                break;
            } else {
                moves.add(new Move(new Square(rank, file), new Square(rank + i, file - i)));
            }
        }
        for (int i = 1; (rank - i > -1 && file + i < 8); i++) {
            p = board[rank - i][file + i];
            if (colorOf(p) == color) {
                break;
            } else if (colorOf(p) != Color.EMPTY) {
                moves.add(new Move(new Square(rank, file), new Square(rank - i, file + i)));
                break;
            } else {
                moves.add(new Move(new Square(rank, file), new Square(rank - i, file + i)));
            }
        }
        for (int i = 1; (rank - i > -1 && file - i > -1); i++) {
            p = board[rank - i][file - i];
            if (colorOf(p) == color) {
                break;
            } else if (colorOf(p) != Color.EMPTY) {
                moves.add(new Move(new Square(rank, file), new Square(rank - i, file - i)));
                break;
            } else {
                moves.add(new Move(new Square(rank, file), new Square(rank - i, file - i)));
            }
        }
        return moves;
    }

    public LinkedList<Move> rookMoves(Square square) {
        LinkedList<Move> moves = new LinkedList<>();
        int rank = square.rank;
        int file = square.file;
        Color color = colorOf(board[rank][file]);
        char p;
        for (int i = 1; rank + i < 8; i++) {
            p = board[rank + i][file];
            if (colorOf(p) == color) {
                break;
            } else if (colorOf(p) != Color.EMPTY) {
                moves.add(new Move(rank, file, rank + i, file));
                break;
            } else {
                moves.add(new Move(rank, file, rank + i, file));
            }
        }
        for (int i = 1; rank - i > -1; i++) {
            p = board[rank - i][file];
            if (colorOf(p) == color) {
                break;
            } else if (colorOf(p) != Color.EMPTY) {
                moves.add(new Move(rank, file, rank - i, file));
                break;
            } else {
                moves.add(new Move(rank, file, rank - i, file));
            }
        }
        for (int i = 1; file + i < 8; i++) {
            p = board[rank][file + i];
            if (colorOf(p) == color) {
                break;
            } else if (colorOf(p) != Color.EMPTY) {
                moves.add(new Move(rank, file, rank, file + i));
                break;
            } else {
                moves.add(new Move(rank, file, rank, file + i));
            }
        }
        for (int i = 1; file - i > -1; i++) {
            p = board[rank][file - i];
            if (colorOf(p) == color) {
                break;
            } else if (colorOf(p) != Color.EMPTY) {
                moves.add(new Move(rank, file, rank, file - i));
                break;
            } else {
                moves.add(new Move(rank, file, rank, file - i));
            }
        }
        return moves;
    }

    public LinkedList<Move> queenMoves(Square square) {
        LinkedList<Move> moves = new LinkedList<>();
        int rank = square.rank;
        int file = square.file;
        LinkedList<Move> rookPart = rookMoves(new Square(rank, file));
        LinkedList<Move> bishopPart = bishopMoves(new Square(rank, file));
        for (int i = 0; i < rookPart.size(); i++) {
            moves.add(rookPart.get(i));
        }
        for (int i = 0; i < bishopPart.size(); i++) {
            moves.add(bishopPart.get(i));
        }
        return moves;
    }

    public LinkedList<Move> kingMoves(Square square) {
        LinkedList<Move> moves = new LinkedList<>();
        int rank = square.rank;
        int file = square.file;
        Color color = colorOf(board[rank][file]);
        if (rank < 7) {
            if (colorOf(board[rank + 1][file]) != color) {
                moves.add(new Move(rank, file, rank + 1, file));
            }
        }
        if (rank > 0) {
            if (colorOf(board[rank - 1][file]) != color) {
                moves.add(new Move(rank, file, rank - 1, file));
            }
        }
        if (file < 7) {
            if (colorOf(board[rank][file + 1]) != color) {
                moves.add(new Move(rank, file, rank, file + 1));
            }
        }
        if (file > 0) {
            if (colorOf(board[rank][file - 1]) != color) {
                moves.add(new Move(rank, file, rank, file - 1));
            }
        }
        if (rank < 7 && file < 7) {
            if (colorOf(board[rank + 1][file + 1]) != color) {
                moves.add(new Move(rank, file, rank + 1, file + 1));
            }
        }
        if (rank > 0 && file > 0) {
            if (colorOf(board[rank - 1][file - 1]) != color) {
                moves.add(new Move(rank, file, rank - 1, file - 1));
            }
        }
        if (file < 7 && rank > 0) {
            if (colorOf(board[rank - 1][file + 1]) != color) {
                moves.add(new Move(rank, file, rank - 1, file + 1));
            }
        }
        if (file > 0 && rank < 7) {
            if (colorOf(board[rank + 1][file - 1]) != color) {
                moves.add(new Move(rank, file, rank + 1, file - 1));
            }
        }
        if (color == Color.WHITE) {
            if (whiteCastlesShort && board[0][5] == '-' && board[0][6] == '-') {
                moves.add(new Move(rank, file, rank, file + 2));
            }
            if (whiteCastlesLong && board[0][1] == '-' && board[0][2] == '-' && board[0][3] == '-') {
                moves.add(new Move(rank, file, rank, file - 3));
            }
        } else {
            if (blackCastlesShort && board[7][5] == '-' && board[7][6] == '-') {
                moves.add(new Move(rank, file, rank, file + 2));
            }
            if (blackCastlesLong && board[7][1] == '-' && board[7][2] == '-' && board[7][3] == '-') {
                moves.add(new Move(rank, file, rank, file - 3));
            }
        }
        return moves;
    }

    public LinkedList<Move> pieceMoves(Square square) {
        int rank = square.rank;
        int file = square.file;
        switch (board[rank][file]) {
            case 'P':
                return pawnMoves(new Square(rank, file));
            case 'p':
                return pawnMoves(new Square(rank, file));
            case 'N':
                return knightMoves(new Square(rank, file));
            case 'n':
                return knightMoves(new Square(rank, file));
            case 'B':
                return bishopMoves(new Square(rank, file));
            case 'b':
                return bishopMoves(new Square(rank, file));
            case 'R':
                return rookMoves(new Square(rank, file));
            case 'r':
                return rookMoves(new Square(rank, file));
            case 'Q':
                return queenMoves(new Square(rank, file));
            case 'q':
                return queenMoves(new Square(rank, file));
            case 'K':
                return kingMoves(new Square(rank, file));
            case 'k':
                return kingMoves(new Square(rank, file));
            default:
                return new LinkedList<>();
        }
    }

    public boolean isLegal() {
        boolean isLegal = true;
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                if (colorOf(board[rank][file]) == toPlay) {
                    LinkedList<Move> pm = pieceMoves(new Square(rank, file));
                    for (int i = 0; i < pm.size(); i++) {
                        Move move = pm.get(i);
                        Square toSquare = move.toSquare;
                        if(toSquare.rank > 8 || toSquare.file > 8){
                            System.out.println("piece supposedly going to: " + toSquare.rank + "-" + toSquare.file + " from " + rank + "-" + file);
                            System.out.println("piecemoves: ");
                            System.out.println(pm);
                        }
                        char p = board[toSquare.rank][toSquare.file];
                        if (colorOf(p) == (toPlay == Color.WHITE ? Color.BLACK : Color.WHITE)
                                && (p == 'k' || p == 'K')) {
                            isLegal = false;
                        }
                    }
                }
            }
        }
        return isLegal;
    }

    public LinkedList<Move> getAllMoves() {
        LinkedList<Move> allMoves = new LinkedList<>();
        LinkedList<Move> sMoves = new LinkedList<>();
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                if (colorOf(board[rank][file]) == toPlay) {
                    sMoves = pieceMoves(new Square(rank, file));
                    for (int i = 0; i < sMoves.size(); i++) {
                        Board copy = this.copyBoard();
                        copy.playMove(sMoves.get(i));
                        if(copy.isLegal()){
                            allMoves.add(sMoves.get(i));
                        }
                    }
                }
            }
        }
        return allMoves;
    }

    public LinkedList<Move> getAllMovesForOtherPlayer() {
        LinkedList<Move> allMoves = new LinkedList<>();
        LinkedList<Move> sMoves = new LinkedList<>();
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                if (colorOf(board[rank][file]) == (toPlay == Color.WHITE ? Color.BLACK : Color.WHITE)) {
                    sMoves = pieceMoves(new Square(rank, file));
                    for (int i = 0; i < sMoves.size(); i++) {
                        allMoves.add(sMoves.get(i));
                    }
                }
            }
        }
        return allMoves;
    }

    public double evaluation() {
        double evaluation = 0.0;
        int totalPointCount = 0;
        int whiteBishopCount = 0;
        int blackBishopCount = 0;
        boolean isolated;
        boolean open;
        char p;
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                p = board[rank][file];
                if (p == 'P') {
                    evaluation++;
                    totalPointCount++;
                    isolated = true;
                    if (file > 0) {
                        for (int i = 0; i < 8; i++) {
                            if (board[i][file-1] == 'P') {
                                isolated = false;
                            }
                        }
                    }
                    if (file < 7) {
                        for (int i = 0; i < 8; i++) {
                            if (board[i][file+1] == 'P') {
                                isolated = false;
                            }
                        }
                    }
                    evaluation -= isolated ? 0.1 : 0;
                    if (file > 0) {
                        if (board[rank-1][file-1] == 'P') {
                            evaluation += 0.05;
                        }
                    }
                    if (file < 7) {
                        if (board[rank-1][file+1] == 'P') {
                            evaluation += 0.05;
                        }
                    }
                    evaluation += rank * 0.05;
                }
                if (p == 'p') {
                    evaluation--;
                    totalPointCount++;
                    isolated = true;
                    if (file > 0) {
                        for (int i = 0; i < 8; i++) {
                            if (board[i][file-1] == 'p') {
                                isolated = false;
                            }
                        }
                    }
                    if (file < 7) {
                        for (int i = 0; i < 8; i++) {
                            if (board[i][file+1] == 'p') {
                                isolated = false;
                            }
                        }
                    }
                    evaluation += isolated ? 0.1 : 0;
                    if (file > 0) {
                        if (board[rank+1][file-1] == 'p') {
                            evaluation -= 0.05;
                        }
                    }
                    if (file < 7) {
                        if (board[rank+1][file+1] == 'p') {
                            evaluation -= 0.05;
                        }
                    }
                    evaluation -= (7 - rank) * 0.05;
                }
                if (p == 'N') {
                    evaluation += 3;
                    totalPointCount += 3;
                    if (rank != 0 && rank != 7 && file != 0 && file != 7) {
                        evaluation += 0.2;
                    }
                }
                if (p == 'n') {
                    evaluation -= 3;
                    totalPointCount += 3;
                    if (rank != 0 && rank != 7 && file != 0 && file != 7) {
                        evaluation -= 0.2;
                    }
                }
                if (p == 'B') {
                    evaluation += 3;
                    totalPointCount += 3;
                    whiteBishopCount++;
                    LinkedList<Move> bm = bishopMoves(new Square(rank, file));
                    evaluation += 0.01 * bm.size();
                }
                if (p == 'b') {
                    evaluation -= 3;
                    totalPointCount += 3;
                    blackBishopCount++;
                    LinkedList<Move> bm = bishopMoves(new Square(rank, file));
                    evaluation -= 0.01 * bm.size();
                }
                if (p == 'R') {
                    evaluation += 5;
                    totalPointCount += 5;
                    open = true;
                    for (int i = 0; i < 8; i++) {
                        if (board[i][file] == 'P' || board[i][file] == 'p') {
                            open = false;
                        }
                    }
                    evaluation += open ? 0.05 : 0;
                    if (rank == 6) {
                        evaluation += 0.02;
                    }
                }
                if (p == 'r') {
                    evaluation -= 5;
                    totalPointCount += 5;
                    open = true;
                    for (int i = 0; i < 8; i++) {
                        if (board[i][file] == 'P' || board[i][file] == 'p') {
                            open = false;
                        }
                    }
                    evaluation -= open ? 0.05 : 0;
                    if (rank == 1) {
                        evaluation -= 0.02;
                    }
                }
                // the queen movearray evaluation was removed due to it causing bad opening
                // practices
                if (p == 'Q') {
                    evaluation += 9;
                    totalPointCount += 9;
                    /*
                     * MoveArray qm = queenMoves(rank, file);
                     * evaluation+=0.01*qm.size();
                     */
                }
                if (p == 'q') {
                    evaluation -= 9;
                    totalPointCount += 9;
                    /*
                     * MoveArray qm = queenMoves(rank, file);
                     * evaluation-=0.01*qm.size();
                     */
                }
            }
        }
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                p = board[rank][file];
                if (p == 'K') {
                    if (totalPointCount > 20 && rank == 0) {
                        evaluation += 0.1;
                        if (file == 2 || file == 6) {
                            evaluation += 0.05;
                        }
                    }
                }
                if (p == 'k') {
                    if (totalPointCount > 20 && rank == 7) {
                        evaluation -= 0.1;
                        if (file == 2 || file == 6) {
                            evaluation -= 0.05;
                        }
                    }
                }
            }
        }
        evaluation += 0.01 * whiteBishopCount;
        evaluation -= 0.01 * blackBishopCount;
        return (Math.ceil(evaluation * 100.0) / 100.0) == 0 ? 0 : (Math.ceil(evaluation * 100.0) / 100.0);
    }
}
