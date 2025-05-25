package com.gatetohell;

import org.joml.Vector2i;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.*;

public class DevWindow {
    static final Vector2i WindowSize = new Vector2i(750,1000);

    /* ============================================= */

    static final Map<GTH_Value, ValueProgressbarInfo> ValueProgressBars = new HashMap<>();
    static final List<CurseField> CurseFields = new ArrayList<>();

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
        CreateTab("Проклятья" , T_Curses    ());

        W.add(Tabs);
    }

    public static void CreateTab(String TabName, JPanel Content){
        Tabs.addTab(TabName, Content);
    }

    /* ============================================= */

    static class BorderListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            if (isSelected) {
                label.setBackground(list.getBackground());
                label.setForeground(list.getForeground());
            }

            return label;
        }
    }
    public static JPanel T_ValuesInfo(){
        JPanel Content = new JPanel(new BorderLayout());

        JPanel Left = new JPanel();
        Left.setBackground(Color.LIGHT_GRAY);
        Left.setPreferredSize(new Dimension(WindowSize.x / 2, WindowSize.y));

        /* -------------------------------------- */

        JPanel Right = new JPanel(new BorderLayout());

        Map<String, String> Values = new LinkedHashMap<>(){{
            put("Персональный сид",String.valueOf(GateToHell.PersonalSeed));
            put("Сид сессии"      ,String.valueOf(GateToHell.SessionSeed ));
        }};

        DefaultListModel<String> LeftListModel = new DefaultListModel<>();
        DefaultListModel<String> RightListModel = new DefaultListModel<>();

        for (Map.Entry<String, String> e : Values.entrySet()) {
            LeftListModel .addElement(e.getKey());
            RightListModel.addElement(e.getValue());
        }

        JList<String> LeftList  = new JList<String>(LeftListModel );
        JList<String> RightList = new JList<String>(RightListModel);

        LeftList .setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        RightList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        LeftList .setCellRenderer(new BorderListCellRenderer());
        RightList.setCellRenderer(new BorderListCellRenderer());

        JPanel ValuesPanel = new JPanel();
        ValuesPanel.setLayout(new GridLayout(1, 2));

        ValuesPanel.add(LeftList );
        ValuesPanel.add(RightList);

        JScrollPane ScrollValuesPanel = new JScrollPane(ValuesPanel);
        Right.add(ScrollValuesPanel, BorderLayout.CENTER);

        /* -------------------------------------- */

        JPanel LeftTop = new JPanel();
        LeftTop.setLayout(new BoxLayout(LeftTop, BoxLayout.Y_AXIS));

        for(GTH_Value GTH_V : GTH_Value.values()){
            LeftTop.add(E_CreateValueProgressbar(GTH_V));
        }

        for(Map.Entry<GTH_Value, ValueProgressbarInfo> e : ValueProgressBars.entrySet()){
            e.getKey().SetValue(
                    (int)(((Math.random()-0.5)*2)*1000)
            );
        }

        /* -------------------------------------- */

        JPanel LeftDown = new JPanel();
        LeftDown.setBackground(Color.green);

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

    public enum CurseFieldType{ Checkbox }
    public static class CurseField{
        private final Field Field;
        private final CurseFieldType Type;

        private CurseField(Field Field, CurseFieldType Type){ this.Field = Field; this.Type = Type; }

        public JLabel Text = null;
        public JCheckBox Checkbox = null;
        public ItemListener IL = null;

        public void Update(){
            switch (Type){
                case Checkbox:
                {
                    Checkbox.removeItemListener(IL);
                    try{
                        boolean Value = this.Field.getBoolean(null);
                        Checkbox.setSelected(Value);
                    }catch(IllegalAccessException e){
                        e.printStackTrace();
                    }
                    Checkbox.addItemListener(IL);
                }
            }
        }

        public void AddDesc(String Desc){
            switch (Type){
                case Checkbox:
                {
                    this.Text.setToolTipText(Desc);
                    this.Checkbox.setToolTipText(Desc);
                    break;
                }
            }
        }
    }
    public static JPanel T_Curses(){
        JPanel Content = new JPanel(new GridLayout(1, 3));
        JPanel Column1 = new JPanel(new GridLayout(0, 1));
        JPanel Column2 = new JPanel(new GridLayout(0, 1));
        JPanel Column3 = new JPanel(new GridLayout(0, 1));

        Column2.setBackground(Color.LIGHT_GRAY);

        Map<String,String> Descs = new HashMap<>();

        int WhatColumn = 0;
        Field[] Fields = Curses.class.getDeclaredFields();
        for(Field F : Fields){
            if(Modifier.isStatic(F.getModifiers())){
                F.setAccessible(true);

                boolean ThatString = F.getType().equals(String.class);

                if(ThatString){
                    String Desc = "err?";

                    try{
                        Desc = (String) F.get(null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    Descs.put(F.getName().replace("_Desc",""), Desc);
                    continue;
                }

                CurseFieldType Type = CurseFieldType.Checkbox;

                CurseField CF = new CurseField(F, Type);

                JPanel Result = new JPanel(new BorderLayout());
                Result.setOpaque(false);

                switch (Type){
                    case Checkbox:
                    {
                        JLabel Name = new JLabel(F.getName());
                        Name.setOpaque(false);
                        JCheckBox Checkbox = new JCheckBox();
                        Checkbox.setOpaque(false);

                        Result.add(Name    , BorderLayout.WEST);
                        Result.add(Checkbox, BorderLayout.EAST);

                        CF.Text = Name;
                        CF.Checkbox = Checkbox;

                        CF.IL = (ItemEvent e) -> {
                            try{
                                F.setBoolean(null, Checkbox.isSelected());
                            } catch (IllegalAccessException ex) {
                                ex.printStackTrace();
                            }
                        };

                        break;
                    }
                }

                CF.Update();
                CurseFields.add(CF);

                switch (WhatColumn){
                    case 0: Column1.add(Result); break;
                    case 1: Column2.add(Result); break;
                    case 2: Column3.add(Result); break;
                }

                WhatColumn++;
                if(WhatColumn==3){ WhatColumn = 0; }
            }
        }

        for(Map.Entry<String,String> e : Descs.entrySet()){
            String FieldName = e.getKey();
            String Desc      = e.getValue();

            for(CurseField CF : CurseFields){
                if(CF.Field.getName().equals(FieldName)){
                    CF.AddDesc(Desc);
                    break;
                }
            }
        }

        Content.add(Column1);
        Content.add(Column2);
        Content.add(Column3);
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
