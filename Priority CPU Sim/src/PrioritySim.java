/////////////////////////////////////////////////////////////////////
//	Thom Kitchen
//	
//	December 24, 2013
//	
//	Priority CPU Job Scheduling Simulation
//	
//	
/////////////////////////////////////////////////////////////////////

import java.io.*;
import java.text.DecimalFormat;
import java.util.LinkedList;

public class PrioritySim {
	//Lists to serve as queues for the CPU and i/o device
	static LinkedList readyQueue = new LinkedList();
	static LinkedList deviceQueue = new LinkedList();
	
	//List used to act as the CPU and i/o device
	static LinkedList CPU = new LinkedList();
	static LinkedList IO = new LinkedList();
	
	//Variables used as to set arrival times and determine wait times for each job
	static int simTime = 80;
	static int t=0;	
	static int avgWaitTime=0;
	static int avgTurn=0;
	static int cpuUtilization=0;
	//String to keep track of the order in which jobs are sent to the cpu
	static StringBuilder seqString = new StringBuilder();
	
	public static void main(String[] args) throws IOException {
		ProcessList priorityInput = new ProcessList("input_file.txt");		
		System.out.println();		
		System.out.println("/************* Priority Algorithm **********/");		
		PrioritySim sim = new PrioritySim(priorityInput.pList);		
	}
	
	public PrioritySim(Process[] p) {
		Process temp;
		Process ioTemp;
		Process inIOCheck;
			
	//Sort then initialize ready queue with processes
		prioritySort(p);		
		for (int i=0; i < p.length; i++) {                
			readyQueue.add(p[i]);
		}		
		
	//While loop that runs the actual simulation.  As long is something is in either the cpu, ready queue, or IO the simulation runs
		while (!readyQueue.isEmpty() || !CPU.isEmpty() || !IO.isEmpty()) {
			//Resort the ready queue by priority in case improper insertion from the IO device.
			prioritySort(readyQueue);
			System.out.println();
			System.out.println("Ready Queue jobs(" + readyQueue.size() + ") CPU (" + CPU.size() + ") Device(" + IO.size() + ")"  + "    t = " + t);
			
			if (!IO.isEmpty()) {
				//Handles any IO requests from processes.  IO can be handle multiple requests at a time.
				for (int index=0; index <IO.size(); index++) {
					ioTemp =  (Process) IO.get(index);					
					if (ioTemp.ioDuration < ioTemp.ioBurst-1) {
						ioTemp.ioDuration++;
						System.out.println("Process " + ioTemp.pID + " io burst remaining = " + (ioTemp.ioBurst - ioTemp.ioDuration) + " cpu burst remaining = " + (ioTemp.cpuBurst - ioTemp.cpuDuration));
						//Update queue with current time left
						IO.remove(index);
						IO.add(index, ioTemp);						
					}
					
					//Removes process from IO if no more burst, adds to readyQueue
					else {
						System.out.println("Removing process " + ioTemp.pID + " from io queue and sending to the ready queue" );
						readyQueue.add(ioTemp);
						IO.remove(index);					
						index--;
					}
				}
			}
			
			//First thing every cycle is to check the CPU to see if it's available
			if (!CPU.isEmpty()) {
				//Since the CPU is not empty, we must service the job in the CPU
				temp = (Process) CPU.getFirst();
				System.out.println("Process " + temp.pID + " cpu burst remaining = " + (temp.cpuBurst-temp.cpuDuration));
				
				//Check the jobs cpu duration.  If there is still time, increase duration and check if it needs IO.
				
				if (temp.cpuDuration < temp.cpuBurst) {
					temp.cpuDuration++;
					if (temp.cpuDuration == (temp.cpuBurst /2) && temp.ioBurst != 0) {			//Handles CPU requests from process
						System.out.println("adding process " + temp.pID + " to io queue");
						IO.add(temp);
						CPU.remove(temp);											
					}
				}
				
				//The job is finished and needs to have its servicing time recorded as well as remove it from the CPU 
				else {
					for (int k=0; k <p.length; k++) {
						if (temp.pID == p[k].pID) {
							p[k].arrivalTime = temp.arrivalTime;		
							p[k].turnAroundTime = t;
							p[k].finishTime = t;
							System.out.println("PID: " +p[k].pID + " Arrival time = " + p[k].arrivalTime + " Turn around time = " + p[k].turnAroundTime);
						}
					}
					CPU.removeFirst();										
				}				
			}
			
			//The CPU is currently empty and we need to check the ready queue and add a job if necessary
			else {
				if (t > 0)		//this was a quick fix to get the wait times to report correctly since the cpu can add and service a burst in the same cycle
					t= t-1;
				
				//Since the CPU is free, check the CPU ready queue. If something is there, use a temp process to move from the queue to the CPU
				if (readyQueue.size() != 0) {
					temp = (Process) readyQueue.poll();
					if (temp.cpuDuration ==0) {
						temp.arrivalTime = t;					
						temp.waitTime = t;
					}
					//Job is both added to CPU and burst serviced in same cycle
					temp.cpuDuration++;
					CPU.add(temp);
					System.out.println("Adding process " + temp.pID +" to CPU");
					seqString.append(Integer.toString(temp.pID));
					seqString.append('-');
				}				
				cpuUtilization++;				
			}						
			t++; //increase the timer
		}
				
		//The Simulation is finished.  The following displays the results from the sim.
		System.out.println();
		pIDSort(p);
		seqString.deleteCharAt(seqString.length()-1);						
		String seq = seqString.toString();									
		System.out.println("PID\tWait Time");
		for (int avg=0;avg<p.length;avg++){
			avgWaitTime+=p[avg].waitTime;
			System.out.println(p[avg].pID + "\t" + p[avg].waitTime);
		}
		avgWaitTime = avgWaitTime/p.length;
		System.out.println();
		System.out.println("Average wait time = " + avgWaitTime );
		System.out.println();
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		System.out.println("CPU utilization = " + df.format((100- ((float)cpuUtilization/t*100 ))) + "%");
		System.out.println();
		System.out.println("Sequence of processes in CPU: " + seq);		
	}	

