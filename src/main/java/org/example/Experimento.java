package org.example;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.Locale;

// Orquestrador dos testes de desempenho. Executa as baterias de operações, cronometra os tempos e exporta os resultados para CSV.
public class Experimento {
    private static final int[] TAMANHOS = {5000, 10000, 100000};
    private static final String[] ORDENS = {"crescente", "decrescente", "aleatoria"};
    private static final String[] ESTRUTURAS = {"ArrayLinear", "ArrayOrdenado", "ListaDuplamenteEncadeada",
            "TabelaHash", "ArvoreBinariaBusca", "ArvoreAVL", "ArvoreRubroNegra"};
    private static final String[] TESTES = {"construcao", "busca_presente", "busca_ausente",
            "insercao", "remocao", "intervalo"};
    private static final int REPETICOES = 5;

    private static long tempo;
    private static long resultado;
    private static Conjunto estruturaAtual;

    private static Conjunto criar(int estrutura, int n) {
        int capacidade = n + n / 10;

        switch (estrutura) {
            case 0:
                return new ArrayLinear(capacidade);
            case 1:
                return new ArrayOrdenado(capacidade);
            case 2:
                return new ListaDuplamenteEncadeada();
            case 3:
                return new TabelaHash(n);
            case 4:
                return new ArvoreBinariaBusca();
            case 5:
                return new ArvoreAVL();
            default:
                return new ArvoreRubroNegra();
        }
    }

    private static Conjunto preparar(int estrutura, int n, int[] ordem) {
        Conjunto conjunto = criar(estrutura, n);
        for (int i = 0; i < ordem.length; i++) {
            conjunto.inserir(ordem[i]);
        }
        return conjunto;
    }

    private static int operacoes(int teste, int n) {
        if (teste == 0) {
            return n;
        }
        if (teste == 1 || teste == 2) {
            return GeradorDados.BUSCAS;
        }
        if (teste == 3 || teste == 4) {
            return n / 10;
        }
        return GeradorDados.INTERVALOS;
    }

    private static void executar(int estrutura, int teste, int n, int[] ordem, GeradorDados dados) {
        long soma = 0;

        if (teste == 0) {
            long inicio = System.nanoTime();
            Conjunto conjunto = criar(estrutura, n);
            for (int i = 0; i < ordem.length; i++) {
                if (conjunto.inserir(ordem[i])) {
                    soma++;
                }
            }
            long fim = System.nanoTime();

            tempo = fim - inicio;
            resultado = soma;
            estruturaAtual = conjunto;
            return;
        }

        Conjunto conjunto = preparar(estrutura, n, ordem);
        int[] alvos;

        if (teste == 1) {
            alvos = dados.getBuscasPresentes();
        } else if (teste == 2) {
            alvos = dados.getBuscasAusentes();
        } else if (teste == 3) {
            alvos = dados.getInsercoes();
        } else if (teste == 4) {
            alvos = dados.getRemocoes();
        } else {
            alvos = dados.getMinimosIntervalo();
        }

        long inicio = System.nanoTime();
        if (teste == 1 || teste == 2) {
            for (int i = 0; i < alvos.length; i++) {
                if (conjunto.buscar(alvos[i])) {
                    soma++;
                }
            }
        } else if (teste == 3) {
            for (int i = 0; i < alvos.length; i++) {
                if (conjunto.inserir(alvos[i])) {
                    soma++;
                }
            }
        } else if (teste == 4) {
            for (int i = 0; i < alvos.length; i++) {
                if (conjunto.remover(alvos[i])) {
                    soma++;
                }
            }
        } else {
            for (int i = 0; i < alvos.length; i++) {
                soma += conjunto.contarIntervalo(alvos[i], alvos[i] + GeradorDados.LARGURA);
            }
        }
        long fim = System.nanoTime();

        tempo = fim - inicio;
        resultado = soma;
        estruturaAtual = conjunto;
    }

