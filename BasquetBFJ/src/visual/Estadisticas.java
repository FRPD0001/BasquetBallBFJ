package visual;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import logico.Jugador;
import logico.StatsJugador;
import logico.SerieNacional;

public class Estadisticas extends JDialog {
    private JTextField txtId;
    private JTextField txtNombre, txtPeso, txtAltura, txtEquipo, txtLesionado;
    private JTextField txtPuntos, txtRebotes, txtAsistencias, txtTirosLibres, txtTirosCampo, txtTriples;
    private JTextField txtCoefEficiencia, txtSalario;
    private Color colorOscuro;
    private Color colorClaro;
    private JButton btnModificar;
    private Jugador jugadorActual;
    private boolean modoEdicion = false;

    public Estadisticas(Color colorOscuro, Color colorClaro) {
        this.colorOscuro = colorOscuro;
        this.colorClaro = colorClaro;
        initialize();
    }

    private void initialize() {
        setIconImage(new ImageIcon("media/LogoProyecto.png").getImage());
        setTitle("Estadísticas de Jugador");
        setSize(600, 700);
        setLocationRelativeTo(null);
        setModal(true);
        getContentPane().setBackground(colorOscuro);
        getContentPane().setLayout(new BorderLayout(5, 5));

        // Panel superior con búsqueda
        JPanel panelSuperior = crearPanelBusqueda();
        add(panelSuperior, BorderLayout.NORTH);

        // Panel central con datos
        JPanel panelDatos = crearPanelDatos();
        add(new JScrollPane(panelDatos), BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel panelInferior = crearPanelInferior();
        add(panelInferior, BorderLayout.SOUTH);
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
        panel.setBackground(colorOscuro);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("Ingrese el ID del jugador:");
        lblTitulo.setFont(new Font("Times New Roman", Font.BOLD, 16));
        lblTitulo.setForeground(Color.BLACK);
        panel.add(lblTitulo);

        txtId = new JTextField(10);
        txtId.setFont(new Font("Arial", Font.PLAIN, 14));
        txtId.setText("J-");
        txtId.setHorizontalAlignment(JTextField.CENTER);
        txtId.addActionListener(e -> buscarJugador());
        
        txtId.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (txtId.getCaretPosition() < 2 && e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });
        
        panel.add(txtId);

        return panel;
    }

