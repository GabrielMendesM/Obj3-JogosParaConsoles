import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Janela extends JFrame {
    public Janela(Predio predio) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(580, predio.getAlturaPredio()));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(predio, BorderLayout.CENTER);
        setVisible(true);

        JPanel pnlGUI = new JPanel();
        pnlGUI.setLayout(new FlowLayout(FlowLayout.LEFT));
        Button btnComecar = new Button("Começar");
        Button btnAbrirPorta = new Button("Abrir Porta");
        Button btnFecharPorta = new Button("Fechar Porta");
        pnlGUI.add(btnComecar);
        pnlGUI.add(btnAbrirPorta);
        pnlGUI.add(btnFecharPorta);
        add(pnlGUI, BorderLayout.SOUTH);

        btnComecar.addActionListener(e -> {
            pnlGUI.remove(btnComecar);
            predio.comecar();
        });
        
        btnAbrirPorta.addActionListener(e -> {
            predio.getElevador().abrirPorta();
        });
        
        btnFecharPorta.addActionListener(e -> {
            predio.getElevador().fecharPorta();
        });

        pack();
    }
}
