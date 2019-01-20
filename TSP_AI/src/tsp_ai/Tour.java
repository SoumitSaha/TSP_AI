package tsp_ai;

import java.util.ArrayList;
import java.util.List;

public class Tour implements Comparable<Tour>{
    public List<Place> t;
    public double cost;
    
    public Tour(){
        t = new ArrayList<>();
    }
    
    public void pushback(Place p){
        t.add(p);
    }
    
    public void clear(){
        t.clear();
    }
    
    public int size(){
        return t.size();
    }
    
    @Override
    public String toString(){
        String str = "[ ";
        for (int i = 0; i < t.size(); i++) {
            str += t.get(i).id + " ";
            if(i != (t.size() - 1)){
                str += "-> ";
            }
        }
        str += "]";
        return str;
    }
    
    public void CalculateCost(){
        cost = 0;
        int s = t.size();
        for (int i = 0; i < s; i++) {
            Place x = t.get(i);
            Place y = t.get((i + 1) % s);
            cost += x.dist(y);
        }
    }

    @Override
    public int compareTo(Tour o) {
        if(this.cost == o.cost){
            return 0;
        }
        else if(this.cost < o.cost){
            return -1;
        }
        else{
            return 1;
        }
    }
    
    
}
