package com.gatetohell;

public class Curses {
    private static final boolean D_Boolean = false;
    private static final double  D_Double  = 1;

    protected static final String DevTest_Desc = "Тестовая штука";
    public static boolean DevTest = false;

    protected static final String DevTestNumber_Desc = "Тестовая штука (число)";
    public static double DevTestNumber = D_Double;

    protected static final String RandomTextures_Desc = "Делает случайные текстуры у всего";
    public static boolean RandomTextures = D_Boolean;

    protected static final String DisableAnimationTextures_Desc = "Отключает анимацию у текстур";
    public static boolean DisableAnimationTextures = D_Boolean;

    protected static final String BrokeBufferClear_Desc = "Убирает очистку графического буфера";
    public static boolean BrokeBufferClear = D_Boolean;

    protected static final String BrokeSkyBufferClear_Desc = "Убирает очистку у неба";
    public static boolean BrokeSkyBufferClear = D_Boolean;

    protected static final String Wireframe_Desc = "Делает всё состоящим из сеток";
    public static boolean Wireframe = D_Boolean;

    protected static final String FanTriangles_Desc = "Изменяет порядок наложения полигонов, из-за чего ломаются модели";
    public static boolean FanTriangles = D_Boolean;

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

    protected static final String ClearPanorama_Desc = "Убирает панораму";
    public static boolean ClearPanorama = D_Boolean;

    protected static final String NoButtons_Desc = "Убирает кнопки";
    public static boolean NoButtons = D_Boolean;
}
