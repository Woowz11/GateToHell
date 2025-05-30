package com.gatetohell;

import org.joml.Vector2i;

import javax.swing.Timer;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.*;

public class DevWindow {
    static final Vector2i WindowSize = new Vector2i(750,1000);
    static final String  WindowTitle = "GATE TO HELL";

    /* ============================================= */

    static final Map<GTH_Value, ValueProgressbarInfo> ValueProgressBars = new HashMap<>();
    static final List<ValueInfo>   ValueInfos   = new ArrayList<>();
    static final List<FieldInfo>  CurseFields  = new ArrayList<>();
    static final List<FieldInfo>  InitFields   = new ArrayList<>();
    static final List<EventMethod> EventMethods = new ArrayList<>();

    static JFrame W = null;

    protected static void CreateWindow(){
        W = new JFrame("...");
        W.setResizable(false);

        W.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        W.setSize(WindowSize.x, WindowSize.y);

        CreateWindowContent();
    }

    private static JTabbedPane Tabs = null;
    private static int SelectedTab = 0;
    private static void CreateWindowContent(){
        Tabs = new JTabbedPane();

        CreateTab("Переменные"      , T_ValuesInfo()); /* 0 */
        CreateTab("Проклятья"       , T_Curses    ()); /* 1 */
        CreateTab("Ивенты"          , T_Events    ()); /* 2 */
        CreateTab("Проклятья ивенты", T_Events    ()); /* 3 */

        Tabs.addChangeListener((e) -> {
            SelectedTab = Tabs.getSelectedIndex();
            UpdateWindowTitle();
            UpdateTabContent();
        });
        UpdateWindowTitle();
        UpdateTabContent();

        W.add(Tabs);
    }
    private static void UpdateWindowTitle(){
        W.setTitle(WindowTitle + " [" + SelectedTab + "]");
    }
    protected static void UpdateTabContent(){
        switch (SelectedTab){
            case 0:
            {
                for(ValueInfo VI : ValueInfos){
                    VI.Update();
                }
                break;
            }
            case 1:
            {
                for(FieldInfo CF : CurseFields){
                    CF.Update();
                }
                break;
            }
            default:
                break;
        }
    }

    private static void CreateTab(String TabName, JPanel Content){
        Tabs.addTab(TabName, Content);
    }

    /* ============================================= */

    protected static boolean WaitingInitialization = false;
    protected static void CreateInitializingWindow(){
        WaitingInitialization = true;

        SwingUtilities.invokeLater(() -> {
            JFrame Dialog = new JFrame("Выберите инициализируемое!");
            Dialog.setSize(WindowSize.x, WindowSize.y);
            Dialog.setResizable(false);
            Dialog.setLocationRelativeTo(null);
            Dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            Dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    WaitingInitialization = false;
                    W.setVisible(true);
                }
            });

            Dialog.add(T_Initializing());

            for(FieldInfo CF : InitFields){
                CF.Update();
            }

