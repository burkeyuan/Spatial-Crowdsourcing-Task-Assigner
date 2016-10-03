package igeocrowd;

import java.util.*;


public class FlowNetwork {
    private final int V;
    private int E;
    private ArrayList<FlowEdge>[] adj;

    // empty graph with V vertices
    public FlowNetwork(int V) {
        this.V = V;
        this.E = 0;
        adj = new ArrayList[V];
        for (int v = 0; v < V; v++)
            adj[v] = new ArrayList();
    }

    public FlowNetwork(int V, Hashtable<Integer, ArrayList<Task>> table, ArrayList<Worker> workerList, ArrayList<Task> taskList, type assign_type, int indicator) {
    	this(V+2);
        double capacity = 1;
        for(int i = 0; i < table.size(); i++) {
            int maxTask = workerList.get(i).getMaxTaskNo(); 
            double workerLat = workerList.get(i).getLatitude();
            double workerLng = workerList.get(i).getLongitude();
        //for(int i = List.size()-1; i>=0; i--) {
            ArrayList<Task> tasks  = table.get(i);
            if(tasks != null){
                for(int j = 0; j < tasks.size(); j++) {
               // for(int j = regions.size()-1; j >=0; j--) {
                    Task task = taskList.get(j); 
                    double taskLat = task.getLat();
                    double taskLng = task.getLng();
                    double accuracy, cost;
                    double dist = Math.sqrt(((workerLat - taskLat)*(workerLat - taskLat))+((workerLng - taskLng)*(workerLng - taskLng))); 
                    if(task.getType().charAt(0) == 'A') {
                    	accuracy = workerList.get(i).getacc().getDaccA();
                    } else if(task.getType().charAt(0) == 'B') {
                    	accuracy = workerList.get(i).getacc().getDaccB();
                    } else if(task.getType().charAt(0) == 'C') {
                    	accuracy = workerList.get(i).getacc().getDaccC();
                    } else {
                    	accuracy = workerList.get(i).getacc().getDaccD();
                    }
                    if(accuracy == 0.00) {
                    	cost = 10000;
                    } else {
                    	cost = dist / accuracy;
                    }
                    if(indicator == 0) {
	                    if(assign_type == type.HSP)
	                        addEdge(new FlowEdge(i, table.size()+j, capacity, dist, dist, accuracy));
                    }else{
                    	if(assign_type == type.HSP)
	                        addEdge(new FlowEdge(i, table.size()+j, capacity, cost, dist, accuracy));
                    }
                    /*if(assign_type == type.ENTROPY)
                        addEdge(new FlowEdge(i, table.size()+j, capacity, task.getEntropy(),dist, accuracy));
                    else if(assign_type == type.HSP)
                        addEdge(new FlowEdge(i, table.size()+j, capacity, dist, dist, accuracy));
                    else
                        addEdge(new FlowEdge(i, table.size()+j, capacity, task.getEntryTime(),dist, accuracy));*/
                }
            }
            addEdge(new FlowEdge(V, i, maxTask, 0, 0, 0));  //this is for adding edges from source to all points
        }
        for(int i =  0; i < taskList.size(); i++) {
              addEdge(new FlowEdge(table.size()+i, V+1, capacity, 0, 0, 0));
         }
    }
    
    
    // graph, read from input stream

    // number of vertices and edges
    public int V() { return V; }
    public int E() { return E; }

    // add edge e in both v's and w's adjacency lists
    public void addEdge(FlowEdge e) {
        E++;
        int v = e.from();
        int w = e.to();
        adj[v].add(e);
        adj[w].add(e);
    }

    // return list of edges incident to  v
    public ArrayList<FlowEdge> adj(int v) {
        return adj[v];
    }
   

    // return list of all edges
    public Iterable<FlowEdge> edges() {
        ArrayList<FlowEdge> list = new ArrayList<FlowEdge>();
        for (int v = 0; v < V; v++)
            for (FlowEdge e : adj(v))
                list.add(e);
        return list;
    }


    // string representation of Graph - takes quadratic time
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < (V-2)/2; v++) {
            s.append(v + ":  ");
            for (FlowEdge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
    
    public String test() {
    	return "test";
    }
}

