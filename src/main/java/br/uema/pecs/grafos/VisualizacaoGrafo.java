package br.uema.pecs.grafos;

import org.graphper.api.*;
import org.graphper.api.attributes.ArrowShape;
import org.graphper.api.attributes.Color;
import org.graphper.api.attributes.NodeShapeEnum;
import org.graphper.api.attributes.Rank;
import org.graphper.draw.ExecuteException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VisualizacaoGrafo {

    private final MatrizAdjacencia matriz;

    public VisualizacaoGrafo(MatrizAdjacencia matrizAdjacencia){
        this.matriz = matrizAdjacencia;
    }

    public String gerarImagem() throws ExecuteException, IOException {

        List<Node> nodes = new ArrayList<>();
        for (int x = 0; x < matriz.getTamanho(); x++) {
            var vertice = matriz.getVertice(x);
            if (vertice != null){
                Node node = Node.builder().label(matriz.getVertice(x)).build();
                nodes.add(node);
            }
        }

        Graphviz.GraphvizBuilder graphvizBuilder;
        if (matriz.getTipoGrafo() == TipoGrafo.NAO_DIRIGIDO){
            graphvizBuilder = Graphviz.graph();
        } else {
            graphvizBuilder = Graphviz.digraph();
        }
        graphvizBuilder.tempNode(Node.builder().shape(NodeShapeEnum.RECT).build());

        for (int x = 0; x < matriz.getTamanho(); x++) {
            for (int y = x; y < matriz.getTamanho(); y++) {
                if (matriz.verificarAdjacencia(x, y)){
                    graphvizBuilder.addLine(nodes.get(x), nodes.get(y));
                }
            }
        }
        String path = "./";
        String nomeImagem = "example";
        graphvizBuilder.build().toFile(FileType.PNG).save(path, nomeImagem);
        return path + nomeImagem + ".png";
    }
}
