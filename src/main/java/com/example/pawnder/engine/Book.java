package com.example.pawnder.engine;

import java.io.File;
import java.util.Optional;
import java.util.Scanner;

public class Book {
    public static Optional<String> checkBook(String fen){
        File book = new File("book.fen");
        try{
            Scanner scanner = new Scanner(book);
            while(scanner.hasNextLine()){
                if(scanner.nextLine().equals(fen)){
                    scanner.close();
                    return Optional.of(scanner.nextLine());
                }
            }
            scanner.close();
        }
        catch(Exception e){}
        return Optional.empty();
    }
}
