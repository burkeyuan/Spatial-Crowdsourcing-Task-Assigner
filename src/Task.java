package igeocrowd;

import java.util.ArrayList;

/**
*
* @author TongGou
*/

public class Task {
	private double lat;
    private double lng;
    private int k;
    private int entryTime;
    private int assigned=0;
    private boolean expired = false;
    private double entropy;
    private int entropyRowId;
    private String type;
    //Task t = new Task(lat,lng,k,time,density);
    public Task(String tp, double lt, double ln, int K, int entry, double dens, int row){
    	type = tp;
    	lat = lt;
        lng = ln;
        k = K;
        entryTime = entry;
        entropy = dens;
        entropyRowId = row;
    } 
    /*public double[] getPoint(){
        return point;
    }
    */
    public String getType(){
    	return type;	
    }
    public int getEntryTime(){
        return entryTime;
    }
    public double getLat(){
        return lat;
    }
    public double getLng(){
        return lng;
    }
     public int getEntropyRowId(){
        return entropyRowId;
    }
    public void print(){
        System.out.println("lat:"+lat+"   lng:"+lng+"   k:"+k+"   time:"+entryTime+"   type:"+type);
    }
    public boolean isAssigned(){
        return (assigned>=k);
    }
    public void incAssigned(){
        assigned++;
    }
    public boolean isExpired(){
        return expired;
    }
    public void setExpired(){
        expired =true;
    }
    public double getEntropy(){
        return entropy;
    }
    public boolean isOverlapped(double minLat_mbr,double minLng_mbr,double maxLat_mbr, double maxLng_mbr){
        boolean overlapped = false;
        if((lat>=minLat_mbr)&&(lat<=maxLat_mbr)&&(lng>=minLng_mbr)&&(lng<=maxLng_mbr))
            overlapped =true;
        return overlapped;
    }
    public boolean isMatched(int workerIdx, ArrayList<Worker> workerList){
        boolean match = false;
        if(type.charAt(0) == 'A' && workerList.get(workerIdx).getacc().getIaccA() == 1) 
        	match = true;
        if(type.charAt(0) == 'B' && workerList.get(workerIdx).getacc().getIaccB() == 1) 
    		match = true;
        if(type.charAt(0) == 'C' && workerList.get(workerIdx).getacc().getIaccC() == 1) 
    		match = true;
        if(type.charAt(0) == 'D' && workerList.get(workerIdx).getacc().getIaccD() == 1) 
    		match = true;
        return match;
    }
}