            Dialog.setVisible(true);
        });
    }

    /* ============================================= */

    private static class BorderListCellRenderer extends DefaultListCellRenderer {
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
    private static DefaultListModel<String> RightListModel = null;
    private static class ValueInfo{
        public String Value = "?";
        private final Field Field;
        private final int I;

        private ValueInfo(Field Field, int I){ this.Field = Field; this.I = I; }

        public void Update(){
            try{
                this.Field.setAccessible(true);
                Object ValueObj = this.Field.get(null);
                String Value = String.valueOf(ValueObj);

                RightListModel.set(I, Value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    private static int NumberValueRange = 1;
    private static JPanel T_ValuesInfo(){
        JPanel Content = new JPanel(new BorderLayout());

        JPanel Left = new JPanel();
        Left.setBackground(Color.LIGHT_GRAY);
        Left.setPreferredSize(new Dimension(WindowSize.x / 2, WindowSize.y));

        /* -------------------------------------- */

        JPanel Right = new JPanel(new BorderLayout());

        try {
            Class<?> Clazz = GateToHell.class;
            Map<String, Field> Values = new LinkedHashMap<>() {{
                put("Прошло времени"  , Clazz.getDeclaredField("TimeAfterStart"));
                put("Персональный сид", Clazz.getDeclaredField("PersonalSeed"  ));
                put("Сид сессии"      , Clazz.getDeclaredField("SessionSeed"   ));
            }};

            DefaultListModel<String> LeftListModel = new DefaultListModel<>();
            RightListModel = new DefaultListModel<>();

            int I = 0;
            for (Map.Entry<String, Field> e : Values.entrySet()) {
                LeftListModel.addElement(e.getKey());
                RightListModel.addElement("err?");

                ValueInfo VI = new ValueInfo(e.getValue(), I);
                ValueInfos.add(VI);
                I++;
            }

            JList<String> LeftList  = new JList<>(LeftListModel );
            JList<String> RightList = new JList<>(RightListModel);

            LeftList .setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
            RightList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);

            LeftList.setCellRenderer(new BorderListCellRenderer());
            RightList.setCellRenderer(new BorderListCellRenderer());

            JPanel ValuesPanel = new JPanel();
            ValuesPanel.setLayout(new GridLayout(1, 2));

            ValuesPanel.add(LeftList);
            ValuesPanel.add(RightList);

            JScrollPane ScrollValuesPanel = new JScrollPane(ValuesPanel);
            Right.add(ScrollValuesPanel, BorderLayout.CENTER);
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }
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

        JSpinner NumberRange = new JSpinner(new SpinnerNumberModel(NumberValueRange, -1000, 1000, 1));

        NumberRange.addChangeListener(e -> {
            NumberValueRange = (Integer) NumberRange.getValue();
        });

        LeftDown.add(NumberRange);

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

    private static JPanel CreateEvalPanel(boolean Eval){
        JPanel Result = null;
        if(Eval) {
            Result = new JPanel(new GridBagLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2D = (Graphics2D) g;
                    g2D.setColor(new Color(0, 0, 0, 10));
                    g2D.fillRect(0, 0, getWidth(), getHeight());
                }
            };
        }else{
            Result = new JPanel(new GridBagLayout());
        }

        Result.setOpaque(false);
        return Result;
    }
    private enum FieldInfoType { Checkbox, Range, Color, Enums }
    private static class FieldInfo {
        private final Field Field;
        private final FieldInfoType Type;
        protected boolean HasDesc = false;
        private Color CurrentColor = new Color(228,228,228,228);
        protected Class<Enum> EnumClass;

        private FieldInfo(Field Field, FieldInfoType Type){ this.Field = Field; this.Type = Type; }

        protected JLabel Text = null;
        protected JCheckBox Checkbox = null;
        protected JSpinner NumberRange = null;
        protected JComboBox<Enum<?>> SelectEnum = null;
        protected JButton Button = null;
        protected ItemListener IL = null;
        protected ChangeListener CL = null;
        protected ActionListener AL = null;

        protected void Update(){
            switch (Type){
                case Checkbox:
                {
                    this.Checkbox.removeItemListener(IL);
                    try{
                        boolean Value = this.Field.getBoolean(null);
                        this.Checkbox.setSelected(Value);
                    }catch(IllegalAccessException e){
                        e.printStackTrace();
                    }
                    this.Checkbox.addItemListener(IL);
                    break;
                }
                case Range:
                {
                    this.NumberRange.removeChangeListener(CL);
                    try{
                        this.NumberRange.setValue(this.Field.getDouble(null));
                    }catch(IllegalAccessException e){
                        e.printStackTrace();
                    }
                    this.NumberRange.addChangeListener(CL);
                    break;
                }
                case Color:
                {
                    this.Button.removeActionListener(AL);
                    try{
                        CurrentColor = (Color) this.Field.get(null);
                        Button.setBackground(CurrentColor);
                        Button.setForeground(getContrastColor(CurrentColor));
                    }catch(IllegalAccessException e){
                        e.printStackTrace();
                    }
                    this.Button.addActionListener(AL);
                    break;
                }
                case Enums:
                {
                    this.SelectEnum.removeActionListener(AL);
                    try{
                        Enum<?> CurrentEnum = (Enum<?>) this.Field.get(null);
                        this.SelectEnum.setSelectedItem(CurrentEnum);
                    }catch(IllegalAccessException e){
                        e.printStackTrace();
                    }
                    this.SelectEnum.addActionListener(AL);
                    break;
                }
            }
        }

        protected void AddDesc(String Desc){
            HasDesc = true;
            switch (Type){
                case Checkbox:
                {
                    this.Text.setToolTipText(Desc);
                    this.Checkbox.setToolTipText(Desc);
                    break;
                }
                case Range:
                {
                    this.Text.setToolTipText(Desc);
                    this.NumberRange.setToolTipText(Desc);
                    break;
                }
                case Color:
                {
                    this.Text.setToolTipText(Desc);
                    this.Button.setToolTipText(Desc);
                    break;
                }
                case Enums:
                {
                    this.Text.setToolTipText(Desc);
                    this.SelectEnum.setToolTipText(Desc);
                    break;
                }
            }
        }
    }
    private static JPanel CreateValuesChangerPanel(Class<?> WhereFields, List<FieldInfo> ResultList){
        JPanel Content = new JPanel(new GridLayout(1, 3));
        JPanel Column1 = new JPanel(new GridLayout(0, 1));
        JPanel Column2 = new JPanel(new GridLayout(0, 1));
        JPanel Column3 = new JPanel(new GridLayout(0, 1));

        Column2.setBackground(Color.LIGHT_GRAY);

        Map<String,String> Descs = new HashMap<>();

        int WhatColumn = 0;
        boolean Eval = false;
        int i = 0;
        Field[] Fields = WhereFields.getDeclaredFields();
        for(Field F : Fields){
            if(Modifier.isStatic(F.getModifiers()) && !Modifier.isPrivate(F.getModifiers())){
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

                i++;

                FieldInfoType Type = FieldInfoType.Checkbox;
                if(F.getType().equals(double.class) || F.getType().equals(Double.class)){
                    Type = FieldInfoType.Range;
                }else if(F.getType().equals(Color.class)){
                    Type = FieldInfoType.Color;
                }

                if(F.getType().isEnum()){
                    Type = FieldInfoType.Enums;
                }

                FieldInfo CF = new FieldInfo(F, Type);

                switch (WhatColumn){
                    case 0: Eval = Column1.getComponentCount() % 2 == 0; break;
                    case 1: Eval = Column2.getComponentCount() % 2 == 0; break;
                    case 2: Eval = Column3.getComponentCount() % 2 == 0; break;
                }

                JPanel Result = CreateEvalPanel(Eval);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weighty = 1;
                gbc.gridy = 0;

                String FName = "[" + i + "] " + F.getName();

                switch (Type){
                    case Checkbox:
                    {
                        JLabel Name = new JLabel(FName);
                        Name.setOpaque(false);
                        JCheckBox Checkbox = new JCheckBox();
                        Checkbox.setOpaque(false);

                        gbc.weightx = 0.75;
                        gbc.gridx = 0;
                        Result.add(Name    , gbc);
                        gbc.weightx = 0.25;
                        gbc.gridx = 1;
                        Result.add(Checkbox, gbc);

                        CF.Text     = Name;
                        CF.Checkbox = Checkbox;

                        CF.IL = e -> {
                            try{
                                F.setBoolean(null, Checkbox.isSelected());
                            } catch (IllegalAccessException ex) {
                                ex.printStackTrace();
                            }
                        };

                        break;
                    }
                    case Range:
                    {
                        JLabel Name = new JLabel(FName);
                        Name.setOpaque(false);
                        JSpinner NumberRange = new JSpinner(new SpinnerNumberModel(0, -1000, 1000, 0.1));
                        NumberRange.setOpaque(false);

                        gbc.weightx = 0.75;
                        gbc.gridx = 0;
                        Result.add(Name       , gbc);
                        gbc.weightx = 0.25;
                        gbc.gridx = 1;
                        Result.add(NumberRange, gbc);

                        CF.Text        = Name       ;
                        CF.NumberRange = NumberRange;

                        CF.CL = e -> {
                            try{
                                F.setDouble(null, (Double)NumberRange.getValue());
                            } catch (IllegalAccessException ex) {
                                ex.printStackTrace();
                            }
                        };

                        break;
                    }
                    case Color:
                    {
                        JLabel Name = new JLabel(FName);
                        Name.setOpaque(false);
                        JButton ColorSelect = new JButton("Цвет");
                        ColorSelect.setOpaque(true);

                        gbc.weightx = 0.75;
                        gbc.gridx = 0;
                        Result.add(Name       , gbc);
                        gbc.weightx = 0.25;
                        gbc.gridx = 1;
                        Result.add(ColorSelect, gbc);

                        CF.Text   = Name       ;
                        CF.Button = ColorSelect;

                        CF.AL = e -> {
                            Color ResultColor = JColorChooser.showDialog(W, "Выберите цвет для " + FName, CF.CurrentColor);
                            if(ResultColor != null) {
                                try {
                                    F.set(null, ResultColor);

                                    ColorSelect.setBackground(ResultColor);
                                    ColorSelect.setForeground(getContrastColor(ResultColor));
                                } catch (IllegalAccessException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        };

                        break;
                    }
                    case Enums:
                    {
                        JLabel Name = new JLabel(FName);
                        Name.setOpaque(false);
                        Class<?> EnumClass = F.getType();
                        JComboBox<Enum<?>> SelectEnum = new JComboBox<>((Enum<?>[]) EnumClass.getEnumConstants());
                        SelectEnum.setOpaque(false);

                        gbc.weightx = 0.75;
                        gbc.gridx = 0;
                        Result.add(Name       , gbc);
                        gbc.weightx = 0.25;
                        gbc.gridx = 1;
                        Result.add(SelectEnum, gbc);

                        CF.Text        = Name     ;
                        CF.SelectEnum = SelectEnum;

                        CF.AL = e -> {
                            try{
                                Enum<?> Value = (Enum<?>) SelectEnum.getSelectedItem();
                                F.set(null, Value);
                            } catch (IllegalAccessException ex) {
                                ex.printStackTrace();
                            }
                        };

                        break;
                    }
                }

                ResultList.add(CF);

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

            for(FieldInfo CF : ResultList){
                if(CF.Field.getName().equals(FieldName)){
                    CF.AddDesc("[" + CF.Field.getName() + "] " + Desc);
                    break;
                }
            }
        }

        for(FieldInfo CF : ResultList){
            if(!CF.HasDesc){
                CF.AddDesc("Описание отсутствует!");
            }
        }

        int Max = Math.max(Column1.getComponentCount(), Math.max(Column2.getComponentCount(), Column3.getComponentCount()));
        if(Column1.getComponentCount() != Max){ Column1.add(CreateEvalPanel((Column1.getComponentCount()) % 2 == 0 )); }
        if(Column2.getComponentCount() != Max){ Column2.add(CreateEvalPanel((Column2.getComponentCount()) % 2 == 0 )); }
        if(Column3.getComponentCount() != Max){ Column3.add(CreateEvalPanel((Column3.getComponentCount()) % 2 == 0 )); }

        Content.add(Column1);
        Content.add(Column2);
        Content.add(Column3);
        return Content;
    }

    private static JPanel T_Curses(){
        return CreateValuesChangerPanel(Curses.class, CurseFields);
    }
    private static JPanel T_Initializing(){
        return CreateValuesChangerPanel(Initializing.class, InitFields);
    }

    private static class EventMethod{
        private final Method Method;
        protected boolean HasDesc = false;

        private EventMethod(Method Method){ this.Method = Method; }

        protected JLabel Text = null;
        protected JButton Button = null;

        protected void AddDesc(String Desc){
            HasDesc = true;
            this.Text.setToolTipText(Desc);
            this.Button.setToolTipText(Desc);
        }

    }
    private static JPanel T_Events(){
        JPanel Content = new JPanel(new GridLayout(1, 2));
        JPanel Column1 = new JPanel(new GridLayout(0, 1));
        JPanel Column2 = new JPanel(new GridLayout(0, 1));

        Column2.setBackground(Color.LIGHT_GRAY);

        Map<String,String> Descs = new HashMap<>();

        Field[] Fields = Events.class.getDeclaredFields();
        for(Field F : Fields) {
            if (Modifier.isStatic(F.getModifiers())) {
                F.setAccessible(true);

                boolean ThatString = F.getType().equals(String.class);

                if (ThatString) {
                    String Desc = "err?";

                    try {
                        Desc = (String) F.get(null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    Descs.put(F.getName().replace("_Desc", ""), Desc);
                }
            }
        }

        int WhatColumn = 0;
        boolean Eval = false;
        Method[] Methods = Events.class.getDeclaredMethods();
        for(Method M : Methods){
            if(Modifier.isStatic(M.getModifiers())){
                M.setAccessible(true);

                EventMethod EM = new EventMethod(M);

                switch (WhatColumn){
                    case 0: Eval = Column1.getComponentCount() % 2 == 0; break;
                    case 1: Eval = Column2.getComponentCount() % 2 == 0; break;
                }

                JPanel Result = CreateEvalPanel(Eval);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weighty = 1;
                gbc.gridy = 0;

                JLabel Name = new JLabel(M.getName());
                Name.setOpaque(false);
                JButton Button = new JButton("INVOKE");
                Button.setOpaque(false);

                gbc.weightx = 0.75;
                gbc.gridx = 0;
                Result.add(Name  , gbc);
                gbc.weightx = 0.25;
                gbc.gridx = 1;
                Result.add(Button, gbc);

                EM.Text   = Name  ;
                EM.Button = Button;

                Button.addActionListener((e) -> {
                    try{
                        M.invoke(null);
                    } catch (InvocationTargetException | IllegalAccessException ex) {
                        ex.printStackTrace();
                    }
                });

                EventMethods.add(EM);

                switch (WhatColumn){
                    case 0: Column1.add(Result); break;
                    case 1: Column2.add(Result); break;
                }

                WhatColumn++;
                if(WhatColumn==2){ WhatColumn = 0; }
            }
        }

        for(Map.Entry<String,String> e : Descs.entrySet()){
            String MethodName = e.getKey();
            String Desc       = e.getValue();

            for(EventMethod EM : EventMethods){
                if(EM.Method.getName().equals(MethodName)){
                    EM.AddDesc(Desc);
                    break;
                }
            }
        }

        for(EventMethod EM : EventMethods){
            if(!EM.HasDesc){
                EM.AddDesc("Описание отсутствует!");
            }
        }

        int Max = Math.max(Column1.getComponentCount(), Column2.getComponentCount());
        if(Column1.getComponentCount() != Max){ Column1.add(CreateEvalPanel((Column1.getComponentCount()) % 2 == 0 )); }
        if(Column2.getComponentCount() != Max){ Column2.add(CreateEvalPanel((Column2.getComponentCount()) % 2 == 0 )); }

        Content.add(Column1);
        Content.add(Column2);
        return Content;
    }

    /* ============================================= */

    protected static class ValueProgressbarInfo {
        private final JProgressBar Bar;
        private int Value = 0;

        private ValueProgressbarInfo(JProgressBar Bar){ this.Bar = Bar; }

        protected void SetValue(int Value){
            this.Value = Value;
            Bar.setValue(Value);
            Bar.setString(String.valueOf(Value));
            Bar.setToolTipText(Value + " — " + (((float)Value / GTH_Value.MAX) * 100) + "%");
        }

        public int GetValue(){ return Value; }
    }
    protected static class ButtonMouseAdapter extends MouseAdapter {
        private Timer timer;
        private final JButton button;

        public ButtonMouseAdapter(JButton button) {
            this.button = button;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            timer = new Timer(100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    button.doClick();
                }
            });
            timer.start();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (timer != null) {
                timer.stop();
            }
        }
    }
    private static JPanel E_CreateValueProgressbar(GTH_Value GTH_V){
        JPanel Result = new JPanel();
        Result.setLayout(new BorderLayout());
        Result.setBorder(new EmptyBorder(0,0,0,0));

        JPanel Name = new JPanel(new BorderLayout());
        Name.setBorder(new EmptyBorder(0,0,0,5));
        JLabel NameText = new JLabel("[" + GTH_V.Code + "] " + GTH_V.Name);
        NameText.setPreferredSize(new Dimension(WindowSize.x / 6, NameText.getPreferredSize().height));
        NameText.setToolTipText(GTH_V.Name + " — " + GTH_V.Desc);
        Name  .add(NameText);
        Result.add(Name    ,BorderLayout.WEST);

        JProgressBar Bar = new JProgressBar(-GTH_Value.MAX, GTH_Value.MAX);
        Bar.setValue(0);
        Bar.setStringPainted(true);
        Bar.setForeground(GTH_V.Color);
        Bar.setString("?");
        Bar.setPreferredSize(new Dimension(WindowSize.x / 5, 15));

        ValueProgressBars.put(GTH_V, new ValueProgressbarInfo(Bar));

        Result.add(Bar, BorderLayout.CENTER);

        JPanel Buttons  = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        JButton B_Add = new JButton("+");
        JButton B_Mul = new JButton("*");
        JButton B_Set = new JButton("S");

        Dimension ButtonSize = new Dimension(20,40);
        B_Add.setPreferredSize(ButtonSize);
        B_Mul.setPreferredSize(ButtonSize);
        B_Set.setPreferredSize(ButtonSize);

        B_Add.setMargin(new Insets(0,0,0,0));
        B_Mul.setMargin(new Insets(0,0,0,0));
        B_Set.setMargin(new Insets(0,0,0,0));

        Font F = new Font("Arial", Font.PLAIN, 15);
        B_Add.setFont(F);
        B_Mul.setFont(F);
        B_Set.setFont(F);

        B_Add.addActionListener(e -> {
            GTH_V.AddValue(NumberValueRange);
        });
        B_Mul.addActionListener(e -> {
            GTH_V.MulValue(NumberValueRange);
        });
        B_Set.addActionListener(e -> {
            GTH_V.SetValue(NumberValueRange);
        });

        B_Add.addMouseListener(new ButtonMouseAdapter(B_Add));
        B_Mul.addMouseListener(new ButtonMouseAdapter(B_Mul));
        B_Set.addMouseListener(new ButtonMouseAdapter(B_Set));

        Buttons.add(B_Add);
        Buttons.add(B_Mul);
        Buttons.add(B_Set);

        Result.add(Buttons, BorderLayout.EAST);

        return Result;
    }

    private static Color getContrastColor(Color color) {
        double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
        return luminance > 0.5 ? Color.BLACK : Color.WHITE;
    }
}
