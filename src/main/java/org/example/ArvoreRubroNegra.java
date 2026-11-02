package org.example;

import org.example.Conjunto;

// Árvore balanceada por sistema de cores, limitando a quantidade de rotações necessárias após atualizações.
public class ArvoreRubroNegra implements Conjunto {
    private enum Cor{
        VERMELHO,
        PRETO
    }

    private class No{
        private int valor;
        private Cor cor;
        private No esq, dir, pai;

        public No(int valor){
            this.valor = valor;
            cor = Cor.VERMELHO;
            esq = dir = pai = null;
        }
    }
    private No raiz;
    private int tamanho;

    public ArvoreRubroNegra(){
        raiz = null;
        tamanho = 0;
    }

    // Insere um novo valor no conjunto. Retorna falso caso o elemento já exista ou a capacidade limite seja atingida.
    public boolean inserir(int valor){
        No pai = null;
        No atual = raiz;

        while (atual != null) {
            pai = atual;
            if (valor < atual.valor) {
                atual = atual.esq;
            } else if (valor > atual.valor) {
                atual = atual.dir;
            } else {
                return false;
            }
        }

        No novoNo = new No(valor);
        novoNo.pai = pai;

        if (pai == null) {
            raiz = novoNo;
        } else if (valor < pai.valor) {
            pai.esq = novoNo;
        } else {
            pai.dir = novoNo;
        }

        tamanho++;
        corrigirinsercao(novoNo);
        return true;
    }

    // Restabelece as propriedades de balanceamento da árvore através de rotações e recoloração dos nós.
    private void corrigirinsercao(No no){
        while (no != raiz && cor(no.pai) == Cor.VERMELHO){
            No pai = no.pai;
            No avo = pai.pai;

            if (pai == avo.esq) {
                No tio = avo.dir;

                if(cor(tio) == Cor.VERMELHO){
                    pai.cor = Cor.PRETO;
                    tio.cor = Cor.PRETO;
                    avo.cor = Cor.VERMELHO;
                    no = avo;

                }else {
                    if (no == pai.dir) {
                        no = pai;
                        rotacaoEsquerda(no);

                        pai = no.pai;
                        avo = pai.pai;
                    }

                    pai.cor = Cor.PRETO;
                    avo.cor = Cor.VERMELHO;
                    rotacaoDireita(avo);
                }
            }else {
                No tio = avo.esq;
                if (cor(tio) == Cor.VERMELHO) {
                    pai.cor = Cor.PRETO;
                    tio.cor = Cor.PRETO;
                    avo.cor = Cor.VERMELHO;
                    no = avo;

                } else {
                    if (no == pai.esq){
                        no = pai;
                        rotacaoDireita(no);

                        pai = no.pai;
                        avo = pai.pai;
                    }
                    pai.cor = Cor.PRETO;
                    avo.cor = Cor.VERMELHO;
                    rotacaoEsquerda(avo);
                }

            }
        }

        raiz.cor = Cor.PRETO;
    }

    // Remove o valor especificado, garantindo a integridade dos dados remanescentes.
    public boolean remover(int valor){
        No z = buscarNo(valor);
        if (z == null) {
            return false;
        }

        No y = z;
        Cor corOriginal = y.cor;
        No x;
        No paiX;

        if (z.esq == null) {
            x = z.dir;
            paiX = z.pai;
            transplantar(z, z.dir);
        } else if (z.dir == null) {
            x = z.esq;
            paiX = z.pai;
            transplantar(z, z.esq);
        } else {
            y = z.dir;
            while (y.esq != null) {
                y = y.esq;
            }
            corOriginal = y.cor;
            x = y.dir;

            if (y.pai == z) {
                paiX = y;
            } else {
                paiX = y.pai;
                transplantar(y, y.dir);
                y.dir = z.dir;
                y.dir.pai = y;
            }

            transplantar(z, y);
            y.esq = z.esq;
            y.esq.pai = y;
            y.cor = z.cor;
        }

        tamanho--;

        if (corOriginal == Cor.PRETO) {
            corrigirRemocao(x, paiX);
        }
        return true;
    }

    private void transplantar(No u, No v){
        if (u.pai == null) {
            raiz = v;
        } else if (u == u.pai.esq) {
            u.pai.esq = v;
        } else {
            u.pai.dir = v;
        }
        if (v != null) {
            v.pai = u.pai;
        }
    }

