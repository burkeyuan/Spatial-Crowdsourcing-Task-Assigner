package igeocrowd;

public class FlowEdge {
    private final int v;             // from
    private final int w;             // to
    private final double capacity;   // capacity
    private double acc;
    public double distance;  //the spatial distance between the two worker and task
    private double flow;             // flow
    private double cost;     //this is the entry time for a given task

    public FlowEdge(int v, int w, double capacity, double c, double dist, double acc) {
        this.v         = v;
        this.w         = w;
        this.capacity  = capacity;
        this.flow      = 0;
        this.cost = c;
        this.distance = dist;
        this.acc = acc;
    }

    // accessor methods
    public int from()         { return v;        }
    public int to()           { return w;        }
    public double capacity()  { return capacity; }
    public double flow()      { return flow;     }
    public double getCost()	  {   return cost;   }
    public double getAcc()    {   return acc;    }

    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new RuntimeException("Illegal endpoint");
    }

    public double residualCapacityTo(int vertex) {
        if      (vertex == v) return flow;
        else if (vertex == w) return capacity - flow;
        else throw new RuntimeException("Illegal endpoint");
    }

    public void addResidualFlowTo(int vertex, double delta) {
        if      (vertex == v) flow -= delta;
        else if (vertex == w) flow += delta;
        else throw new RuntimeException("Illegal endpoint");
    }


    public String toString() {
        if (v==196)
            return  "s ->" + w + " " + flow + "/" + capacity;
        else if (w==197)
            return v + "-> t " + flow + "/" + capacity;
        else
            return v + "->" + (w-98) + " " + flow + "/" + capacity;
    }
}

