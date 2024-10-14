package graph;

public class Arc
{
    Vertex start;
    Vertex end;
    boolean directed;
    double weight;
    public Arc(Vertex start, Vertex end)
    {
        this.start = start;
        this.end = end;
        this.directed = false;
        this.weight = 1;
        this.addArctoVertecis();
    }

    public Arc(Vertex start, Vertex end, double weight)
    {
        this.start = start;
        this.end = end;
        this.directed = false;
        this.weight = weight;
        this.addArctoVertecis();
    }
    public Arc(Vertex start, Vertex end, boolean directed)
    {
        this.start = start;
        this.end = end;
        this.directed = directed;
        this.weight = 1;
        this.addArctoVertecis();
    }
    public Arc(Vertex start, Vertex end, double weight, boolean directed)
    {
        this.start = start;
        this.end = end;
        this.directed = directed;
        this.weight = weight;
        this.addArctoVertecis();
    }

    private void addArctoVertecis()
    {
        this.start.addArc(this);
        this.end.addArc(this);
    }

    public Vertex connectedTo(Vertex who)
    {
        if (this.directed && who.equals(end))
        {
            return null;
        }
        else if (who.equals(end))
        {
            return start;
        }
        else if (who.equals(start))
        {
            return end;
        }
        else {return null;}
    }

    @Override
    public String toString() {
        if (this.directed){return this.start + "->" + this.end;}
        return this.start + "<->" + this.end;
    }
}
