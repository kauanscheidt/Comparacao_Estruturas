package org.example;

import org.example.Conjunto;

// Árvore binária de busca padrão, sem mecanismos de balanceamento automático da altura.
public class ArvoreBinariaBusca implements Conjunto {
    private class No{
        int valor;
        No dir;
        No esq;

        public No(int valor) {
            this.valor = valor;
            dir = esq = null;
        }
    }

    No raiz;
    private int tamanho;
    private No[] pilha;

    // Insere um novo valor no conjunto. Retorna falso caso o elemento já exista ou a capacidade limite seja atingida.
    public boolean inserir(int valor) {
        if(raiz == null) {
            raiz = new No(valor);
            tamanho++;
            return true;
        }

        No atual = raiz;
        while(true) {
            if(valor < atual.valor) {
                if(atual.esq == null) {
                    atual.esq = new No(valor);
                    tamanho++;
                    return true;
                }
                atual = atual.esq;
            } else if(valor > atual.valor) {
                if(atual.dir == null) {
                    atual.dir = new No(valor);
                    tamanho++;
                    return true;
                }
                atual = atual.dir;
            } else {
                return false;
            }
        }
    }

    // Verifica a existência de um valor específico armazenado na estrutura.
    public boolean buscar(int valor) {
        No atual = raiz;

        while(atual != null) {
            if(valor == atual.valor) {
                return true;
            }

            if(valor < atual.valor) {
                atual = atual.esq;
            }else {
                atual = atual.dir;
            }
        }

        return false;
    }

    // Remove o valor especificado, garantindo a integridade dos dados remanescentes.
    public boolean remover(int valor) {
        No pai = null;
        No atual = raiz;

        // procurando pelo no
        while(atual != null && atual.valor != valor) {
            pai = atual;
            if(valor < atual.valor) {
                atual = atual.esq;
            }else {
                atual = atual.dir;
            }
        }
        if(atual == null) {
            return false;
        }

        //caso 3: possui dois filhos, troca pelo sucessor
        if(atual.esq != null && atual.dir != null) {
            No paiSucessor = atual;
            No sucessor = atual.dir;

            while(sucessor.esq != null) {
                paiSucessor = sucessor;
                sucessor = sucessor.esq;
            }
            atual.valor = sucessor.valor;
            pai = paiSucessor;
            atual = sucessor;
        }

        //caso 1 e 2: folha ou um filho
        No filho;
        if(atual.esq != null) {
            filho = atual.esq;
        }else {
            filho = atual.dir;
        }

        if(pai == null) {
            raiz = filho;
        }else if(pai.esq == atual) {
            pai.esq = filho;
        }else {
            pai.dir = filho;
        }

        tamanho--;
        return true;
    }

    // Contabiliza a quantidade exata de elementos presentes no intervalo fechado [minimo, maximo].
    public int contarIntervalo(int minimo, int maximo) {
        if(pilha == null || pilha.length < tamanho + 1) {
            pilha = new No[tamanho + 1];
        }
        int topo = 0;
        int contador = 0;
        No atual = raiz;

        while(atual != null || topo > 0) {
            while(atual != null) {
                if(atual.valor >= minimo) {
                    pilha[topo] = atual;
                    topo++;
                    atual = atual.esq;
                }else {
                    atual = atual.dir;
                }
            }
            if(topo == 0) {
                break;
            }
            topo--;
            atual = pilha[topo];
            if(atual.valor > maximo) {
                break;
            }
            contador++;
            atual = atual.dir;
        }
        return contador;
    }

    public int tamanho() {
        return tamanho;
    }

    public void emOrdem() {
        emOrdem(raiz);
    }

    private void emOrdem(No no) {
        if(no != null) {
            emOrdem(no.esq);
            System.out.print(no.valor + " ");
            emOrdem(no.dir);
        }
    }

    public int MenorValor() {
        No no = raiz;
        while(no.esq != null) {
            no = no.esq;
        }
        return no.valor;
    }

    public int MaiorValor() {
        No no = raiz;
        while(no.dir != null) {
            no = no.dir;
        }
        return no.valor;
    }

    public int nivelDoNo(int valor) {
        No atual = raiz;
        int nivel = 0;

        while(atual != null) {
            if(valor == atual.valor) {
                return nivel;
            }
            if(valor < atual.valor) {
                atual = atual.esq;
            } else {
                atual = atual.dir;
            }
            nivel++;
        }

        return -1;
    }
}
