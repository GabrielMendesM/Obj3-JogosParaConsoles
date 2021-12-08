public class Predio {
    private final int ANDAR_INICIAL;
    private static final int N_ANDARES = 5;
    
    private final Passageiro[] passageiros;
    private final Elevador elevador;
    
    public Predio(int andarInicial, Passageiro[] passageiros) {
        this.ANDAR_INICIAL = andarInicial;
        this.passageiros = passageiros;

        this.elevador = new Elevador(N_ANDARES, ANDAR_INICIAL);
    }
}
