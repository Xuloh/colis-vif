package fr.insa.colisvif.model;

import java.util.HashMap;

public class PathsFromVertex{
    private HashMap<Long, Long> prevVertices;
    private HashMap<Long, Double> lengths;

    //No need to make a special constructor

    public void addPrev(Long id, Long prev){
        prevVertices.put(id, prev);
    }
    public void addLength(Long id, Double length){
        lengths.put(id, length);
    }
    public Long getPrev(Long id){
        return prevVertices.get(id);
    }
    public void setPrev(Long id, Long prev){
        prevVertices.put(id, prev);
    }
    public Double getLength(Long id){
        return lengths.get(id);
    }
    public void setLength(Long id, Double length){
        lengths.put(id, length);
    }
}
