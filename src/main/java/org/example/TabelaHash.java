package org.example;

import org.example.Conjunto;

// Tabela de espalhamento com tratamento de colisões por sondagem linear e redimensionamento dinâmico.
public class TabelaHash implements Conjunto {
    private final double FATOR_DE_CARGA = 0.7;
    private final byte VAZIO = 0;
    private final byte OCUPADO = 1;
    private final byte LAPIDE = 2;

    private int[] chaves;
    private byte[] estado;
    private int ocupados;
    private int lapides;

    public TabelaHash(int capacidadeInicial) {
        chaves = new int[capacidadeInicial];
        estado = new byte[capacidadeInicial];
        ocupados = 0;
        lapides = 0;
    }

    // Função de espalhamento baseada em multiplicação e máscara de bits para distribuir as chaves na estrutura.
    private int hash(int valor) {
        long h = (valor * 2654435761L) & 0x7fffffffL;
        return (int)(h % chaves.length);
    }

    // Insere um novo valor no conjunto. Retorna falso caso o elemento já exista ou a capacidade limite seja atingida.
    public boolean inserir(int valor) {
        int indice = hash(valor);
        int primeiraLapide = -1;

        while (estado[indice] != VAZIO) {
            if (estado[indice] == OCUPADO && chaves[indice] == valor) {
                return false;
            }
            if (estado[indice] == LAPIDE && primeiraLapide == -1) {
                primeiraLapide = indice;
            }
            indice = (indice + 1) % chaves.length;
        }

        if ((double) (ocupados + lapides + 1) / chaves.length > FATOR_DE_CARGA) {
            redimensionar();

            indice = hash(valor);
            primeiraLapide = -1;
            while (estado[indice] != VAZIO) {
                if (estado[indice] == LAPIDE && primeiraLapide == -1) {
                    primeiraLapide = indice;
                }
                indice = (indice + 1) % chaves.length;
            }
        }
        
        if (primeiraLapide != -1) {
            indice = primeiraLapide;
            lapides--;
        }
        chaves[indice] = valor;
        estado[indice] = OCUPADO;
        ocupados++;
        return true;
    }

    private int buscarIndice(int valor) {
        int indice = hash(valor);

        while(estado[indice] != VAZIO) {
            if(estado[indice] == OCUPADO && chaves[indice] == valor) {
                return indice;
            }
            indice = (indice + 1) % chaves.length;
        }
        return -1;
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
        estado[indice] = LAPIDE;
        ocupados--;
        lapides++;
        return true;
    }

    // Contabiliza a quantidade exata de elementos presentes no intervalo fechado [minimo, maximo].
    public int contarIntervalo(int minimo, int maximo) {
        int contador = 0;
        for(int v = minimo; v <= maximo; v++) {
            if(buscarIndice(v) != -1) {
                contador++;
            }
        }
        return contador;
    }

    // Amplia a capacidade da tabela e realoca os elementos existentes para preservar o fator de carga configurado.
    private void redimensionar() {
        int[] antigasChaves = chaves;
        byte[] antigoEstado = estado;

        chaves = new int[antigasChaves.length * 2];
        estado = new byte[antigasChaves.length * 2];
        ocupados = 0;
        lapides = 0;

        for(int i = 0; i < antigasChaves.length; i++) {
            if(antigoEstado[i] == OCUPADO) {
                inserirSemVerificar(antigasChaves[i]);
            }
        }
    }

    private void inserirSemVerificar(int valor) {
        int indice = hash(valor);
        while(estado[indice] != VAZIO) {
            indice = (indice + 1) % chaves.length;
        }
        chaves[indice] = valor;
        estado[indice] = OCUPADO;
        ocupados++;
    }

    public int tamanho() {
        return ocupados;
    }
}
