/**
 * Created by josephchiou on 5/23/17.
 */
import java.util.LinkedList;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import static java.lang.Float.NaN;

public class AirportCheckin {

    static int lamdaFCAvg;
    static int lamdaCoachAvg;
    static int checkinDuration;
    static int serviceTimeFCAvg;
    static int serviceTimeCoachAvg;
    static int userInputCount=0;

    public static void main(String[] args) {
        //user input
        inputValidation();

        // variables
        boolean done = false;
        int globalTime = 0;
        int globalEndTime = globalTime + checkinDuration ; //station stops taking passengers after this time
        int lamdaFC;
        int lamdaCoach;

        // instantiating stations and queues
        Station fc1 = new Station(1,serviceTimeFCAvg,serviceTimeCoachAvg);
        Station fc2 = new Station(1,serviceTimeFCAvg,serviceTimeCoachAvg);
        Station c1 = new Station(0,serviceTimeFCAvg,serviceTimeCoachAvg);
        Station c2 = new Station(0,serviceTimeFCAvg,serviceTimeCoachAvg);
        Station c3 = new Station(0,serviceTimeFCAvg,serviceTimeCoachAvg);

        Queue fcQ = new Queue(1);  //first class queue: 1
        Queue cQ = cQ = new Queue(0);  //coach class queue : 0
        Queue [] queueArray = {cQ,fcQ};

        lamdaFC = generateArrivalRate(1);
        lamdaCoach = generateArrivalRate(0);

        // inputs

        for(; (!done)||globalTime < (0+globalEndTime);globalTime ++){

            if(     fc1.checkServiceDone(globalTime) &&
                    fc2.checkServiceDone(globalTime) &&
                    c1.checkServiceDone(globalTime) &&
                    c2.checkServiceDone(globalTime) &&
                    c3.checkServiceDone(globalTime) && fcQ.isEmpty() && cQ.isEmpty())
            {
                done  = true;
            }else{
                done = false;
            }


            // enqueue passenager based on lamda
            if (globalTime < (0+globalEndTime) && globalTime != 0) {
                if (globalTime % lamdaFC == 0) {
                    fcQ.enQueue(globalTime);
                    lamdaFC = generateArrivalRate(1);
                }
                if (globalTime % lamdaCoach == 0) {
                    cQ.enQueue(globalTime);
                    lamdaCoach = generateArrivalRate(0);
                }
            }
            test(globalTime,fc1,fc2,c1,c2,c3, fcQ,cQ);
            // stations service begins
            // do nothing if station is busy
            fc1.service(queueArray, globalTime);
            fc2.service(queueArray, globalTime);
            c1.service(queueArray, globalTime);
            c2.service(queueArray, globalTime);
            c3.service(queueArray, globalTime);
        }

        // output stats
        System.out.println("Total simulation time: " + --globalTime + " min");
        System.out.println("First class queue avg wait time: " + fcQ.getAverageWaitTime() + " min");
        System.out.println("Coach class queue avg wait time: " + cQ.getAverageWaitTime() + " min");
        System.out.println("First class queue max wait time: " + fcQ.getMaxWaitTime() + " min");
        System.out.println("Coach class queue max wait time: " + cQ.getMaxWaitTime() + " min");
        System.out.println("First class queue max queue length: " + fcQ.getMaxQueueLength());
        System.out.println("Coach class queue max queue length: " + cQ.getMaxQueueLength());
        System.out.println("First class station 1 rate of occupancy: " + fc1.getStationOccupancyRate(globalTime)+ "%");
        System.out.println("First class station 2 rate of occupancy: " + fc2.getStationOccupancyRate(globalTime)+ "%");
        System.out.println("Coach class station 1 rate of occupancy: " + c1.getStationOccupancyRate(globalTime)+ "%");
        System.out.println("Coach class station 2 rate of occupancy: " + c2.getStationOccupancyRate(globalTime)+ "%");
        System.out.println("Coach class station 3 rate of occupancy: " + c3.getStationOccupancyRate(globalTime)+ "%");


        System.exit(0);
    }
    public static void test(int time, Station fc1, Station fc2, Station c1, Station c2, Station c3, Queue fcQ, Queue cQ){
        System.out.println("current time: " + time);
        System.out.println("fcQ (queue status): " + fcQ.toString());
        System.out.println("cQ (queue status): " + cQ.toString());
        System.out.println("fc1 status (station busy?): " + fc1.isBusy());
        System.out.println("fc2 status (station busy?): " + fc2.isBusy());
        System.out.println("c1 status (station busy?): " + c1.isBusy());
        System.out.println("c2 status (station busy?): " + c2.isBusy());
        System.out.println("c3 status (station busy?): " + c3.isBusy());
        System.out.println();

    }

