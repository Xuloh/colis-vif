module colisvif.main {
    exports fr.insa.colisvif;
    requires java.xml;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    opens fr.insa.colisvif.view;
}
