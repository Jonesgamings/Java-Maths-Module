package graph;

import base.Matrix;

import java.util.ArrayList;

public class Vertex
{
    Graph graph;
    int ID;
    ArrayList<Arc> arcs;

    public Vertex(Graph graph)
    {
        this.graph = graph;
        this.ID = graph.addVertex(this);
        this.arcs = new ArrayList<Arc>();
    }

    public void addArc(Arc arc)
    {
        this.arcs.add(arc);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {return false;}
        if (obj.getClass() != this.getClass()) {return false;}
        Vertex objVertex = (Vertex) obj;
        if (!objVertex.graph.equals(this.graph)) {return false;}
        return (this.ID == objVertex.ID);
    }
}