	//Just a simple bubblesort to sort by priority of each process.
	public static void prioritySort( Process [] p ) {
	     int j;
	     boolean flag = true;  								
	     Process temp;  

	     while ( flag ) {
	            flag= false;   
	            for( j=0;  j < p.length -1;  j++ ) {
	                   if ( p[ j ].priority > p[j+1].priority ) {
	                           temp = p[ j ];          
	                           p[ j ] = p[ j+1 ];
	                           p[ j+1 ] = temp;
	                          flag = true;           
	                   } 
	            } 
	      } 
	} 
	
	//Overloaded method to resort the ready queue
	public static void prioritySort( LinkedList list ) {
		 int i,j;
	     boolean flag = true;  	
	     Process[] p = new Process[list.size()];
	     for (i=0; i<list.size(); i++){
	    	 p[i]=(Process)list.get(i);
	     }
	     Process temp;  

	     while ( flag ) {
	            flag= false;   
	            for( j=0;  j < p.length -1;  j++ ) {
	                   if ( p[ j ].priority > p[j+1].priority ) {
	                           temp = p[ j ];          
	                           p[ j ] = p[ j+1 ];
	                           p[ j+1 ] = temp;
	                          flag = true;           
	                   } 
	            } 
	      } 
	} 
	
	//Just a simple bubblesort to sort by pID of each process to display at the end
	public static void pIDSort( Process [] p ) {
	     int j;
	     boolean flag = true;  								
	     Process temp;  
	     while ( flag ) {
	            flag= false;   
	            for( j=0;  j < p.length -1;  j++ ) {
	                   if ( p[ j ].pID > p[j+1].pID ) {
	                           temp = p[ j ];          
	                           p[ j ] = p[ j+1 ];
	                           p[ j+1 ] = temp;
	                          flag = true;           
	                   } 
	            } 
	      } 
	} 
}