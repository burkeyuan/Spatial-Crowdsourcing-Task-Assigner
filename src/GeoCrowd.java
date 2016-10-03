package igeocrowd;

import java.text.DecimalFormat;
import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import igeocrowd.MBR;
import igeocrowd.Worker;

public class GeoCrowd {

	public static Hashtable<Integer, ArrayList<Task>> TaskForWorker; 
	public static Hashtable<Task, ArrayList<Integer>> WorkerForTask;
	
	public static String workerFileName = "worker_1k.txt";
	public static String workerFileName1 = "worker_1k_maxT_20.txt";
	public static String taskFileName = "task_10k.txt";
	public static String RealTaskSetIn = "tasksNY.txt";
	public static String RealTaskSetOut = "Processed_Task_NY.txt";
	public static String RealWorkerSetIn = "workersNY.txt";
	public static String RealWorkerSetOut = "Processed_Worker_NY.txt";
	
	public static int WorkerNo = 1000; //number of workers
	public static double minLatitude=37.90;
	public static double maxLatitude=38.10;
	public static double minLongitude=-77.10;
	public static double maxLongitude=-76.90;
	public static double DifRangeLng = 0.2;
	public static double DifRangeLat = 0.2;
	
	public int WorkerCount=0; //number of workers generated so far
	
	public type assign_type = type.HSP;
	
	public static int timeCounter =0;
	public ArrayList<Worker> workerList = new ArrayList<>();
	public static ArrayList<Task> taskList = new ArrayList<>();
	public static int TaskNo = 40000; //00; // number of tasks
	public static int TaskCount = 0; //number of tasks generated so far
	public int factor = 200;
	public static double maxRangePerc = 0.4;//maximum range of an mbr
	public int TaskAssignedTotal=0;
	public double SumDistanceTotal=0;
	
	public long totalTime1 = 0;
	public long totalTime2 = 0;
	
	public static void main(String[] args) {
		try{
			GeoCrowd iGEOCrowd = new GeoCrowd();
			//iGEOCrowd.generateWorkers1(workerFileName);
			iGEOCrowd.readWorkers1(workerFileName);
			//iGEOCrowd.generateTasks(taskFileName);
			iGEOCrowd.readTasks(taskFileName);
			//iGEOCrowd.readInTask();
			//iGEOCrowd.readInWorker();
			//iGEOCrowd.mbr();
			//iGEOCrowd.maxt();
		    iGEOCrowd.assignAllWorkers();
		    iGEOCrowd.calculateFlow();
			//System.out.println("2");
			//iGEOCrowd.print(taskList);
		}catch(Exception e){}
	}
    // generate the worker set based on accuracy of ABCD type
	public void generateWorkers1(String fileName){
        int maxSumTaskWorkers = 0;
    	double maxRangeX = (maxLatitude-minLatitude)*maxRangePerc;
        double maxRangeY = (maxLongitude-minLongitude)*maxRangePerc;
        
		try{
	        FileWriter writer = new FileWriter(fileName);
	        BufferedWriter out = new BufferedWriter(writer);
	        for(int i = 0; i < WorkerNo; i++){
	        	double lat = Math.random()*(maxLatitude-minLatitude)+minLatitude;
	        	double lng = Math.random()*(maxLongitude-minLongitude)+minLongitude;
	        	int maxT = (int)(Math.random()*10 + 1);
	        	maxSumTaskWorkers += maxT;
	        	double rangeX = maxRangeX;
	            double rangeY = maxRangeY;
	            DecimalFormat dcmFmt = new DecimalFormat("0.00");
	            double acc_A = Math.random()*10;
	            double acc_B = Math.random()*10;
	            double acc_C = Math.random()*10;
	            double acc_D = Math.random()*10;
	            MBR mbr = MBR.createMBR(lat, lng, rangeX, rangeY);
	            checkBoundaryMBR(mbr);
	        	out.write(i+","+lat+","+lng+","+maxT+","+"["+mbr.getMinLat()+","+mbr.getMaxLat()+","+mbr.getMinLng()+","+mbr.getMaxLng()+"],"+
	        			dcmFmt.format(acc_A)+","+dcmFmt.format(acc_B)+","+dcmFmt.format(acc_C)+","+dcmFmt.format(acc_D)+"\n");
	        }
	        out.close();
		}catch(Exception e){}
	    System.out.println("Workers generated!");
	    System.out.println("sum of maxTask of all workers:"+ maxSumTaskWorkers);
	}
	
