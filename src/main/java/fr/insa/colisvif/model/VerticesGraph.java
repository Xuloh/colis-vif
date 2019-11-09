package fr.insa.colisvif.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class VerticesGraph {

    public static void main(String args[]){
        int n = 12;
        int k = 2*n+1;
        ArrayList<ArrayList<Double>> len = new ArrayList<>();

        for(int i = 0; i<k; ++i){
            len.add(new ArrayList<>());
            for(int j=0; j<k; ++j){
                len.get(i).add(j, Math.abs(Math.cos(i) + Math.sin(j)) + 0.001);
            }
        }

        VerticesGraph G = new VerticesGraph(n, len);
        long pickUps = 0;
        long a = 1;
        for(int truc = 0; truc < n; ++truc){
            pickUps += a;
            a *= 4;
        }
        pickUps *= 2;

        System.out.println(pickUps);

        var debut = System.nanoTime();
        LinkedList<Integer> L = G.resolveSubProblem(0, pickUps).getPath();
        var fin = System.nanoTime();
        for(int i : L){
            System.out.print(i);
            System.out.print("  ");
        }
        System.out.println(" ");
        System.out.println((fin - debut)*0.000000001);

//        var debut = System.nanoTime();
//        var L = G.naiveRound();
//        var fin = System.nanoTime();
//        for(Vertex v : L){
//            System.out.print(v.getId());
//            System.out.print("  ");
//        }
//        System.out.println(" ");
//        System.out.println((fin - debut)*0.000000001);
    }



    private long N;
    private DeliveryMap deliveries;
    private HashMap<Long, PathsFromVertex> pathsFromVertices;
    private ArrayList<ArrayList<Double>> lengths;
    private HashMap<Long, SubResult> subResults;

    private long idFromIndex(int index){
        if(index == 0){
            return deliveries.getWarehouseNodeId();
        }
        Delivery delivery = deliveries.getDelivery((index - 1) / 2);
        if(index%2 == 1){
            return delivery.getPickUpNodeId();
        }
        return delivery.getDropOffNodeId();
    }

    public VerticesGraph(DeliveryMap deliveries, HashMap<Long, PathsFromVertex> pathsFromVertices){
        int n = deliveries.size();
        N = 0b1 << (2*n);
        this.deliveries = deliveries;
        this.pathsFromVertices = pathsFromVertices;

        long warehouse = deliveries.getWarehouseNodeId();
        lengths = new ArrayList<>(2*n+1);
        lengths.add(new ArrayList<>(2*n+1));
        for(int i=0; i<2*n+1; ++i){
            lengths.add(new ArrayList<>(2*n+1));
            long id1 = idFromIndex(i);
            for(int j=0; j<2*n+1; ++j){
                long id2 = idFromIndex(j);
                double len = pathsFromVertices.get(id1).getLength(id2);
                lengths.get(i).add(len);
            }
        }

        subResults = new HashMap<>();
    }

    public VerticesGraph(int n, ArrayList<ArrayList<Double>> len){
        N = 0b1 << (2*n+1);
        lengths = len;
        subResults = new HashMap<>();
    }

    private Long subProblemKey(int start, long setCode){
        return setCode + N * start;
    }

    private void update(int start, int next, SubResult subResult, SubResult candidate){
        double a = subResult.getLength();
        double b = candidate.getLength() + lengths.get(start).get(next);
        if(a == -1 || a > b) {
            subResult.setLength(b);
            subResult.setPath(candidate.getPath());
        }
    }

    private SubResult resolveSubProblem(int start, long setCode){
        long key = subProblemKey(start, setCode);
        if(subResults.containsKey(key)){
            return subResults.get(key);
        }
        if(setCode == 0){
            SubResult subResult = new SubResult();
            subResult.setLength(0);
            subResult.addVertex(start);
            subResults.put(key, subResult);
            return subResult;
        }
        SubResult subResult = new SubResult();
        //int n = deliveries.size();
        int n = 12;
        long a = 1; //will be 2^k
        long copy = setCode;
        for(int k = 0; k < 2 * n + 1; ++k){
            if(copy % 2 == 1){
                if(k % 2 == 1){
//                    System.out.print("k%2==1 ");
//                    System.out.print(Long.toBinaryString(setCode));
//                    System.out.print(" -> ");
//                    System.out.println(Long.toBinaryString(setCode + a));
                    SubResult candidate = resolveSubProblem(k, setCode + a);
                    //setCode+a is in fact setCode-a+2a
                    //that means we remove int k from the set and add k+1 instead
                    update(start, k, subResult, candidate);
                }
                else{
//                    System.out.print("k%2==0 ");
//                    System.out.print(Long.toBinaryString(setCode));
//                    System.out.print(" -> ");
//                    System.out.println(Long.toBinaryString(setCode - a));
                    SubResult candidate = resolveSubProblem(k, setCode - a);
                    update(start, k, subResult, candidate);
                }
            }
            a = 2 * a;
            copy = copy / 2;
        }
        subResult.setPath(new LinkedList<>(subResult.getPath()));
        subResult.addVertex(start);
//        System.out.print("start ");
//        System.out.print(start);
//        System.out.print("  set ");
//        System.out.println(Long.toBinaryString(setCode));
//        for(int i : subResult.getPath()){
//            System.out.print(i);
//            System.out.print(" ");
//        }
//        System.out.println();
//        System.out.println();
        subResults.put(key, subResult);
        return subResult;
    }



    private static class SubResult {
        private double length;
        private LinkedList<Integer> path;

        /*package-private*/ SubResult(){
            length = -1;
            path = new LinkedList<>();
        }

        /*package-private*/ double getLength() {
            return length;
        }
        /*package-private*/ LinkedList<Integer> getPath() {
            return path;
        }
        /*package-private*/ void setLength(double length) {
            this.length = length;
        }
        /*package-private*/ void setPath(LinkedList<Integer> path) {
            this.path = path;
        }
        /*package-private*/ void addVertex(int v){
            path.addFirst(v);
        }
    }
}
