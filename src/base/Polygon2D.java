package base;

import java.util.ArrayList;
import java.util.Arrays;

public class Polygon2D
{
    Vector2D[] points;
    int sides;
    static int tolerance = 10;

    public Polygon2D(Vector2D[] points)
    {
        this.points = points;
        this.sides = points.length;
    }

    public Polygon2D(int sides, double radius)
    {
        this.sides = sides;
        double angleBetweenPoints = 2 * Math.PI / sides;
        Vector2D[] points = new Vector2D[sides];
        for (int i = 0; i < sides; i++)
        {
            double angle = angleBetweenPoints * i;
            points[i] = new Vector2D(radius * Math.cos(angle), radius * Math.sin(angle));
        }
        this.points = points;
    }

    public Polygon2D(int sides)
    {
        this.sides = sides;
        double angleBetweenPoints = 2 * Math.PI / sides;
        Vector2D[] points = new Vector2D[sides];
        for (int i = 0; i < sides; i++)
        {
            double angle = angleBetweenPoints * i;
            points[i] = new Vector2D(Math.cos(angle), Math.sin(angle));
        }
        this.points = points;
    }

    public static double roundTo(double val, int dp)
    {
        double scale = Math.pow(10, dp);
        return Math.round(val * scale) / scale;
    }

    public boolean isRegular()
    {
        double[] sideLengths = sideLengths();
        double[] angles = interiorAngles();
        if (sideLengths[0] * Math.pow(10, tolerance) >= Double.MAX_VALUE) {return false;}
        double sameS = roundTo(sideLengths[0], tolerance);
        double sameA = roundTo(angles[0], tolerance);
        for (int i = 1; i < this.sides; i++) {
            if (roundTo(angles[i], tolerance) != sameA) {return false;}
            if (roundTo(sideLengths[i], tolerance) != sameS) {return false;}
        }
        return true;
    }

    public double perimeter()
    {
        double[] sideLengths = this.sideLengths();
        double perimeter = 0;
        for (double side: sideLengths)
        {
            perimeter += side;
        }
        return perimeter;
    }

    public double area()
    {
        if (this.sides == 3)
        {
            double[] sideLengths = this.sideLengths();
            double s = (sideLengths[0] + sideLengths[1] + sideLengths[2])/2;
            return Math.sqrt(s * (s-sideLengths[0]) * (s-sideLengths[1]) * (s-sideLengths[2]));
        }
        if (this.isRegular())
        {
            double sideLength = points[0].distance(points[1]);
            return this.sides * Math.pow(sideLength, 2) / (4*Math.tan(Math.PI/this.sides));
        }
        else
        {
            double area = 0;
            for (Polygon2D shape: this.subDivide())
            {
                area += shape.area();
            }
            return area;
        }
    }

    public double[] sideLengths()
    {
        double[] sideLengths = new double[this.sides];
        for (int i = 0; i < this.sides; i++) {
            int nextI = i+1;
            if (nextI == this.sides) {nextI = 0;}
            sideLengths[i] = this.points[i].distance(this.points[nextI]);
        }
        return sideLengths;
    }

    public boolean[] convexVertices()
    {
        boolean[] vertices = new boolean[this.sides];
        for (int i = 0; i < this.sides; i++) {
            Vector2D v1;
            Vector2D v3;
            if (i==0)
            {
                v1 = this.points[this.sides-1];
                v3 = this.points[1];
            }
            else if (i == this.sides - 1)
            {
                v1 = this.points[this.sides-2];
                v3 = this.points[0];
            }
            else
            {
                v1 = this.points[i-1];
                v3 = this.points[i+1];
            }
            Vector2D v2 = this.points[i];
            vertices[i] = (v2.x - v1.x)*(v3.y - v1.y) - (v2.y - v1.y)*(v3.x - v1.x) > 0;
        }
        return vertices;
    }

