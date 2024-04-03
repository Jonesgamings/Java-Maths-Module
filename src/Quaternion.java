public class Quaternion
{
    double a;
    double i;
    double j;
    double k;

    public Quaternion(double a, double i, double j, double k)
    {
        this.a = a;
        this.i = i;
        this.j = j;
        this.k = k;
    }

    public Quaternion(double a, Vector3D ijkVector)
    {
        this.a = a;
        this.i = ijkVector.x;
        this.j = ijkVector.y;
        this.k = ijkVector.z;
    }

    // yaw - rotation about z-axis
    // pitch - rotation about y-axis
    // roll - rotation about x-axis
    public Quaternion(double roll, double pitch, double yaw) {
        this.a = Math.sin(roll / 2) * Math.cos(pitch / 2) * Math.cos(yaw / 2) - Math.cos(roll / 2) * Math.sin(pitch / 2) * Math.sin(yaw / 2);
        this.i = Math.cos(roll / 2) * Math.sin(pitch / 2) * Math.cos(yaw / 2) + Math.sin(roll / 2) * Math.cos(pitch / 2) * Math.sin(yaw / 2);
        this.j = Math.cos(roll / 2) * Math.cos(pitch / 2) * Math.sin(yaw / 2) - Math.sin(roll / 2) * Math.sin(pitch / 2) * Math.cos(yaw / 2);
        this.k = Math.cos(roll / 2) * Math.cos(pitch / 2) * Math.cos(yaw / 2) + Math.sin(roll / 2) * Math.sin(pitch / 2) * Math.sin(yaw / 2);
    }

    @Override
    public String toString() {
        return this.a + " + " + this.i + "i + " + this.j + "j + " + this.k + "k";
    }

    public double magnitude()
    {
        return Math.sqrt(a*a + i*i + j*j + k*k);
    }

    public Quaternion conjugate()
    {
        return new Quaternion(a, -i, -j, -k);
    }

    public Quaternion inverse()
    {
        return this.conjugate().divide(a*a + i*i + j*j + k*k);
    }

    public Quaternion add(Quaternion quaternion)
    {
        return new Quaternion(a + quaternion.a, i + quaternion.i, j + quaternion.j, k + quaternion.k);
    }

    public Quaternion normalise()
    {
        return this.divide(this.magnitude());
    }

    public Quaternion subtract(Quaternion quaternion)
    {
        return new Quaternion(a - quaternion.a, i - quaternion.i, j - quaternion.j, k - quaternion.k);
    }

    /*
    public Quaternion multiply(Quaternion quaternion)
    {
        double newA = a * quaternion.a - i* quaternion.i - j* quaternion.j - k* quaternion.k;
        double newI = a*quaternion.i + i*quaternion.a + j*quaternion.k - k*quaternion.j;
        double newJ = a*quaternion.j - i*quaternion.k + j*quaternion.a + k*quaternion.i;
        double newK = a*quaternion.k + i*quaternion.j - j*quaternion.i + k*quaternion.a;
        return new  Quaternion(newA, newI, newJ, newK);
    }
    */

    public Quaternion multiply(Quaternion quaternion)
    {
        Vector3D v1 = new Vector3D(i, j, k);
        Vector3D v2 = new Vector3D(quaternion.i, quaternion.j, quaternion.k);
        double newA = a * quaternion.a - v1.dot(v2);
        Vector3D newIJK = (v2.multiply(a).add(v1.multiply(quaternion.a))).add(v1.cross(v2));
        return new Quaternion(newA, newIJK);
    }

    public Vector3D imaginary()
    {
        return new Vector3D(this.i , this.j, this.k);
    }

    public double real()
    {
        return this.a;
    }

    public Vector3D rotate(Vector3D vector)
    {
        Quaternion qVector = vector.toQuaternion();
        return (this.multiply(qVector)).multiply(this.conjugate()).imaginary();
    }

    public Quaternion rotate(Quaternion quaternion)
    {
        return (this.multiply(quaternion)).multiply(this.conjugate());
    }

    public Quaternion divide(Quaternion quaternion)
    {
        return this.multiply(quaternion.inverse());
    }

    public Quaternion multiply(double factor)
    {
        return new Quaternion(a * factor, i * factor, j * factor, k * factor);
    }

    public Quaternion divide(double factor)
    {
        return new Quaternion(a / factor, i / factor, j / factor, k / factor);
    }

    public Matrix toMatrix()
    {
        Matrix matrix = new Matrix(3, 3);
        matrix.setMatrix(new double[][]
                        {
                                {2*(a*a + i*i) -1, 2*(i*j - a*k), 2*(i*k + a*j)},
                                {2*(i*j + a*k), 2*(a*a + j*j) -1, 2*(j*k - a*i)},
                                {2*(i*k - a*j), 2*(j*k + a*i), 2*(a*a + k*k) -1}
                        });
        return matrix;
    }

    // yaw - rotation about z-axis
    // pitch - rotation about y-axis
    // roll - rotation about x-axis
    public Vector3D toEulerAngles()
    {
        double t0 = 2.0 * (a * i + j * k);
        double t1 = +1.0 - 2.0 * (a * a + j *j);

        double t2 = +2.0 * (a * j - k * i);
        t2 = Math.clamp(t2, -1, 1);

        double t3 = +2.0 * (a * k + i * j);
        double t4 = +1.0 - 2.0 * (j * j + k * k);

        return new Vector3D(Math.atan2(t0, t1), Math.asin(t2), Math.atan2(t3, t4)); //roll, pitch yaw
    }

    public static Quaternion rotation(Vector3D n, double angle)
    {
        double a = Math.cos(angle/2);
        Vector3D ijkVector = n.normalise().multiply(Math.sin(angle/2));
        return new Quaternion(a, ijkVector);
    }
}
