import java.awt.Graphics;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.ImageIcon;

public class Passageiro extends Thread implements IElevador {
    private volatile boolean rodando = false;
    private static final int INTERVALO_EXECUCAO = Elevador.getIntervaloExecucao();
    private static final int TEMPO_ESPERA = 100;
    
    private int rodouVezes = 0;

    private int id;
    private int lugarNaFila;
    private int andarAtual;
    private int andarDestino;
    private int posX;
    private int posY;
    private int posXDestino;
    private int posYDestino;

    private static final int POS_DENTRO_ELEVADOR = 10;
    
    private Predio predio;
    private ImageIcon img;

    private boolean estaNoElevador = false;
    private boolean chegouAoDestino = true;

    public Passageiro(int id, int lugarNaFila, int andarInicial, Predio predio) {
        this.id = id + 1;
        this.lugarNaFila = lugarNaFila;
        this.andarAtual = andarInicial;

        this.posXDestino = POS_DENTRO_ELEVADOR;
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
        System.out.println("No total, personagem " + id + " usou o elevador " + rodouVezes + " vezes.");
    }

    private void esperar() {
        if (lugarNaFila != 0) {
            try {
                Thread.sleep(TEMPO_ESPERA);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void avancarNaFila() {
        if (!estaNoElevador &&
            lugarNaFila > 1 &&
            andarAtual == predio.getElevador().getAndarAtual() &&
            predio.getElevador().getEstaOcupado() && 
            chegouAoDestino) {
        
            if (Predio.getFilaSem().tryAcquire()) {
                chegouAoDestino = false;
                lugarNaFila--;
                posXDestino = predio.getElevador().getLargura() * lugarNaFila;
                
                while (posX > posXDestino) {
                    posX--;
                    predio.repintar();
                    
                    try {
                        Thread.sleep(INTERVALO_EXECUCAO / 6);
                    } catch (InterruptedException e) {}
                }
                chegouAoDestino = true;
                Predio.getFilaSem().release();
                if (lugarNaFila == 1) {
                    posXDestino = POS_DENTRO_ELEVADOR;
                }
                if (Predio.getFilaSem().availablePermits() == predio.getFilas().get(andarAtual)) {
                    Predio.setFilaSem(new Semaphore(0));
                }

                try {
                    Thread.sleep(TEMPO_ESPERA);
                } catch (InterruptedException e) {}
            }
        }
    }    

    private void entrarNoElevador() {
        if (!estaNoElevador && 
            lugarNaFila == 1 && 
            andarAtual == predio.getElevador().getAndarAtual() && 
            !predio.getElevador().getEstaOcupado() &&
            predio.getElevador().getEstaNoDestino() && 
            chegouAoDestino) {
            
            if (Elevador.getElevadorSem().tryAcquire()) {
                abrirPorta();
                while (posX > posXDestino) {
                    posX--;
                    predio.repintar();
                    
                    try {
                        Thread.sleep(INTERVALO_EXECUCAO / 6);
                    } catch (InterruptedException e) {}
                }
                estaNoElevador = true;
                chegouAoDestino = false;
                lugarNaFila = 0;
                predio.setFilas(andarAtual, false);
                predio.getElevador().setEstaOcupado(true);
                fecharPorta();

                Predio.setFilaSem(new Semaphore(predio.getFilas().get(andarAtual)));

                int destinoAux = ThreadLocalRandom.current().nextInt(0, predio.getAndares().size());
                if (Elevador.getAndaresVisitados().size() == predio.getAndares().size()) Elevador.getAndaresVisitados().clear();
                while (destinoAux == andarAtual || Elevador.getAndaresVisitados().contains(destinoAux)) {
                    destinoAux = ThreadLocalRandom.current().nextInt(0, predio.getAndares().size());
                }
                visitarAndar(destinoAux);
            }
        }
    }

    private void sairDoElevador() {
        if (estaNoElevador && 
            lugarNaFila == 0 && 
            andarAtual == predio.getElevador().getAndarAtual() &&
            predio.getElevador().getEstaNoDestino()) {

            Predio.setFilaSem(new Semaphore(0));

            abrirPorta();
            while (posX < posXDestino) {
                posX++;
                if (estaNoElevador && posX >= predio.getElevador().getLargura()) {
                    fecharPorta();
                    estaNoElevador = false;
                    andarAtual = andarDestino;
                    predio.setFilas(andarAtual, true);
                    lugarNaFila = predio.getFilas().get(andarAtual);
                    predio.getElevador().setEstaOcupado(false);
                }
                predio.repintar();
                
                try {
                    Thread.sleep(INTERVALO_EXECUCAO / 6);
                } catch (InterruptedException e) {}
            }
            posXDestino = POS_DENTRO_ELEVADOR;
            rodouVezes++;
            chegouAoDestino = true;
            Elevador.getElevadorSem().release();
        }
    }

    private void moverY() {
        if (!chegouAoDestino &&
            estaNoElevador && 
            lugarNaFila == 0) {
            if (posYDestino < posY) {
                posY -= 3;
            } else if (posYDestino > posY) {
                posY += 3;
            } else {
                chegouAoDestino = true;
                andarAtual = andarDestino;
            }
            predio.repintar();
        }
    }

    private void update() {
        esperar();
        avancarNaFila();
        entrarNoElevador();
        sairDoElevador();
        moverY();
    }

    @Override
    public void run() {
        super.run();

        while (rodando) {
            update();

            try {
                Thread.sleep(INTERVALO_EXECUCAO);
            } catch (InterruptedException e) {}
        }
    }


    @Override
    public void abrirPorta() {
        predio.getElevador().abrirPorta();
    }
  
    @Override
    public void fecharPorta() {
        predio.getElevador().fecharPorta();
    }

    @Override
    public void visitarAndar(int andar) {
        andarDestino = andar;
        posXDestino = predio.getElevador().getLargura() * (predio.getFilas().get(andarDestino) + 1);
        predio.getElevador().visitarAndar(andar);
        posYDestino = predio.getAndares().get(andar).getPosY();
        chegouAoDestino = false;        
    }
}
