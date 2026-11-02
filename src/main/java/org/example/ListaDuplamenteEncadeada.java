package org.example;

import org.example.Conjunto;

// Estrutura de dados baseada em nós encadeados nas duas direções, permitindo travessia bidirecional.
public class ListaDuplamenteEncadeada implements Conjunto {
    private NodoDuplo inicio;
    private NodoDuplo fim;
    private int tamanho;

    public ListaDuplamenteEncadeada() {
        inicio = null;
        fim = null;
        tamanho = 0;
    }
    public NodoDuplo getInicio() {
        return inicio;
    }
    public NodoDuplo getFim() {
        return fim;
    }
    public boolean vazia() {
        return inicio == null;
    }

    public void inserirInicio(int dado) {
        NodoDuplo novoNodo = new NodoDuplo(dado);
        tamanho++;

        if(vazia()) {
            inicio = novoNodo;
            fim = novoNodo;
            return;
        }

        novoNodo.setProx(inicio);
        inicio.setAnt(novoNodo);
        inicio = novoNodo;
    }
    public void inserirFInal(int dado) {
        if(vazia()) {
            inserirInicio(dado);
            return;
        }

        NodoDuplo novoNodo =  new NodoDuplo(dado);
        fim.setProx(novoNodo);
        novoNodo.setAnt(fim);
        fim = novoNodo;
        tamanho++;
    }

    // Insere um novo valor no conjunto. Retorna falso caso o elemento já exista ou a capacidade limite seja atingida.
    public boolean inserir(int valor) {
        if(buscar(valor)) {
            return false;
        }
        inserirFInal(valor);
        return true;
    }

    // Verifica a existência de um valor específico armazenado na estrutura.
    public boolean buscar(int valor) {
        NodoDuplo aux = inicio;
        while(aux != null) {
            if(aux.getDado() == valor) {
                return true;
            }
            aux = aux.getProx();
        }
        return false;
    }

    // Remove o valor especificado, garantindo a integridade dos dados remanescentes.
    public boolean remover(int valor) {
        return removerValor(valor);
    }

    public boolean removerValor(int valor) {
        if (vazia()) return false;

        if(inicio.getDado() == valor) {
            if(inicio== fim) {
                inicio = null;
                fim = null;
                tamanho--;
                return true;
            }
            inicio = inicio.getProx();
            inicio.setAnt(null);
            tamanho--;
            return true;
        }

        if(fim.getDado() == valor) {
            fim = fim.getAnt();
            fim.setProx(null);
            tamanho--;
            return true;
        }
        NodoDuplo aux = inicio.getProx();
        while(aux != null) {
            if(aux.getDado() == valor) {
                aux.getAnt().setProx(aux.getProx());
                aux.getProx().setAnt(aux.getAnt());
                tamanho--;
                return true;
            }
            aux = aux.getProx();
        }
        return false;
    }

    // Contabiliza a quantidade exata de elementos presentes no intervalo fechado [minimo, maximo].
    public int contarIntervalo(int minimo, int maximo) {
        int contador = 0;
        NodoDuplo aux = inicio;
        while(aux != null) {
            if(aux.getDado() >= minimo && aux.getDado() <= maximo) {
                contador++;
            }
            aux = aux.getProx();
        }
        return contador;
    }

    public int tamanho() {
        return tamanho;
    }

    public void mostrarLista() {
        if(vazia()) {
            System.out.println("lista vazia");
            return;
        }
        NodoDuplo aux = inicio;
        while (aux != null) {
            System.out.println(aux.getDado());
            aux= aux.getProx();
        }
    }
    public void mostrarInverso() {
        if(vazia()) {
            System.out.println("lista vazia");
            return;
        }
        NodoDuplo aux = fim;
        while (aux != null) {
            System.out.print(aux.getDado() + " ");
            aux= aux.getAnt();
        }
    }
    public void maiorMenor() {
        if(vazia()) {
            System.out.println("lista vazia");
            return;
        }
        int maior = inicio.getDado();
        int menor = inicio.getDado();
        NodoDuplo aux = inicio;

        while(aux != null) {
            if(aux.getDado() > maior) {
                maior = aux.getDado();
            }
            if(aux.getDado() < menor) {
                menor = aux.getDado();
            }
            aux = aux.getProx();
        }
        System.out.println("maior: "+ maior + " menor: " + menor);
    }
}