    public double[] interiorAngles()
    {
        double[] angles = new double[this.sides];
        boolean[] vertices = this.convexVertices();
        for (int i = 0; i < this.sides; i++) {
            Vector2D v1;
            Vector2D v3;
            if (i==0)
            {
                v1 = this.points[this.sides-1];
                v3 = this.points[1];
            }
            else if (i == this.sides - 1)
            {
                v1 = this.points[this.sides-2];
                v3 = this.points[0];
            }
            else
            {
                v1 = this.points[i-1];
                v3 = this.points[i+1];
            }
            Vector2D v2 = this.points[i];
            Vector2D a = v2.subtract(v1);
            Vector2D b = v2.subtract(v3);
            if (vertices[i]) {
                angles[i] = a.angle(b);
            }
            else
            {
                angles[i] = 2 * Math.PI - a.angle(b);
            }
        }
        return angles;
    }

    public Vector2D point(int index)
    {
        return this.points[index];
    }

    public static Polygon2D triangleAbout(Polygon2D shape, int i)
    {
        int left = i-1;
        int right = i+1;
        if (left == -1)
        {
            left = shape.sides-1;
        }
        if (right == shape.sides)
        {
            right = 0;
        }
        return new Polygon2D(new Vector2D[] {shape.point(left), shape.point(i), shape.point(right)});
    }

    private static void __addToArray__(ArrayList<Integer> array, int index, int value)
    {
        if (index > array.size())
        {
            index = array.size();
        }
        array.add(index, value);
    }

    public ArrayList<Polygon2D> subDivide()
    {
        if (this.sides == 3)
        {
            ArrayList<Polygon2D> shapes =  new ArrayList<Polygon2D>();
            shapes.add(Polygon2D.triangleAbout(this, 1));
            return shapes;
        }
        double[] angles = this.interiorAngles();
        ArrayList<Integer> earTips = new ArrayList<>();
        for (int i = 0; i < this.sides; i++) {
            double angle = angles[i];
            if (angle <= Math.PI && angle >= -Math.PI)
            {
                Polygon2D.__addToArray__(earTips, (int) (angle * 360), i);
            }
        }
        int index = earTips.getFirst();
        Vector2D[] newPoints = new Vector2D[this.sides-1];
        for (int i = 0, k = 0; i < this.sides; i++) {
            if (i == index) {continue;}
            newPoints[k] = this.points[i];
            k+=1;
        }
        Polygon2D newShape = new Polygon2D(newPoints);
        ArrayList<Polygon2D> tris = newShape.subDivide();
        tris.add(Polygon2D.triangleAbout(this, index));
        return tris;
    }

    public double apothem()
    {
        if (this.isRegular())
        {
            return 2 * this.area() / this.perimeter();
        }
        else
        {
            double area = this.area();
            double perimeter = Math.sqrt(4 * this.sides * area * Math.tan(Math.PI / this.sides));
            return 2 * area / perimeter;
        }
    }

    public boolean isConvex()
    {
        for (boolean vertex: this.convexVertices())
        {
            if (!vertex) {return false;}
        }
        return true;
    }

    @Override
    public String toString() {
        return "(Sides: " + this.sides + ", Regular: " + this.isRegular() + ")";
    }

    public Polygon2D regularise()
    {
        double area = this.area();
        double perimeter = Math.sqrt(4 * this.sides * area * Math.tan(Math.PI / this.sides));
        double apothem = 2 * area / perimeter;
        double sideLength = 2 * apothem * Math.tan(Math.PI/this.sides);
        double radius = Math.sqrt(Math.pow(sideLength/2, 2) + Math.pow(apothem, 2));
        return new Polygon2D(this.sides, radius);
    }

    public static void main(String[] args) {
        Polygon2D shape = new Polygon2D(new Vector2D[]{new Vector2D(-50, -1), new Vector2D(0, 0), new Vector2D(1, -1), new Vector2D(1, 2), new Vector2D(-1, 2)});
        System.out.println(shape.isConvex());
        System.out.println(Arrays.toString(shape.convexVertices()));
        System.out.println(shape.area());
    }
}
