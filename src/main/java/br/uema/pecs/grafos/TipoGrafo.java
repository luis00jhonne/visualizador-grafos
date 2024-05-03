package br.uema.pecs.grafos;

public enum TipoGrafo {
    DIRIGIDO("D"),
    NAO_DIRIGIDO("ND");

    private final String sigla;

    TipoGrafo(String sigla) {
        this.sigla = sigla;
    }

    public String getSigla(){
        return sigla;
    }

    public static TipoGrafo valueOfSigla(String sigla) {
        if (sigla == null) {
            return null;
        }
        for (TipoGrafo tipo : TipoGrafo.values()) {
            if (tipo.getSigla().equals(sigla)) {
                return tipo;
            }
        }
        return null;
    }
}
