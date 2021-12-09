import java.awt.Graphics;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.ImageIcon;

public class Passageiro extends Thread {
    private volatile boolean rodando = false;
    private static final int INTERVALO_EXECUCAO = 20;
    
    private int posX;
    private int posY;
    private int posXDestino;
    private int posYDestino;
    
    private Predio predio;
    private ImageIcon img;

    private boolean estaNoElevador = false;
    private boolean chegouAoDestino = true;
    
    public Passageiro(int lugarNaFila, int andarInicial, Predio predio) {
        this.predio = predio;

        this.posX = predio.getElevador().getLargura() * lugarNaFila;
        this.posY = predio.getAndares().get(andarInicial).getPosY();

        img = new ImageIcon(getClass().getResource("./img/Pedestre" + ThreadLocalRandom.current().nextInt(1, 9) + ".png"));
    }

    public void draw(Graphics g) {
        img.paintIcon(predio, g, posX, posY);
    }

    public void comecar() {
        this.start();
        this.rodando = true;
    }

    public void parar() {
        this.interrupt();
        this.rodando = false;
    }

    private void esperar() {

    }

    private void entrarNoElevador() {

    }

    private void sairDoElevador() {

    }

    private void moverY() {

    }

    private void update() {
        //mover();
        //System.out.println("Passageiro update");
    }

    @Override
    public void run() {
        super.run();

        while (rodando) {
            update();

            try {
                Thread.sleep(INTERVALO_EXECUCAO);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void abrirPorta() {
        predio.getElevador().abrirPorta();
    }

    private void fecharPorta() {
        predio.getElevador().fecharPorta();
    }

    private void visitarAndar(int andar) {
        predio.getElevador().visitarAndar(andar);
        posYDestino = predio.getAndares().get(andar).getPosY();
        chegouAoDestino = false;        
    }
}
