import java.awt.Graphics;
import javax.swing.ImageIcon;

public class Elevador extends Thread implements IElevador {
    private volatile boolean rodando = false;
    private static final int INTERVALO_EXECUCAO = 20;

    private int pos;
    private int posDestino;

    private Predio predio;
    private ImageIcon portaAberta;
    private ImageIcon portaFechada;

    private boolean portaEstaAberta = false;
    private boolean chegouAoDestino = true;

    public Elevador(Predio predio, int nAndares, int posInicial) {
        this.pos = posInicial;

        this.predio = predio;
        
        portaAberta = new ImageIcon(getClass().getResource("./img/elevator_open.png"));
        portaFechada = new ImageIcon(getClass().getResource("./img/elevator_close.png"));
    }

    public void draw(Graphics g) {
        if (portaEstaAberta) {
            portaAberta.paintIcon(predio, g, 10, pos);
        } else {
            portaFechada.paintIcon(predio, g, 10, pos);
        }
    }

    public void comecar() {
        this.start();
        this.rodando = true;
    }

    public void parar() {
        this.interrupt();
        this.rodando = false;
    }

    private void mover() {        
        if (!chegouAoDestino) {
            if (posDestino < pos) {
                pos -= 3;
            } else if (posDestino > pos) {
                pos += 3;
            } else {
                chegouAoDestino = true;
            }
            predio.repintar();
        }
    }

    @Override
    public void run() {
        super.run();

        while (rodando) {
            mover();

            try {
                Thread.sleep(INTERVALO_EXECUCAO);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public int getLargura() {
        return portaAberta.getIconWidth();
    }

    @Override
    public void abrirPorta() {
        portaEstaAberta = true;        
        
        predio.repintar();
    }

    @Override
    public void fecharPorta() {
        portaEstaAberta = false;
        predio.repintar();
    }

    @Override
    public void visitarAndar(int andar) {
        posDestino = predio.getAndares().get(andar).getPosY();
        chegouAoDestino = false;
    }
}