    private static void conferir(int estrutura, int teste, int n, GeradorDados dados) {
        long esperadoResultado;
        int esperadoTamanho;

        if (teste == 0) {
            esperadoResultado = n;
            esperadoTamanho = n;
        } else if (teste == 1) {
            esperadoResultado = GeradorDados.BUSCAS;
            esperadoTamanho = n;
        } else if (teste == 2) {
            esperadoResultado = 0;
            esperadoTamanho = n;
        } else if (teste == 3) {
            esperadoResultado = n / 10;
            esperadoTamanho = n + n / 10;
        } else if (teste == 4) {
            esperadoResultado = n / 10;
            esperadoTamanho = n - n / 10;
        } else {
            esperadoResultado = dados.getGabaritoIntervalo();
            esperadoTamanho = n;
        }

        if (resultado != esperadoResultado || estruturaAtual.tamanho() != esperadoTamanho) {
            throw new IllegalStateException("Conferencia falhou: " + ESTRUTURAS[estrutura] + " / "
                    + TESTES[teste] + " / n=" + n + " resultado=" + resultado + " esperado="
                    + esperadoResultado + " tamanho=" + estruturaAtual.tamanho() + " esperado="
                    + esperadoTamanho);
        }
    }

    private static void informacoesMaquina() {
        com.sun.management.OperatingSystemMXBean sistema =
                (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        System.out.println("Processador: " + System.getenv("PROCESSOR_IDENTIFIER"));
        System.out.println("Arquitetura: " + System.getProperty("os.arch"));
        System.out.println("Nucleos logicos: " + Runtime.getRuntime().availableProcessors());
        System.out.println("Memoria fisica (MB): " + sistema.getTotalPhysicalMemorySize() / (1024 * 1024));
        System.out.println("Memoria maxima da JVM (MB): " + Runtime.getRuntime().maxMemory() / (1024 * 1024));
        System.out.println("Sistema: " + System.getProperty("os.name") + " " + System.getProperty("os.version"));
        System.out.println("Java: " + System.getProperty("java.version"));
    }

    public static void main(String[] args) throws IOException {
        informacoesMaquina();

        StringBuilder brutos = new StringBuilder();
        StringBuilder resumo = new StringBuilder();
        brutos.append("tamanho;ordem;estrutura;teste;repeticao;operacoes;tempo_ns\n");
        resumo.append("tamanho;ordem;estrutura;teste;mediana_ms;mediana_ns_por_op\n");

        for (int t = 0; t < TAMANHOS.length; t++) {
            int n = TAMANHOS[t];
            GeradorDados dados = new GeradorDados(n);

            for (int o = 0; o < ORDENS.length; o++) {
                int[] ordem = dados.getOrdem(o);

                for (int e = 0; e < ESTRUTURAS.length; e++) {
                    for (int teste = 0; teste < TESTES.length; teste++) {
                        int ops = operacoes(teste, n);
                        long[] tempos = new long[REPETICOES];

                        executar(e, teste, n, ordem, dados);
                        conferir(e, teste, n, dados);

                        for (int r = 0; r < REPETICOES; r++) {
                            executar(e, teste, n, ordem, dados);
                            tempos[r] = tempo;
                            conferir(e, teste, n, dados);

                            brutos.append(n).append(";").append(ORDENS[o]).append(";")
                                    .append(ESTRUTURAS[e]).append(";").append(TESTES[teste]).append(";")
                                    .append(r + 1).append(";").append(ops).append(";")
                                    .append(tempos[r]).append("\n");
                        }

                        Arrays.sort(tempos);
                        long mediana = tempos[2];
                        double medianaMs = mediana / 1000000.0;
                        double medianaPorOp = mediana / (double) ops;

                        resumo.append(n).append(";").append(ORDENS[o]).append(";")
                                .append(ESTRUTURAS[e]).append(";").append(TESTES[teste]).append(";")
                                .append(String.format(Locale.US, "%.6f", medianaMs)).append(";")
                                .append(String.format(Locale.US, "%.3f", medianaPorOp)).append("\n");

                        System.out.println("n=" + n + " " + ORDENS[o] + " " + ESTRUTURAS[e] + " "
                                + TESTES[teste] + " ok");
                    }
                }
            }
        }

        FileWriter arquivoBrutos = new FileWriter("resultados_brutos.csv");
        arquivoBrutos.write(brutos.toString());
        arquivoBrutos.close();

        FileWriter arquivoResumo = new FileWriter("resumo.csv");
        arquivoResumo.write(resumo.toString());
        arquivoResumo.close();

        System.out.println("Arquivos gerados.");
    }
}
