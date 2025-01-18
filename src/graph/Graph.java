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
            adjacencyMatrix.setAt(arc.start.ID, arc.end.ID, adjacencyMatrix.getAt(arc.start.ID  , arc.end.ID) + 1);
            if (!arc.directed)
            {
                adjacencyMatrix.setAt(arc.end.ID, arc.start.ID, adjacencyMatrix.getAt(arc.end.ID  , arc.start.ID) + 1);
            }
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
            for (int j =0; j <= i; j++)
            {
                if (i != j)
                {
                    newGraph.addArc(new Arc(newGraph.vertices.get(i), newGraph.vertices.get(j)));
                }
            }
        }
        return newGraph;
    }

    public boolean isEulerian()
    {
        boolean Eulerian = true;
        for (int i = 0; i < this.numberVertices; i++) {
            if (this.vertices.get(i).vertexDegree() % 2 != 0)
            {
                Eulerian = false;
            }
        }
        return Eulerian;
    }

    public boolean isSemiEulerian()
    {
        int oddVertices = 0;
        for (int i = 0; i < this.numberVertices; i++) {
            if (this.vertices.get(i).vertexDegree() % 2 != 0)
            {
                oddVertices += 1;
            }
        }
        if (oddVertices == 2)
        {
            return true;
        }
        return false;
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
        System.out.println(graph.vertices.get(0).arcs);
        System.out.println(graph.adjacencyMatrix());
    }
}
