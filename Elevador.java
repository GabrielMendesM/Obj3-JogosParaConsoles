public class Elevador extends Thread {
    private boolean rodando = false;
    private static final int INTERVALO_EXECUCAO = 2000;

    private int nAndares;
    private int andarAtual;
    private int andarDestino;
    private final int ANDAR_INICIAL;

    public Elevador(int nAndares, int andarInicial) {
        this.nAndares = nAndares;
        this.ANDAR_INICIAL = andarInicial;
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
        
    }

    public void fecharPorta() {
        
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
            mover(andarAtual, andarDestino);

            try {
                Thread.sleep(INTERVALO_EXECUCAO);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
