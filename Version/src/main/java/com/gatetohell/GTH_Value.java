package com.gatetohell;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public enum GTH_Value {
    M("M","Безумность"   , new Color(255,213,0  ), (Integer Value) -> GateToHell.Madness       = Value, () -> GateToHell.Madness      , "Добавляет в мир больше хаоса и безумия!"),
    S("S","Страшность"   , new Color(18 ,9  ,9  ), (Integer Value) -> GateToHell.Scariness     = Value, () -> GateToHell.Scariness    , "Делает мир более страшным и ужасающим!"),
    V("V","Пустотность"  , new Color(78 ,78 ,78 ), (Integer Value) -> GateToHell.Voidness      = Value, () -> GateToHell.Voidness     , "Делает мир пустотным, больше карманных измерений и безнадёжности!"),
    D("D","Демоничность" , new Color(255,89 ,0  ), (Integer Value) -> GateToHell.Hellness      = Value, () -> GateToHell.Hellness     , "Добавляет в мир религии, демоны и ангелы"),
    B("B","Гличность"    , new Color(255,31 ,237), (Integer Value) -> GateToHell.Glitchness    = Value, () -> GateToHell.Glitchness   , "Делает мир более глючным и баганым"),
    G("G","Жестокость"   , new Color(211,4  ,4  ), (Integer Value) -> GateToHell.Goreness      = Value, () -> GateToHell.Goreness     , "Добавляет больше расчленёнки и жестокости в мир"),
    L("L","Лиминальность", new Color(45 ,91 ,237), (Integer Value) -> GateToHell.Liminality    = Value, () -> GateToHell.Liminality   , "Делает мир более лиминальным"),
    W("W","Вувзкор-ность", new Color(122,58 ,0  ), (Integer Value) -> GateToHell.Woowzcoreness = Value, () -> GateToHell.Woowzcoreness, "Делает мир похожим на Woowzcore"),
    A("A","Одиночество"  , new Color(0  ,73 ,108), (Integer Value) -> GateToHell.Loneliness    = Value, () -> GateToHell.Loneliness   , "Делает игрока одним живым существом в мире"),
    H("H","Счастье"      , new Color(71 ,255,29 ), (Integer Value) -> GateToHell.Happiness     = Value, () -> GateToHell.Happiness    , "Делает мир более весёлым или грустным"),
    R("R","Музыкальность", new Color(169,100,251), (Integer Value) -> GateToHell.Musical       = Value, () -> GateToHell.Musical      , "Делает мир более музыкальным"),
    C("C","Взломность"   , new Color(69 ,104,0  ), (Integer Value) -> GateToHell.Hackness      = Value, () -> GateToHell.Hackness     , "Делает игру более взаимодействующей с компьютером"),
    Q("Q","Загадочность" , new Color(74 ,225,203), (Integer Value) -> GateToHell.Mystery       = Value, () -> GateToHell.Mystery      , "Делает мир более загадочным!"),
    U("U","Литуизм"      , new Color(182,182,182), (Integer Value) -> GateToHell.Lituism       = Value, () -> GateToHell.Lituism      , "ЛИТУИЗМ 😁😁😁");

    public static final int MAX =  1000;

    final String Code;
    final String Name;
    final Color  Color;
    final String Desc;
    private final Consumer<Integer> SetValueF;
    private final Supplier<Integer> GetValueF;

    GTH_Value(String Code, String Name, Color Color, Consumer<Integer> SetValueF, Supplier<Integer> GetValueF, String Desc){
        this.Code = Code; this.Name = Name; this.Color = Color; this.SetValueF = SetValueF; this.GetValueF = GetValueF; this.Desc = Desc;
    }

    public void SetValue(int Value){ this.SetValueF.accept(Value); if(GateToHell.Dev){ DevWindow.ValueProgressBars.get(this).SetValue(Value); } }
    public int  GetValue(         ){ return this.GetValueF.get() ; }
    public void AddValue(int Value){ SetValue(GetValue() + Value); }
    public void SubValue(int Value){ SetValue(GetValue() - Value); }
    public void MulValue(int Value){ SetValue(GetValue() * Value); }
    public void DivValue(int Value){ SetValue(GetValue() / Value); }
}
