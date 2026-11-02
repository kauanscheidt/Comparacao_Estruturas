package org.example;

public class BuscaBinaria {

    public static int BuscaBinaria(int[] array, int valorProcurado) {
        int inicio = 0;
        int fim = array.length - 1;
        int meio;

        while (inicio <= fim) {
            meio = inicio + (fim - inicio) / 2;

            if (array[meio] == valorProcurado) {
                return meio;
            }
            if (valorProcurado > array[meio]) {
                inicio = meio + 1;
            } else {
                fim = meio - 1;
            }
        }

        return -1;
    }

}
