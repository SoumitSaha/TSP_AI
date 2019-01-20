package tsp_ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class TSP_AI_SAVINGS {

    public static Place[] places;

    public static List<Tour> make_pair_tours(int start_pos, int total) {
        List<Tour> pairs = new ArrayList<Tour>();
        int j = 0;
        for (int i = 0; i < total; i++) {
            if (places[i].id != (start_pos + 1)) {
                Tour temp = new Tour();
                temp.pushback(places[start_pos]);
                temp.pushback(places[i]);
                temp.CalculateCost();
                pairs.add(temp);
            }
        }
        return pairs;
    }

    public static Tour Savings(int start_pos, int total) {
        List<Tour> sub_tours = make_pair_tours(start_pos, total);

        //Calculating savings in 2-D Array
        int s = sub_tours.size();
        double[][] saving = new double[total][total];
        for (int i = 0; i < places.length; i++) {
            for (int j = 0; j < places.length; j++) {
                if (i != j) {
                    saving[i][j] = places[start_pos].dist(places[i]) + places[j].dist(places[start_pos]) - places[i].dist(places[j]);
                } else {
                    saving[i][j] = 0;
                }
            }
        }
        temp_ordering[] savings_in_order = new temp_ordering[total * total];
        int l = 0;
        // taking savings in 1-D Array
        for (int i = 0; i < places.length; i++) {
            for (int j = 0; j < places.length; j++) {
                savings_in_order[l] = new temp_ordering();
                savings_in_order[l].i = i;
                savings_in_order[l].j = j;
                savings_in_order[l].isUsed = false;
                savings_in_order[l].save = saving[i][j];
                l++;
            }
        }
        //Sorting 1-D Array of savings
        Arrays.sort(savings_in_order);

        // Merging Tours if merging is feasible
        for (int i = 0; i < savings_in_order.length; i += 2) { // i+=2 as 2 edges has always same savings, i.e : 3-4 and 4-3 has same savings
            if ((savings_in_order[i].isUsed == false) && (savings_in_order[i].i != start_pos) && (savings_in_order[i].j != start_pos)) {
                int a = savings_in_order[i].i;
                int b = savings_in_order[i].j;
                Boolean feasible1 = false;
                Boolean feasible2 = false;
                Tour t1 = null;
                Tour t2 = null;
                for (int j = 0; j < sub_tours.size(); j++) { // getting 2 tours to be merged
                    if (sub_tours.get(j).t.contains(places[a])) {
                        t1 = sub_tours.get(j);
                    }
                    if (sub_tours.get(j).t.contains(places[b])) {
                        t2 = sub_tours.get(j);
                    }
                }
                if (t1 == t2) { // edge is in same tour, so merging discarded in this turn
                    continue;
                }

                int t1_size = t1.size();
                int t2_size = t2.size();
                int a_idx_in_t1 = t1.t.indexOf(places[a]);
                int b_idx_in_t2 = t2.t.indexOf(places[b]);
                if ((a_idx_in_t1 == 1) || (a_idx_in_t1 == (t1_size - 1))) {
                    feasible1 = true;
                }
                if ((b_idx_in_t2 == 1) || (b_idx_in_t2 == (t2_size - 1))) {
                    feasible2 = true;
                }
                if (feasible1 && feasible2) { // link breaking capability check
                    for (int j = 1; j < t2.size(); j++) {
                        t1.pushback(t2.t.get(j));
                    }
                    sub_tours.remove(t2);
                }
                if (sub_tours.get(0).size() == total && sub_tours.size() == 1) { // result has been constructed
                    break;
                }
            }
        }

        return sub_tours.get(0);
    }

    public static Tour TOH(Tour t, String type) {
        while (true) {
            //System.out.println("brfore : cost : " + t.cost);
            Boolean improved = false;
            for (int i = 0; i < t.size(); i++) {
                for (int j = i + 2; j < t.size(); j++) {
                    int si = j - i + 1;
                    t.CalculateCost();
                    double temp_cost = t.cost;
                    //reverse
                    for (int k = i + 1; k < si / 2; k++) {
                        Place temp_place = t.t.get(k);
                        t.t.set(k, t.t.get(si - k - 1));
                        t.t.set(si - k - 1, temp_place);
                    }
                    t.CalculateCost();
                    double new_cost = t.cost;
                    //System.out.println("After : cost : " + t.cost);
                    if (new_cost < temp_cost) {
                        //System.out.println("here");
                        improved = true;
                        if (type.equalsIgnoreCase("first")) {
                            break;
                        }
                        if (type.equalsIgnoreCase("best")) {
                            continue;
                        }
                    }
                    //reverse again
                    for (int k = i + 1; k < si / 2; k++) {
                        Place temp_place = t.t.get(k);
                        t.t.set(k, t.t.get(si - k - 1));
                        t.t.set(si - k - 1, temp_place);
                    }
                }
                if (improved && type.equalsIgnoreCase("first")) {
                    break;
                }
            }
            if (!improved) {
                break;
            }
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
        // start_pos in (placeid - 1) form
//        Tour t = Savings(0, total);
//        System.out.println(t);
//        t = TOH(t);
//        System.out.println(t);

        int k;
        k = in.nextInt();
        Tour t = null;
        Vector<Tour> greedy_tours = new Vector<>();
        for (int i = 0; i < k; i++) {
            int start = ThreadLocalRandom.current().nextInt(0, total);
            System.out.println("Generated Random : " + (start + 1));
            t = Savings(start, total);
            t.CalculateCost();
            greedy_tours.add(t);
        }
//        for (int i = 0; i < total; i++) {
//            Tour t1 = Savings(i, total);
//            System.out.println(t1);
//            t1 = TOH(t1, "first");
//            System.out.println(t1);
//        }

//        System.out.println("here 1");
        Collections.sort(greedy_tours);
        Tour best_greedy_tour = greedy_tours.get(0);
        Tour worst_greedy_tour = greedy_tours.get(k - 1);
//        System.out.println("here 2");
        System.out.println("---------------------- Greedy Savings Heuristic ----------------");
        System.out.println("Best : " + best_greedy_tour + " Cost : " + best_greedy_tour.cost);
        System.out.println("Worst : " + worst_greedy_tour + " Cost : " + worst_greedy_tour.cost);
        System.out.println("----------------------------------------------------------------");
        Vector<Tour> TOH_tours = new Vector<Tour>();
        System.out.println("greedy best : " + best_greedy_tour);
        TOH_tours.add(TOH(best_greedy_tour, "first"));
        System.out.println("2-opt best : " + TOH_tours.get(0));

        System.out.println("greedy worst : " + worst_greedy_tour);
        TOH_tours.add(TOH(worst_greedy_tour, "first"));
        System.out.println("2-opt worstt : " + TOH_tours.get(1));
//        System.out.println("here 3");
        for (int i = 0; i < TOH_tours.size(); i++) {
            TOH_tours.get(i).CalculateCost();
            System.out.println(i + " : Cost " + TOH_tours.get(i).cost);
        }
//        System.out.println("here 4");
        Collections.sort(TOH_tours);
//        System.out.println("here 5");
        System.out.println("--------------------- First 2-OPT Heuristic --------------------");
        System.out.println("Best : " + TOH_tours.get(0) + " Cost : " + TOH_tours.get(0).cost);
        System.out.println("Worst : " + TOH_tours.get(1) + " Cost : " + TOH_tours.get(1).cost);
        System.out.println("----------------------------------------------------------------");

    }

}
