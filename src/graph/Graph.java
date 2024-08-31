package graph;

import base.Matrix;

import java.util.ArrayList;
import java.util.Objects;

public class Graph
{
    ArrayList<Vertex> vertices;
    ArrayList<Arc> arcs;
    int numberVertices;

    static String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public Graph()
    {
        this.vertices = new ArrayList<Vertex>();
        this.arcs = new ArrayList<Arc>();
        this.numberVertices = 0;
    }

    public int addVertex(Vertex vertex)
    {
        this.vertices.add(vertex);
        this.numberVertices += 1;
        return numberVertices-1;
    }

    public void addArc(Arc arc)
    {
        this.arcs.add(arc);
    }

    public base.Matrix adjacencyMatrix()
    {
        Matrix adjacencyMatrix = Matrix.zeroesMatrix(this.vertices.size());
        for (Arc arc: this.arcs)
        {
            adjacencyMatrix.setAt(arc.start.ID, arc.end.ID, arc.weight);
        }
        return adjacencyMatrix;
    }

    public static Graph K(int size)
    {
        Graph newGraph = new Graph();
        for (int i =0; i < size; i++)
        {
            new Vertex(newGraph);
        }
        for (int i =0; i < size; i++)
        {
            for (int j =0; j < size; j++)
            {
                if (i != j)
                {
                    newGraph.addArc(new Arc(newGraph.vertices.get(i), newGraph.vertices.get(j)));
                }
            }
        }
        return newGraph;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Graph graph = (Graph) o;
        return numberVertices == graph.numberVertices && Objects.equals(vertices, graph.vertices) && Objects.equals(arcs, graph.arcs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, arcs, numberVertices);
    }

    public static void main(String[] args) {
        Graph graph = Graph.K(5);
        System.out.println(graph.adjacencyMatrix());
    }
}
