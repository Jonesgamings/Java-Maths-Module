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
}
