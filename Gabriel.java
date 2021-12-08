import java.util.concurrent.ThreadLocalRandom;

public class Gabriel {
    private Passageiro[] passageiros;
    private static Predio predio;
    private static final int N_ANDARES = 8;
    private static final int N_PASSAGEIROS = 8;
    private static final int ANDAR_INICIAL = ThreadLocalRandom.current().nextInt(0, N_ANDARES);

    public Gabriel() {
        criarPassageiros();

        predio = new Predio(N_ANDARES, ANDAR_INICIAL, passageiros);

        new Jogo(predio);
    }

    private void criarPassageiros() {
        passageiros = new Passageiro[N_PASSAGEIROS];
        for (int i = 0; i < passageiros.length; i++) {
            //DISTRIBUIR PASSAGEIROS
        }
    }
}
