package br.uema.pecs.grafos;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class MatrizAdjacencia {

    private final TipoGrafo tipoGrafo;

    private final List<String> vertices;
    private final int[][] matriz;

    public MatrizAdjacencia(Arquivo arquivo) {
        tipoGrafo = TipoGrafo.valueOfSigla(arquivo.getPrimeiraLinha());
        if (tipoGrafo == null){
            throw new RuntimeException("Tipo de Grafo inválido.");
        }
        vertices = new ArrayList<>();
        matriz = new int[arquivo.getTotalLinhas()][arquivo.getTotalLinhas()];
        try (BufferedReader reader = arquivo.lerArquivo() ) {
            reader.readLine(); //passar primeira linha
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] verticesLinha = linha.split(",");
                if (!vertices.contains(verticesLinha[0])){
                    vertices.add(verticesLinha[0]);
                }
                if (!vertices.contains(verticesLinha[1])){
                    vertices.add(verticesLinha[1]);
                }
                adicionarAresta(vertices.indexOf(verticesLinha[0]), vertices.indexOf(verticesLinha[1]));
            }
        } catch (Exception e){
            System.err.println("Erro ao criar a matriz de adjacência para o arquivo: " + e.getMessage());
        }
    }

    public void adicionarAresta(int verticeOrigem, int verticeDestino) {
        matriz[verticeOrigem][verticeDestino] = 1;
        if (tipoGrafo == TipoGrafo.NAO_DIRIGIDO){
            matriz[verticeDestino][verticeOrigem] = 1;
        }
    }

    public String imprimirMatriz() {
        StringBuilder sb = new StringBuilder();
        for (int[] linhas : matriz) {
            for (int coluna : linhas) {
                sb.append(coluna).append(" ");
            }
            sb.append(System.getProperty("line.separator"));
        }
        return sb.toString();
    }

    public boolean verificarAdjacencia(int vertice1, int vertice2) {
        boolean existeVerticeEntre1e2 = matriz[vertice1][vertice2] == 1;
        boolean existeVerticeEntre2e1 = tipoGrafo == TipoGrafo.NAO_DIRIGIDO && matriz[vertice2][vertice1] == 1;
        return existeVerticeEntre1e2 || existeVerticeEntre2e1;
    }

    public String grauVertice(int vertice){
        StringBuilder builder = new StringBuilder();
        int grau = 0;
        for (int y = 0; y < matriz.length; y++) {
            grau += matriz[vertice][y];
        }
        builder.append("Vértice: ").append(vertices.get(vertice))
                .append(", Grau: ").append(grau);
        return builder.toString();
    }

    public String vizinhosVertice(String vertice) {
        StringBuilder builder = new StringBuilder();
        for (int y = 0; y < matriz.length; y++) {
             if (matriz[vertices.indexOf(vertice)][y] == 1) {
                builder.append("Vizinho: ").append(vertices.get(y))
                       .append(System.getProperty("line.separator"));
            }
        }
        return builder.toString();
    }

    public String visitarTodasArestas(){
        int count = 0;
        StringBuilder builder = new StringBuilder();
        int[][] matrizAux = new int[matriz.length][matriz.length];
        for (int x = 0; x < matrizAux.length; x++) {
            for (int y = x; y < matrizAux.length; y++) {
                matrizAux[x][y] = matriz[x][y];
                int value = matrizAux[x][y];
                if (value != 0) {
                    count++;
                    builder.append("Aresta: ").append(vertices.get(x))
                            .append(tipoGrafo == TipoGrafo.DIRIGIDO ? "->" : "<->").append(vertices.get(y))
                            .append(";")
                            .append(System.getProperty("line.separator"));
                    matrizAux[y][x] = 0;
                }
            }
        }
        return "Número de Arestas: " + count + System.getProperty("line.separator") +
                builder.toString();
    }


    public TipoGrafo getTipoGrafo(){
        return tipoGrafo;
    }

    public String getVertice(int posicao){
        try{
            return vertices.get(posicao);
        } catch (IndexOutOfBoundsException boundsException){
            return null;
        }
    }

    public int getPosicaoVertice(String vertice){
        return vertices.indexOf(vertice);
    }

    public int getTamanho(){
        if (matriz != null) return matriz.length;
        return 0;
    }
}
