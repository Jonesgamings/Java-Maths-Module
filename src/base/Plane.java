package base;

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

    public double angle(Line line)
    {
        return Math.PI/2 - Math.acos(Math.abs(this.normal.dot(line.direction))/(this.normal.magnitude() * line.direction.magnitude()));
    }

    public double angle(Plane plane)
    {
        return Math.acos((this.normal.dot(plane.normal)) / (this.normal.magnitude() * plane.normal.magnitude()));
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

    public Vector3D reflect(Vector3D point)
    {
        return point.add(this.normal.multiply((2 * (this.scalar_product - this.normal.dot(point)))/(this.normal.dot(this.normal))));
    }

    public Line reflect(Line line)
    {
        Vector3D p1 = this.reflect(line.position(-10));
        Vector3D p2 = this.reflect(line.position(10));
        Vector3D direction = p1.subtract(p2);
        return new Line(direction, p1);
    }

    public Plane reflect(Plane plane)
    {
        Line l1 = this.reflect(new Line(plane.normal, plane.point));
        return new Plane(l1.direction, l1.point);
    }

    public static void main(String[] args)
    {
        Plane p1 = new Plane(new Vector3D(3 ,1, 2), 13);
        Plane p2 = new Plane(new Vector3D(4, -2, 5), Vector3D.zeroes());
        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p1.reflect(p2));
    }
}
