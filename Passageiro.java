public class Passageiro extends Thread {
    private boolean rodando = false;
    private int andarDestino;
    private static final int INTERVALO_EXECUCAO = 2000;
    
    public Passageiro(int andarDestino) {
        this.andarDestino = andarDestino;
    }

    public void comecar() {
        this.start();
        this.rodando = true;
    }

    public void parar() {
        this.interrupt();
        this.rodando = false;
    }

    @Override
    public void run() {
        super.run();

        while (rodando) {
            try {
                Thread.sleep(INTERVALO_EXECUCAO);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
