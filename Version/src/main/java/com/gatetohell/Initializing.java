package com.gatetohell;

public class Initializing {
    private static final boolean D_Boolean = false;

    protected static final String DevTest_Desc = "Тестовая штука";
    public static boolean DevTest = true;

    protected static final String TransparentBuffer_Desc = "Добавляет буфер прозрачности окну";
    public static boolean TransparentBuffer = D_Boolean;

    protected static final String BigWindow_Desc = "Делает окно с игрой гигантским";
    public static boolean BigWindow = D_Boolean;

    protected static final String InvertMoveYInAABB_Desc = "Меняет в функции Move, Y делает отрицательным, в AABB";
    public static boolean InvertMoveYInAABB = D_Boolean;

    protected static final String ChanceToNoCollisionBlock_Desc = "Делает 25% шанс что блок бехавер будет без коллизии";
    public static boolean ChanceToNoCollisionBlock = D_Boolean;

    protected static final String RandomColors_Desc = "Случайные цвета";
    public static boolean RandomColors = D_Boolean;

    protected static final String NoHeads_Desc = "Существа без голов";
    public static boolean NoHeads = D_Boolean;

    protected static final String Missingo_Desc = "Делает текстуры в виде missingo";
    public static boolean Missingo = D_Boolean;

    protected static final String MissingoType_Desc = "Изменяет тип генерации missingo текстуры";
    public static Enums.MissingoType MissingoType = Enums.MissingoType.None;
}
