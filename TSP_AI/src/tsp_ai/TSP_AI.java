package tsp_ai;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Math.abs;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Scanner;

public class TSP_AI {
    
    public static Place[] places;
    
    public static int Nearest_Unvisited(int idx, int total, Boolean[] visit_arr){
        double cur_min = MAX_VALUE;
        int index = -1;
        for (int i = 0; i < total; i++) {
            if(!visit_arr[i]){
                if(places[idx].dist(places[i]) < cur_min){
                    cur_min = places[idx].dist(places[i]);
                    index = i;
                }
            }
        }
        return index;
    }
    
    public static Neighbour[] k_nearest_neighbour(int idx, int total){
        int j = 0;
        Neighbour[] k_neigh_arr = new Neighbour[total - 1];
        for (int i = 0; i < total; i++) {
            if(idx != i){
                k_neigh_arr[j] = new Neighbour();
                k_neigh_arr[j].id = places[i].id;
                k_neigh_arr[j].cost = places[idx].dist(places[i]);
                j++;
            }
        }
        Arrays.sort(k_neigh_arr, (Neighbour o1, Neighbour o2) -> {
            if(o1.cost > o2.cost) return 1;
            if(o1.cost < o2.cost) return -1;
            else return 0;
        });
        return k_neigh_arr;
    }
    
    public static int Nearest_Unvisited(int idx, int total, Boolean[] visit_arr, String Case, int k){
        Neighbour[] k_neigh_arr = k_nearest_neighbour(idx, total);
        List<Neighbour> k_neigh_arr_unvisited = new ArrayList<>();
        for (int i = 0; i < k_neigh_arr.length; i++) {
            if(!visit_arr[k_neigh_arr[i].id - 1]){
                k_neigh_arr_unvisited.add(k_neigh_arr[i]);
            }
        }
        int index;
        if(Case.equalsIgnoreCase("avg")){
            int s = k_neigh_arr_unvisited.size();
            if(k <= s) index = ThreadLocalRandom.current().nextInt(0, k);
            else index = ThreadLocalRandom.current().nextInt(0, s);
            return (k_neigh_arr_unvisited.get(index).id - 1);
        }
        else if(Case.equalsIgnoreCase("best")){
            return (k_neigh_arr_unvisited.get(0).id - 1);
        }
        else{
            int s = k_neigh_arr_unvisited.size();
            if(k <= s) return (k_neigh_arr_unvisited.get(k - 1).id - 1);
            else return (k_neigh_arr_unvisited.get(s - 1).id - 1);
        }
    }
    
    public static Tour NNH(int start_place, int total_places){
        Tour temp = new Tour();
        Boolean[] isVisited = new Boolean[total_places];
        for (int i = 0; i < isVisited.length; i++) {
            isVisited[i] = false;
        }
        int last = start_place - 1;
        isVisited[last] = true;
        temp.pushback(places[last]);
        while(temp.size() < total_places){
            int idx = Nearest_Unvisited(last, total_places, isVisited);
            isVisited[idx] = true;
            temp.pushback(places[idx]);
            last = idx;
        }
        return temp;
    }
    
    public static Tour ran_NNH(int start_place, int total_places, String Case, int k){
        Tour temp = new Tour();
        Boolean[] isVisited = new Boolean[total_places];
        for (int i = 0; i < isVisited.length; i++) {
            isVisited[i] = false;
        }
        int last = start_place - 1;
        isVisited[last] = true;
        temp.pushback(places[last]);
        while(temp.size() < total_places){
            int idx = Nearest_Unvisited(last, total_places, isVisited, Case, k);
            isVisited[idx] = true;
            temp.pushback(places[idx]);
            last = idx;
        }
        return temp;
    }
    
    public static double CalculateCost(Tour t){
        double cost = 0;
        int s = t.size();
        for (int i = 0; i < s; i++) {
            int x = t.t.get(i).id - 1;
            int y = t.t.get((i + 1) % s).id - 1;
            cost += places[x].dist(places[y]);
        }
        return cost;
    }
    
    public static Tour TOH(Tour t){
        while(true){
            double temp_cost = CalculateCost(t);
            Boolean improved = false;
            for (int i = 0; i < t.size(); i++) {
                for (int j = i + 2; j < t.size(); j++) {
                    int si = j - i + 1;
                    //reverse
                    for (int k = i + 1; k < si/2; k++) {
                        Place temp_place = t.t.get(k);
                        t.t.set(k, t.t.get(si - k -1));
                        t.t.set(si - k - 1, temp_place);
                    }
                    double new_cost = CalculateCost(t);
                    if(new_cost < temp_cost) {
                        improved = true;
                        break;
                    }
                    //reverse again
                    for (int k = i + 1; k < si/2; k++) {
                        Place temp_place = t.t.get(k);
                        t.t.set(k, t.t.get(si - k -1));
                        t.t.set(si - k - 1, temp_place);
                    }
                }
                if(improved) break;
            }
            if(!improved) break;
        }
        return t;
    }
    
