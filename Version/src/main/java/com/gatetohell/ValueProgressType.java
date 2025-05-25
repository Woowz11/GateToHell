package com.gatetohell;

import java.awt.*;

public enum ValueProgressType {
    M("M","Безумность"   , new Color(255,213,0  )),
    S("S","Страшность"   , new Color(18 ,9  ,9  )),
    V("V","Пустотность"  , new Color(78 ,78 ,78 )),
    D("D","Демоничность" , new Color(255,89 ,0  )),
    B("B","Гличность"    , new Color(255,31 ,237)),
    G("G","Жестокость"   , new Color(211,4  ,4  )),
    L("L","Лиминальность", new Color(45 ,91 ,237)),
    W("W","Вувзкор-ность", new Color(122,58 ,0  )),
    A("A","Одиночество"  , new Color(0  ,73 ,108)),
    H("H","Счастье"      , new Color(71 ,255,29 )),
    R("R","Музыкальность", new Color(169,100,251)),
    C("C","Взломность"   , new Color(69 ,104,0  )),
    Q("Q","Загадочность" , new Color(74 ,225,203)),
    U("U","Литуизм"      , new Color(255,255,255));

    final String Code;
    final String Name;
    final Color  Color;

    ValueProgressType(String Code, String Name, Color Color){ this.Code = Code; this.Name = Name; this.Color = Color; }
}
