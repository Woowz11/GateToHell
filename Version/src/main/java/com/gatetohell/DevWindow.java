package com.gatetohell;

import org.joml.Vector2i;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DevWindow {
    static final Vector2i WindowSize = new Vector2i(750,1000);

    /* ============================================= */

    static final Map<ValueProgressType, ValueProgressbarInfo> ValueProgressBars = new HashMap<>();

    static JFrame W = null;

    public static void CreateWindow(){
        W = new JFrame("GATE TO HELL");
        W.setResizable(false);

        W.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        W.setSize(WindowSize.x, WindowSize.y);

        CreateWindowContent();

        W.setVisible(true);
    }

    private static JTabbedPane Tabs = null;
    public static void CreateWindowContent(){
        Tabs = new JTabbedPane();

        CreateTab("Переменные", T_ValuesInfo());

        W.add(Tabs);
    }

    public static void CreateTab(String TabName, JPanel Content){
        JPanel Tab = new JPanel();
        Tab.add(Content);
        Tabs.addTab(TabName, Tab);
    }

    /* ============================================= */

    public static JPanel T_ValuesInfo(){
        JPanel Content = new JPanel(new BorderLayout());

        JPanel Left = new JPanel();
        Left.setBackground(Color.LIGHT_GRAY);
        Left.setPreferredSize(new Dimension(WindowSize.x / 2, WindowSize.y));

        for(ValueProgressType VPT : ValueProgressType.values()){
            Left.add(E_CreateValueProgressbar(VPT));
        }

        for(Map.Entry<ValueProgressType, ValueProgressbarInfo> e : ValueProgressBars.entrySet()){
            e.getValue().SetValue(
                    (int)(((Math.random()-0.5)*2)*1000)
            );
        }

        JPanel Right = new JPanel();

        Content.add(Left , BorderLayout.WEST  );
        Content.add(Right, BorderLayout.CENTER);

        return Content;
    }

    /* ============================================= */

    public static class ValueProgressbarInfo {
        private final JProgressBar Bar;
        private int Value = 0;

        private ValueProgressbarInfo(JProgressBar Bar){ this.Bar = Bar; }

        public void SetValue(int Value){
            this.Value = Value;
            Bar.setValue(Value);
            Bar.setString(String.valueOf(Value));
        }

        public int GetValue(){ return Value; }
    }
    public static JPanel E_CreateValueProgressbar(ValueProgressType VPT){
        JPanel Result = new JPanel();
        Result.setLayout(new BorderLayout());
        Result.setBorder(new EmptyBorder(2,2,2,2));

        JPanel Name = new JPanel(new BorderLayout());
        Name.setBorder(new EmptyBorder(0,0,0,5));
        JLabel NameText = new JLabel("[" + VPT.Code + "] " + VPT.Name);
        NameText.setPreferredSize(new Dimension(WindowSize.x / 6, NameText.getPreferredSize().height));
        Name.add(NameText, BorderLayout.WEST);
        Result.add(Name, BorderLayout.WEST);

        JProgressBar Bar = new JProgressBar(-1000,1000);
        Bar.setValue(0);
        Bar.setStringPainted(true);
        Bar.setForeground(VPT.Color);
        Bar.setString("?");
        Bar.setPreferredSize(new Dimension(WindowSize.x / 4, 15));

        ValueProgressBars.put(VPT, new ValueProgressbarInfo(Bar));

        Result.add(Bar, BorderLayout.CENTER);

        return Result;
    }
}