	// generate the worker set based on can or cannot do the task
	/*public void generateWorkers2(String fileName){
        int maxSumTaskWorkers = 0;
    	double maxRangeX = (maxLatitude-minLatitude)*maxRangePerc;
        double maxRangeY = (maxLongitude-minLongitude)*maxRangePerc;
        
		try{
	        FileWriter writer = new FileWriter(fileName);
	        BufferedWriter out = new BufferedWriter(writer);
	        for(int i = 0; i < WorkerNo; i++){
	        	double lat = Math.random()*(maxLatitude-minLatitude)+minLatitude;
	        	double lng = Math.random()*(maxLongitude-minLongitude)+minLongitude;
	        	int maxT = (int)(Math.random()*10);
	        	maxSumTaskWorkers += maxT;
	        	double rangeX = Math.random()* maxRangeX;
	            double rangeY = Math.random()* maxRangeY;
	            int acc_A = Math.random()>0.5?1:0;
	            int acc_B = Math.random()>0.5?1:0;
	            int acc_C = Math.random()>0.5?1:0;
	            int acc_D = Math.random()>0.5?1:0;
	            MBR mbr = MBR.createMBR(lat, lng, rangeX, rangeY);
	            checkBoundaryMBR(mbr);
	        	out.write(i+","+lat+","+lng+","+maxT+","+"["+mbr.getMinLat()+","+mbr.getMaxLat()+","+mbr.getMinLng()+","+mbr.getMaxLng()+"],"+
	        			acc_A+","+acc_B+","+acc_C+","+acc_D+"\n");
	        }
	        out.close();
		}catch(Exception e){}
	    System.out.println("Workers generated!");
	    System.out.println("sum of maxTask of all workers:"+ maxSumTaskWorkers);
	}*/
	
	public void checkBoundaryMBR(MBR mbr){
	       if(mbr.getMinLat()<minLatitude)
	           mbr.setMinLat(minLatitude);
	       if(mbr.getMaxLat() > maxLatitude)
	           mbr.setMaxLat(maxLatitude);
	       if(mbr.getMinLng()<minLongitude)
	           mbr.setMinLng(minLongitude);
	       if(mbr.getMaxLng() > maxLongitude)
	           mbr.setMaxLng(maxLongitude);
	}
	
	public void readWorkers1(String fileName){
	       workerList = new ArrayList<>();
	       int maxSumTaskWorkers=0;
	       System.out.println("Workers:");
	       //double maxRangeX = (maxLatitude-minLatitude)*maxRangePerc;
	       //double maxRangeY = (maxLongitude-minLongitude)*maxRangePerc;
	       int cnt=0;
	       try{
	    	   FileReader reader = new FileReader(fileName);
	    	   BufferedReader in = new BufferedReader(reader);
	       
	    	   while(in.ready()){
	    		   String line = in.readLine();
	    		   line = line.replace("[", "");
	    		   line = line.replace("]", "");
	    		   String[] parts = line.split(",");
	    		   int userId = Integer.parseInt(parts[0]);
	               double lat = Double.parseDouble(parts[1]);
	               double lng = Double.parseDouble(parts[2]);
	               int maxT = Integer.parseInt(parts[3]);
	               maxSumTaskWorkers += maxT;
	               double mbr_minLat = Double.parseDouble(parts[4]);
	               double mbr_minLng = Double.parseDouble(parts[6]);
	               double mbr_maxLat = Double.parseDouble(parts[5]);
	               double mbr_maxLng = Double.parseDouble(parts[7]);
	               double A_acc = Double.parseDouble(parts[8]);
	               double B_acc = Double.parseDouble(parts[9]);
	               double C_acc = Double.parseDouble(parts[10]);
	               double D_acc = Double.parseDouble(parts[11]);
	               MBR mbr = new MBR(mbr_minLat, mbr_minLng, mbr_maxLat, mbr_maxLng);
	               Accuracy acc = new Accuracy(A_acc, B_acc, C_acc, D_acc);
	               Worker w = new Worker(userId, lat, lng, maxT, mbr, acc);
	               workerList.add(w);
	               cnt++;
	    	   }
	       in.close();
	       }catch(Exception e){}
	       System.out.println(cnt+ "  Workers generated!");
	       WorkerCount += cnt;
	       System.out.println("Sum of maxTask of all workers:"+maxSumTaskWorkers+"\n");
	   }
	
