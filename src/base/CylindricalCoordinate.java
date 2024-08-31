package base;

public class CylindricalCoordinate
{
    double radius;
    double height;
    double angle;

    public CylindricalCoordinate(double radius, double height, double angle)
    {
        this.radius = radius;
        this.height = height;
        this.angle = angle;
    }
    public Vector3D toVector()
    {
        return new Vector3D(Math.cos(this.angle) * radius, Math.sin(this.angle) * radius, this.height);
    }

    public SphericalCoordinate toSpherical()
    {
        double radius = Math.sqrt(Math.pow(this.radius, 2) + Math.pow(this.height, 2));
        double phi = Math.acos(this.height / radius);
        return new SphericalCoordinate(this.angle, phi, radius);
    }

}
