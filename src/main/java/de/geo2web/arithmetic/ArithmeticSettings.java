package de.geo2web.arithmetic;

public class ArithmeticSettings {

    private ArithmeticSettings(){}

    private static ArithmeticSettings instance;

    public static ArithmeticSettings Instance(){
        if (instance == null){
            instance = new ArithmeticSettings();
        }
        return instance;
    }

    public static char[] ALLOWED_CHARS_TO_CHANGE =
            {',','(',')','{','}','[',']',';','|'};

    public char Argument_Separator = ',';

    public char Open_Parentheses = '(';
    public char Close_Parentheses = ')';

    public char Open_Vector = '{';
    public char Close_Vector = '}';

    public char Open_Index = '[';
    public char Close_Index = ']';

}
