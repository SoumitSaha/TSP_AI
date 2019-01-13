package tsp_ai;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Place {
    public double x;
    public double y;
    public int id;
    
    public Place(int id, double x, double y){
        this.id = id;
        this.x = x;
        this.y = y;
    }
    double dist(Place another_place){
        double temp;
        double temp_x;
        double temp_y;
        temp_x = another_place.x - this.x;
        temp_x = pow(temp_x, 2);
        temp_y = another_place.y - this.y;
        temp_y = pow(temp_y, 2);
        temp = sqrt(temp_x + temp_y);
        return temp;
    }
}
