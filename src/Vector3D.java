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
        x = this.y * vector.z - this.z * vector.y;
        y = this.z * vector.x - this.x * vector.z;
        z = this.x * vector.y - this.y * vector.x;
        return new Vector3D(x, y, z);
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

    public Matrix toMatrix()
    {
        return new Matrix(3, 1).setMatrix(new double[][] {{this.x}, {this.y}, {this.z}});
    }
}
