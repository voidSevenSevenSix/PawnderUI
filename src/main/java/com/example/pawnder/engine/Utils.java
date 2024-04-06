package com.example.pawnder.engine;
public class Utils {
    public static String moveToString(Move m){
        char[] sm = new char[5];
        sm[4] = '\0';
        char[] f = {'a','b','c','d','e','f','g','h'};
        char[] l = {'1','2','3','4','5','6','7','8'};
        Square fromT = m.fromSquare;
        Square toT = m.toSquare;
        sm[0] = f[fromT.file];
        sm[1] = l[fromT.rank];
        sm[2] = f[toT.file];
        sm[3] = l[toT.rank];
        return new String(sm);
    }
}
