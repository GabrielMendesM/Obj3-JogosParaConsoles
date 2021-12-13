/*

============ PERSONAGENS ANDAREM NA FILA ============

*/

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import javax.swing.ImageIcon;

public class Elevador extends Thread implements IElevador {
    private volatile boolean rodando = false;
    private static final int INTERVALO_EXECUCAO = 32;
    private static final Semaphore ELEVADOR_SEM = new Semaphore(1);

    //int aux = 0;


    private int pos;
    private int posDestino;
    private int andarAtual;
    private int andarDestino;

    private Predio predio;
    private ImageIcon portaAberta;
    private ImageIcon portaFechada;

    private static List<Integer> andaresVisitados = new ArrayList<>();

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

    //MÉTODO VISITAR ANDAR MAIS CHEIO
    
    private void mover() {        
        if (!chegouAoDestino) {
            if (posDestino < pos) {
                pos -= 3;
            } else if (posDestino > pos) {
                pos += 3;
            } else {
                //aux++;
                //System.out.println("Elevador chegou " + aux + " vezes.");
                andarAtual = andarDestino;
                chegouAoDestino = true;
                andaresVisitados.add(andarDestino);
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
                //e.printStackTrace();
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

    public boolean getEstaNoDestino() {
        return chegouAoDestino;
    }

    public int getAndarAtual() {
        return andarAtual;
    }

    public boolean getEstaOcupado() {
        return estaOcupado;
    }
    //tentar semaphore para todo passageiro usar o elevador 1 vez
    //MÉTODO VISITAR ANDAR MAIS CHEIO

    public void setEstaOcupado(boolean estaOcupado) {
        this.estaOcupado = estaOcupado;
    }

    public static final Semaphore getElevadorSem() {
        return ELEVADOR_SEM;
    }

    public static final int getIntervaloExecucao() {
        return INTERVALO_EXECUCAO;
    }

    public static List<Integer> getAndaresVisitados() {
        return andaresVisitados;
    }
}
