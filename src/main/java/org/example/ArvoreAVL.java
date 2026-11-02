package org.example;

import org.example.Conjunto;

// Árvore binária de busca (AVL), que ajusta as subárvores estruturalmente para manter o balanceamento contínuo.
public class ArvoreAVL implements Conjunto {
    private class No {
        int valor;
        No esq, dir;
        int alturaEsq, alturaDir;

        public No(int valor) {
            this.valor = valor;
            esq = dir = null;
            alturaEsq = alturaDir = 0;
        }
    }

    private No raiz;
    private int tamanho;
    private boolean alterou;

    public ArvoreAVL() {
        raiz = null;
        tamanho = 0;
    }

    // Insere um novo valor no conjunto. Retorna falso caso o elemento já exista ou a capacidade limite seja atingida.
    public boolean inserir(int valor) {
        alterou = false;
        raiz = inserirNo(raiz, valor);
        if (alterou) {
            tamanho++;
        }
        return alterou;
    }

    private No inserirNo(No atual, int valor) {
        if (atual == null) {
            alterou = true;
            return new No(valor);
        }

        if (valor < atual.valor) {
            atual.esq = inserirNo(atual.esq, valor);
        } else if (valor > atual.valor) {
            atual.dir = inserirNo(atual.dir, valor);
        } else {
            return atual;
        }

        atualizarAltura(atual);
        return balancear(atual);
    }

    // Verifica a existência de um valor específico armazenado na estrutura.
    public boolean buscar(int valor) {
        No atual = raiz;

        while (atual != null) {
            if (valor == atual.valor) {
                return true;
            }
            if (valor < atual.valor) {
                atual = atual.esq;
            } else {
                atual = atual.dir;
            }
        }
        return false;
    }

    // Remove o valor especificado, garantindo a integridade dos dados remanescentes.
    public boolean remover(int valor) {
        alterou = false;
        raiz = removerNo(raiz, valor);
        if (alterou) {
            tamanho--;
        }
        return alterou;
    }

    private No removerNo(No atual, int valor) {
        if (atual == null) {
            return null;
        }

        if (valor < atual.valor) {
            atual.esq = removerNo(atual.esq, valor);
        } else if (valor > atual.valor) {
            atual.dir = removerNo(atual.dir, valor);
        } else {
            alterou = true;

            if (atual.esq == null) {
                return atual.dir;
            }
            if (atual.dir == null) {
                return atual.esq;
            }

            No sucessor = atual.dir;
            while (sucessor.esq != null) {
                sucessor = sucessor.esq;
            }
            atual.valor = sucessor.valor;
            atual.dir = removerNo(atual.dir, sucessor.valor);
        }

        atualizarAltura(atual);
        return balancear(atual);
    }

    // Contabiliza a quantidade exata de elementos presentes no intervalo fechado [minimo, maximo].
    public int contarIntervalo(int minimo, int maximo) {
        return contarIntervalo(raiz, minimo, maximo);
    }

    private int contarIntervalo(No no, int minimo, int maximo) {
        if (no == null) {
            return 0;
        }
        if (no.valor < minimo) {
            return contarIntervalo(no.dir, minimo, maximo);
        }
        if (no.valor > maximo) {
            return contarIntervalo(no.esq, minimo, maximo);
        }
        return 1 + contarIntervalo(no.esq, minimo, maximo) + contarIntervalo(no.dir, minimo, maximo);
    }

    public int tamanho() {
        return tamanho;
    }

    // Restabelece as propriedades de balanceamento da árvore através de rotações e recoloração dos nós.
    private No balancear(No atual) {
        int fatorBalanceamento = atual.alturaDir - atual.alturaEsq;

        if (fatorBalanceamento == 2) {
            if (atual.dir.alturaDir >= atual.dir.alturaEsq) {
                return rotacaoEsquerda(atual);
            } else {
                atual.dir = rotacaoDireita(atual.dir);
                return rotacaoEsquerda(atual);
            }

        } else if (fatorBalanceamento == -2) {
            if (atual.esq.alturaEsq >= atual.esq.alturaDir) {
                return rotacaoDireita(atual);
            } else {
                atual.esq = rotacaoEsquerda(atual.esq);
                return rotacaoDireita(atual);
            }
        }

        return atual;
    }

    private No rotacaoEsquerda(No raiz) {
        No novaRaiz = raiz.dir;
        raiz.dir = novaRaiz.esq;
        novaRaiz.esq = raiz;

        atualizarAltura(raiz);
        atualizarAltura(novaRaiz);

        return novaRaiz;
    }

    private No rotacaoDireita(No raiz) {
        No novaRaiz = raiz.esq;
        raiz.esq = novaRaiz.dir;
        novaRaiz.dir = raiz;

        atualizarAltura(raiz);
        atualizarAltura(novaRaiz);

        return novaRaiz;
    }

    private void atualizarAltura(No no) {
        if(no !=null) {
            no.alturaEsq = (no.esq == null) ? 0: Math.max(no.esq.alturaEsq, no.esq.alturaDir) +1;

            no.alturaDir = (no.dir == null) ? 0: Math.max(no.dir.alturaEsq, no.dir.alturaDir) +1;
        }
    }

    public void mostrarFB() {
        mostrarFB(raiz);
    }

    //Mostrar árvore e fator de balancemanto//
    private void mostrarFB(No no) {
        if (no != null) {
            mostrarFB(no.esq);
            int fatorBalancemanto = no.alturaDir - no.alturaEsq;
            System.out.println("No:" + no.valor + " Fator de balanceamento:" + fatorBalancemanto);
            mostrarFB(no.dir);
        }
    }
}
