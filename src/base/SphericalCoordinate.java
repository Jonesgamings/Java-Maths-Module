package base;

public class SphericalCoordinate
{
    double theta;
    double phi;
    double radius;

    public SphericalCoordinate(double theta, double phi, double radius)
    {
        this.theta = theta;
        this.phi = phi;
        this.radius = radius;
    }
    public Vector3D toVector()
    {
        double x = this.radius * Math.sin(phi) * Math.cos(theta);
        double y = this.radius * Math.sin(phi) * Math.sin(theta);
        double z = this.radius * Math.cos(phi);
        return new Vector3D(x, y, z);
    }

    public CylindricalCoordinate toCylindrical()
    {
        double radius = this.radius * Math.sin(this.theta);
        double height = this.radius * Math.cos(this.phi);
        return new CylindricalCoordinate(radius, height, this.theta);
    }
}
