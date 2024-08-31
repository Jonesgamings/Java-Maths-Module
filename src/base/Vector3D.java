package base;

public class Vector3D
{
    double x;
    double y;
    double z;

    public Vector3D(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3D midpoint(Vector3D point)
    {
        double newX = (this.x + point.x)/2;
        double newY = (this.y + point.y)/2;
        double newZ = (this.z + point.z)/2;
        return new Vector3D(newX, newY, newZ);
    }

    public CylindricalCoordinate toCylindrical()
    {
        double angle = Math.atan2(this.y, this.x);
        double radius = Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));
        return new CylindricalCoordinate(radius, this.z, angle);
    }

    public SphericalCoordinate toSpherical()
    {
        double radius = this.magnitude();
        double theta = Math.atan2(this.y, this.x);
        double phi = Math.acos(this.z / radius);
        return new SphericalCoordinate(theta, phi, radius);
    }

    @Override
    public String toString()
    {
        return this.x + ", " + this.y + ", " + this.z;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) {return false;}

        if (obj.getClass() != this.getClass()) {return false;}

        final Vector3D other = (Vector3D) obj;
        if (this.x != other.x) { return false;}
        if (this.y != other.y) { return false;}
        return (this.z == other.z);
    }

    public Vector3D normalise()
    {
        double magnitude = this.magnitude();
        return new Vector3D(this.x / magnitude, this.y / magnitude, this.z / magnitude);
    }

    public double distance(Vector3D vector)
    {
        return Math.pow(Math.pow(vector.x - this.x, 2) + Math.pow(vector.y - this.y, 2) + Math.pow(vector.z - this.z, 2), 0.5);
    }

    public double magnitude()
    {
        return Math.pow(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2), 0.5);
    }

    public double angle(Vector3D vector)
    {
        return Math.acos(dot(vector) / (vector.magnitude() * magnitude()));
    }

    public Vector3D add(Vector3D vector)
    {
        return new Vector3D(this.x + vector.x, this.y + vector.y, this.z + vector.z);
    }

    public Vector3D subtract(Vector3D vector)
    {
        return new Vector3D(this.x - vector.x, this.y - vector.y, this.z - vector.z);
    }

    public Vector3D cross(Vector3D vector)
    {
        double newX = this.y * vector.z - this.z * vector.y;
        double newY = this.z * vector.x - this.x * vector.z;
        double newZ = this.x * vector.y - this.y * vector.x;
        return new Vector3D(newX, newY, newZ);
    }

    public Vector3D project(Vector3D a)
    {
        return this.multiply((this.dot(a) / this.dot(this)));
    }

    public Vector3D reject(Vector3D a)
    {
        return a.subtract(this.project(a));
    }

    public Vector3D hadamardProduct(Vector3D v)
    {
        return new Vector3D(x * v.x, y * v.y, z * v.z);
    }

    public static Vector3D projection(Vector3D a, Vector3D b)
    {
        return b.multiply((b.dot(a) / b.dot(b)));
    }

    public static Vector3D rejection(Vector3D a, Vector3D b)
    {
        return a.subtract(Vector3D.projection(a, b));
    }

    public static double scalarTripleProduct(Vector3D v1, Vector3D v2, Vector3D v3)
    {
        return v1.dot(v2.cross(v3));
    }

    public static Vector3D vectorTripleProduct(Vector3D v1, Vector3D v2, Vector3D v3)
    {
        return v1.cross(v2.cross(v3));
    }

    public static Vector3D zeroes()
    {
        return new Vector3D(0, 0, 0);
    }

    public double dot(Vector3D vector)
    {
        return this.x * vector.x + this.y * vector.y + this.z * vector.z;
    }

    public Vector3D multiply(double factor)
    {
        return new Vector3D(this.x*factor, this.y*factor, this.z*factor);
    }

    public Vector3D divide(double factor)
    {
        return new Vector3D(this.x/factor, this.y/factor, this.z/factor);
    }

    public Vector3D elementDivision(Vector3D vector)
    {
        return new Vector3D(this.x/vector.x, this.y/vector.y, this.z/vector.z);
    }

    public VectorND toVectorND()
    {
        return new VectorND(new double[] {this.x, this.y, this.z});
    }

    public Matrix toMatrix()
    {
        return new Matrix(3, 1).setMatrix(new double[][] {{this.x}, {this.y}, {this.z}});
    }

    public Quaternion toQuaternion()
    {
        return new Quaternion(0, this);
    }

    public Vector3D inverse()
    {
        return this.divide(Math.pow(this.magnitude(),2));
    }

    public Matrix outerProduct(Vector3D v)
    {
        return this.toMatrix().multiply(v.toMatrix().transpose());
    }

    public Matrix tensorProduct(Vector3D v)
    {
        return this.outerProduct(v);
    }

    public static Vector3D xAxis()
    {
        return new Vector3D(1, 0, 0);
    }

    public static Vector3D YAxis()
    {
        return new Vector3D(0, 1, 0);
    }

    public static Vector3D ZAxis()
    {
        return new Vector3D(0, 0, 1);
    }

    public static void main(String[] args) {
        Vector3D v = new Vector3D(1, 2,3);
        Vector3D v2 = new Vector3D(2,1,2);
        System.out.println(v.project(v2));
    }
}
