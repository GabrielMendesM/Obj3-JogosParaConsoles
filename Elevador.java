import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Elevador extends Thread {
    private volatile boolean rodando = false;
    private static final int INTERVALO_EXECUCAO = 20;

    private final int N_ANDARES;
    private int andarAtual; //comparar a posição do elevador com as posições da lista
    private int pos;
    private int posDestino;

    private Predio predio;
    private ImageIcon portaAberta;
    private ImageIcon portaFechada;

    private boolean portaEstaAberta = false;
    private boolean chegouAoDestino = true;

    public Elevador(Predio predio, int nAndares, int andarInicial, int posInicial) {
        this.N_ANDARES = nAndares;
        this.andarAtual = andarInicial;
        this.pos = posInicial;

        this.predio = predio;
        this.portaAberta = new ImageIcon(getClass().getResource("./img/elevator_open.png"));
        this.portaFechada = new ImageIcon(getClass().getResource("./img/elevator_close.png"));
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

    public void abrirPorta() {
        portaEstaAberta = true;        
        
        predio.repintar();
    }

    public void fecharPorta() {
        portaEstaAberta = false;
        predio.repintar();
    }

    private void subirAndar() {
        pos += 3;
        predio.repintar();
    }

    private void descerAndar() {
        pos -= 3;
        predio.repintar();
    }

    private void mover() {        
        if (!chegouAoDestino) {
            if (posDestino > pos) {
                subirAndar();
            } else if (posDestino < pos) {
                descerAndar();
            } else {
                chegouAoDestino = true;
            }
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

    public void setDestino(int andarDestino) {       
        posDestino = predio.getAndares().get(andarDestino).getPosY();
        chegouAoDestino = false;

        System.out.println("Andar destino: " + (andarDestino + 1));
    }
}
