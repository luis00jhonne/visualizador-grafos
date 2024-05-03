module br.uema.pecs.grafos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.neo4j.driver;
    requires graph.support;

    opens br.uema.pecs.grafos to javafx.fxml;
    exports br.uema.pecs.grafos;
}