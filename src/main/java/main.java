import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class main extends JFrame {

    private JLabel label_top;
    private JTextField tf_nazwa;
    private JRadioButton rb_wegle_p;
    private JRadioButton rb_wegle_z;
    private JRadioButton rb_tluszcze_r;
    private JRadioButton rb_tluszcze_z;
    private JButton Clear;
    private JButton OK;
    private JCheckBox cb_cukier;
    private JList witaminyList;
    private JPanel panel_main;
    private JCheckBox cb_bialko;
    private JTextField tf_kalorie;
    private ButtonGroup wegle = new ButtonGroup();
    private ButtonGroup tluszcze = new ButtonGroup();

    public static void main(String [] args){
        main m1 = new main();
    }

    main(){
        wegle.add(rb_wegle_p);
        wegle.add(rb_wegle_z);
        tluszcze.add(rb_tluszcze_r);
        tluszcze.add(rb_tluszcze_z);
        setTitle("Ontology");
        setContentPane(panel_main);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);;
        pack();
        setVisible(true);

        //Clear components
        Clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tf_nazwa.setText("");
                tf_kalorie.setText("");
                wegle.clearSelection();
                tluszcze.clearSelection();
                witaminyList.clearSelection();
                cb_cukier.setSelected(false);
                cb_bialko.setSelected(false);
            }
        });

        //Start reasoner
        OK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

}
