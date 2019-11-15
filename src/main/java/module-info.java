module colisvif.main {
    exports fr.insa.colisvif;
    requires java.xml;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    opens fr.insa.colisvif.controller;
    opens fr.insa.colisvif.controller.command;
    opens fr.insa.colisvif.controller.state;
    opens fr.insa.colisvif.exception;
    opens fr.insa.colisvif.model;
    opens fr.insa.colisvif.util;
    opens fr.insa.colisvif.view;
}
