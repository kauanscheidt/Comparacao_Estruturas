package org.example;

import org.example.Conjunto;

// Implementação baseada em array mantido em ordem crescente, otimizada com busca binária.
public class ArrayOrdenado implements Conjunto {
    private int[] array;
    private int tamanho;

    public ArrayOrdenado(int capacidade) {
        array = new int[capacidade];
        tamanho = 0;
    }

    // Busca binária auxiliar que localiza o índice exato para inserção ou o limite inferior de um intervalo de busca.
    private int primeiroMaiorOuIgual(int valor) {
        int inicio = 0;
        int fim = tamanho;

        while(inicio < fim) {
            int meio = inicio + (fim - inicio)/2;
            if(array[meio] < valor) {
                inicio = meio + 1;
            }else {
                fim = meio;
            }
        }
        return inicio;
    }

    // Insere um novo valor no conjunto. Retorna falso caso o elemento já exista ou a capacidade limite seja atingida.
    public boolean inserir(int valor) {
        int posicao = primeiroMaiorOuIgual(valor);

        if(posicao < tamanho && array[posicao] == valor) {
            return false;
        }
        if(tamanho == array.length) {
            return false;
        }
        for(int i = tamanho; i > posicao; i--) {
            array[i] = array[i - 1];
        }
        array[posicao] = valor;
        tamanho++;
        return true;
    }

    // Verifica a existência de um valor específico armazenado na estrutura.
    public boolean buscar(int valor) {
        int posicao = primeiroMaiorOuIgual(valor);
        return posicao < tamanho && array[posicao] == valor;
    }

    // Remove o valor especificado, garantindo a integridade dos dados remanescentes.
    public boolean remover(int valor) {
        int posicao = primeiroMaiorOuIgual(valor);

        if(posicao >= tamanho || array[posicao] != valor) {
            return false;
        }
        for(int i = posicao; i < tamanho - 1; i++) {
            array[i] = array[i + 1];
        }
        tamanho--;
        return true;
    }

    // Contabiliza a quantidade exata de elementos presentes no intervalo fechado [minimo, maximo].
    public int contarIntervalo(int minimo, int maximo) {
        return primeiroMaiorOuIgual(maximo + 1) - primeiroMaiorOuIgual(minimo);
    }

    public int tamanho() {
        return tamanho;
    }
}
