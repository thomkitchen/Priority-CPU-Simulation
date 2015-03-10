/////////////////////////////////////////////////////////////////////
//	Thom Kitchen
//	
//	December 24, 2013
//	
//	Priority CPU Job Scheduling Simulation
//		-Array of processes to be created from reading in a text file.
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
		
		//quick return to determine how many processes are to be added to array
		BufferedReader is = new BufferedReader(new FileReader(str));
		while(is.readLine()!=null)
			NUM ++;
		is.close();
		
		//create array for NUM processes
		pList = new Process[NUM];
		
		//This is made specifically for the format of the data given in input_file.txt to determine attributes for each process
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