	/*public void readWorkers2(String fileName){
	       workerList = new ArrayList<>();
	       int maxSumTaskWorkers=0;
	       System.out.println("Workers:");
	       //double maxRangeX = (maxLatitude-minLatitude)*maxRangePerc;
	       //double maxRangeY = (maxLongitude-minLongitude)*maxRangePerc;
	       int cnt=0;
	       try{
	    	   FileReader reader = new FileReader(fileName);
	    	   BufferedReader in = new BufferedReader(reader);
	       
	    	   while(in.ready()){
	    		   String line = in.readLine();
	    		   line = line.replace("[", "");
	    		   line = line.replace("]", "");
	    		   String[] parts = line.split(",");
	    		   int userId = Integer.parseInt(parts[0]);
	               double lat = Double.parseDouble(parts[1]);
	               double lng = Double.parseDouble(parts[2]);
	               int maxT = Integer.parseInt(parts[3]);
	               maxSumTaskWorkers += maxT;
	               double mbr_minLat = Double.parseDouble(parts[4]);
	               double mbr_minLng = Double.parseDouble(parts[5]);
	               double mbr_maxLat = Double.parseDouble(parts[6]);
	               double mbr_maxLng = Double.parseDouble(parts[7]);
	               int A_acc = Integer.parseInt(parts[8]);
	               int B_acc = Integer.parseInt(parts[9]);
	               int C_acc = Integer.parseInt(parts[10]);
	               int D_acc = Integer.parseInt(parts[11]);
	               MBR mbr = new MBR(mbr_minLat, mbr_minLng, mbr_maxLat, mbr_maxLng);
	               Accuracy acc = new Accuracy(A_acc, B_acc, C_acc, D_acc);
	               Worker w = new Worker(userId, lat, lng, maxT, mbr, acc);
	               workerList.add(w);
	               cnt++;
	    	   }
	       in.close();
	       }catch(Exception e){}
	       System.out.println(cnt+ "  Workers generated!");
	       WorkerCount += cnt;
	       System.out.println("sum of maxTask of all workers:"+maxSumTaskWorkers);
	   }*/
	
	
	/* Shuaiqi's Code*/
	
	public int getRowIdx(double lat){
	       int row = (int)((lat - minLatitude)* factor);
	       return row;
	}
	public int getColIdx(double lng){
	       int col = (int)((lng - minLongitude)* factor);
	       return col;
	}
	
	
	
	public String generateType(){
		int type =(int)(4*Math.random());
		String ret = null;
		switch(type){
		case 0:
			ret= "A";
			break;
		case 1:
			ret= "B";
			break;
		case 2:
			ret= "C";
			break;
		case 3:
			ret= "D";
			break;
		}
		return ret;
		
	}
	public void generateTasks(String fileName){
		taskList = new ArrayList<>();
        int listCount = 0;
        
        //TaskCount =0;
        try{
        FileWriter writer = new FileWriter(fileName);
        BufferedWriter out = new BufferedWriter(writer);
        for(int i=0; i<TaskNo ; i++){
           //*******arbitrary task******
           /*Random l= new Random();
           int desiredStandardDeviation = 5;
           double desiredMean = 0.5;
           double randomLa = l.nextGaussian()*desiredStandardDeviation+desiredMean;
           double randomLo = l.nextGaussian()*desiredStandardDeviation+desiredMean;*/
           double lat = Math.random()*(maxLatitude-minLatitude)+minLatitude;
           double lng = Math.random()*(maxLongitude-minLongitude)+minLongitude;
           int row =  getRowIdx(lat);
           int col =  getColIdx(lng);
           String tp= generateType();
           int density = 0;
           int time = timeCounter++;
           int k = 1;
           
           Task t = new Task(tp, lat,lng,k,time,density,row);
           out.write(lat+","+lng+","+k+","+time+","+density+","+tp+"\n");           
           taskList.add(t);

           listCount++;
       }
        TaskCount += TaskNo;
        System.out.println("number of tasks generated:"+listCount);
        //System.out.println("number of tasks generated so far:"+taskList.size());
        out.close();
       }catch(Exception e){}
   }
	
