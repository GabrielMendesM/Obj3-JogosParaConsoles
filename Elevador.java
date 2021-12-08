import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class Elevador extends Thread {
    private volatile boolean rodando = false;
    private static final int INTERVALO_EXECUCAO = 2000;

    private JPanel panel;
    private ImageIcon portaAberta;
    private ImageIcon portaFechada;

    private boolean portaEstaAberta = false;

    private int nAndares;
    private int andarAtual;
    private int andarDestino;
    private int pos;

    public Elevador(JPanel panel, int nAndares, int andarInicial) {
        this.panel = panel;
        this.nAndares = nAndares;
        
        this.portaAberta = new ImageIcon(getClass().getResource("./img/elevator_open.png"));
        this.portaFechada = new ImageIcon(getClass().getResource("./img/elevator_close.png"));

        pos = andarInicial * (portaAberta.getIconHeight() + 11);
    }

    public void draw(Graphics g) {
        if (portaEstaAberta) {
            portaAberta.paintIcon(panel, g, 10, pos);
        } else {
            portaFechada.paintIcon(panel, g, 10, pos);
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
        
        panel.revalidate();
        panel.repaint();
    }

    public void fecharPorta() {
        portaEstaAberta = false;        
        
        panel.revalidate();
        panel.repaint();
    }

    public void subirAndar() {
        
    }

    public void descerAndar() {
        
    }

    public void mover(int andarAtual, int andarDestino) {
        if (andarDestino < andarAtual) {
            descerAndar();
        } else {
            subirAndar();
        }
        
    }

    @Override
    public void run() {
        super.run();

        while (rodando) {
            System.out.println("Elevador rodando.");
            mover(andarAtual, andarDestino);

            try {
                Thread.sleep(INTERVALO_EXECUCAO);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
