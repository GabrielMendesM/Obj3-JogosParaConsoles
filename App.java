import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class App {
    private List<Passageiro> passageiros = new ArrayList<>();
    private static Predio predio;
    private static final int N_ANDARES = 8;
    private static final int N_PASSAGEIROS = 20;
    private static final int ANDAR_INICIAL = ThreadLocalRandom.current().nextInt(0, N_ANDARES);

    private List<Integer> filas = new ArrayList<>();

    public App() {
        predio = new Predio(N_ANDARES, ANDAR_INICIAL);

        for (int i = 0; i < N_ANDARES; i++) {
            filas.add(0);
        }
        criarPassageiros();
        predio.setPassageiros(passageiros.toArray(new Passageiro[N_PASSAGEIROS]));        
        predio.setFilas(filas);
        new Janela(predio);
    }

    public void parar() {
        predio.parar();
    }

    //MÉTODO VISITAR ANDAR MAIS CHEIO

    private void criarPassageiros() {
        for (int i = 0; i < N_PASSAGEIROS; i++) {
            int andarInicial = ThreadLocalRandom.current().nextInt(0, N_ANDARES);
            int posNaFila;
            for (int j = 0; j < N_ANDARES; j++) {
                if (j == andarInicial) {
                    if (filas.get(j) > 0) {
                        posNaFila = filas.get(j) + 1;
                        passageiros.add(new Passageiro(i, posNaFila, j, predio));
                        filas.set(j, filas.get(j) + 1);
                    } else {
                        posNaFila = 1;
                        passageiros.add(new Passageiro(i, posNaFila, j, predio));
                        filas.set(j, filas.get(j) + 1);
                    }
                }
            }
        }
    }
}
