package igeocrowd;

import java.util.ArrayList;

/**
 *
 * @author TongGou
 */
public class Cell {
    //private ArrayList<Task> taskList;
    private ArrayList<Integer> taskList;
    private int density;
   
    public Cell(){
        taskList = new ArrayList<Integer>();
    }
    public Cell(int d){
        taskList = new ArrayList<Integer>();
        density = d;
    }
    public ArrayList<Integer> getTaskList(){
        return taskList;
    }
    public void addTask(Integer t){
        taskList.add(t);
    }
    public void setDensity(int d){
        density = d ;
    }
    public int getDensity(){
        return density;
    }
}
 