    // Restabelece as propriedades de balanceamento da árvore através de rotações e recoloração dos nós.
    private void corrigirRemocao(No x, No pai){
        while (x != raiz && cor(x) == Cor.PRETO) {
            if (x == pai.esq) {
                No irmao = pai.dir;

                if (cor(irmao) == Cor.VERMELHO) {
                    irmao.cor = Cor.PRETO;
                    pai.cor = Cor.VERMELHO;
                    rotacaoEsquerda(pai);
                    irmao = pai.dir;
                }

                if (cor(irmao.esq) == Cor.PRETO && cor(irmao.dir) == Cor.PRETO) {
                    irmao.cor = Cor.VERMELHO;
                    x = pai;
                    pai = x.pai;
                } else {
                    if (cor(irmao.dir) == Cor.PRETO) {
                        irmao.esq.cor = Cor.PRETO;
                        irmao.cor = Cor.VERMELHO;
                        rotacaoDireita(irmao);
                        irmao = pai.dir;
                    }
                    irmao.cor = pai.cor;
                    pai.cor = Cor.PRETO;
                    irmao.dir.cor = Cor.PRETO;
                    rotacaoEsquerda(pai);
                    x = raiz;
                }
            } else {
                No irmao = pai.esq;

                if (cor(irmao) == Cor.VERMELHO) {
                    irmao.cor = Cor.PRETO;
                    pai.cor = Cor.VERMELHO;
                    rotacaoDireita(pai);
                    irmao = pai.esq;
                }

                if (cor(irmao.esq) == Cor.PRETO && cor(irmao.dir) == Cor.PRETO) {
                    irmao.cor = Cor.VERMELHO;
                    x = pai;
                    pai = x.pai;
                } else {
                    if (cor(irmao.esq) == Cor.PRETO) {
                        irmao.dir.cor = Cor.PRETO;
                        irmao.cor = Cor.VERMELHO;
                        rotacaoEsquerda(irmao);
                        irmao = pai.esq;
                    }
                    irmao.cor = pai.cor;
                    pai.cor = Cor.PRETO;
                    irmao.esq.cor = Cor.PRETO;
                    rotacaoDireita(pai);
                    x = raiz;
                }
            }
        }

        if (x != null) {
            x.cor = Cor.PRETO;
        }
    }

    private void rotacaoEsquerda(No no){
        No novaRaiz = no.dir;
        no.dir = novaRaiz.esq;
        if(novaRaiz.esq != null){
            novaRaiz.esq.pai = no;
        }
        novaRaiz.pai = no.pai;
        if (no.pai == null) {
            raiz = novaRaiz;
        } else if (no == no.pai.esq){
            no.pai.esq = novaRaiz;
        } else {
            no.pai.dir = novaRaiz;
        }

        novaRaiz.esq = no;
        no.pai = novaRaiz;
    }

    private void rotacaoDireita(No no){
        No novaRaiz = no.esq;
        no.esq = novaRaiz.dir;
        if(novaRaiz.dir != null){
            novaRaiz.dir.pai = no;
        }
        novaRaiz.pai = no.pai;
        if (no.pai == null) {
            raiz = novaRaiz;
        } else if (no == no.pai.dir){
            no.pai.dir = novaRaiz;
        } else {
            no.pai.esq = novaRaiz;
        }

        novaRaiz.dir = no;
        no.pai = novaRaiz;
    }

    // Verifica a existência de um valor específico armazenado na estrutura.
    public boolean buscar(int valor){
        return buscarNo(valor) != null;
    }

    private No buscarNo(int valor){
        No atual = raiz;

        while (atual != null) {
            if (valor == atual.valor) {
                return atual;
            }
            if (valor < atual.valor) {
                atual = atual.esq;
            } else {
                atual = atual.dir;
            }
        }
        return null;
    }

    // Contabiliza a quantidade exata de elementos presentes no intervalo fechado [minimo, maximo].
    public int contarIntervalo(int minimo, int maximo){
        return contarIntervalo(raiz, minimo, maximo);
    }

    private int contarIntervalo(No no, int minimo, int maximo){
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

    public int tamanho(){
        return tamanho;
    }

    private Cor cor (No no){
        return no == null ? Cor.PRETO: no.cor;

    }

    public void mostrar() {
        mostrar(raiz);
    }

    private void mostrar (No raiz){
        if (raiz != null){
            mostrar(raiz.esq);
            System.out.println(raiz.valor+" ("+raiz.cor+")");
            mostrar(raiz.dir);
        }

    }
}