    public static void inputValidation(){
        Scanner sc = new Scanner(System.in);
        for( ; AirportCheckin.userInputCount<5; ++AirportCheckin.userInputCount){
            String msg= "";
            int userInput=0;

            switch (AirportCheckin.userInputCount){
                case 0: msg = "Please enter the duration of check-in time: ";
                    break;
                case 1: msg = "Please enter coach average arrival rate (A passenger comes in in x min on avg): ";
                    break;
                case 2: msg = "Please enter coach average service rate: ";
                    break;
                case 3: msg = "Please enter first class average arrival rate (A passenger comes in in x min on avg): ";
                    break;
                case 4: msg = "Please enter first class average service rate: ";
                    break;
            }
            System.out.print(msg);   //prompting user
            try{
                userInput = sc.nextInt();
            }catch (Exception e){
                System.out.println("Error: Please enter a positive Integer!");
                inputValidation();
            }

            switch (AirportCheckin.userInputCount){
                case 0: checkinDuration = userInput;
                    break;
                case 1: lamdaCoachAvg = userInput;
                    break;
                case 2: serviceTimeCoachAvg = userInput;
                    break;
                case 3: lamdaFCAvg = userInput;
                    break;
                case 4: serviceTimeFCAvg = userInput;
                    break;
            }
        }
    }

    /**
     * This function generates a random number
     * from a normal distribution with a mean given by user
     * @param queuePriority: 1 is first class and 0 is coach class
     * @return a random integer number from a normal distribution with mean given by user
     */
    private static int generateArrivalRate(int queuePriority) {
        int min = 1;
        int max;
        int randomNumber;
        if (queuePriority == 1) {
            max = lamdaFCAvg * 2;
        } else {
            max = lamdaCoachAvg * 2;
        }
        randomNumber = ThreadLocalRandom.current().nextInt(min, max);
        return randomNumber;
    }
}

class Station{
    public Station(int priority, int serviceTimeFCAvg, int serviceTimeCoachAvg){
        this.priority = priority;
        this.serviceTimeCoachAvg = serviceTimeCoachAvg;
        this.serviceTimeFCAvg = serviceTimeFCAvg;
    }
    private int priority;   //high priority queue can go to low priority station
    private boolean isBusy = false;
    private long serviceStartTime;
    private long serviceEndTime;
    private int serviceTime; // serviceTime changes depending on the passenger coming in
    private int serviceTimeFCAvg;
    private int serviceTimeCoachAvg;
    private int stationTotalBusyTime = 0;

    // did not implement getPriority function since it is never used outside of Station class

    public float getStationOccupancyRate(int actualCheckinDuration) {
        float rateOfOccupancy;
        if (actualCheckinDuration==0) {
            return 0;
        }
        rateOfOccupancy = ((float)this.stationTotalBusyTime)/actualCheckinDuration * 100;
        return rateOfOccupancy;
    }

    public boolean isBusy() {
        return this.isBusy;
    }

