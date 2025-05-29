package com.gatetohell;

import java.awt.*;

public class Curses {
    private static final boolean D_Boolean = false;
    private static final double  D_Double  = 1;
    private static final Color   D_Color = new Color(228,228,228,228);

    protected static final String DevTest_Desc = "Тестовая штука";
    public static boolean DevTest = false;

    protected static final String DevTestNumber_Desc = "Тестовая штука (число)";
    public static double DevTestNumber = D_Double;

    protected static final String RandomTextures_Desc = "Делает случайные текстуры у всего";
    public static boolean RandomTextures = D_Boolean;

    protected static final String DisableAnimationTextures_Desc = "Отключает анимацию у текстур";
    public static boolean DisableAnimationTextures = D_Boolean;

    protected static final String ClearBuffer_Desc = "Изменяет очистку графического буфера";
    public static Enums.BrokeBuffer ClearBuffer = Enums.BrokeBuffer.None;

    protected static final String RenderVertex_Desc = "Изменяет рендер вертексов";
    public static Enums.RenderVertexType RenderVertex = Enums.RenderVertexType.None;

    protected static final String SoundSpeed_Desc = "Скорость звуков";
    public static double SoundSpeed = D_Double;

    protected static final String RandomSpeedSound_Desc = "Случайная скорость у звуков (speed *= rand(SoundSpeed))";
    public static boolean RandomSpeedSound = D_Boolean;

    protected static final String BlackStars_Desc = "Делает чёрные звёзды";
    public static boolean BlackStars = D_Boolean;

    protected static final String BlackSky_Desc = "Делает чёрное небо";
    public static boolean BlackSky = D_Boolean;

    protected static final String MoonSize_Desc = "Меняет размер луны";
    public static double MoonSize = D_Double;

    protected static final String ClearPanorama_Desc = "Убирает панораму (делает определённый цвет)";
    public static Color ClearPanorama = D_Color;

    protected static final String NoButtons_Desc = "Убирает кнопки";
    public static boolean NoButtons = D_Boolean;

    protected static final String ColorWater_Desc = "Цветная вода вода";
    public static Color ColorWater = D_Color;
}
