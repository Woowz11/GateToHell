package com.gatetohell;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import java.util.ArrayList;
import java.util.List;

public class Enums {
    public enum RenderVertexType{
        None                     (-1                          , 0),
        Points                   (GL11.GL_POINTS                  , 1),
        Lines                    (GL11.GL_LINES                   , 2),
        Lines_Adjacency          (GL32.GL_LINES_ADJACENCY         , 2),
        Line_Strip               (GL11.GL_LINE_STRIP              , 2),
        Line_Strip_Adjacency     (GL32.GL_LINE_STRIP_ADJACENCY    , 2),
        Line_Loop                (GL11.GL_LINE_LOOP               , 2),
        Triangles                (GL11.GL_TRIANGLES               , 3),
        Triangles_Adjacency      (GL32.GL_TRIANGLES_ADJACENCY     , 1),
        Triangle_Strip           (GL11.GL_TRIANGLE_STRIP          , 3),
        Triangles_Strip_Adjacency(GL32.GL_TRIANGLE_STRIP_ADJACENCY, 3),
        Triangle_Fan             (GL11.GL_TRIANGLE_FAN            , 3),
        Random                   (-1                          , 0),
        Random_Lines             (-1                          , 0),
        Random_Triangles         (-1                          , 0);

        public final int GL;
        public final int DoRandom;
        RenderVertexType(int GL, int DoRandom){ this.GL = GL; this.DoRandom = DoRandom; }

        public static final List<RenderVertexType> DoRandoms           = new ArrayList<>();
        public static final List<RenderVertexType> DoRandoms_Lines     = new ArrayList<>();
        public static final List<RenderVertexType> DoRandoms_Triangles = new ArrayList<>();
        static {
            for (RenderVertexType v : RenderVertexType.values()){
                if(v.DoRandom >= 1){
                    DoRandoms.add(v);

                    if(v.DoRandom == 2) {
                        DoRandoms_Lines.add(v);
                    }else if(v.DoRandom == 3){
                        DoRandoms_Triangles.add(v);
                    }
                }
            }
        }
    }

    public enum BrokeBuffer{ None, Sky, Everything }
    public enum MissingoType { None, Red, Noise, ColorNoise, Black, White }
}
