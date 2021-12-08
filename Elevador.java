public class Elevador extends Thread implements IElevador {
    private boolean rodando = false;
    private static final int INTERVALO_EXECUCAO = 2000;

    private int nAndares;
    private final int ANDAR_INICIAL;

    public Elevador(int nAndares, int andarInicial) {
        this.nAndares = nAndares;
        this.ANDAR_INICIAL = andarInicial;
    }

    @Override
    public void abrirPorta() {
        System.out.println("Abrir porta");
    }

    @Override
    public void fecharPorta() {

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
