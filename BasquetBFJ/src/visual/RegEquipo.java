package visual;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Dialog.ModalityType;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;

public class RegEquipo extends JDialog {

    private final JPanel contentPanel = new JPanel();
    private JTextField textField;
    private JTextField textField_1;
    private JLabel lblColorSeleccionado;
    private Color colorSeleccionado;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(new RegEquipo());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            RegEquipo dialog = new RegEquipo();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public RegEquipo() {
    	setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        setTitle("Agregar Equipo");
        setBounds(100, 100, 500, 250);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);
        contentPanel.setBackground(new Color(230, 240, 250));

        JLabel lblNewLabel = new JLabel("ID:");
        lblNewLabel.setBounds(30, 20, 50, 20);
        lblNewLabel.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Nombre:");
        lblNewLabel_1.setBounds(250, 20, 60, 20);
        lblNewLabel_1.setFont(new Font("Arial", Font.BOLD, 12));
        contentPanel.add(lblNewLabel_1);

        textField = new JTextField();
        textField.setBounds(70, 20, 150, 25);
        contentPanel.add(textField);
        textField.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(310, 20, 150, 25);
        contentPanel.add(textField_1);

        JButton btnSeleccionarColor = new JButton("Seleccionar Color");
        btnSeleccionarColor.setBounds(50, 80, 170, 30);
        btnSeleccionarColor.setFont(new Font("Arial", Font.BOLD, 12));
        btnSeleccionarColor.setBackground(new Color(100, 149, 237));
        btnSeleccionarColor.setForeground(Color.WHITE);
        contentPanel.add(btnSeleccionarColor);

        lblColorSeleccionado = new JLabel(" ");
        lblColorSeleccionado.setBounds(250, 80, 60, 30);
        lblColorSeleccionado.setOpaque(true);
        lblColorSeleccionado.setBackground(Color.WHITE);
        lblColorSeleccionado.setBorder(new LineBorder(Color.WHITE));
        contentPanel.add(lblColorSeleccionado);

        btnSeleccionarColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                colorSeleccionado = JColorChooser.showDialog(null, "Elige un color", Color.WHITE);
                if (colorSeleccionado != null) {
                    lblColorSeleccionado.setBackground(colorSeleccionado);
                }
            }
        });

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
}
