import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Predio extends JPanel {
    private final int ANDAR_INICIAL;
    private static final int N_ANDARES = 5;
    private ImageIcon[] andares = new ImageIcon[N_ANDARES];
    
    private final Passageiro[] passageiros;
    private final Elevador elevador;
    
    public Predio(int andarInicial, Passageiro[] passageiros) {
        super();

        this.ANDAR_INICIAL = andarInicial;
        this.passageiros = passageiros;

        for (int i = 0; i < andares.length; i++) {
            andares[i] = new ImageIcon(getClass().getResource("./img/andar.png"));
        }

        this.elevador = new Elevador(this, N_ANDARES, ANDAR_INICIAL);
    }

    public void comecar() {
        this.elevador.comecar();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (int i = 0; i < andares.length; i++) {
            andares[i].paintIcon(this, g, 0, i * andares[i].getIconHeight());
        }

        elevador.draw(g);
    }

    public Elevador getElevador() {
        return this.elevador;
    }
}
