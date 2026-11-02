package org.example;

import org.example.Conjunto;

// Implementação de conjunto utilizando um array não ordenado, dependente de busca sequencial.
public class ArrayLinear implements Conjunto {
    private int[] array;
    private int tamanho;

    public ArrayLinear(int capacidade) {
        array = new int[capacidade];
        tamanho = 0;
    }

    private int buscarIndice(int valor) {
        for(int i = 0; i < tamanho; i++) {
            if(array[i] == valor) {
                return i;
            }
        }
        return -1;
    }

    // Insere um novo valor no conjunto. Retorna falso caso o elemento já exista ou a capacidade limite seja atingida.
    public boolean inserir(int valor) {
        if(buscarIndice(valor) != -1) {
            return false;
        }
        if(tamanho == array.length) {
            return false;
        }
        array[tamanho] = valor;
        tamanho++;
        return true;
    }

    // Verifica a existência de um valor específico armazenado na estrutura.
    public boolean buscar(int valor) {
        return buscarIndice(valor) != -1;
    }

    // Remove o valor especificado, garantindo a integridade dos dados remanescentes.
    public boolean remover(int valor) {
        int indice = buscarIndice(valor);
        if(indice == -1) {
            return false;
        }
        array[indice] = array[tamanho - 1];
        tamanho--;
        return true;
    }

    // Contabiliza a quantidade exata de elementos presentes no intervalo fechado [minimo, maximo].
    public int contarIntervalo(int minimo, int maximo) {
        int contador = 0;
        for(int i = 0; i < tamanho; i++) {
            if(array[i] >= minimo && array[i] <= maximo) {
                contador++;
            }
        }
        return contador;
    }

    public int tamanho() {
        return tamanho;
    }
}
