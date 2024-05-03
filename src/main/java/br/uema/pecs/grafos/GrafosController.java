package br.uema.pecs.grafos;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GrafosController {

    private final String NEW_LINE_SEPARATOR = System.getProperty("line.separator");

    @FXML
    private MenuItem fileAbrir;
    @FXML
    private Menu menuGrafos;

    @FXML
    private Button buttonAbrirArquivo;
    @FXML
    private Button buttonInformacoesGrafo;
    @FXML
    private Button buttonVisualizar;
    @FXML
    private Button buttonVerticesArticulacao;

    @FXML
    private TabPane tabsConteudo;
    @FXML
    private Tab tabArquivo;
    @FXML
    private Text conteudoArquivo;
    @FXML
    private Tab tabGrafo;
    @FXML
    private Text conteudoGrafo;
    @FXML
    private ImageView imagemGrafo;
    @FXML
    private Label statusInferiorDireito;

    private Arquivo arquivo;
    private MatrizAdjacencia matrizAdjacencia;

    @FXML
    protected void onArquivoAbrirClick(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.setInitialFileName("myfile.txt");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        File selectedFile = fileChooser.showOpenDialog(fileAbrir.getParentPopup().getScene().getWindow());
        if (Objects.nonNull(selectedFile)) {
            arquivo = Arquivo.criarArquivo(selectedFile);
            matrizAdjacencia = new MatrizAdjacencia(arquivo);
            conteudoArquivo.setText("""
               Arquivo carregado com sucesso.
               
               Tipo do Grafo: #tipo_grafo#
               Total de Linhas: #total_linhas#
               """
                    .replace("#tipo_grafo#", arquivo.getPrimeiraLinha())
                    .replace("#total_linhas#", String.valueOf(arquivo.getTotalLinhas()))
            );
            toogleBotoes(false);
            tabsConteudo.getSelectionModel().select(tabArquivo);
            statusInferiorDireito.setText("Arquivo carregado com sucesso");
        }
    }

    @FXML
    protected void onArquivoFecharClick(){
        arquivo = null;
        matrizAdjacencia = null;
        toogleBotoes(true);
        conteudoArquivo.setText("Nenhum arquivo carregado...");
        statusInferiorDireito.setText("Nenhum arquivo carregado");
        conteudoGrafo.setText(null);
    }

    @FXML
    protected void onInformacoesGrafoClick(){
        tabsConteudo.getSelectionModel().select(tabGrafo);

        String content = "Informações Gerais do Grafo" + NEW_LINE_SEPARATOR;
        content += "Matriz de Adjacência: " + NEW_LINE_SEPARATOR;
        content += matrizAdjacencia.imprimirMatriz();
        content += NEW_LINE_SEPARATOR + matrizAdjacencia.visitarTodasArestas();

        conteudoGrafo.setText(content);
        conteudoGrafo.setVisible(true);
        imagemGrafo.setVisible(false);
    }

    @FXML
    protected void onVisualizacaoClick(){
        var viewGrafo = new VisualizacaoGrafo(matrizAdjacencia);
        try {
            String caminhoImagem = viewGrafo.gerarImagem();
            FileInputStream file = new FileInputStream(caminhoImagem);
            Image imagem = new Image(file);
            imagemGrafo.setImage(imagem);
            imagemGrafo.setVisible(true);

            conteudoGrafo.setVisible(false);
        } catch (Exception e){
            conteudoGrafo.setText("Erro ao gerar a imagem do grafo.");
            conteudoGrafo.setVisible(true);
            imagemGrafo.setVisible(false);
        }
        tabsConteudo.getSelectionModel().select(tabGrafo);
    }

    @FXML
    protected void onAbrirNeo4jClick(){}

    @FXML
    protected void onImprimirMatrizClick(){
        tabsConteudo.getSelectionModel().select(tabGrafo);
        conteudoGrafo.setText("Matriz de Adjacência: " + NEW_LINE_SEPARATOR + matrizAdjacencia.imprimirMatriz());
        conteudoGrafo.setVisible(true);
        imagemGrafo.setVisible(false);
    }

    @FXML
    protected void onGrauVerticeClick(){
        TextInputDialog td = new TextInputDialog(null);
        td.setTitle("Grau do Vértice");
        td.setHeaderText("");
        td.setContentText("Informe o vértice");
        Optional<String> verticeInformado = td.showAndWait();

        tabsConteudo.getSelectionModel().select(tabGrafo);
        if (verticeInformado.isPresent()){
            conteudoGrafo.setText(matrizAdjacencia.grauVertice(matrizAdjacencia.getPosicaoVertice(verticeInformado.get())));
        } else {
            conteudoGrafo.setText("O vértice não foi informado ou não existe.");
        }
        conteudoGrafo.setVisible(true);
        imagemGrafo.setVisible(false);
    }

    @FXML
    protected void onVisitarArestasClick(){
        String content = "Visitando Todas as arestas do Grafo..." + NEW_LINE_SEPARATOR + NEW_LINE_SEPARATOR + matrizAdjacencia.visitarTodasArestas();
        conteudoGrafo.setText(content);
        conteudoGrafo.setVisible(true);
        imagemGrafo.setVisible(false);
        tabsConteudo.getSelectionModel().select(tabGrafo);
    }

    @FXML
    protected void onVerificarAdjacenciaClick(){
        TextInputDialog td = new TextInputDialog(null);
        td.setTitle("Verificar Adjacência");
        td.setHeaderText("");
        td.setContentText("Informe o vértice 1:");
        Optional<String> vertice1 = td.showAndWait();

        TextInputDialog td2 = new TextInputDialog(null);
        td2.setTitle("Verificar Adjacência");
        td2.setHeaderText("");
        td2.setContentText("Informe o vértice 2:");
        Optional<String> vertice2 = td2.showAndWait();

        tabsConteudo.getSelectionModel().select(tabGrafo);
        if (vertice1.isPresent() && vertice2.isPresent()){
            int v1 = matrizAdjacencia.getPosicaoVertice(vertice1.get());
            int v2 = matrizAdjacencia.getPosicaoVertice(vertice2.get());
            if (matrizAdjacencia.verificarAdjacencia(v1, v2)){
                conteudoGrafo.setText("Os vértices " + vertice1.get() + " e " + vertice2.get() + " são adjacentes.");
            } else if (v1 == v2){
                conteudoGrafo.setText("Os vértices " + vertice1.get() + " e " + vertice2.get() + " são iguais.");
            } else {
                conteudoGrafo.setText("Os vértices " + vertice1.get() + " e " + vertice2.get() + " não são adjacentes.");
            }
        } else {
            conteudoGrafo.setText("Os vértices não foram informado ou não existem.");
        }
        conteudoGrafo.setVisible(true);
        imagemGrafo.setVisible(false);
    }

    @FXML
    protected void onLocalizarVizinhosClick(){
        TextInputDialog td = new TextInputDialog(null);
        td.setTitle("Localizar Vizinhos");
        td.setHeaderText("");
        td.setContentText("Informe o vértice: ");
        Optional<String> verticeInformado = td.showAndWait();

        tabsConteudo.getSelectionModel().select(tabGrafo);
        if (verticeInformado.isPresent()){
            conteudoGrafo.setText("Vizinhos do Vértice: " + verticeInformado.get() + NEW_LINE_SEPARATOR
                    + matrizAdjacencia.vizinhosVertice(verticeInformado.get()));
        } else {
            conteudoGrafo.setText("O vértice não foi informado ou não existe.");
        }
        conteudoGrafo.setVisible(true);
        imagemGrafo.setVisible(false);
    }

    @FXML
    protected void onBuscarVerticesArticulacaoClick(){
        var verticesArticulacao = new VerticesArticulacao(matrizAdjacencia);
        List<String> vertices = verticesArticulacao.encontrarVerticesArticulacao();
        if (vertices.isEmpty()){
            conteudoGrafo.setText("O grafo não possui vértices de articulação.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Vértices de articulação do grafo:").append(NEW_LINE_SEPARATOR);
            for (String v : vertices) {
                sb.append("Vértice: ").append(v).append(NEW_LINE_SEPARATOR);
            }
            conteudoGrafo.setText(sb.toString());
        }
        tabsConteudo.getSelectionModel().select(tabGrafo);
        conteudoGrafo.setVisible(true);
        imagemGrafo.setVisible(false);
    }

    @FXML
    protected void onMenuSobreClick(){
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Sobre");
        dialog.setContentText("Projeto desenvolvido por Luis Jhonne.");

        ButtonType type = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        //Adding buttons to the dialog pane
        dialog.getDialogPane().getButtonTypes().add(type);
        dialog.show();
    }

    private void toogleBotoes(boolean value){
        buttonInformacoesGrafo.setDisable(value);
        buttonVisualizar.setDisable(value);
        buttonVerticesArticulacao.setDisable(value);

        tabArquivo.setDisable(value);
        tabGrafo.setDisable(value);

        menuGrafos.setDisable(value);
    }
}