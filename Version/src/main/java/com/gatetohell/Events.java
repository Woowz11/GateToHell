package com.gatetohell;

import org.lwjgl.glfw.GLFW;

public class Events {
    protected static String DevTest_Desc = "Тестовая штука";
    public static void DevTest(){

    }

    protected static String MaximizeWindow_Desc = "Делает окно максимизированым, или наоборот";
    public static void MaximizeWindow(){
        if(GLFW.glfwGetWindowAttrib(MinecraftInfo.WindowID, GLFW.GLFW_MAXIMIZED)==1){
            GLFW.glfwRestoreWindow(MinecraftInfo.WindowID);
        }else{
            GLFW.glfwMaximizeWindow(MinecraftInfo.WindowID);
        }
    }
}
