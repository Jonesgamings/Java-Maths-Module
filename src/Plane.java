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

    public Plane(Vector3D normal, Vector3D point)
    {
        this.point = point;
        this.normal = normal;
        this.scalar_product = this.normal.dot(this.point);
    }

    public Plane(Vector3D normal, double scalar_product)
    {
        this.normal = normal;
        this.scalar_product = scalar_product;
        this.point = new Vector3D(0, 0, scalar_product/normal.z);
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
        Matrix simul_point = new Matrix(2, 1).setMatrix(new double[][] {{this.scalar_product}, {plane.scalar_product}});
        Matrix simul_plane_xy = new Matrix(2, 2).setMatrix(new double[][] {{this.normal.x, this.normal.y}, {plane.normal.x, plane.normal.y}});
        Matrix simul_plane_xz = new Matrix(2, 2).setMatrix(new double[][] {{this.normal.x, this.normal.z}, {plane.normal.x, plane.normal.z}});
        Matrix simul_plane_yz = new Matrix(2, 2).setMatrix(new double[][] {{this.normal.y, this.normal.z}, {plane.normal.y, plane.normal.z}});
        if (simul_plane_xy.determinate() != 0) {
            Matrix point_Matrix = simul_plane_xy.inverse().multiply(simul_point);
            Vector3D point = new Vector3D(point_Matrix.getAt(0, 0), point_Matrix.getAt(1, 0), 0);
            return new Line(direction, point);
        }
        if (simul_plane_xz.determinate() != 0)
        {
            Matrix point_Matrix = simul_plane_xz.inverse().multiply(simul_point);
            Vector3D point = new Vector3D(point_Matrix.getAt(0, 0), 0, point_Matrix.getAt(1, 0));
            return new Line(direction, point);
        }
        if (simul_plane_yz.determinate() != 0)
        {
            Matrix point_Matrix = simul_plane_yz.inverse().multiply(simul_point);
            Vector3D point = new Vector3D(0, point_Matrix.getAt(0, 0), point_Matrix.getAt(1, 0));
            return new Line(direction, point);
        }
        return null;
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
        Plane p1 = new Plane(new Vector3D(7 ,7, 7.7), 13);
        Plane p2 = new Plane(new Vector3D(7,7,7.7),29);
        System.out.println(p1.intersection(p2));
        System.out.println(p1);
        System.out.println(p2);
    }
}
