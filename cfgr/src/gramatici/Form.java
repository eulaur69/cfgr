package gramatici;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class Form extends JFrame{
    private JPanel ContentPanel;
    private JPanel InsidePanel;
    private JLabel Nonterminals;
    private JTextField NonterminalsField;
    private JLabel ClosingNonterminals;
    private JTextField TerminalsField;
    private JTextField ProductionField;
    private JButton ListaBtn;
    private JButton TabelBtn;
    private JButton VectorBtn;
    private JButton genereazaGramaticaButton;
    private JPanel Definition;
    private JPanel DisplayGrammar;
    private JPanel ResultPanel;
    private JLabel VectorLabelNonterminals;
    private JLabel VectorLabelTerminals;
    private JLabel VectorLabelProductions;
    public List<String> nonTerminale;
    public List<String> terminale;
    public List<Productie> productii;
    boolean gramaticaGenerata = false; //doar pentru primul run, dupa nu mai conteaza, totusi SUBJECT TO CHANGE

    public Form() {

///////////////////EVENIMENT LA CLICK PE BUTONUL GENEREAZA GRAMATICA///////////////////////////
        genereazaGramaticaButton.addActionListener(e -> {
            VectorLabelNonterminals.setText("");
            VectorLabelTerminals.setText("");
            VectorLabelProductions.setText("");
            productii = new ArrayList<>();
            String stringBuffer = NonterminalsField.getText();
            String[] arrayBuffer = stringBuffer.split(",");
            nonTerminale = Arrays.asList(arrayBuffer);
            stringBuffer = TerminalsField.getText();
            arrayBuffer = stringBuffer.split(",");
            terminale = Arrays.asList(arrayBuffer);
            for(int i = 0;i<nonTerminale.size();i++){
                nonTerminale.set(i,nonTerminale.get(i).trim());
            }
            //input filtering : nonterminalele sa fie doar un caracter
            for(String nonT : nonTerminale){
                if(nonT.length() != 1){
                    //JOptionPane.showMessageDialog(null,"Simbolurile non-terminale nu sunt definite corect.");
                    JOptionPane.showMessageDialog(null,"Non-terminal symbols are not correctly defined.");

                    return;
                }
                if(terminale.contains(nonT)){
                    //JOptionPane.showMessageDialog(null,"Un simbol non-terminal nu poate sa fie si terminal.");
                    JOptionPane.showMessageDialog(null,"A non-terminal symbol cannot also be a terminal symbol.");
                    return;
                }
            }
            for(int i = 0;i<terminale.size();i++){
                terminale.set(i,terminale.get(i).trim());
            }
            //input filtering : terminalele sa fie doar un caracter
            for(String term : terminale){
                if(term.length() != 1){
                    //JOptionPane.showMessageDialog(null,"Simbolurile terminale nu sunt definite corect.");
                    JOptionPane.showMessageDialog(null,"Terminal symbols are not correctly defined.");
                    return;
                }
                if(nonTerminale.contains(term)){
                    //JOptionPane.showMessageDialog(null,"Un simbol terminal nu poate sa fie si non-terminal.");
                    JOptionPane.showMessageDialog(null,"A terminal symbol cannot also be a non-terminal symbol.");

                    return;
                }
            }
            String productionLHS;
            String productionRHS;
            stringBuffer = ProductionField.getText();
            stringBuffer = stringBuffer.trim();
            //input filtering : daca productionfield e gol
            if("".equals(stringBuffer)){
                //JOptionPane.showMessageDialog(null,"Introduceti productiile.");
                JOptionPane.showMessageDialog(null,"Please insert the production rules.");
                return;
            }
            arrayBuffer = stringBuffer.split(",");
            int nrCasute = 0;
            for(String production:arrayBuffer) {
                Productie productionBuffer = new Productie();
                stringBuffer = production.trim();
                //input filtering : trebuie sa exista lhs si rhs
                String[] checker = stringBuffer.split("->");
                if(checker.length!=2){
                    //JOptionPane.showMessageDialog(null,"Productiile nu au fost scrise corect.");
                    JOptionPane.showMessageDialog(null,"The production rules were not correctly defined.");
                    return;
                }
                productionLHS = stringBuffer.split("->")[0];
                productionLHS = productionLHS.trim(); //here if it causes error
                productionRHS = stringBuffer.split("->")[1];
                productionRHS = productionRHS.trim(); //here if it causes error
                /*if(productionRHS.contains(" ")){
                    System.out.println("CONTINE SPATIU");
                }*/
                //input filtering : lhs sa fie doar un caracter, si sa fie in lista non-terminalelor
                if(productionLHS.length() != 1 || !nonTerminale.contains(productionLHS)){
                    System.out.println("LHS");
                    //JOptionPane.showMessageDialog(null,"Una dintre partile stangi ale unei productii este incorecta.");
                    JOptionPane.showMessageDialog(null,"One of the left-hand sides of the production rules is not correctly defined.");

                    return;
                }
                //input filtering : elementele din rhs sa fie in lista terminalelor sau non-terminalelor
                for(int i = 0;i<productionRHS.length();i++){
                    char tmp = productionRHS.charAt(i);
                    String currentElement = Character.toString(tmp);
                    if(!(terminale.contains(currentElement) || nonTerminale.contains(currentElement))){
                        //JOptionPane.showMessageDialog(null,"Una dintre partile drepte ale unei productii este incorecta.");
                        JOptionPane.showMessageDialog(null,"One of the right-hand sides of the production rules is not correctly defined.");

                        //System.out.println("AICI : " + currentElement);
                        return;
                    }
                }
                productionBuffer.lhs = productionLHS;
                productionBuffer.rhs = productionRHS;

                for(int i = 0;i<productionBuffer.rhs.length();i++) {
                    Casuta casutaBuffer = new Casuta();
                    nrCasute++;
                    casutaBuffer.a = productionBuffer.rhs.charAt(i);
                    casutaBuffer.index = nrCasute;
                    productionBuffer.casute.add(casutaBuffer);
                }
                productii.add(productionBuffer);
            }
            //pentru b
            for(Productie prod:productii) {
                for (Casuta casuta : prod.casute ) {
                    if (terminale.contains(String.valueOf(casuta.a))) {
                        casuta.b = 0;
                    }else {
                        for(Productie prod1:productii) {
                            if (prod1.lhs.equals(String.valueOf(casuta.a))) {
                                casuta.b = prod1.casute.get(0).index;
                                break;
                            }
                        }
                    }
                }
            }

            //pentru c
            for(int i = 0;i<productii.size()-1;i++){
                //pentru primul element din rhs
                if(!productii.get(i).lhs.equals(productii.get(i+1).lhs)){
                    productii.get(i).casute.get(0).c = 0;
                } else {
                    productii.get(i).casute.get(0).c = productii.get(i+1).casute.get(0).index;
                }
                //pentru restul elementelor inafara de ultimul
                for(int j = 1;j<productii.get(i).casute.size()-1;j++){
                    productii.get(i).casute.get(j).c = -1;
                }
                //pentru ultimul element
                int n = productii.get(i).casute.size() - 1;
                if(n!=0){
                    if(productii.get(i).lhs.equals(productii.get(i+1).lhs)){
                        productii.get(i).casute.get(n).c = -1;
                    } else {
                        productii.get(i).casute.get(n).c = 0;
                    }
                }

            }
            int prodSize = productii.size() - 1;
            int casuteSize = productii.get(prodSize).casute.size() - 1;
            productii.get(prodSize).casute.get(casuteSize).c = 0;
            for(int i = 1;i<productii.get(prodSize).casute.size()-1;i++){
                productii.get(prodSize).casute.get(i).c = -1;
            }
            //pentru d
            for(Productie prod:productii){
                for(int i = 0;i<prod.casute.size()-1;i++){
                    prod.casute.get(i).d = prod.casute.get(i+1).index;
                }
                prod.casute.get(prod.casute.size()-1).d = 0;
            }
            gramaticaGenerata = true;
            //JOptionPane.showMessageDialog(null,"Gramatica generata cu succes!");
            JOptionPane.showMessageDialog(null,"The data structures were successfully generated!");

        });

///////////////////EVENIMENT LA CLICK PE BUTONUL VECTOR///////////////////////////
        VectorBtn.addActionListener(e -> {
            if(!gramaticaGenerata){
                //JOptionPane.showMessageDialog(null,"Gramatica nu a fost generata.");
                JOptionPane.showMessageDialog(null,"The data structures failed to be generated!");
                return;
            }
            //afisare non-terminale
            StringBuilder stringBuffer = new StringBuilder();
            for (String i : nonTerminale) {
                stringBuffer.append(i.trim());
            }
            stringBuffer.append("$");
            stringBuffer.insert(0, "N : ");
            VectorLabelNonterminals.setText(stringBuffer.toString());

            //afisare terminale
            stringBuffer = new StringBuilder();
            for (String i : terminale) {
                stringBuffer.append(i.trim());
            }
            stringBuffer.append("$");
            stringBuffer.insert(0, "T : ");
            VectorLabelTerminals.setText(stringBuffer.toString());

            //afisare productii
            stringBuffer = new StringBuilder();
            final ListIterator<Productie> itr = productii.listIterator();

            for (Productie next = (itr.hasNext() ? itr.next() : null), current = null; next != null;) {
                Productie previous = current;
                current = next;
                next = itr.hasNext() ? itr.next() : null;
                if (previous == null) {
                    stringBuffer.append(current.lhs);
                    stringBuffer.append(current.rhs);
                }
                if (next != null) {
                    if (next.lhs.equals(current.lhs)) {
                        stringBuffer.append("|");
                        stringBuffer.append(next.rhs);
                    } else {
                        stringBuffer.append("#");
                        stringBuffer.append(next.lhs);
                        stringBuffer.append(next.rhs);
                    }
                } else {
                    stringBuffer.append("#$");
                }
            }

            stringBuffer.insert(0, "P : ");
            VectorLabelProductions.setText(stringBuffer.toString());
        });

///////////////////EVENIMENT LA CLICK PE BUTONUL LISTA///////////////////////////
        ListaBtn.addActionListener(e -> {
            if(!gramaticaGenerata){
                //JOptionPane.showMessageDialog(null,"Gramatica nu a fost generata.");
                JOptionPane.showMessageDialog(null,"The data structures failed to be generated!");

                return;
            }
            JPanel panel = new JPanel();
            JFrame listFrame = new JFrame();
            listFrame.setTitle("Linked lists representation");
            listFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            GridLayout gridLayout = new GridLayout(0,2);
            gridLayout.setHgap(10);
            //listFrame.setLayout(gridLayout);
            panel.setLayout(gridLayout);
            JScrollPane scrollBar=new JScrollPane(panel,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            listFrame.add(scrollBar);
            for (Productie prod : productii) {
                StringBuilder stringBuffer = new StringBuilder();
                stringBuffer.append(prod.lhs);
                stringBuffer.append("->");
                stringBuffer.append(prod.rhs);
                JPanel leftPane = new JPanel();
                leftPane.setLayout(new BorderLayout());
                leftPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                leftPane.setPreferredSize(new Dimension(80,80));
                JLabel leftPaneLabel = new JLabel();
                leftPaneLabel.setText(stringBuffer.toString());
                leftPane.add(leftPaneLabel, BorderLayout.CENTER);

                panel.add(leftPane);

                JPanel rightPane = new JPanel();
                rightPane.setLayout(new FlowLayout(3,25,50));
                rightPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                for (Casuta casuta : prod.casute) {
                    JPanel casutaWrapper = new JPanel();
                    JPanel casutaPanel = new JPanel();
                    casutaPanel.setLayout(new BorderLayout());
                    casutaWrapper.setLayout(new BorderLayout());

                    JLabel indexLabel = new JLabel("",SwingConstants.LEFT);
                    indexLabel.setText(String.valueOf(casuta.index));
                    indexLabel.setFont(new Font("Montserrat", Font.BOLD,16));
                    casutaWrapper.add(indexLabel, BorderLayout.PAGE_START);

                    JLabel aLabel = new JLabel("",SwingConstants.CENTER);
                    aLabel.setText(String.valueOf(casuta.a));
                    aLabel.setFont(new Font("Montserrat", Font.BOLD,16));
                    aLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
                    casutaPanel.add(aLabel, BorderLayout.PAGE_START);



                    JLabel bLabel = new JLabel("",SwingConstants.CENTER);
                    bLabel.setText(String.valueOf(casuta.b));
                    bLabel.setFont(new Font("Montserrat", Font.BOLD,16));
                    bLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
                    casutaPanel.add(bLabel, BorderLayout.LINE_START);

                    JLabel cLabel = new JLabel("",SwingConstants.CENTER);
                    cLabel.setText(String.valueOf(casuta.c));
                    cLabel.setFont(new Font("Montserrat", Font.BOLD,16));
                    cLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
                    casutaPanel.add(cLabel, BorderLayout.CENTER);

                    JLabel dLabel = new JLabel("",SwingConstants.CENTER);
                    dLabel.setText(String.valueOf(casuta.d));
                    dLabel.setFont(new Font("Montserrat", Font.BOLD,16));
                    dLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
                    casutaPanel.add(dLabel, BorderLayout.LINE_END);

                    casutaWrapper.add(casutaPanel, BorderLayout.PAGE_END);
                    rightPane.add(casutaWrapper);
                }
                panel.add(rightPane);
            }
            panel.setSize(1200,900);
            listFrame.setSize(1200,900);
            listFrame.setVisible(true);
        });
///////////////////EVENIMENT LA CLICK PE BUTONUL TABEL///////////////////////////
        TabelBtn.addActionListener(e -> {
            if(!gramaticaGenerata){
                //JOptionPane.showMessageDialog(null,"Gramatica nu a fost generata.");
                JOptionPane.showMessageDialog(null,"The data structures failed to be generated!");

                return;
            }
            JFrame tableFrame = new JFrame();
            tableFrame.setTitle("Table representation");
            tableFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            int i = productii.get(productii.size() - 1).casute.get(productii.get(productii.size() - 1).casute.size()-1).index;
            String[] columnNames = { "", "a", "b", "c", "d" };
            String[][] tableData = new String[i][5];

            for (Productie prod : productii) {
                for (Casuta casuta : prod.casute) {
                    i = casuta.index-1;
                    tableData[i][0] = String.valueOf(casuta.index);
                    tableData[i][1] = String.valueOf(casuta.a);
                    tableData[i][2] = String.valueOf(casuta.b);
                    tableData[i][3] = String.valueOf(casuta.c);
                    tableData[i][4] = String.valueOf(casuta.d);
                }
            }

            JTable tabel = new JTable(tableData, columnNames);
            tabel.setFont(new Font("Montserrat", Font.BOLD,16));
            JScrollPane scrollPane = new JScrollPane(tabel);

            tableFrame.add(scrollPane);

            JTableHeader tableHeader = tabel.getTableHeader();
            tableHeader.setBackground(Color.GRAY);
            tableHeader.setFont(new Font("Montserrat", Font.BOLD,20));
            tableFrame.setSize(300,420);
            tableFrame.setResizable(false);
            tableFrame.setVisible(true);
        });
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Context-free Grammars Representations");
        frame.setContentPane(new Form().ContentPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

