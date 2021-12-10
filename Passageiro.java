import java.awt.Graphics;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.ImageIcon;

public class Passageiro extends Thread implements IElevador {
    private volatile boolean rodando = false;
    private static final int INTERVALO_EXECUCAO = 32;
    private static final int TEMPO_ESPERA = 100;
    
    int aux = 1;

    private int rodouVezes = 0;

    private int id;
    private int lugarNaFila;
    private int andarAtual;
    private int andarDestino;
    private int posX;
    private int posY;
    private int posXDestino;
    private int posYDestino;
    
    private Predio predio;
    private ImageIcon img;

    private boolean estaNoElevador = false;
    private boolean chegouAoDestino = true;

    public Passageiro(int id, int lugarNaFila, int andarInicial, Predio predio) {
        this.id = id + 1;
        this.lugarNaFila = lugarNaFila;
        this.andarAtual = andarInicial;

        this.posXDestino = 10;
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
        //System.out.println("Passageiro " + id + " está esperando");
        if (lugarNaFila != 0) {
            //System.out.println("Passageiro " + id + " é o " + lugarNaFila + "º da fila do " + (andarAtual + 1) + "º andar");
            try {
                /*
                int esperaAux = lugarNaFila;
                if (lugarNaFila < 3) {
                    esperaAux = TEMPO_ESPERA;
                } else {
                    esperaAux = TEMPO_ESPERA * (lugarNaFila / 2);
                }*/
                Thread.sleep(TEMPO_ESPERA);
            } catch (InterruptedException e) {
                //e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }

    private void avancarNaFila() {
        //Se o elevador estiver cheio e estiver no andar atual, todos do andar devem diminuir 1 lugar na fila e avançarem até o novo ponto
        //Ir para o último lugar levando em consideração q a fila vai andar pra frente
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
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    }
                }
                Predio.getFilaSem().release();
                //System.out.println("permits liberados: " + Predio.getFilaSem().availablePermits());
                if (Predio.getFilaSem().availablePermits() == predio.getFilas().get(andarAtual)) {
                    Predio.setFilaSem(new Semaphore(0));
                }
            }
        }
    }    

    private void entrarNoElevador() {
        if (!estaNoElevador && 
            lugarNaFila == 1 && 
            andarAtual == predio.getElevador().getAndarAtual() && 
            !predio.getElevador().getEstaOcupado() &&
            predio.getElevador().getEstaNoDestino()) {

            if (Elevador.getElevadorSem().tryAcquire()) {
                //System.out.println("Passageiro[" + id + "] pode entrar no elevador.");
                abrirPorta();
                while (posX > posXDestino) {
                    posX--;
                    predio.repintar();
                    
                    try {
                        Thread.sleep(INTERVALO_EXECUCAO / 6);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    }
                }
                estaNoElevador = true;
                chegouAoDestino = false;
                lugarNaFila = 0;
                predio.setFilas(andarAtual, false);
                predio.getElevador().setEstaOcupado(true);
                fecharPorta();

                int destinoAux = ThreadLocalRandom.current().nextInt(0, predio.getAndares().size());
                while (destinoAux == andarAtual) {
                    destinoAux = ThreadLocalRandom.current().nextInt(0, predio.getAndares().size());
                }
                visitarAndar(destinoAux); //ThreadLocalRandom.current().nextInt(0, predio.getAndares().size()));
                
                Predio.setFilaSem(new Semaphore(predio.getFilas().get(andarAtual)));
//                Predio.getFilaSem().release(predio.getFilas().get(andarAtual));
                //System.out.println("permits liberados: " + Predio.getFilaSem().availablePermits());
            }
        }
    }

    private void sairDoElevador() {
        //System.out.println("estaNoElevador: " + estaNoElevador + "\nlugarNaFila: " + lugarNaFila + "\nandarAtual: " + andarAtual + "\npredio.getElevador().getAndarAtual(): " + predio.getElevador().getAndarAtual() + "\npredio.getElevador().getEstaNoDestino(): " + predio.getElevador().getEstaNoDestino());
        if (estaNoElevador && 
            lugarNaFila == 0 && 
            andarAtual == predio.getElevador().getAndarAtual() &&
            predio.getElevador().getEstaNoDestino()) {

            Predio.setFilaSem(new Semaphore(0));
           // System.out.println("permits liberados: " + Predio.getFilaSem().availablePermits());

//            Predio.getFilaSem().release(0);

            //System.out.println("Passageiro[" + id + "] pode sair do elevador.");
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
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
            posXDestino = 10;
            rodouVezes++;
            chegouAoDestino = true;
            Elevador.getElevadorSem().release();

            //Predio.setFilaSem(new Semaphore(predio.getFilas().get(andarAtual)));
            //System.out.println("permits liberados: " + Predio.getFilaSem().availablePermits());

            //Predio.getFilaSem().release(predio.getFilas().get(andarAtual));
        }
    }

    private void moverY() {
        if (!chegouAoDestino &&
            estaNoElevador && 
            lugarNaFila == 0) { //&& !predio.getElevador().getEstaNoDestino()) {
            if (posYDestino < posY) {
                posY -= 3;
            } else if (posYDestino > posY) {
                posY += 3;
            } else {
                aux++;
                System.out.println("Passageiro chegou " + aux + " vezes.");
                chegouAoDestino = true;
                andarAtual = andarDestino;
            }
            predio.repintar();
        }
    }

    private void update() {
        esperar();
        entrarNoElevador();
        moverY();
        avancarNaFila();
        sairDoElevador();
        //visitarAndar(ThreadLocalRandom.current().nextInt(0, predio.getAndares().size()));
        //sairDoElevador();
    }

    @Override
    public void run() {
        super.run();

        while (rodando) {
            update();

            try {
                Thread.sleep(INTERVALO_EXECUCAO);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
        }
    }

    public void setLugarNaFila(int lugarNaFila) {
        this.lugarNaFila = lugarNaFila;
    }

    public void setAndarAtual(int andar) {
        this.andarAtual = andar;
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
        //System.out.println("Passageiro " + id + " está saindo do " + (andarAtual + 1) + "º andar e indo para o " + (andar + 1) + "º andar.");
        andarDestino = andar;
        posXDestino = predio.getElevador().getLargura() * (predio.getFilas().get(andarDestino) + 1);
        predio.getElevador().visitarAndar(andar);
        posYDestino = predio.getAndares().get(andar).getPosY();
        chegouAoDestino = false;        
    }
}
