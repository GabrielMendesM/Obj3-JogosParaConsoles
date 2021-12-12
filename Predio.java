/*

============ PERSONAGENS ANDAREM NA FILA ============

*/

import java.awt.Graphics;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;

//MÉTODO VISITAR ANDAR MAIS CHEIO

public class Predio extends JPanel {
    private final int N_ANDARES;
    private static Semaphore filaSem = new Semaphore(0);
    
    private final List<Andar> andares = new ArrayList<>();
    
    private Passageiro[] passageiros;
    private final Elevador elevador;

    private List<Integer> filas = new ArrayList<>();

    public Predio(int nAndares, int andarInicial) {
        super();
        this.N_ANDARES = nAndares;

        for (int i = 0; i < N_ANDARES; i++) {
            andares.add(new Andar(this));
            andares.get(i).setPosY( i * andares.get(i).getImgHeight());
        }
        Collections.reverse(andares);

        this.elevador = new Elevador(this, N_ANDARES, andares.get(andarInicial).getPosY(), andarInicial);
    }

    public void comecar() {
        for (Passageiro p : passageiros) {
            p.comecar();
        }
        elevador.comecar();

        int andarDestino = elevador.getAndarAtual();
        if (filas.get(elevador.getAndarAtual()) > 0) {
            andarDestino = elevador.getAndarAtual();
        } else if (andarDestino > N_ANDARES / 2) {
            for (int i = filas.size() - 1; i > -1; i--) {
                //System.out.println("Fila " + i + " tem " + filas.get(i) + " pessoas.");
                if (filas.get(andarDestino) < filas.get(i)) {
                    andarDestino = i;
                }
            }    
        } else {
            for (int i = 0; i < filas.size(); i++) {
                //System.out.println("Fila " + i + " tem " + filas.get(i) + " pessoas.");
                if (filas.get(andarDestino) < filas.get(i)) {
                    andarDestino = i;
                }
            }    
        }
        elevador.visitarAndar(andarDestino);
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
        elevador.draw(g);
        
        if (passageiros.length > 0) {
            for (Passageiro p : passageiros) {
                p.draw(g);
            }    
        }
        
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

    public List<Integer> getFilas() {
        return this.filas;
    }

    public void setFilas(int indice, boolean aumentar) {
        if (aumentar) {
            this.filas.set(indice, filas.get(indice) + 1);
        } else {
            this.filas.set(indice, filas.get(indice) - 1);
        }

        //if (filas.get(elevador.getAndarAtual()) > 0) filaSem.release(filas.get(elevador.getAndarAtual()));

        //System.out.println((elevador.getAndarAtual() + 1) + "º andar: " + filas.get(elevador.getAndarAtual()) + " pessoas na fila.");

        /*for (int i = 0; i < N_ANDARES; i++) {
            System.out.println((i + 1) + "º andar: " + filas.get(i) + " pessoas na fila.");
        }*/
        
    }

    public void setFilas(List<Integer> filas) {
        this.filas = filas;

        /*for (int i = 0; i < N_ANDARES; i++) {
            System.out.println((i + 1) + "º andar: " + filas.get(i) + " pessoas na fila.");
        }*/
        //if (filas.get(elevador.getAndarAtual()) > 0) filaSem.release(filas.get(elevador.getAndarAtual()));
        //System.out.println((elevador.getAndarAtual() + 1) + "º andar: " + filas.get(elevador.getAndarAtual()) + " pessoas na fila.");
    }

    public void repintar() {
        revalidate();
        repaint();
    }

    public static Semaphore getFilaSem() {
        return filaSem;
    }

    public static void setFilaSem(Semaphore sem) {
        filaSem = sem;
    }
}
