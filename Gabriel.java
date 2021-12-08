public class Gabriel {
    private Passageiro[] passageiros;
    private static Predio predio;
    private static final int N_ANDARES = 5;
    private static final int N_PASSAGEIROS = 5;
    private static final int ANDAR_INICIAL = 0;

    public Gabriel(int nPassageiros) {
        criarPassageiros(nPassageiros);

        predio = new Predio(ANDAR_INICIAL, passageiros);
    }

    private void criarPassageiros(int nPassageiros) {
        passageiros = new Passageiro[nPassageiros];
        for (int i = 0; i < passageiros.length; i++) {
            //DISTRIBUIR PASSAGEIROS
        }
    }
}
