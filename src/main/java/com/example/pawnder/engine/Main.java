package com.example.pawnder.engine;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter FEN:");
        String fen = scanner.nextLine();
        System.out.println("Enter depth:");
        try{
            int depth = Integer.parseInt(scanner.nextLine());
            System.out.println(Pawnder.analyze(fen, depth));
        }
        catch(Exception e){}
        scanner.close();
    }
}