    public static void main(String[] args) {
        Scanner in;
        in = new Scanner(System.in);
        int total;
        total = in.nextInt();
        places = new Place[total];
        for (int i = 0; i < total; i++) {
            int id = in.nextInt();
            double x = in.nextDouble();
            double y = in.nextDouble();
            Place temp = new Place(id, x, y);
            places[i] = temp;
        }
        int k = in.nextInt();
        Tour t;
        int start = ThreadLocalRandom.current().nextInt(1, total + 1);
//        t = NNH(start, total);
//        System.out.println("Random Starting Point by NNH : -----------------------------------");
//        System.out.println("Start : " + start);
//        System.out.println("Tour : " + t);
//        System.out.println("Cost : " + CalculateCost(t));
//        System.out.println("------------------------------------------------------------------");

        
        
        double best_cost_NNH = Double.MAX_VALUE;
        Tour best_NNH_Tour = null;
        int best_NNH_start = 1;
        Tour[] t_arr = new Tour[total];
        for (int i = 0; i < total; i++) {
            t_arr[i] = NNH(i + 1, total);
            double c = CalculateCost(t_arr[i]);
            if(best_cost_NNH > c){
                best_cost_NNH = c;
                best_NNH_start = i + 1;
                best_NNH_Tour = t_arr[i];
            }
            //System.out.println("Start : " + (i + 1) + " Cost : " + c);
        }
        
//        System.out.println("Best Tour by NNH: ------------------------------------------------");
//        System.out.println("Start : " + best_NNH_start);
//        System.out.println("Tour : " + best_NNH_Tour);
//        System.out.println("Cost : " + best_cost_NNH);
//        System.out.println("------------------------------------------------------------------");
        
        
        System.out.println("-----------------------Greedy Simple NNH--------------------------");
        System.out.println("------------------------------------------------------------------");
        
        int best_start_id = 0;
        int worst_start_id = 4;
        double[] my_cost = new double[k];
        int[] my_start = new int[k];
        Tour[] my_tour = new Tour[k];
        for (int i = 0; i < k; i++) {
            my_start[i] = ThreadLocalRandom.current().nextInt(1, total + 1);
            my_tour[i] = NNH(my_start[i], total);
            my_cost[i] = CalculateCost(my_tour[i]);
        }
        for (int i = 1; i < k; i++) {
            if(my_cost[best_start_id] > my_cost[i]){
                best_start_id = i;
            }
            if(my_cost[worst_start_id] < my_cost[i]){
                worst_start_id = i;
            }
        }
        
        int avg_sim_id = 0;
        double avg_cost_sim = (my_cost[best_start_id] + my_cost[worst_start_id])/2;
        double diff_sim = avg_cost_sim;
        for (int i = 0; i < k; i++) {
            if(abs(my_cost[i] - diff_sim) < diff_sim){
                avg_sim_id = i;
                diff_sim = abs(my_cost[i] - diff_sim);
            }
        }
        
        System.out.println("NNH (best) : -----------------------------------------------------");
        System.out.println("Start : " + my_start[best_start_id]);
        System.out.println("Tour : " + my_tour[best_start_id]);
        System.out.println("Cost : " + my_cost[best_start_id]);
        System.out.println("------------------------------------------------------------------");
        
        
        System.out.println("NNH (worst) : ----------------------------------------------------");
        System.out.println("Start : " + my_start[worst_start_id]);
        System.out.println("Tour : " + my_tour[worst_start_id]);
        System.out.println("Cost : " + my_cost[worst_start_id]);
        System.out.println("------------------------------------------------------------------");
        
        System.out.println("NNH (avg) : ------------------------------------------------------");
        System.out.println("Start : " + my_start[avg_sim_id]);
        System.out.println("Tour : " + my_tour[avg_sim_id]);
        System.out.println("Cost : " + my_cost[avg_sim_id]);
        System.out.println("------------------------------------------------------------------");
        
        
        int n = in.nextInt();
        int best_start_id_ran = 0;
        int worst_start_id_ran = 4;
        double[] my_cost_ran = new double[n];
        int[] my_start_ran = new int[n];
        Tour[] my_tour_ran = new Tour[n];
        for (int i = 0; i < n; i++) {
            my_start_ran[i] = ThreadLocalRandom.current().nextInt(1, total + 1);
            my_tour_ran[i] = ran_NNH(my_start_ran[i], total, "best", k);
            my_cost_ran[i] = CalculateCost(my_tour_ran[i]);
        }
        for (int i = 1; i < n; i++) {
            if(my_cost_ran[best_start_id_ran] > my_cost_ran[i]){
                best_start_id_ran = i;
            }
            if(my_cost_ran[worst_start_id_ran] < my_cost_ran[i]){
                worst_start_id_ran = i;
            }
        }
        
        int avg_ran_id = 0;
        double avg_cost_ran = (my_cost_ran[best_start_id_ran] + my_cost_ran[worst_start_id_ran])/2;
        double diff_ran = avg_cost_ran;
        for (int i = 0; i < n; i++) {
            if(abs(my_cost_ran[i] - diff_ran) < diff_ran){
                avg_ran_id = i;
                diff_ran = abs(my_cost_ran[i] - diff_ran);
            }
        }
        
        
        System.out.println("---------------------Greedy Randomized NNH------------------------");
        System.out.println("------------------------------------------------------------------");
        
        System.out.println("Random Starting Point by Random_NNH (best) : ---------------------");
        System.out.println("Start : " + my_start_ran[best_start_id_ran]);
        System.out.println("Tour : " + my_tour_ran[best_start_id_ran]);
        System.out.println("Cost : " + my_cost_ran[best_start_id_ran]);
        System.out.println("------------------------------------------------------------------");
        
        System.out.println("Random Starting Point by Random_NNH (worst) : --------------------");
        System.out.println("Start : " + my_start_ran[worst_start_id_ran]);
        System.out.println("Tour : " + my_tour_ran[worst_start_id_ran]);
        System.out.println("Cost : " + my_cost_ran[worst_start_id_ran]);
        System.out.println("------------------------------------------------------------------");
        
        System.out.println("Random Starting Point by Random_NNH (avg) : ----------------------");
        System.out.println("Start : " + my_start_ran[avg_ran_id]);
        System.out.println("Tour : " + my_tour_ran[avg_ran_id]);
        System.out.println("Cost : " + my_cost_ran[avg_ran_id]);
        System.out.println("------------------------------------------------------------------");
    
        Tour[] best_of_three = new Tour[3];
        double second_best_cost, third_best_cost;
        second_best_cost = my_cost_ran[worst_start_id_ran];
        third_best_cost = my_cost_ran[worst_start_id_ran];
        int sec_best_id, third_best_id;
        sec_best_id = worst_start_id;
        third_best_id = worst_start_id;
        
        for (int i = 0; i < n; i++) {
            if((my_cost_ran[best_start_id_ran] < my_cost_ran[i]) && (my_cost_ran[i] < second_best_cost)){
                sec_best_id = i;
                second_best_cost = my_cost_ran[i];
            }
        }
        for (int i = 0; i < n; i++) {
            if((my_cost_ran[sec_best_id] < my_cost_ran[i]) && (my_cost_ran[i] < third_best_cost)){
                third_best_id = i;
                third_best_cost = my_cost_ran[i];
            }
        }
        
        best_of_three[0] = my_tour_ran[best_start_id_ran];
        best_of_three[1] = my_tour_ran[sec_best_id];
        best_of_three[2] = my_tour_ran[third_best_id];
        
        Tour[] TOH_tour = new Tour[3];
        TOH_tour[0] = TOH(best_of_three[0]);
        TOH_tour[1] = TOH(best_of_three[1]);
        TOH_tour[2] = TOH(best_of_three[2]);
        double toh_min_cost = CalculateCost(TOH_tour[0]);
        int toh_min_id = 0;
        double toh_max_cost = CalculateCost(TOH_tour[0]);
        int toh_max_id = 0;
        for (int i = 1; i < 3; i++) {
            if(CalculateCost(TOH_tour[i]) < toh_min_cost){
                toh_min_cost = CalculateCost(TOH_tour[i]);
                toh_min_id = i;
            }
            if(CalculateCost(TOH_tour[i]) > toh_max_cost){
                toh_max_cost = CalculateCost(TOH_tour[i]);
                toh_max_id = i;
            }
        }
        
        System.out.println("----------------------------- TOH --------------------------------");
        System.out.println("------------------------------------------------------------------");
        
        System.out.println("TOH (best) : -----------------------------------------------------");
        System.out.println("Start : " + TOH_tour[toh_min_id].t.get(0).id);
        System.out.println("Tour : " + TOH_tour[toh_min_id]);
        System.out.println("Cost : " + CalculateCost(TOH_tour[toh_min_id]));
        System.out.println("------------------------------------------------------------------");


        System.out.println("TOH (worst) : ----------------------------------------------------");
        System.out.println("Start : " + TOH_tour[toh_max_id].t.get(0).id);
        System.out.println("Tour : " + TOH_tour[toh_max_id]);
        System.out.println("Cost : " + CalculateCost(TOH_tour[toh_max_id]));
        System.out.println("------------------------------------------------------------------");
    }

}