	public void readTasks(String fileName){
		taskList = new ArrayList<>();
        System.out.println("Tasks:");
        int listCount =0;
        try{
        FileReader reader = new FileReader(fileName);
        BufferedReader in = new BufferedReader(reader);
        while (in.ready()){
            String line = in.readLine();
            String[] parts = line.split(",");
            
            double lat = Double.parseDouble(parts[0]);
            double lng = Double.parseDouble(parts[1]);
            int k = Integer.parseInt(parts[2]);
            int time = Integer.parseInt(parts[3]);
            double density = Double.parseDouble(parts[4]);
            String tp = parts[5];
            int row=0;//temporary no meaning!!!!!!!!!!!!
            Task t = new Task(tp, lat, lng, k, time, density, row);
            taskList.add(t);
            listCount++;           
        }
        TaskCount += TaskNo;
        //timeCounter++;
        System.out.println("number of tasks generated:"+listCount);
        //System.out.println("number of tasks generated so far:"+taskList.size());
        in.close();
       }catch(Exception e){}
   }
	// Process raw task data
	public void readInTask(){
		int TaskCount=0;
        try{
	        FileReader reader = new FileReader(RealTaskSetIn);
	        BufferedReader in = new BufferedReader(reader);
	        FileWriter writer = new FileWriter(RealTaskSetOut);
	        BufferedWriter out = new BufferedWriter(writer);
	        while (in.ready()){
	            String line = in.readLine();
	            String[] parts = line.split(",");
	          	
	            double lat = Double.parseDouble(parts[3]);
	            double lng = Double.parseDouble(parts[4]);
	            
	            //System.out.println(lat);
	            //System.out.println(lng);
	       //--------------------same above
	            
	            String tp= generateType();
	            int density = 0;
	            int time = timeCounter++; 
	            int k = 1;
	            int row =0;//no meaning
	            
	            //---generate random
	            
	            Task t = new Task(tp, lat, lng, k, time, density, row);
	            out.write(lat+","+lng+","+k+","+time+","+density+","+tp+"\n");  
	            taskList.add(TaskCount, t);
	            TaskCount++;           
	        }
	        System.out.println("number of tasks generated so far:"+TaskCount);
	        in.close();
	        out.close();
       }catch(Exception e){}
   }
	
	public void readInWorker(){
		int SumWorkers = 0;
		double maxRangeX = DifRangeLat*maxRangePerc;
        double maxRangeY = DifRangeLng*maxRangePerc;
		try{
	        FileReader reader = new FileReader(RealWorkerSetIn);
	        BufferedReader in = new BufferedReader(reader);
	        FileWriter writer = new FileWriter(RealWorkerSetOut);
	        BufferedWriter out = new BufferedWriter(writer);
			while (in.ready()){
		        String line = in.readLine();
		        String[] parts = line.split(",");
		        	double lat = Double.parseDouble(parts[3]);
		        	double lng = Double.parseDouble(parts[4]);
		        	int maxT = (int)(Math.random()*10);
		        	double rangeX = Math.random()* maxRangeX;
		            double rangeY = Math.random()* maxRangeY;
		            DecimalFormat dcmFmt = new DecimalFormat("0.00");
		            double acc_A = Math.random()*10;
		            double acc_B = Math.random()*10;
		            double acc_C = Math.random()*10;
		            double acc_D = Math.random()*10;
		            
		            MBR mbr = MBR.createMBR(lat, lng, rangeX, rangeY);

		        	out.write(SumWorkers+","+lat+","+lng+","+maxT+","+"["+mbr.getMinLat()+","+mbr.getMaxLat()+","+mbr.getMinLng()+","+mbr.getMaxLng()+"],"+
		        			dcmFmt.format(acc_A)+","+dcmFmt.format(acc_B)+","+dcmFmt.format(acc_C)+","+dcmFmt.format(acc_D)+"\n");
		        SumWorkers++;
			}
	        in.close();
	        out.close();
	        System.out.print(SumWorkers);
       }catch(Exception e){}
   }
	
