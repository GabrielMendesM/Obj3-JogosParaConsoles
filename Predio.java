import java.awt.Graphics;

import javax.print.attribute.standard.MediaSize.NA;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Predio extends JPanel {
    private final int ANDAR_INICIAL;
    private final int N_ANDARES;
    private ImageIcon[] andares;
    
    private final Passageiro[] passageiros;
    private final Elevador elevador;
    
    public Predio(int nAndares, int andarInicial, Passageiro[] passageiros) {
        super();
        this.N_ANDARES = nAndares;
        this.ANDAR_INICIAL = andarInicial;
        this.passageiros = passageiros;

        this.andares = new ImageIcon[N_ANDARES];

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

    public int getAlturaPredio() {
        return (N_ANDARES + 1) * andares[0].getIconHeight();
    }
}
