import java.util.concurrent.ThreadLocalRandom;

public class Gabriel {
    private Passageiro[] passageiros;
    private static Predio predio;
    private static final int N_ANDARES = 8;
    private static final int N_PASSAGEIROS = 8;
    private static final int ANDAR_INICIAL = ThreadLocalRandom.current().nextInt(0, N_ANDARES);

    public Gabriel(int nPassageiros) {
        predio = new Predio(N_ANDARES, ANDAR_INICIAL, passageiros);
        
        criarPassageiros(nPassageiros);

        new Jogo(predio);

        System.out.println(ANDAR_INICIAL + 1);
    }

    private void criarPassageiros(int nPassageiros) {
        passageiros = new Passageiro[nPassageiros];
        for (int i = 0; i < passageiros.length; i++) {
            //DISTRIBUIR PASSAGEIROS
        }
    }

}