	public void mbr(){
		int SumWorkers = 0;
		double maxRangeX = DifRangeLat*maxRangePerc;
        double maxRangeY = DifRangeLng*maxRangePerc;
		try{
	        FileReader reader = new FileReader(workerFileName);
	        BufferedReader in = new BufferedReader(reader);
	        FileWriter writer = new FileWriter(workerFileName1);
	        BufferedWriter out = new BufferedWriter(writer);
			while (in.ready()){
	    		 String line = in.readLine();
	    		 line = line.replace("[", "");
	    		 line = line.replace("]", "");
	    		 String[] parts = line.split(",");
	    		 int userId = Integer.parseInt(parts[0]);
	             double lat = Double.parseDouble(parts[1]);
	             double lng = Double.parseDouble(parts[2]);
	             int maxT = Integer.parseInt(parts[3]);
	             
	             double rangeX = Math.random()* maxRangeX;
		         double rangeY = Math.random()* maxRangeY;
		         DecimalFormat dcmFmt = new DecimalFormat("0.00");
		         
		         double A_acc = Double.parseDouble(parts[8]);
		         double B_acc = Double.parseDouble(parts[9]);
		         double C_acc = Double.parseDouble(parts[10]);
		         double D_acc = Double.parseDouble(parts[11]);
		         
		         MBR mbr = MBR.createMBR(lat, lng, rangeX, rangeY);

		         checkBoundaryMBR(mbr);
		         out.write(userId+","+lat+","+lng+","+maxT+","+"["+mbr.getMinLat()+","+mbr.getMaxLat()+","+mbr.getMinLng()+","+mbr.getMaxLng()+"],"+
		        			dcmFmt.format(A_acc)+","+dcmFmt.format(B_acc)+","+dcmFmt.format(C_acc)+","+dcmFmt.format(D_acc)+"\n");
		         SumWorkers++;
			}
	        in.close();
	        out.close();
	        System.out.print(SumWorkers);
       }catch(Exception e){}
   }
	
	
	public void maxt(){
		int SumWorkers = 0;
		try{
	        FileReader reader = new FileReader(workerFileName);
	        BufferedReader in = new BufferedReader(reader);
	        FileWriter writer = new FileWriter(workerFileName1);
	        BufferedWriter out = new BufferedWriter(writer);
			while (in.ready()){
	    		 String line = in.readLine();
	    		 line = line.replace("[", "");
	    		 line = line.replace("]", "");
	    		 String[] parts = line.split(",");
	    		 int userId = Integer.parseInt(parts[0]);
	             double lat = Double.parseDouble(parts[1]);
	             double lng = Double.parseDouble(parts[2]);
	             
	             int maxT = (int)(Math.random()*20 + 1);
	             
	             double mbr_minLat = Double.parseDouble(parts[4]);
	               double mbr_minLng = Double.parseDouble(parts[6]);
	               double mbr_maxLat = Double.parseDouble(parts[5]);
	               double mbr_maxLng = Double.parseDouble(parts[7]);
	               double A_acc = Double.parseDouble(parts[8]);
	               double B_acc = Double.parseDouble(parts[9]);
	               double C_acc = Double.parseDouble(parts[10]);
	               double D_acc = Double.parseDouble(parts[11]);

		         out.write(userId+","+lat+","+lng+","+maxT+","+"["+mbr_minLat+","+mbr_maxLat+","+mbr_minLng+","+mbr_maxLng+"],"+
		        			A_acc+","+B_acc+","+C_acc+","+D_acc+"\n");
		         SumWorkers++;
			}
	        in.close();
	        out.close();
	        System.out.print(SumWorkers);
       }catch(Exception e){}
   }
	
	

	// generate hashmap
	public void assignAllWorkers(){
	       WorkerForTask = new Hashtable<Task, ArrayList<Integer>>(); 
	       TaskForWorker = new Hashtable<Integer, ArrayList<Task>>();
	       //inverted = new ArrayList();
	       //timeCounter++;
	       for(int i=0; i<workerList.size();i++){
	           //timeCounter++;
	           Worker w = workerList.get(i);
	           rangeQuery1(i,w.getMBR());
	       }
	}
	// rangeQuery without filtering the unsatisfied workers
	public void rangeQuery1(int workerIdx, MBR mbr){
	       //int t =0;
	       for(Task task: taskList){
	           if (task.isOverlapped(mbr.minLat, mbr.minLng, mbr.maxLat, mbr.maxLng)){
	               if (!task.isAssigned()){
	            	   //if (!task.isExpired()){
		                   //if ((timeCounter - task.getEntryTime()) < TaskDuration){
		                	   // Tasks for worker, task as VALUE and worker_id as KEY
		                	   if (!TaskForWorker.containsKey(workerIdx)){
		                           ArrayList<Task> arrT = new ArrayList<>();
		                           arrT.add(task);
		                           TaskForWorker.put(workerIdx, arrT);
		                       }else{
		                    	   ArrayList<Task> arrT = TaskForWorker.get(workerIdx);
		                           arrT.add(task);
		                           TaskForWorker.put(workerIdx, arrT);
		                       }
		                	   // worker for task, task as KEY and worker_id as VALUE
		                       if (!WorkerForTask.containsKey(task)){
		                           ArrayList<Integer> arrW = new ArrayList<>();
		                           arrW.add(workerIdx);
		                           WorkerForTask.put(task, arrW);
		                       }else{
		                           ArrayList<Integer> arrW = WorkerForTask.get(task);
		                           arrW.add(workerIdx);
		                           WorkerForTask.put(task, arrW);
		                       }
		                   }/*else{
		                       task.setExpired();
		                       System.out.print("task expired \n");
		                   }*/
	           }//if not overlapped
	           //t++;
	       }
	}
	