    /**
     *
     * @param queueArray
     * @param time
     */
    public void takePassenger(Queue[] queueArray, int time){  //return service time of a passenger depending on his/her priority
        if(queueArray[priority].isEmpty()) {
            if (priority >= queueArray[0].getPriority() && !queueArray[0].isEmpty() && queueArray[1].isEmpty()) {
                queueArray[0].deQueue(time);  //first class station takes passengers from coach queue
                this.serviceTime = this.generateServiceTime(0);
                this.isBusy = true;
                this.serviceStartTime = time;
                this.serviceEndTime = this.serviceStartTime + this.serviceTime;
                this.incrementTotalBusyTime(queueArray[0]);
            }
        } else {
            queueArray[priority].deQueue(time);  // station takes on its corresponding queue
            this.serviceTime = (this.priority==0) ? this.generateServiceTime(0) : this.generateServiceTime(1);
            this.isBusy = true;
            this.serviceStartTime = time;
            this.serviceEndTime = this.serviceStartTime + this.serviceTime;
            this.incrementTotalBusyTime(queueArray[priority]);
        }

    }

    /**
     * This function generates a random number
     * from a normal distribution with a mean given by user
     * @param passengerPriority: 1 is first class and 0 is coach class
     * @return a random integer number from a normal distribution with mean given by user
     */
    private int generateServiceTime(int passengerPriority) {
        int min = 1; // min is always 1
        int max;
        int randomNumber;
        if (passengerPriority == 1) { // FC
            max = this.serviceTimeFCAvg * 2;
        } else { // coach
            max = this.serviceTimeCoachAvg * 2;
        }
        randomNumber = ThreadLocalRandom.current().nextInt(min, max);
        return randomNumber;
    }

    private void incrementTotalBusyTime(Queue servedQueue) {
        //if (servedQueue.isEmpty()) {
          //  return;
        //}
        // we do not care about the start/end time, only the duration
        this.stationTotalBusyTime += this.serviceTime;
        //System.out.println("Station: " + this.getClass() +"Total time: " + this.stationTotalBusyTime);
    }


    public void service(Queue[] queueArray, int time){
        if (isBusy) {
            return;
        }
        takePassenger(queueArray, time);
    }

    public boolean checkServiceDone(int currentTime){
        if(currentTime >= this.serviceEndTime){
            this.isBusy = false;
            return true;
        }
        return false;
    }

}


class Queue{
    private int priority;
    private LinkedList<Integer> passengerList;
    private int waitTimeSum = 0;
    private int passengerSum=0;
    private int maxWaitTime = 0;
    private int maxQueueLength = 0;

    public Queue(int priority){
        this.passengerList = new LinkedList<Integer>();
        this.priority = priority;
    }

    public int getPriority() {
        return this.priority;
    }

    public String toString() {
        return this.passengerList.toString();
    }

    public int getMaxWaitTime() {
        return this.maxWaitTime;
    }

    public int getMaxQueueLength() {
        return this.maxQueueLength;
    }

    public float getAverageWaitTime() {
        float avgWaitTime;
        if (passengerSum==0) {
            return 0;
        }
        // not using try catch as any float divide by 0 would be NaN instead of an exception
        avgWaitTime = (float) waitTimeSum / passengerSum;
        return avgWaitTime;
    }


    public boolean isEmpty() {
        return this.passengerList.isEmpty();
    }

    // using LinkedList.add();
    public void enQueue(int currentTime){
        int queueLength;
        this.passengerList.add(currentTime);
        queueLength = this.passengerList.size();
        this.maxQueueLength = this.maxQueueLength < queueLength ? queueLength : this.maxQueueLength;
    }

    public void deQueue(int currentTime){
        int waitTime=0;
        if(!isEmpty()){
            waitTime = currentTime - (int)this.passengerList.getFirst();
            this.passengerList.removeFirst();
            this.waitTimeSum+= waitTime;
            this.passengerSum++;
            this.maxWaitTime = this.maxWaitTime < waitTime ? waitTime : this.maxWaitTime;
        }
    }
}