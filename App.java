import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class App {
    private List<Passageiro> passageiros = new ArrayList<>();
    private static Predio predio;
    private static final int N_ANDARES = 8;
    private static final int N_PASSAGEIROS = 8;
    private static final int ANDAR_INICIAL = ThreadLocalRandom.current().nextInt(0, N_ANDARES);

    private List<List<Passageiro>> filas = new ArrayList<>();

    public App() {
        predio = new Predio(N_ANDARES, ANDAR_INICIAL);

        criarPassageiros();
        predio.setPassageiros(passageiros.toArray(new Passageiro[N_PASSAGEIROS]));        
        new Janela(predio);
    }

    private void criarPassageiros() {
        for (int i = 0; i < N_PASSAGEIROS; i++) {
            int andarInicial = ThreadLocalRandom.current().nextInt(0, N_ANDARES);
            passageiros.add(new Passageiro(i + 1, andarInicial, predio));
        }
    }

    private void addPassageirosNaFila(int indice, Passageiro passageiro) {
        /*
        filas.add(passageiros[i]);
        filas.get(i).add
        */
    }

    private void removerPassageirosDaFila() {

    }
}
