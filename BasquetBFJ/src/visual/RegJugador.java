package visual;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.util.List;

public class RegJugador extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtPeso;
    private JTextField txtAltura;
    private JComboBox<String> comboEquipos;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(new RegJugador());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            RegJugador dialog = new RegJugador();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RegJugador() {
        setTitle("Registrar Jugador");
        setBounds(100, 100, 500, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(230, 240, 250));

        JLabel lblId = new JLabel("ID:");
        lblId.setBounds(30, 20, 50, 20);
        lblId.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblId);

        txtId = new JTextField();
        txtId.setBounds(100, 20, 150, 25);
        contentPanel.add(txtId);
        txtId.setColumns(10);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(30, 60, 60, 20);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(100, 60, 150, 25);
        contentPanel.add(txtNombre);
        txtNombre.setColumns(10);

        JLabel lblPeso = new JLabel("Peso (kg):");
        lblPeso.setBounds(30, 100, 70, 20);
        lblPeso.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblPeso);

        txtPeso = new JTextField();
        txtPeso.setBounds(100, 100, 150, 25);
        contentPanel.add(txtPeso);
        txtPeso.setColumns(10);

        JLabel lblAltura = new JLabel("Altura (cm):");
        lblAltura.setBounds(30, 140, 80, 20);
        lblAltura.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblAltura);

        txtAltura = new JTextField();
        txtAltura.setBounds(100, 140, 150, 25);
        contentPanel.add(txtAltura);
        txtAltura.setColumns(10);

        JLabel lblEquipo = new JLabel("Equipo:");
        lblEquipo.setBounds(30, 180, 60, 20);
        lblEquipo.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblEquipo);

        comboEquipos = new JComboBox<>();
        comboEquipos.setBounds(100, 180, 200, 25);
        contentPanel.add(comboEquipos);
        
        cargarEquipos();

        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.setBackground(new Color(200, 220, 240));
        getContentPane().add(buttonPane, BorderLayout.SOUTH);

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 12));
        okButton.setBackground(new Color(34, 139, 34));
        okButton.setForeground(Color.WHITE);
        okButton.setActionCommand("OK");
        buttonPane.add(okButton);
        getRootPane().setDefaultButton(okButton);

        JButton cancelButton = new JButton("Cancelar");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 12));
        cancelButton.setBackground(new Color(178, 34, 34));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setActionCommand("Cancel");
        buttonPane.add(cancelButton);
    }
    
    private void cargarEquipos() {
        
        String[] equipos = {"Lakers", "Bulls", "Celtics", "Heat", "Warriors"};
        for (String equipo : equipos) {
            comboEquipos.addItem(equipo);
        }
    }
}
