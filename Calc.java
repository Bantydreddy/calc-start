import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calc extends JFrame implements ActionListener{

     //attributes
     private Container container;
     private JPanel jplView, buttons;
     private JTextField display;
     private JButton btn1, btn2 ,btn3, btn4, btn5, btn6, btn7, btn8,
      btn9, btn10, btn11, btn12, btn13, btn14, btn15, btn16;
     private int firstValue;
     private int secondValue;
     private String operator;

    //Main method
    public static void main (String[] args){
        Calc calc= new Calc();
        calc.setTitle("Calculator test1");
        calc.setSize(400,400);
        calc.setVisible(true);
        calc.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    //CONSTUCTOR
    public Calc(){
        createComponents();
        createContainer();
        addComponents();
    }

    private void createComponents(){
        display= new JTextField("0");
        jplView =new JPanel();
        btn1=new JButton("9");
        btn2=new JButton("8");
        btn3=new JButton("7");
        btn4=new JButton("/");

        btn5=new JButton("6");
        btn6=new JButton("5");
        btn7=new JButton("4");
        btn8=new JButton("*");

        btn9=new JButton("3");
        btn10=new JButton("2");
        btn11=new JButton("1");
        btn12=new JButton("+");

        btn13=new JButton("0");
        btn14=new JButton("C");
        btn15=new JButton("=");
        btn16=new JButton("-");
        buttons=new JPanel();

        JButton[] btns= {
            btn1,btn2,btn3,btn4,
            btn5,btn6,btn7,btn8,
            btn9,btn10,btn11,btn12,
            btn13,btn14,btn15,btn16,
        };
        for(JButton btn : btns){
            btn.addActionListener(this);
        }

        buttons.setLayout(new GridLayout(4,4,4,4));
        buttons.setBackground(Color.WHITE);
        buttons.setForeground(Color.BLUE);
        buttons.setFont(new Font("Arial",Font.BOLD, 50));
        
        display.setBackground(Color.WHITE);
        display.setFont(new Font("Arial",Font.BOLD, 60));
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.LEFT);

        //display.setBorder(BorderFactory.createEmptyBorder(40,20,40,20));
        display.setBorder(
            BorderFactory.createCompoundBorder(   
                BorderFactory.createEmptyBorder(15,10,20,10),//outside
                BorderFactory.createLineBorder(Color.GRAY, 1)//inside
            )
        );
        
        jplView.setLayout(new BorderLayout());
        jplView.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    }

    private void createContainer(){
        container=getContentPane();
    }

    private void addComponents(){
        
        jplView.add(display, BorderLayout.NORTH);
        jplView.add(buttons, BorderLayout.CENTER);
        
        buttons.add(btn1);
        buttons.add(btn2);
        buttons.add(btn3);
        buttons.add(btn4);

        buttons.add(btn5);
        buttons.add(btn6);
        buttons.add(btn7);
        buttons.add(btn8);

        buttons.add(btn9);
        buttons.add(btn10);
        buttons.add(btn11);
        buttons.add(btn12);

        buttons.add(btn13);
        buttons.add(btn14);
        buttons.add(btn15);
        buttons.add(btn16);

        container.add(jplView);
    }

    @Override
    public void actionPerformed(ActionEvent e){

        /*u can also use
        if(e.getSource() instanceOf JButton)
        {
        JButton clicked=(JButton) e.getSource();
        } */

        JButton clicked=(JButton) e.getSource();
        String text=clicked.getText();

        if(text.matches ("[0-9]")){
            if(display.getText().equals("0")){
                display.setText(text);
            }
            else{
                display.setText(display.getText()+ text);
            }
        }

        else if (text.equals("+") || equals("-") || equals("/") || equals("*")){
            firstValue= Integer.parseInt(display.getText());
            operator=text;
            display.setText("");
            //display.setText(display.getText()+ text);
            //display.setText(display.getText() + operator);//I need to separate valeu from operator
        }

        else if (text.equals("=")){
            secondValue= Integer.parseInt(display.getText());
            int result=0;

            if (operator.equals("+")){
                result=firstValue+secondValue;

            }

            else if (operator.equals("-")){
                result=firstValue-secondValue;

            }

             else if (operator.equals("/")){
                result=firstValue/secondValue;
            }

             else if (operator.equals("*")){
                result=firstValue*secondValue;
            }

            display.setText(""+ result);
            
        }

        else if(text.equals("C")){
            display.setText("0");
        }
      
    }

} 