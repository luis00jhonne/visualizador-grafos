package br.uema.pecs.grafos;

import java.io.*;

public class Arquivo {
    private final File arquivoEscolhido;
    private int totalLinhas;
    private String primeiraLinha;

    private Arquivo(File arquivoEscolhido){
        this.arquivoEscolhido = arquivoEscolhido;
    }

    public static Arquivo criarArquivo(File arquivoEscolhido) {
        var arquivo = new Arquivo(arquivoEscolhido);
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(arquivoEscolhido))) {
            arquivo.primeiraLinha = bufferedReader.readLine();
            arquivo.totalLinhas = (int) bufferedReader.lines().count();
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
            e.printStackTrace();
        }
        return arquivo;
    }

    public BufferedReader lerArquivo(){
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(arquivoEscolhido));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return bufferedReader;
    }

    public int getTotalLinhas() {
        return totalLinhas;
    }

    public String getPrimeiraLinha() {
        return primeiraLinha;
    }
}
