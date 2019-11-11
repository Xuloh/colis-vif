package fr.insa.colisvif.model;

import java.util.HashMap;

/*package-private*/ class PathsFromVertex {

    private HashMap<Long, Section> prevSections;

    private HashMap<Long, Double> lengths;

    /*package-private*/ PathsFromVertex() {
        prevSections = new HashMap<>();
        lengths = new HashMap<>();
    }

    /*package-private*/ Section getPrevSection(long id) {
        return prevSections.get(id);
    }

    /*package-private*/ long getPrevNode(long id) {
        return prevSections.get(id).getOrigin();
    }

    /*package-private*/ void setPrev(long id, Section section) {
        prevSections.put(id, section);
    }

    /*package-private*/ double getLength(long id) {
        if (lengths.containsKey(id)) {
            return lengths.get(id);
        }
        return -1;
    }
    
    /*package-private*/ void setLength(long id, double length) {
        lengths.put(id, length);
    }
}
