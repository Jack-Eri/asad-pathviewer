package pathviewer.visualization;

import pathviewer.data.Data;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ControlPanel extends JPanel 
{
    private final Data data;
    private SelectionType selectionType;
    private String algorithm;
    
    private JComboBox<String> selector;
    private JComboBox<String> algorithmSelector;
    private JSpinner spinnerWidth;
    private JSpinner spinnerHeight;
    private JTextField identifierField;

    public ControlPanel(ApplicationFrame parent, Data data, int width)
    {
        this.data = data;
        this.selectionType = SelectionType.START;

        setBorder(new LineBorder(Color.gray));
        setLayout(null);

        Label selectionLabel = new Label("Selection type:");
        selectionLabel.setBounds(7, 10, width - 20, 25);
        add(selectionLabel);

        selector = new JComboBox<>();
        
        selector.addItem("Start");
        selector.addItem("End");
        selector.addItem("Cost");
        selector.addItem("Zoom In");
        selector.addItem("Zoom Out");
        selector.addItem("Change Cell to Field");
        selector.addItem("Change Field to Cell");
        selector.setBounds(10, 35, width - 20, 30);
        
        selector.addActionListener((ActionEvent e) -> {
            selectionType = SelectionType.values()[selector.getSelectedIndex()];
        });
        
        add(selector);
        
        Label spinnerLabel = new Label("New Field Size (Width, Height) :");
        spinnerLabel.setBounds(7, 180, width - 20, 25);
        add(spinnerLabel);
        
        SpinnerModel spinnerModelWidth = new SpinnerNumberModel(10, 1, 20, 1);
        SpinnerModel spinnerModelHeight = new SpinnerNumberModel(10, 1, 20, 1);
        
        spinnerWidth = new JSpinner(spinnerModelWidth);
        spinnerWidth.setBounds(7, 210, width - 20, 25);
        spinnerHeight = new JSpinner(spinnerModelHeight);
        spinnerHeight.setBounds(7, 240, width - 20, 25);
        
        add(spinnerWidth);
        add(spinnerHeight);


        // Field
        Label fieldLabel = new Label("Field Identifier");
        fieldLabel.setBounds(7, 300, width - 20, 25);
        add(fieldLabel);

        identifierField = new JTextField();
        identifierField.setBounds(10, 330, width - 20, 25);
        add(identifierField);

        JButton createButton = new JButton("CREATE");
        JButton loadButton = new JButton("LOAD");
        JButton deleteButton = new JButton("DELETE CURRENT FIELD");

        createButton.addActionListener(e -> {
            data.createField(identifierField.getText(), GetFieldWidth(), GetFieldHeight());
            parent.Redraw();
        });
        loadButton.addActionListener(e -> {
            data.fetchField(identifierField.getText());
            parent.Redraw();
        });
        deleteButton.addActionListener(e -> {
            data.deleteField();
            parent.Redraw();
        });

        createButton.setBounds(10, 360, (width - 20) / 2, 25);
        loadButton.setBounds(width / 2 + 10, 360, (width - 20) / 2, 25);
        deleteButton.setBounds(10, 400, width - 20, 25);

        add(createButton);
        add(loadButton);
        add(deleteButton);

        // Algorithm selector
        Label algorithmLabel = new Label("Algorithm Selection");
        algorithmLabel.setBounds(10, 460, width - 20, 30);
        add(algorithmLabel);

        algorithmSelector = new JComboBox<>();

        algorithmSelector.addItem("ASTAR");
        algorithmSelector.addItem("ASTAR_DIAG");
        algorithmSelector.setBounds(10, 490, width - 20, 30);

        algorithmSelector.addActionListener((ActionEvent e) -> {
            data.setAlgorithm((String) algorithmSelector.getSelectedItem());
            System.out.println(data.getAlgorithm());
        });

        add(algorithmSelector);
    }
    
    public int GetFieldWidth()
    {
    	return (int)spinnerWidth.getValue();
    }
    
    public int GetFieldHeight()
    {
    	return (int)spinnerHeight.getValue();
    }

    public enum SelectionType
    {
        START, END, COST, ZOOM_IN, ZOOM_OUT, CHANGE_CELL_TO_FIELD, CHANGE_FIELD_TO_CELL
    }
    
    public SelectionType getSelectionType() { return selectionType; }
}
