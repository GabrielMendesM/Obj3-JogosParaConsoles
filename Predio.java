import java.awt.Graphics;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
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
            andares.get(i).setPosY( i * andares.get(i).getImgHeight());
        }
        Collections.reverse(andares);

        elevador = new Elevador(this, N_ANDARES, andarInicial, andares.get(andarInicial).getPosY());

        System.out.println("Andar Inicial: " + (andarInicial + 1));
    }

    public void comecar() {
        elevador.comecar();
        elevador.setDestino(ThreadLocalRandom.current().nextInt(0, N_ANDARES));
    }

    public void parar() {
        this.elevador.parar();
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
        return (N_ANDARES + 1) * andares.get(0).getImgHeight();
    }

    public List<Andar> getAndares() {
        return andares;
    }

    public void repintar() {
        revalidate();
        repaint();
    }
}
