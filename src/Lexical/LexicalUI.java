package Lexical;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class LexicalUI extends JFrame {
    private JPanel mainPanel;
    private JTextArea TXTcode;
    private JButton BTNsubmitCode;

    public ArrayList<Character> currentLineCharacters = new ArrayList<>();
    public ArrayList<String> preAnalyzeExport = new ArrayList<>();
    public ArrayList<String> words = new ArrayList<>();
    public ArrayList<String[]> lexicalAnalyzeExport = new ArrayList<>();

    public int lineCounter = 0;

    LexicalUI() {
        add(mainPanel);
        setVisible(true);
        setSize(600, 400);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);


        BTNsubmitCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser chooser = new JFileChooser();
                int result = chooser.showOpenDialog(null);
                File file = chooser.getSelectedFile();

                try {
                    FileReader reader;
                    reader = new FileReader(file);
                    int i = reader.read();
                    while (i != -1) {
                        char ch = (char) i;
                        preAnalyze(ch);

                        if ((Character.compare(ch, '\n')) == 0) {
                            lineCounter++;
                        }
                        i = reader.read();
                    }

                    reader.close();

                    String tmp = "";
                    for (char a : currentLineCharacters) {
                        tmp = tmp + a;
                    }
                    preAnalyzeExport.add(tmp);
                    currentLineCharacters.clear();

                } catch (Exception exp) {
                    JOptionPane.showMessageDialog(null, "صحت فایل انتخاب شده را بررسی نمایید");
                    System.err.println(exp);
                }

                System.out.println("\n \n Phase1 : ---------------");

                String export = "";
                for (String tmp : preAnalyzeExport) {
                    export = export + tmp;
                }
                System.out.println(export);
                System.out.println("\n \n Phase2 : ---------------");
                lexicalAnalyze();
                for (String[] tmp : lexicalAnalyzeExport) {
                    System.out.println(tmp[0] + " :: " + tmp[1]);
                }
            }
        });
    }

    public static void main(String[] args) {
        new LexicalUI();
    }

    void preAnalyze(char currentChar) {
        currentLineCharacters.add(currentChar);

        switch (currentLineCharacters.get(0)) {
            case '#':
                if ((Character.compare(currentChar, '\n')) == 0) {
                    currentLineCharacters.clear();
                }
                break;
            case '/':
                if (currentLineCharacters.size() > 2 && (Character.compare(currentLineCharacters.get(1), '/') == 0)) {
                    if ((Character.compare(currentChar, '\n')) == 0) {
                        currentLineCharacters.clear();
                    }
                }
                break;
        }
        if ((Character.compare(currentChar, '\n')) == 0) {

            String tmp = "";
            for (char a : currentLineCharacters) {
                tmp = tmp + a;
            }
            preAnalyzeExport.add(tmp);
            currentLineCharacters.clear();
        }

    }

    void lexicalAnalyze() {
        //-------------------------------------------------------------------- Lets separate the words
        //System.out.println("\nLexical Started\n");
        String tmpWord;
        for (String tmpLine : preAnalyzeExport) {

            tmpWord = "";
            char[] lineChars = new char[tmpLine.length()];
            lineChars = tmpLine.toCharArray();
            for (char tmpChar : lineChars) {

                if (tmpWord.length() > 0) {
                    if (isLetter(tmpWord.charAt(0))) {
                        //System.err.print(tmpChar);
                        if (!(isLetter(tmpChar))) {
                            if (isKeyword(tmpWord)) {
                                lexicalAnalyzeExport.add(new String[]{"keyword", tmpWord});
                            } else {
                                lexicalAnalyzeExport.add(new String[]{"identifier", tmpWord});
                                //System.err.println("id added");
                            }
                            tmpWord = "";
                        }

                    } else {
                        if (isOperator(tmpWord.charAt(0))){

                            lexicalAnalyzeExport.add(new String[]{"operator", tmpWord});
                            tmpWord = "";

                            /*
                            String lastWord=lexicalAnalyzeExport.get(lexicalAnalyzeExport.size()-1)[1];
                            if(lastWord.equals(tmpWord) ){
                                lexicalAnalyzeExport.remove(lexicalAnalyzeExport.size()-1);
                                lexicalAnalyzeExport.add(new String[]{"operator", lastWord+tmpWord});

                            }else {
                                lexicalAnalyzeExport.add(new String[]{"operator", tmpWord});
                                tmpWord = "";

                            }
                            */

                        }else {
                            tmpWord = "";
                        }
                    }
                }
                tmpWord = tmpWord + tmpChar;

            }

        }
    }


    //-------------------------------------------------------------------- Useful part, but not the main part
    boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    boolean isNumber(char c) {
        return (c >= '0' && c <= '9');
    }

    boolean isOperator(char c) {

        if (Character.compare(c, ')') == 0
                || Character.compare(c, '(') == 0
                || Character.compare(c, '}') == 0
                || Character.compare(c, '{') == 0
                || Character.compare(c, '=') == 0
                || Character.compare(c, '+') == 0
                || Character.compare(c, '-') == 0
                || Character.compare(c, '*') == 0
                || Character.compare(c, '<') == 0
                || Character.compare(c, '>') == 0

        ) {
            return true;
        }

        return false;
    }

    boolean isKeyword(String word) {

        if (isLetter(word.charAt(0)) && isLetter((word.charAt(word.length() - 1)))) {
            ArrayList<String> cKeywords = new ArrayList<>();
            cKeywords.add("auto");
            cKeywords.add("double");
            cKeywords.add("int");
            cKeywords.add("struct");
            cKeywords.add("break");
            cKeywords.add("else");
            cKeywords.add("long");
            cKeywords.add("switch");
            cKeywords.add("case");
            cKeywords.add("enum");
            cKeywords.add("register");
            cKeywords.add("typedef");
            cKeywords.add("char");
            cKeywords.add("extern");
            cKeywords.add("return");
            cKeywords.add("union");
            cKeywords.add("continue");
            cKeywords.add("for");
            cKeywords.add("signed");
            cKeywords.add("void");
            cKeywords.add("do");
            cKeywords.add("if");
            cKeywords.add("static");
            cKeywords.add("while");
            cKeywords.add("default");
            cKeywords.add("goto");
            cKeywords.add("sizeof");
            cKeywords.add("volatile");
            cKeywords.add("const");
            cKeywords.add("float");
            cKeywords.add("short");
            cKeywords.add("unsigned");

            for (String tmp : cKeywords) {
                if (tmp.compareTo(word) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

}


