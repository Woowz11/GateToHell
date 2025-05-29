package com.gatetohell;

import java.awt.*;
import java.util.concurrent.ThreadLocalRandom;

public class Helper {
    /** Проверяет, является ли цвет изменённым (ЯВЛЯЕТСЯ ЛИ ЦВЕТОМ 228, 228, 228, 228) */
    public static boolean ThatChangedColor(Color C){
        if(C==null){ return false; }
        return !(C.getRed() == 228 && C.getGreen() == 228 && C.getBlue() == 228 && C.getAlpha() == 228);
    }

    /** Случайное число от MIN до MAX */
    public static double Random(double min, double max){ return ThreadLocalRandom.current().nextDouble(min, max + 1); }
    /** Случайное число от 0 до MAX */
    public static double Random(double max){ return Random(0,max); }
    /** Случайное число от 0 до 1 */
    public static double Random(){ return Random(0,1); }

    /** Случайное целое число от MIN до MAX */
    public static int RandomI(int min, int max){ return ThreadLocalRandom.current().nextInt(min, max + 1); }
}
