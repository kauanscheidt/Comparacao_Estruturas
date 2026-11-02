package org.example;

// Interface base que define as operações fundamentais para a comparação de desempenho entre as estruturas.
public interface Conjunto {
    // Insere um novo valor no conjunto. Retorna falso caso o elemento já exista ou a capacidade limite seja atingida.
    boolean inserir(int valor);
    // Verifica a existência de um valor específico armazenado na estrutura.
    boolean buscar(int valor);
    // Remove o valor especificado, garantindo a integridade dos dados remanescentes.
    boolean remover(int valor);
    // Contabiliza a quantidade exata de elementos presentes no intervalo fechado [minimo, maximo].
    int contarIntervalo(int minimo, int maximo);
    int tamanho();
}
