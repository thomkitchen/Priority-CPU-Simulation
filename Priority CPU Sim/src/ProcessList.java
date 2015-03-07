/////////////////////////////////////////////////////////////////////
//	Thom Kitchen
//	
//	December 24, 2013
//	
//	Priority CPU Job Scheduling Simulation
//		-Array of processes with necessary information required for the simulation
//	
/////////////////////////////////////////////////////////////////////

import java.util.Scanner;
import java.io.*;

public class ProcessList {
	
	Process[] pList;
	int NUM=0;
	int PID;
	int CPU;
	int IO;
	int PRI;
	
	File file;
	public ProcessList(String str) throws IOException {
		
		File file = new File(str);
		Scanner input = new Scanner(file);
		
		BufferedReader is = new BufferedReader(new FileReader(str));
		while(is.readLine()!=null)
			NUM ++;
		is.close();
		
		pList = new Process[NUM];
		for (int index = 0; index <pList.length; index++) {
			PID = input.nextInt();
			CPU = input.nextInt();
			IO = input.nextInt();
			PRI = input.nextInt();
			pList[index] = new Process(PID, CPU, IO, PRI);
		}
		
		input.close();
		
	}

}
