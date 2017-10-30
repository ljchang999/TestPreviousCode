1. type "javac AirportCheckin.java" to compile
2. type "java AirportCheckin" to run the program

This program is going to simulate a check-in operation for an airline company at an airport terminal.

The program starts by prompting the user and asking for user inputs for: check-in during time, first-class and coach passenger's avg arrival rate (time for a new passenger to join the corresponding waiting queue), and first-class and coach passenger's avg service rate (time got served by corresponding station).

All user inputs should be integers. The program will prompt you to give it a correct type of input if the input you give is not an integer.

The 4 avg rates (FirsstClassArrivalRate, FirstClassServiceRate, CoachArricalRate, CoachServiceRate) that given by a
user are subjected to change in the run time so the same inputs may have difference results in the end of the simulation.

The program shows the stations (busy or not) and queues (grows and shrink) status as well as the passengers in the queues (passengers are represented as int value meaning the time he/she join a queue).


This program's takePassenger() method tends to give coach passenger higher priority than first-class passenger to be served at first-class station based on the order of if-else statement. (I made this assumption sincee it's never mentioned in prof's documentation).

The program waits for the stations to finish serving people in the queues when the check-in time is up and ends when all the queues are clear.



