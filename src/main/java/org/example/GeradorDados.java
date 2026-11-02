package org.example;

import java.util.Arrays;
import java.util.Random;

// Utilitário para geração determinística da massa de dados dos testes, garantindo a reprodutibilidade dos resultados através de uma semente fixa.
public class GeradorDados {
    public static final int BUSCAS = 2000;
    public static final int INTERVALOS = 200;
    public static final int LARGURA = 100;

    private int n;
    private int[] crescente;
    private int[] decrescente;
    private int[] aleatoria;
    private int[] buscasPresentes;
    private int[] buscasAusentes;
    private int[] insercoes;
    private int[] remocoes;
    private int[] minimosIntervalo;
    private long gabaritoIntervalo;

    public GeradorDados(int n) {
        this.n = n;
        Random random = new Random(42);

        crescente = new int[n];
        decrescente = new int[n];
        for (int i = 0; i < n; i++) {
            crescente[i] = 2 * (i + 1);
            decrescente[i] = 2 * (n - i);
        }

        aleatoria = crescente.clone();
        embaralhar(aleatoria, random);

        buscasPresentes = new int[BUSCAS];
        for (int i = 0; i < BUSCAS; i++) {
            buscasPresentes[i] = 2 * (random.nextInt(n) + 1);
        }

        buscasAusentes = new int[BUSCAS];
        for (int i = 0; i < BUSCAS; i++) {
            buscasAusentes[i] = 2 * random.nextInt(n) + 1;
        }

        int[] impares = new int[n];
        for (int i = 0; i < n; i++) {
            impares[i] = 2 * i + 1;
        }
        embaralhar(impares, random);
        insercoes = Arrays.copyOf(impares, n / 10);

        int[] pares = crescente.clone();
        embaralhar(pares, random);
        remocoes = Arrays.copyOf(pares, n / 10);

        minimosIntervalo = new int[INTERVALOS];
        gabaritoIntervalo = 0;
        for (int i = 0; i < INTERVALOS; i++) {
            int minimo = 1 + random.nextInt(2 * n - LARGURA);
            int maximo = minimo + LARGURA;
            minimosIntervalo[i] = minimo;
            gabaritoIntervalo += maximo / 2 - (minimo - 1) / 2;
        }
    }

    private void embaralhar(int[] vetor, Random random) {
        for (int i = vetor.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = vetor[i];
            vetor[i] = vetor[j];
            vetor[j] = temp;
        }
    }

    public int getN() {
        return n;
    }

    public int[] getOrdem(int indice) {
        if (indice == 0) {
            return crescente;
        }
        if (indice == 1) {
            return decrescente;
        }
        return aleatoria;
    }

    public int[] getBuscasPresentes() {
        return buscasPresentes;
    }

    public int[] getBuscasAusentes() {
        return buscasAusentes;
    }

    public int[] getInsercoes() {
        return insercoes;
    }

    public int[] getRemocoes() {
        return remocoes;
    }

    public int[] getMinimosIntervalo() {
        return minimosIntervalo;
    }

    public long getGabaritoIntervalo() {
        return gabaritoIntervalo;
    }
}
