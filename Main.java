/*
Felix Andrew Sapalaran
OS project: "Scheduling Algorithms"
Spring 2019
*/

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
public class Main {
public static class SchedulingRow {

  //Declaring variables
  public int pn;
  public double at, bt, ct, tt, wt, btRemaining;

  public SchedulingRow(double at, double bt) {
    this.at = at;
    this.bt = bt;
  }
  //row number p1 to p6
  public void printRow(int i){
    System.out.printf("%3d %5.1f %5.1f %5.1f %5.1f %5.1f\n", i,at, bt, ct, tt, wt);
  }
}

public static class SchedulingAlgorithm {
  List < SchedulingRow > FCFS =  new ArrayList < SchedulingRow >();
  //SFJ
  //List < SchedulingRow > SJF =  new ArrayList < SchedulingRow >();

  double att;
  double awt;

  public void add(double at, double bt){
    SchedulingRow row = new SchedulingRow(at, bt);
    row.pn = FCFS.size();
    FCFS.add(row);
    //SJF
    //SJF.add(new SchedulingRow(at, bt));

  }

  public void compute(double switchingOverHead, int [] evalOrder){

    double ct = 0;
    double sumtt = 0;
    double sumwt = 0;
    for(int i = 0; i < FCFS.size(); i++){

      //evaluation order this code 41 - 46 lets you re order the number of processes
      int j = i; //the loop compute evaluates position in j
      //if no special order then j= i means sequential
      if (evalOrder != null) {
        //if there is ordering supplied, it look up ordering in the array using the code below
          j = evalOrder[i] - 1;
      }
      SchedulingRow row = FCFS.get(j);

      ct = ct + row.bt;
      row.ct = ct;

      //compute tt
      row.tt = ct - row.at;

      //avg tt for avg
      sumtt += row.tt;

      //compute wt
     row.wt = row.tt - row.bt;

     //compute wt for avg
     sumwt += row.wt;

     ct += switchingOverHead;
    }
    //compute att
    att = (double)sumtt/FCFS.size();
    awt = (double)sumwt/FCFS.size();
  }

  //round robin when timeSlice is exceeded
  public void compute2(double switchingOverHead, int [] evalOrder, double timeSlice){
    List<SchedulingRow> queue = new LinkedList<SchedulingRow> ();
    for (int i = 0; i < FCFS.size(); i++) {
      //evaluation order this code 41 - 46 lets you re order the number of processes
      int j = i; //the loop compute evaluates position in j
      //if no special order then j= i means sequential
      if (evalOrder != null) {
        //if there is ordering supplied, it look up ordering in the array using the code below
          j = evalOrder[i] - 1;
      }
      SchedulingRow row = FCFS.get(j);
      queue.add(row);
      row.btRemaining = row.bt;
    }

    double ct = 0;
    double sumtt = 0;
    double sumwt = 0;
    //double now = 0;

    int ctr = 0;
    while (queue.size() > 0) {
      System.out.printf("queue size is %d\n", queue.size());
      if ( ctr++ > 100 )
        break;

      // search loop to find something ready to run
      for (int i = 0; i < queue.size(); i++) {
        System.out.printf("searching position %d\n", i);
        SchedulingRow row = queue.get(i);

        if (row.at <= ct) {
          // run this row
          // to time slice

          double ts = Math.min(row.btRemaining, timeSlice);
          row.btRemaining -= ts;
          System.out.printf("pn %d at %5.1f giving time %5.1f, remaining %5.1f\n", row.pn+1, ct, ts, row.btRemaining);
          ct = ct + ts;
          if (row.btRemaining == 0) {
            // otherwise compute sums, and, remove it from the queue

            row.ct = ct;

            //compute tt
            row.tt = ct - row.at;

            //avg tt for avg
            sumtt += row.tt;

            //compute wt
            row.wt = row.tt - row.bt;

            //compute wt for avg
            sumwt += row.wt;

            // remove from the Queue
            queue.remove(i);
          }
          else {
            // if anything is left, move it to the end of the queue
            queue.remove(i);
            queue.add(row);
          }

          ct += switchingOverHead;

          // we found something to run, so stop search loop
          //  and go back to outer while loop
          break;
        }

      }
    }

    //compute att
    att = (double)sumtt/FCFS.size();
    awt = (double)sumwt/FCFS.size();
  }

  public void printTable(){
    System.out.printf(" Pro   AT    BT    CT    TT    WT\n");
    for(int i = 0; i < FCFS.size(); i++){
      SchedulingRow row = FCFS.get(i);
      row.printRow(i);
    }
    System.out.printf("att = %.2f\n", att);
    System.out.printf("awt = %.2f\n", awt);
  }
}

  //Driver
  public static void main(String[] args){
    SchedulingAlgorithm FCFS = new SchedulingAlgorithm();
    System.out.println("");
    System.out.println("First Come First Serve Algorithm");
    FCFS.add(0,6);
    FCFS.add(3,2);
    FCFS.add(5,1);
    FCFS.add(9,7);
    FCFS.add(10,4);
    FCFS.add(11,3);
    //context switching is 0
    FCFS.compute(0.0, null);
    FCFS.printTable();


    //SchedulingAlgorithm SJF = new SchedulingAlgorithm();
    //System.out.println("");
    //System.out.println("SJF");
    //SJF.add(0,6);
    //SJF.add(3,2);
    //SJF.add(5,1);
    //SJF.add(9,7);
    //SJF.add(10,4);
    //SJF.add(11,3);
    //compute takes another parameter that re ordering the number of processes ways you wanted them
    //SJF.compute(0.0, new int [] { 1,3,2,4,6,5 });
    //SJF.printTable();

    //SJF, same data as FCFS, but compute with alternative ordering:

    System.out.println("\nSJF");
    FCFS.compute(0.0, new int [] { 1,3,2,4,6,5 });
    FCFS.printTable();
    //FCFS.compute2(0.0, new int [] { 1,3,2,4,6,5 }, 9999);



    //Round robin
    System.out.println("");
    System.out.println("Round Robin");
    FCFS.compute2(0.0, null, 4);
    FCFS.printTable();




    System.out.println("");
    System.out.println("WITH Context Switching Time = 0.4 milisecond.");
    System.out.println("");
    System.out.println("FCFS");
    FCFS.compute(0.4, null);
    FCFS.printTable();

    System.out.println("SJF");
    FCFS.compute(0.4, new int [] { 1,3,2,4,6,5 });
    FCFS.printTable();


    //Round robin
    System.out.println("");
    System.out.println("Round Robin");
    FCFS.compute2(0.4, null, 4);
    FCFS.printTable();


    System.out.println("");
    System.out.println("With the above solution/calculations it shows that the Shortest Job First(SJF) is the optimal & effecient one.");
  }
}
