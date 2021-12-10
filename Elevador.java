/*

============ PERSONAGENS ANDAREM NA FILA ============

*/

import java.awt.Graphics;
import javax.swing.ImageIcon;

public class Elevador extends Thread implements IElevador {
    private volatile boolean rodando = false;
    private static final int INTERVALO_EXECUCAO = 30;

    private int pos;
    private int posDestino;
    private int andarAtual;
    private int andarDestino;

    private Predio predio;
    private ImageIcon portaAberta;
    private ImageIcon portaFechada;

    private boolean portaEstaAberta = false;
    private boolean chegouAoDestino = true;
    private boolean estaOcupado = false;

    public Elevador(Predio predio, int nAndares, int posInicial, int andarInicial) {
        this.pos = posInicial;
        this.andarAtual = andarInicial;

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
                andarAtual = andarDestino;
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
        andarDestino = andar;
        posDestino = predio.getAndares().get(andar).getPosY();
        chegouAoDestino = false;
    }

    public int getAndarAtual() {
        return andarAtual;
    }

    public boolean getEstaNoDestino() {
        return chegouAoDestino;
    }

    public boolean getEstaOcupado() {
        return estaOcupado;
    }

    public void setEstaOcupado(boolean estaOcupado) {
        this.estaOcupado = estaOcupado;
    }
}