	//rangeQuery with filtering the unqualified workers
	/*public void rangeQuery2(int workerIdx, MBR mbr){
	       for(Task task: taskList){
	           if (task.isOverlapped(mbr.minLat, mbr.minLng, mbr.maxLat, mbr.maxLng)){
	        	   if(task.isMatched(workerIdx, workerList)){
		               if (!task.isAssigned()){
		            	   //if (!task.isExpired()){
			                   //if ((timeCounter - task.getEntryTime()) < TaskDuration){
			                	   // Tasks for worker, task as VALUE and worker_id as KEY
			                	   if (!TaskForWorker.containsKey(workerIdx)){
			                           ArrayList<Task> arrT = new ArrayList<>();
			                           arrT.add(task);
			                           TaskForWorker.put(workerIdx, arrT);
			                       }else{
			                    	   ArrayList<Task> arrT = TaskForWorker.get(workerIdx);
			                           arrT.add(task);
			                           TaskForWorker.put(workerIdx, arrT);
			                       }
			                	   // worker for task, task as KEY and worker_id as VALUE
			                       if (!WorkerForTask.containsKey(task)){
			                           ArrayList<Integer> arrW = new ArrayList<>();
			                           arrW.add(workerIdx);
			                           WorkerForTask.put(task, arrW);
			                       }else{
			                           ArrayList<Integer> arrW = WorkerForTask.get(task);
			                           arrW.add(workerIdx);
			                           WorkerForTask.put(task, arrW);
			                       }
			                   }/*else{
			                       task.setExpired();
			                       System.out.print("task expired \n ");
			                   }
	        	   }//t++;
	           }
	       }
	}*/
	
	public void calculateFlow(){
	        int V = TaskForWorker.size() + taskList.size();
	        int s = V, t = V + 1;    //int s = 0, t = V-1;
	        
	        FlowNetwork G1 = new FlowNetwork(V, TaskForWorker, workerList, taskList, assign_type, 0);
	        long time1 = System.currentTimeMillis();
	        FordFulkerson maxflow1 = new FordFulkerson(G1, s, t, assign_type, workerList.size(), taskList);
	        long time2 = System.currentTimeMillis();
	        totalTime1 += (time2-time1);
	        
	        FlowNetwork G2 = new FlowNetwork(V, TaskForWorker, workerList, taskList, assign_type, 1);
	        long time3 = System.currentTimeMillis();
	        FordFulkerson maxflow2 = new FordFulkerson(G2, s, t, assign_type, workerList.size(), taskList);
	        long time4 = System.currentTimeMillis();
	        totalTime2 += (time4-time3);
	        
	        /*System.out.println("Max flow from " + s + " to " + t);
	        System.out.print("Min cut: ");
	        int count = 0;
		    for (int v = 0; v < G.V(); v++) {
		        if (maxflow.inCut(v)) {
		        	System.out.println(v + " ");
		        	count++;
		        }
		    }*/
	        System.out.println("\n/***********************RESULT***********************/");
	        System.out.println("maxT = 5");
		    System.out.println("Maximum Task Assignment without Accuracy:");
	        TaskAssignedTotal = (int) maxflow1.value();
	        SumDistanceTotal = maxflow1.sumDist;
	        //System.out.println("\nNumber of Edges = " +G1.E()+"\nNumber of Nodes: "+G1.V());
	        System.out.println("\nMax flow value = " +  maxflow1.value()+"\n    with sum disntace: "+maxflow1.sumDist+"\n    with sum accuracy: "+maxflow1.sumAcc);
	        System.out.println("\n Total number of assigned tasks:" + TaskAssignedTotal);

	        System.out.println("--------------------------------------------------");
	        
	        System.out.println("Maximum Task Assignment with Accuracy:");
	        TaskAssignedTotal = (int) maxflow2.value();
	        SumDistanceTotal = maxflow2.sumDist;
	        //System.out.println("\nNumber of Edges = " +G2.E()+"\nNumber of Nodes: "+G2.V());
	        System.out.println("\nMax flow value = " +  maxflow2.value()+"\n    with sum disntace: "+maxflow2.sumDist+"\n    with sum accuracy: "+maxflow2.sumAcc);
	        System.out.println("\n Total number of assigned tasks:" + TaskAssignedTotal);
	        return;
	   }
}
