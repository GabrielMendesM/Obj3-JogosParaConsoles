import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

public class Predio extends JPanel {
    private final int N_ANDARES;
    
    private final List<Andar> andares = new ArrayList<>();
    
    private Passageiro[] passageiros;
    private final Elevador elevador;
    
    public Predio(int nAndares, int andarInicial) {
        super();
        this.N_ANDARES = nAndares;

        for (int i = 0; i < N_ANDARES; i++) {
            andares.add(new Andar(this));
            andares.get(i).setPosY( i * andares.get(i).getImgHeight());
        }
        Collections.reverse(andares);

        this.elevador = new Elevador(this, N_ANDARES, andares.get(andarInicial).getPosY());
    }

    public void comecar() {
        for (Passageiro p : passageiros) {
            p.comecar();
        }
        elevador.comecar();
        elevador.visitarAndar(0);
    }

    public void parar() {
        for (Passageiro p : passageiros) {
            p.parar();
        }
        this.elevador.parar();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        for (int i = 0; i < andares.size(); i++) {
            andares.get(i).draw(g);
        }
        if (passageiros.length > 0) {
            for (Passageiro p : passageiros) {
                p.draw(g);
            }    
        }

        elevador.draw(g);
    }

    public void setPassageiros(Passageiro[] passageiros) {
        this.passageiros = passageiros;
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
