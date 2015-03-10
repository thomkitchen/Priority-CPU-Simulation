/////////////////////////////////////////////////////////////////////
//	Thom Kitchen
//	
//	December 24, 2013
//	
//	Priority CPU Job Scheduling Simulation
//		-Process object that contains properties required for simulation	
//	
/////////////////////////////////////////////////////////////////////

public class Process {
	
	 public  int pID;			//																
	 public  int cpuBurst;		//   Read from file     								
	 public  int ioBurst;		//													
	 public  int priority;		//														
																							
																				
	 public  int arrivalTime;	//
	 public  int cpuDuration;
	 public  int ioDuration;
	 public  int responseTime; //Attributes for each process
	 public  int waitTime;		//  Determined by simulation
	 public  int turnAroundTime = 0;	//																	
	 public  int finishTime;	//																	
	 public  boolean isDone;																		
	 public  boolean isInCPU, isInIO, isInQueue; 							 				
		
	//Overloaded constructors initializing all the key values to be read from text file	
	 public Process(int id, int cpu, int io, int p) {
		 pID = id;
		 cpuBurst = cpu;
		 ioBurst = io;
		 priority = p;
		 cpuDuration = 0;
		 ioDuration = 0;
		 isInCPU = false;
		 isInIO = false;
		 isInQueue = true;
	 }
	 
	 public Process() {
	 
	 }
	 
	 public Process( Process p) {
		 pID = p.pID;
		 cpuBurst = p.cpuBurst;
		 ioBurst = p.ioBurst;
		 priority = p.priority;
		 cpuDuration = p.cpuDuration;
		 ioDuration = p.ioDuration;
	 }
	 public String toString() {
		 String str = "" + pID;
		 return str;
	 }
	 
	 public int getPID() {
		 return pID;
	 }
	 
	 public void setPID(int p) {
		 pID = p;
	 }

	//etc. for other attributes. I won't be using any get and set methods since they will be set once by the constructor
}
