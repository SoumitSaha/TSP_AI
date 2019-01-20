package tsp_ai;

public class temp_ordering implements Comparable<temp_ordering>{
    public int i, j;
    public double save;
    public Boolean isUsed;

    @Override
    public int compareTo(temp_ordering o) {
        double c = o.save;
//        c = this.save - c; // for ascendind sort;        
        c = c - this.save; // for descending sort
        if(c == 0){
            return 0;
        }
        else if(c < 0){
            return -1;
        }
        else{
            return 1;
        }
    }
    
    @Override
    public String toString(){
        String str = "i : " + i + ", j : " + j + ", savings : " + save ;
        return str;
    }
    
}