    private JPanel crearPanelDatos() {
        JPanel panel = new JPanel();
        panel.setBackground(colorOscuro);
        panel.setLayout(new GridLayout(14, 2, 5, 3));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // Crear campos de texto
        txtNombre = crearCampoTexto(false);
        txtPeso = crearCampoTexto(false);
        txtAltura = crearCampoTexto(false);
        txtEquipo = crearCampoTexto(false);
        txtLesionado = crearCampoTexto(false);
        txtSalario = crearCampoTexto(false);
        txtPuntos = crearCampoTexto(true);
        txtRebotes = crearCampoTexto(true);
        txtAsistencias = crearCampoTexto(true);
        txtTirosLibres = crearCampoTexto(true);
        txtTirosCampo = crearCampoTexto(true);
        txtTriples = crearCampoTexto(true);
        txtCoefEficiencia = crearCampoTexto(false);

        // Agregar campos
        agregarCampo(panel, "Nombre:", txtNombre);
        agregarCampo(panel, "Peso:", txtPeso);
        agregarCampo(panel, "Altura:", txtAltura);
        agregarCampo(panel, "Equipo:", txtEquipo);
        agregarCampo(panel, "Estado:", txtLesionado);
        agregarCampo(panel, "Salario:", txtSalario);
        agregarCampo(panel, "Puntos/partido:", txtPuntos);
        agregarCampo(panel, "Rebotes/partido:", txtRebotes);
        agregarCampo(panel, "Asistencias/partido:", txtAsistencias);
        agregarCampo(panel, "% Tiros libres:", txtTirosLibres);
        agregarCampo(panel, "% Tiros campo:", txtTirosCampo);
        agregarCampo(panel, "% Triples:", txtTriples);
        agregarCampo(panel, "Eficiencia:", txtCoefEficiencia);

        return panel;
    }

    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(colorOscuro);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 15, 10));
        
        btnModificar = new JButton("Modificar");
        btnModificar.setBackground(colorClaro);
        btnModificar.setForeground(Color.WHITE);
        btnModificar.setFont(new Font("Arial", Font.BOLD, 14));
        btnModificar.addActionListener(e -> toggleModoEdicion());
        panel.add(btnModificar);
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setBackground(new Color(178, 34, 34));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancelar.addActionListener(e -> dispose());
        panel.add(btnCancelar);

        return panel;
    }

    private JTextField crearCampoTexto(boolean esEstadistica) {
        JTextField campo = new JTextField();
        campo.setEditable(false);
        campo.setBackground(Color.WHITE);
        campo.setForeground(Color.BLACK);
        campo.setFont(new Font("Arial", Font.BOLD, 12));
        campo.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
        
        if (esEstadistica) {
            campo.addFocusListener(new FocusAdapter() {
                public void focusLost(FocusEvent e) {
                    if (modoEdicion) {
                        actualizarCoeficienteEficiencia();
                    }
                }
            });
        }
        
        return campo;
    }

    private void agregarCampo(JPanel panel, String etiqueta, JTextField campo) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setForeground(Color.BLACK);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        panel.add(lbl);
        
        campo.setMargin(new Insets(2, 2, 2, 2));
        panel.add(campo);
    }

    private void buscarJugador() {
        String idCompleto = txtId.getText().trim();
        if (idCompleto.length() <= 2) {
            JOptionPane.showMessageDialog(this, "Ingrese un ID válido (Formato: J-XXX)", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        jugadorActual = SerieNacional.getInstance().buscarJugadorPorId(idCompleto);
        
        if (jugadorActual == null) {
            JOptionPane.showMessageDialog(this, "No se encontró un jugador con ID " + idCompleto, "Error", JOptionPane.ERROR_MESSAGE);
            limpiarCampos();
            return;
        }

        // Mostrar datos
        txtNombre.setText(jugadorActual.getNombre());
        txtPeso.setText(String.format("%.1f", jugadorActual.getPeso()));
        txtAltura.setText(String.format("%.2f", jugadorActual.getAltura()));
        txtEquipo.setText(jugadorActual.getEquipo() != null ? jugadorActual.getEquipo().getNombre() : "Sin equipo");
        txtLesionado.setText(jugadorActual.isLesionado() ? "Lesionado" : "Activo");
        txtSalario.setText(String.format("%.2f", jugadorActual.getSalario()));

        // Mostrar estadísticas
        StatsJugador stats = jugadorActual.getEstadistica();
        if (stats != null) {
            txtPuntos.setText(String.format("%.1f", stats.getPuntosPorPartido()));
            txtRebotes.setText(String.format("%.1f", stats.getRebotesPorPartido()));
            txtAsistencias.setText(String.format("%.1f", stats.getAsistenciasPorPartido()));
            txtTirosLibres.setText(String.format("%.1f", stats.getPorcentajeTirosLibres()));
            txtTirosCampo.setText(String.format("%.1f", stats.getPorcentajeTirosCampo()));
            txtTriples.setText(String.format("%.1f", stats.getPorcentajeTriples()));
            txtCoefEficiencia.setText(String.format("%.2f", stats.calcularCoeficienteEfectividad()));
        } else {
            limpiarEstadisticas();
        }
        
        btnModificar.setEnabled(true);
    }

    private void toggleModoEdicion() {
        modoEdicion = !modoEdicion;
        
        if (modoEdicion) {
            btnModificar.setText("Guardar");
            habilitarEdicionCampos();
        } else {
            btnModificar.setText("Modificar");
            deshabilitarEdicionCampos();
            guardarCambios();
        }
    }

    private void habilitarEdicionCampos() {
        // Habilitar todos los campos editables
        txtNombre.setEditable(true);
        txtPeso.setEditable(true);
        txtAltura.setEditable(true);
        txtEquipo.setEditable(true);
        txtLesionado.setEditable(true);
        txtSalario.setEditable(true);
        txtPuntos.setEditable(true);
        txtRebotes.setEditable(true);
        txtAsistencias.setEditable(true);
        txtTirosLibres.setEditable(true);
        txtTirosCampo.setEditable(true);
        txtTriples.setEditable(true);
        
        // Cambiar color de fondo para indicar campos editables
        Color verdeClaro = new Color(220, 255, 220);
        txtNombre.setBackground(verdeClaro);
        txtPeso.setBackground(verdeClaro);
        txtAltura.setBackground(verdeClaro);
        txtEquipo.setBackground(verdeClaro);
        txtLesionado.setBackground(verdeClaro);
        txtSalario.setBackground(verdeClaro);
        txtPuntos.setBackground(verdeClaro);
        txtRebotes.setBackground(verdeClaro);
        txtAsistencias.setBackground(verdeClaro);
        txtTirosLibres.setBackground(verdeClaro);
        txtTirosCampo.setBackground(verdeClaro);
        txtTriples.setBackground(verdeClaro);
    }

    private void deshabilitarEdicionCampos() {
        // Deshabilitar todos los campos editables
        txtNombre.setEditable(false);
        txtPeso.setEditable(false);
        txtAltura.setEditable(false);
        txtEquipo.setEditable(false);
        txtLesionado.setEditable(false);
        txtSalario.setEditable(false);
        txtPuntos.setEditable(false);
        txtRebotes.setEditable(false);
        txtAsistencias.setEditable(false);
        txtTirosLibres.setEditable(false);
        txtTirosCampo.setEditable(false);
        txtTriples.setEditable(false);
        
        // Restaurar color de fondo
        txtNombre.setBackground(Color.WHITE);
        txtPeso.setBackground(Color.WHITE);
        txtAltura.setBackground(Color.WHITE);
        txtEquipo.setBackground(Color.WHITE);
        txtLesionado.setBackground(Color.WHITE);
        txtSalario.setBackground(Color.WHITE);
        txtPuntos.setBackground(Color.WHITE);
        txtRebotes.setBackground(Color.WHITE);
        txtAsistencias.setBackground(Color.WHITE);
        txtTirosLibres.setBackground(Color.WHITE);
        txtTirosCampo.setBackground(Color.WHITE);
        txtTriples.setBackground(Color.WHITE);
    }

    private void guardarCambios() {
        if (jugadorActual == null) return;
        
        try {
            // Actualizar datos básicos del jugador
            jugadorActual.setNombre(txtNombre.getText());
            jugadorActual.setPeso(Float.parseFloat(txtPeso.getText()));
            jugadorActual.setAltura(Float.parseFloat(txtAltura.getText()));
            jugadorActual.setLesionado(txtLesionado.getText().equalsIgnoreCase("Lesionado"));
            jugadorActual.setSalario(Float.parseFloat(txtSalario.getText()));
            
            // Actualizar estadísticas si existen
            if (jugadorActual.getEstadistica() != null) {
                StatsJugador stats = jugadorActual.getEstadistica();
                stats.setPuntosPorPartido(Float.parseFloat(txtPuntos.getText()));
                stats.setRebotesPorPartido(Float.parseFloat(txtRebotes.getText()));
                stats.setAsistenciasPorPartido(Float.parseFloat(txtAsistencias.getText()));
                stats.setPorcentajeTirosLibres(Float.parseFloat(txtTirosLibres.getText()));
                stats.setPorcentajeTirosCampo(Float.parseFloat(txtTirosCampo.getText()));
                stats.setPorcentajeTriples(Float.parseFloat(txtTriples.getText()));
            }
            
            // Actualizar coeficiente de eficiencia
            actualizarCoeficienteEficiencia();
            
            JOptionPane.showMessageDialog(this, "Cambios guardados exitosamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en los datos ingresados", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarCoeficienteEficiencia() {
        if (jugadorActual != null && jugadorActual.getEstadistica() != null) {
            StatsJugador stats = jugadorActual.getEstadistica();
            txtCoefEficiencia.setText(String.format("%.2f", stats.calcularCoeficienteEfectividad()));
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtPeso.setText("");
        txtAltura.setText("");
        txtEquipo.setText("");
        txtLesionado.setText("");
        txtSalario.setText("");
        limpiarEstadisticas();
        btnModificar.setEnabled(false);
    }

    private void limpiarEstadisticas() {
        txtPuntos.setText("");
        txtRebotes.setText("");
        txtAsistencias.setText("");
        txtTirosLibres.setText("");
        txtTirosCampo.setText("");
        txtTriples.setText("");
        txtCoefEficiencia.setText("");
    }
}