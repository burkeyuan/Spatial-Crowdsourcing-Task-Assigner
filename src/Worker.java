package igeocrowd;

//import java.sql.Date;

public class Worker {
    private int userID;
    private double lat;
    private double lng;
    private int maxTaskNo;
    //private Date date;
    //private String dateStr;
    private MBR mbr;
    private Accuracy acc;
    public Worker(int id ,double lt,double ln, MBR m, Accuracy ac){
        userID = id;
        lat = lt;
        lng = ln;
        //dateStr = d;
        maxTaskNo = 1;
        mbr = new MBR(m);
        acc = ac;
    }
    public Worker(int id ,double lt,double ln, int maxT, MBR m, Accuracy ac){
        userID = id;
        lat = lt;
        lng = ln;
        //dateStr = d;
        maxTaskNo = maxT;
        mbr = new MBR(m);
        acc = ac;
    }
   
    public Worker(double lt,double ln, int maxT, MBR m){
        lat = lt;
        lng = ln;
        maxTaskNo = maxT;
        mbr = new MBR(m);
    }
    public int getMaxTaskNo(){
        return maxTaskNo;
    }
    public int getUserID(){
        return userID;
    }
    public void incMaxTaskNo(){
        maxTaskNo++;
    }
    public MBR getMBR(){
        return mbr;
    }
    public Accuracy getacc(){
        return acc;
    }
     public double getLatitude(){
        return lat;
    }
     public double getLongitude(){
        return lng;
    }
    public void print(){
        System.out.println("lat:"+lat+"   lng:"+lng+"   maxTaskNo:"+maxTaskNo+"   mbr:["+mbr.getMinLat()+ ","+mbr.getMinLng()+ ","+mbr.getMaxLat()+ ","+mbr.getMaxLng()+"]"+ ","+ acc.getDaccA()+ ","+ acc.getDaccB()+ ","+ acc.getDaccC()+ ","+ acc.getDaccD());
        //System.out.println("lat:"+lat+"   lng:"+lng+"   maxTaskNo:"+maxTaskNo+"   mbr:["+mbr.getMinLat()+ ","+mbr.getMinLng()+ ","+mbr.getMaxLat()+ ","+mbr.getMaxLng()+"]"+ ","+ acc.getIaccA()+ ","+ acc.getIaccB()+ ","+ acc.getIaccC()+ ","+ acc.getIaccD());
    }
    public String toStr(){
        String str = userID+","+lat+","+lng+","+maxTaskNo+",["+mbr.getMinLat()+ ","+mbr.getMinLng()+ ","+mbr.getMaxLat()+ ","+mbr.getMaxLng()+"]"+ ","+ acc.getDaccA()+ ","+ acc.getDaccB()+ ","+ acc.getDaccC()+ ","+ acc.getDaccD();
        //String str = userID+","+lat+","+lng+","+maxTaskNo+",["+mbr.getMinLat()+ ","+mbr.getMinLng()+ ","+mbr.getMaxLat()+ ","+mbr.getMaxLng()+"]"+ ","+ acc.getIaccA()+ ","+ acc.getIaccB()+ ","+ acc.getIaccC()+ ","+ acc.getIaccD();
        return str;
    }
    public void setMinLat(double l){
        mbr.minLat = l;
    }
    public void setMaxLat(double l){
        mbr.maxLat = l;
    }
    public void setMinLng(double l){
        mbr.minLng = l;
    }
    public void setMaxLng(double l){
        mbr.maxLng = l;
    }
}