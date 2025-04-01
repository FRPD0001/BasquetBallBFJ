package visual;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Jugadores extends JDialog {

    private final JPanel contentPanel = new JPanel();

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            SwingUtilities.updateComponentTreeUI(new Jugadores());
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Jugadores dialog = new Jugadores();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Jugadores() {
        setTitle("Jugadores");
        setBounds(100, 100, 559, 266);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setBackground(new Color(230, 240, 250));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        JButton btnAgregar = new JButton("Agregar otro");
        btnAgregar.setBounds(23, 54, 159, 73);
        btnAgregar.setFont(new Font("Arial", Font.BOLD, 12));
        btnAgregar.setBackground(new Color(100, 149, 237));
        btnAgregar.setForeground(Color.WHITE);
        contentPanel.add(btnAgregar);

        JButton btnRegistrar = new JButton("Listar");
        btnRegistrar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnRegistrar.setBounds(301, 52, 159, 76);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 12));
        btnRegistrar.setBackground(new Color(100, 149, 237));
        btnRegistrar.setForeground(Color.WHITE);
        contentPanel.add(btnRegistrar);

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