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

    static final Map<GTH_Value, ValueProgressbarInfo> ValueProgressBars = new HashMap<>();

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
        Tabs.addTab(TabName, Content);
    }

    /* ============================================= */

    public static JPanel T_ValuesInfo(){
        JPanel Content = new JPanel(new BorderLayout());

        JPanel Left = new JPanel();
        Left.setBackground(Color.LIGHT_GRAY);
        Left.setPreferredSize(new Dimension(WindowSize.x / 2, WindowSize.y));

        JPanel Right = new JPanel();

        JPanel LeftTop = new JPanel();
        LeftTop.setLayout(new BoxLayout(LeftTop, BoxLayout.Y_AXIS));

        JPanel LeftDown = new JPanel();
        LeftDown.setBackground(Color.green);

        /* -------------------------------------- */

        for(GTH_Value GTH_V : GTH_Value.values()){
            LeftTop.add(E_CreateValueProgressbar(GTH_V));
        }

        for(Map.Entry<GTH_Value, ValueProgressbarInfo> e : ValueProgressBars.entrySet()){
            e.getKey().SetValue(
                    (int)(((Math.random()-0.5)*2)*1000)
            );
        }

        /* -------------------------------------- */

        JSplitPane SP_Left = new JSplitPane(JSplitPane.VERTICAL_SPLIT, LeftTop, LeftDown);
        SP_Left.setDividerLocation(WindowSize.y / 2);
        SP_Left.setEnabled(false);

        JSplitPane SP_Content = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, SP_Left, Right);
        SP_Content.setDividerLocation(WindowSize.x / 2);
        SP_Content.setEnabled(false);

        Content.add(SP_Content, BorderLayout.CENTER);

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
            Bar.setToolTipText(Value + " — " + (((float)Value / GTH_Value.MAX) * 100) + "%");
        }

        public int GetValue(){ return Value; }
    }
    public static JPanel E_CreateValueProgressbar(GTH_Value GTH_V){
        JPanel Result = new JPanel();
        Result.setLayout(new BorderLayout());
        Result.setBorder(new EmptyBorder(0,0,0,0));

        JPanel Name = new JPanel(new BorderLayout());
        Name.setBorder(new EmptyBorder(0,0,0,5));
        JLabel NameText = new JLabel("[" + GTH_V.Code + "] " + GTH_V.Name);
        NameText.setPreferredSize(new Dimension(WindowSize.x / 6, NameText.getPreferredSize().height));
        NameText.setToolTipText(GTH_V.Name + " — " + GTH_V.Desc);
        Name.add(NameText, BorderLayout.WEST);
        Result.add(Name, BorderLayout.WEST);

        JProgressBar Bar = new JProgressBar(-GTH_Value.MAX, GTH_Value.MAX);
        Bar.setValue(0);
        Bar.setStringPainted(true);
        Bar.setForeground(GTH_V.Color);
        Bar.setString("?");
        Bar.setPreferredSize(new Dimension(WindowSize.x / 5, 15));

        ValueProgressBars.put(GTH_V, new ValueProgressbarInfo(Bar));

        Result.add(Bar, BorderLayout.CENTER);

        JPanel Buttons  = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JButton B_Minus = new JButton("-");
        JButton B_Plus  = new JButton("+");
        JButton B_Set   = new JButton("S");

        Dimension ButtonSize = new Dimension(20,40);
        B_Minus.setPreferredSize(ButtonSize);
        B_Plus .setPreferredSize(ButtonSize);
        B_Set  .setPreferredSize(ButtonSize);

        B_Minus.setMargin(new Insets(0,0,0,0));
        B_Plus .setMargin(new Insets(0,0,0,0));
        B_Set  .setMargin(new Insets(0,0,0,0));

        Font F = new Font("Arial", Font.PLAIN, 10);
        B_Minus.setFont(F);
        B_Plus .setFont(F);
        B_Set  .setFont(F);

        B_Minus.addActionListener(e -> {
            GTH_V.SubValue(1);
        });
        B_Plus .addActionListener(e -> {
            GTH_V.AddValue(1);
        });
        B_Set  .addActionListener(e -> {
            GTH_V.SetValue(0);
        });

        Buttons.add(B_Minus);
        Buttons.add(B_Plus );
        Buttons.add(B_Set  );

        Result.add(Buttons, BorderLayout.EAST);

        return Result;
    }
}
