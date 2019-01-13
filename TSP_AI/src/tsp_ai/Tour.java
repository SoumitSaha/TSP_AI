package tsp_ai;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    public List<Place> t;
    
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
}
