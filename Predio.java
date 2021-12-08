import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import javax.print.attribute.standard.MediaSize.NA;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Predio extends JPanel {
    private final int N_ANDARES;
    
    private final List<Andar> andares = new ArrayList<>();
    
    private final Passageiro[] passageiros;
    private final Elevador elevador;
    
    public Predio(int nAndares, int andarInicial, Passageiro[] passageiros) {
        super();
        this.N_ANDARES = nAndares;
        this.passageiros = passageiros;

        for (int i = 0; i < N_ANDARES; i++) {
            andares.add(new Andar(this));
            andares.get(i).setPosY( i * andares.get(i).getImg().getIconHeight());
        }
        Collections.reverse(andares);

        this.elevador = new Elevador(this, N_ANDARES, andarInicial, andares.get(andarInicial).getPosY());
    }

    public void comecar() {
        this.elevador.comecar();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (int i = 0; i < andares.size(); i++) {
            andares.get(i).draw(g);
        }

        elevador.draw(g);
    }

    public Elevador getElevador() {
        return this.elevador;
    }

    public int getAlturaPredio() {
        return (N_ANDARES + 1) * andares.get(0).getImg().getIconHeight();
    }
}
