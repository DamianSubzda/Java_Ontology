
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
    private OWLOntology owlOntology = null;
    private OWLOntology owlOntology_new = null;

    public static void main(String[] args) {
        new main();
    }

    main() {
        OWLOntologyManager ontologyManager = OWLManager.createOWLOntologyManager();
        try {
            File file = new File("zywnosc.owl");
            owlOntology = ontologyManager.loadOntologyFromOntologyDocument(file);
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }
        getWitaminy(owlOntology);

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

        //Add to ontology
        OK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNewIndiviual(ontologyManager, owlOntology);
            }
        });
    }

    void getWitaminy(OWLOntology owlOntology) {
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

    void createNewIndiviual(OWLOntologyManager ontologyManager, OWLOntology owlOntology) {
        OWLDataFactory owlDataFactory = ontologyManager.getOWLDataFactory();

        OWLIndividual newIndividual = owlDataFactory.getOWLNamedIndividual(IRI.create(prefix + tf_nazwa.getText()));

        //add individual to class
        OWLClass jedzenieClass = owlDataFactory.getOWLClass(IRI.create(prefix + "Jedzenie"));
        OWLClassAssertionAxiom axiom = owlDataFactory.getOWLClassAssertionAxiom(jedzenieClass, newIndividual);
        AddAxiom addAxiom = new AddAxiom(owlOntology, axiom);
        ontologyManager.applyChange(addAxiom);

        try { //add wegle objectproperty
            OWLObjectProperty posiadaWeglowodany = owlDataFactory.getOWLObjectProperty(IRI.create(prefix + "posiada_weglowodany"));
            OWLIndividual wegleInd = owlDataFactory.getOWLNamedIndividual(IRI.create(prefix + wegle.getSelection().getActionCommand()));
            OWLObjectPropertyAssertionAxiom weglowodanyAxiom =
                    owlDataFactory.getOWLObjectPropertyAssertionAxiom(posiadaWeglowodany, newIndividual, wegleInd);
            AddAxiom addWeglowodanyAxiom = new AddAxiom(owlOntology, weglowodanyAxiom);
            ontologyManager.applyChange(addWeglowodanyAxiom);
        } catch (NullPointerException e) {
        }

        try { // add tłuszcze objectproperty
            OWLObjectProperty posiadaTluszcze = owlDataFactory.getOWLObjectProperty(IRI.create(prefix + "posiada_tłuszcz"));
            OWLIndividual tluszczeInd = owlDataFactory.getOWLNamedIndividual(IRI.create(prefix + tluszcze.getSelection().getActionCommand()));
            OWLObjectPropertyAssertionAxiom tluszczeAxiom =
                    owlDataFactory.getOWLObjectPropertyAssertionAxiom(posiadaTluszcze, newIndividual, tluszczeInd);
            AddAxiom addTluszczeAxiom = new AddAxiom(owlOntology, tluszczeAxiom);
            ontologyManager.applyChange(addTluszczeAxiom);
        } catch (NullPointerException e) {
        }

        // add witaminy objectproperty
        OWLObjectProperty posiadaWitaminy = owlDataFactory.getOWLObjectProperty(IRI.create(prefix + "posiada_witaminy"));
        witaminyList.getSelectedValuesList().forEach((elem) ->{
            OWLIndividual witaminyInd = owlDataFactory.getOWLNamedIndividual(IRI.create(prefix + elem.toString()));
            OWLObjectPropertyAssertionAxiom witaminyAxiom =
                    owlDataFactory.getOWLObjectPropertyAssertionAxiom(posiadaWitaminy, newIndividual, witaminyInd);
            AddAxiom addWitaminyAxiom = new AddAxiom(owlOntology, witaminyAxiom);
            ontologyManager.applyChange(addWitaminyAxiom);
        });

        // add bialko dataproperty
        OWLDataProperty posiadaBialko = owlDataFactory.getOWLDataProperty(IRI.create(prefix + "posiada_białko"));
        OWLDataPropertyAssertionAxiom posiadaBialkoAxiom =
                owlDataFactory.getOWLDataPropertyAssertionAxiom(posiadaBialko, newIndividual, cb_bialko.isSelected());
        AddAxiom addPosiadaBialko = new AddAxiom(owlOntology, posiadaBialkoAxiom);
        ontologyManager.applyChange(addPosiadaBialko);

        // add cukier dataproperty
        OWLDataProperty posiadaCukier = owlDataFactory.getOWLDataProperty(IRI.create(prefix + "posiada_cukier"));
        OWLDataPropertyAssertionAxiom posiadaCukierAxiom =
                owlDataFactory.getOWLDataPropertyAssertionAxiom(posiadaCukier, newIndividual, cb_cukier.isSelected());
        AddAxiom addPosiadaCukier = new AddAxiom(owlOntology, posiadaCukierAxiom);
        ontologyManager.applyChange(addPosiadaCukier);

        // add kalorie dataproperty
        if(isNumeric(tf_kalorie.getText())){
            OWLDataProperty posiadaKalorii = owlDataFactory.getOWLDataProperty(IRI.create(prefix + "posiada_ilość_kalorii"));
            OWLDataPropertyAssertionAxiom posiadaKaloriiAxiom =
                    owlDataFactory.getOWLDataPropertyAssertionAxiom(posiadaKalorii, newIndividual, Integer.parseInt(tf_kalorie.getText()));
            AddAxiom addPosiadaKalorii = new AddAxiom(owlOntology, posiadaKaloriiAxiom);
            ontologyManager.applyChange(addPosiadaKalorii);
        }


        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File("zywnosc_new.owl"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ontologyManager.saveOntology(owlOntology, outputStream);
        } catch (OWLOntologyStorageException e) {
            e.printStackTrace();
        }

        OWLOntologyManager ontologyManager_new = OWLManager.createOWLOntologyManager();
        try {
            File file = new File("zywnosc_new.owl");
            owlOntology_new = ontologyManager_new.loadOntologyFromOntologyDocument(file);
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }

        OWLReasoner owlReasoner = new Reasoner.ReasonerFactory().createReasoner(owlOntology_new);
        OWLIndividual addedInd = owlDataFactory.getOWLNamedIndividual(IRI.create(prefix+ tf_nazwa.getText()));
        NodeSet<OWLNamedIndividual> instances = owlReasoner.getInstances(owlDataFactory.getOWLClass(
                IRI.create(prefix + "Jedzenie")), true);

        for(OWLNamedIndividual instance: instances.getFlattened()){
            System.out.println(instance.getIRI());
        }

        
    }

}
