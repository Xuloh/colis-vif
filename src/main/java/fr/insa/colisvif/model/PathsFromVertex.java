package fr.insa.colisvif.model;

import java.util.HashMap;

/*package-private*/ class PathsFromVertex{
    private HashMap<Long, Section> prevSections;
    private HashMap<Long, Double> lengths;

    //No need to make a special constructor

    /*package-private*/ Section getPrevSection(long id){
        return prevSections.get(id);
    }
    /*package-private*/ long getPrevNode(long id){
        return prevSections.get(id).getOrigin();
    }
    /*package-private*/ void setPrev(long id, Section section){
        prevSections.put(id, section);
    }
    /*package-private*/ Double getLength(long id){
        return lengths.get(id);
    }
    /*package-private*/ void setLength(long id, double length){
        lengths.put(id, length);
    }
}
