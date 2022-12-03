
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
    private List<String> witaminy = new ArrayList<String>();
    private String prefix = "http://www.semanticweb.org/damian/ontologies/2022/11/untitled-ontology-6#";

    public static void main(String[] args) {
        new main();
    }

    main() {
        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
        OWLOntology owlOntology = null;
        try {
            File file = new File("zywnosc.owl");
            owlOntology = ontologyManager.loadOntologyFromOntologyDocument(file);
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        getWitaminy(ontologyManager, owlOntology);

        DefaultListModel model = new DefaultListModel();
        witaminy.forEach((item) -> model.addElement(item));
        witaminyList.setModel(model);

        wegle.add(rb_wegle_p);
        wegle.add(rb_wegle_z);
        tluszcze.add(rb_tluszcze_r);
        tluszcze.add(rb_tluszcze_z);
        setTitle("Ontology");
        setContentPane(panel_main);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
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
                createNewIndiviual();
            }
        });
    }

    void getWitaminy(OWLOntologyManager ontologyManager, OWLOntology owlOntology){
        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();

        for (OWLClass cls : owlOntology.getClassesInSignature()) {

            if (cls.getIRI().getFragment().equals("Witaminy")) {
                OWLReasoner reasoner = reasonerFactory.createReasoner(owlOntology);
                NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(cls, true);

                for (OWLNamedIndividual i : instances.getFlattened()) {
                    witaminy.add(i.getIRI().getFragment());
                }
            }
        }
    }

    void createNewIndiviual(){
        
    }

}
