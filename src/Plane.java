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

    public static void main(String[] args)
    {
        Plane p = new Plane(new Vector3D(0, 1, 2), new Vector3D(3, 4, 5), new Vector3D(0, 2, 0));
        System.out.println(p);
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
}
