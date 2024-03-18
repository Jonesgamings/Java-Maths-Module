import java.util.Objects;

public class Plane
{
    Vector3D vector1;
    Vector3D vector2;
    Vector3D point;
    Vector3D normal;
    double scalar_product;

    public Plane(Vector3D vector1, Vector3D vector2, Vector3D point)
    {
        this.vector1 = vector1;
        this.vector2 = vector2;
        this.point = point;
        this.normal = vector1.cross(vector2);
        this.scalar_product = this.normal.dot(this.point);
    }

    @Override
    public String toString()
    {
        return normal.x + "x + " + normal.y + "y + " + normal.z + "z = " + this.scalar_product;
    }

    public double distance(Plane plane)
    {
        Line normal = new Line(this.normal, this.point);
        Vector3D intersection = plane.intersection(normal);
        return intersection.distance(this.point);
    }

    public double distance(Line line)
    {
        return this.distance(line.point);
    }

    public double distance(Vector3D point)
    {
        return (this.normal.dot(point) + this.scalar_product) / normal.magnitude();
    }

    public Line intersection(Plane plane)
    {
        if (this.normal.cross(plane.normal).equals(new Vector3D(0, 0, 0))) {return null;}
        Vector3D direction = this.normal.cross(plane.normal);
        Matrix simul_plane = new Matrix(2, 2).setMatrix(new double[][] {{this.normal.x, this.normal.y}, {plane.normal.x, plane.normal.y}});
        Matrix simul_point = new Matrix(2, 1).setMatrix(new double[][] {{this.scalar_product}, {plane.scalar_product}});
        Matrix point_Matrix = simul_plane.inverse().multiply(simul_point);
        Vector3D point = new Vector3D(point_Matrix.getAt(0, 0), point_Matrix.getAt(1, 0), 0);
        return new Line(direction, point);
    }

    public Vector3D intersection(Line line)
    {
        if (this.normal.dot(line.direction) != 0) {
            double t = (this.scalar_product - line.point.dot(this.normal)) / (this.normal.dot(line.direction));
            return line.position(t);
        }
        return null;
    }

    public static void main(String[] args)
    {
        Plane p1 = new Plane(new Vector3D(7, 4, 2), new Vector3D(1, 1, -9), new Vector3D(1, -2, -2));
        Plane p2 = new Plane(new Vector3D(7,7,7), new Vector3D(4, 4, -3), new Vector3D(1, -1, -1));
        System.out.println(p1.intersection(p2));
    }
}
