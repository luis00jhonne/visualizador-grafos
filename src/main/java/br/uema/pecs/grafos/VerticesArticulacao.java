package br.uema.pecs.grafos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VerticesArticulacao {
    private final MatrizAdjacencia matriz;
    private final int[] tempoDescoberta;
    private final int[] menorTempoDescoberta;
    private final boolean[] verticesVisitados;
    private int tempo = 0; // Variável para armazenar o tempo de descoberta

    public VerticesArticulacao(MatrizAdjacencia matrizAdjacencia) {
        matriz = matrizAdjacencia;
        tempoDescoberta = new int[matriz.getTamanho()];
        menorTempoDescoberta = new int[matriz.getTamanho()];
        verticesVisitados = new boolean[matriz.getTamanho()];
        Arrays.fill(tempoDescoberta, -1); // Inicializa os tempos de descoberta como -1 (não visitado)
    }

    public List<String> encontrarVerticesArticulacao() {
        List<String> verticesArticulacao = new ArrayList<>();
        for (int x = 0; x < matriz.getTamanho(); x++) {
            if (!verticesVisitados[x]) {
                buscaEmProfundidade(x, -1, verticesArticulacao);
            }
        }
        return verticesArticulacao;
    }

    private void buscaEmProfundidade(int x, int verticePai, List<String> verticesArticulacao) {
        int filhos = 0;
        verticesVisitados[x] = true;
        tempoDescoberta[x] = menorTempoDescoberta[x] = ++tempo;

        for (int y = 0; y < matriz.getTamanho(); y++) {
            if (matriz.verificarAdjacencia(x, y)){
                if (!verticesVisitados[y]) {
                    filhos++;
                    buscaEmProfundidade(y, x, verticesArticulacao);

                    //Atualizar o tempo de descoberta
                    menorTempoDescoberta[x] = Math.min(menorTempoDescoberta[x], menorTempoDescoberta[y]);

                    // Condição para ser um vértice de articulação
                    //Se o tempo de menor tempo de descoberta do vértice for maior ou igual ao tempo de descoberta de um vértice,
                    // aquele vértice é ponto de articulação.
                    if (verticePai != -1 && menorTempoDescoberta[y] >= tempoDescoberta[x]) {
                        verticesArticulacao.add(matriz.getVertice(x));
                    }
                } else if (y != verticePai) {
                    menorTempoDescoberta[x] = Math.min(menorTempoDescoberta[x], tempoDescoberta[y]);
                }
            }
        }

        // Verifica se o vértice raiz é um ponto de articulação
        if (verticePai == -1 && filhos > 1) {
            verticesArticulacao.add(matriz.getVertice(x));
        }
    }
}
