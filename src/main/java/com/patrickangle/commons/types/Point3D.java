/*
 * Patrick Angle Commons Library
 * Copyright 2018 Patrick Angle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.patrickangle.commons.types;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.patrickangle.commons.json.serialization.Point3DDeserializer;
import com.patrickangle.commons.json.serialization.Point3DSerializer;
import com.patrickangle.commons.util.Numbers;
import java.awt.geom.Point2D;
import java.awt.Point;

/**
 * A basic structure representing a point in 3D space. The directions and signs
 * of the axis's are left up to the discretion of the user of this class, with
 * the exception of some transform operations, which have documented the
 * assumptions that have been made.
 *
 * This class is designed such that is can be easily used to store 2D points as
 * well, and has two convenience accessors that return `java.awt.Point` and
 * `java.awt.geom.Point2D` objects from the Point3D for working with other
 * frameworks.
 *
 * A JSON serializer and deserializer are also provided in the
 * `com.patrickangle.commons.json.serialization` package as `.Point3DSerializer`
 * and `.Point3DSerializer`
 *
 * @author patrickangle
 */
@JsonSerialize(using = Point3DSerializer.class)
@JsonDeserialize(using = Point3DDeserializer.class)
public class Point3D {

    protected double x, y, z;

    /**
     * Create a new Point3D with the given coordinates.
     *
     * @param x
     * @param y
     * @param z
     */
    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Create a new Point3D at the same location as the given point.
     *
     * @param point
     */
    public Point3D(Point3D point) {
        this(point.getX(), point.getY(), point.getZ());
    }

    /**
     * Create a new Point3D with the given x and y coordinates, and a z
     * coordinate of 0.
     *
     * @param x
     * @param y
     */
    public Point3D(double x, double y) {
        this(x, y, 0);
    }

    /**
     * Create a new Point3D at the same x and y location as the given point, and
     * a z coordinate of 0.
     *
     * @param point
     */
    public Point3D(Point2D point) {
        this(point.getX(), point.getY());
    }

    /**
     * Create a new Point3D from a list of numeric parameters in string form,
     * separated by commas. The first three comma-separated values are used, and
     * a value of zero is substituted for malformed numbers or missing
     * parameters.
     *
     * @param paramters
     */
    public Point3D(String paramters) {
        // Split the text by commas, and remove the trailing and leading whitespace.
        String[] parts = paramters.replaceAll("^[,\\s]+", "").split("[,\\s]+");

        switch (parts.length) {
            case 0:
                this.x = 0;
                this.y = 0;
                this.z = 0;
                break;
            case 1:
                this.x = Numbers.doubleFromString(parts[0], 0);
                this.y = 0;
                this.z = 0;
                break;
            case 2:
                this.x = Numbers.doubleFromString(parts[0], 0);
                this.y = Numbers.doubleFromString(parts[1], 0);
                this.z = 0;
                break;
            case 3:
            default:
                this.x = Numbers.doubleFromString(parts[0], 0);
                this.y = Numbers.doubleFromString(parts[1], 0);
                this.z = Numbers.doubleFromString(parts[2], 0);
                break;
        }
    }

    /**
     * Create a new Point3D with an x, y, and z coordinate of 0.
     */
    public Point3D() {
        this(0, 0, 0);
    }

    /**
     * Get the x coordinate of this point.
     *
     * @return
     */
    public double getX() {
        return x;
    }

    /**
     * Get the y coordinate of this point.
     *
     * @return
     */
    public double getY() {
        return y;
    }

    /**
     * Get the z coordinate of this point.
     *
     * @return
     */
    public double getZ() {
        return z;
    }

    /**
     * Create a new Point from this Point3D, ignoring the Z value and rounding
     * the x and y to the nearest whole number.
     *
     * @return
     */
    public Point getIntegerPoint() {
        return new Point((int) Math.round(this.getX()), (int) Math.round(this.getY()));
    }

    /**
     * Create a new Point2D from this Point3D.
     *
     * @return
     */
    public Point2D getPoint2D() {
        return new Point2D.Double(this.getX(), this.getY());
    }

    /**
     * Create a new Point3D offset by the given x, y, and z distances.
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Point3D offsetBy(double x, double y, double z) {
        return new Point3D(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    /**
     * Create a new Point3D offset by the x, y, and z coordinates of the given
     * point.
     *
     * @param p
     * @return
     */
    public Point3D offsetBy(Point3D p) {
        return offsetBy(p.getX(), p.getY(), p.getZ());
    }

    /**
     * Create a new Point3D offset by the given x and y distances.
     *
     * @param x
     * @param y
     * @return
     */
    public Point3D offsetBy(double x, double y) {
        return offsetBy(x, y, 0);
    }

    /**
     * Create a new Point3D offset by the x and y coordinates of the given
     * point.
     *
     * @param p
     * @return
     */
    public Point3D offsetBy(Point2D p) {
        return offsetBy(p.getX(), p.getY());
    }

    /**
     * Create a new Point3D scaled by the given x, y, and z factors.
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Point3D scaleBy(double x, double y, double z) {
        return new Point3D(this.getX() * x, this.getY() * y, this.getZ() * z);
    }

    /**
     * Create a new Point3D scaled by the x, y, and z of the given point.
     *
     * @param p
     * @return
     */
    public Point3D scaleBy(Point3D p) {
        return scaleBy(p.getX(), p.getY(), p.getZ());
    }

    /**
     * Create a new Point3D scaled by the given x and y factors.
     *
     * @param x
     * @param y
     * @return
     */
    public Point3D scaleBy(double x, double y) {
        return scaleBy(x, y, 0);
    }
    
    /**
     * Create a new Point3D scaled by the given x and y factors.
     *
     * @param x
     * @param y
     * @return
     */
    public Point3D scaleBy(double f) {
        return scaleBy(f, f, f);
    }

    /**
     * Create a new Point3D scaled by x and y of the given point.
     *
     * @param p
     * @return
     */
    public Point3D scaleBy(Point2D p) {
        return scaleBy(p.getX(), p.getY());
    }

    /**
     * Create a new Point3D, rotated in all three axis, by the given degree
     * increments. Rotation is first applied to the z axis, then the y axis, and
     * finally the x axis. All angles are to be provided in degrees and will be
     * used to rotate the point clockwise on each axis.
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Point3D rotateBy(double x, double y, double z) {
        double xRot = Math.toRadians(x);
        double yRot = Math.toRadians(y);
        double zRot = Math.toRadians(z);

        double xPrime = (this.getX() * Math.cos(-zRot)) - (this.getY() * Math.sin(-zRot));
        double yPrime = (this.getX() * Math.sin(-zRot)) - (this.getY() * Math.cos(-zRot));

        double xDoublePrime = (xPrime * Math.cos(-yRot)) - (this.getZ() * Math.sin(-yRot));
        double zDoublePrime = (xPrime * Math.sin(-yRot)) - (this.getZ() * Math.cos(-yRot));

        double yTriplePrime = (yPrime * Math.cos(-xRot)) - (zDoublePrime * Math.sin(-xRot));
        double zTriplePrime = (yPrime * Math.sin(-xRot)) - (zDoublePrime * Math.cos(-xRot));

        return new Point3D(xDoublePrime, yTriplePrime, zTriplePrime);
    }

    /**
     * Create a new Point3D, rotated only on the z axis by the given degree
     * value. The angle should be provided in degrees and will be used to rotate
     * the point clockwise on the z axis. This is the equivalent of rotating a
     * Point2D by the given angle.
     *
     * @param z
     * @return
     */
    public Point3D rotateBy(double z) {
        return rotateBy(0, 0, z);
    }

    /**
     * Create a new Point3D, sheared by the given amount per axis.
     *
     * @param yx
     * @param zx
     * @param xy
     * @param zy
     * @param xz
     * @param yz
     * @return
     */
    public Point3D shearBy(double yx, double zx, double xy, double zy, double xz, double yz) {
        double xPrime = getX() + (yx * getY()) + (zx * getZ());
        double yPrime = getY() + (xy * getX()) + (zy * getZ());
        double zPrime = getZ() + (xz * getX()) + (yz * getY());

        return new Point3D(xPrime, yPrime, zPrime);
    }

    /**
     * Create a new Point3D, sheared by the given amount per axis.
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Point3D shearBy(double x, double y, double z) {
        return shearBy(x, x, y, y, z, z);
    }

    /**
     * Create a new Point3D, sheared by the coordinates of the given point.
     *
     * @param p
     * @return
     */
    public Point3D shearBy(Point3D p) {
        return shearBy(p.getX(), p.getY(), p.getZ());
    }

    /**
     * Create a new Point3D, sheared by the given amounts on the x and y axis,
     * leaving the z position intact.
     *
     * @param x
     * @param y
     * @return
     */
    public Point3D shearBy(double x, double y) {
        return shearBy(x, y, 0);
    }

    /**
     * Create a new Point3D, sheared by the coordinates of the given point,
     * leaving the z position intact.
     *
     * @param p
     * @return
     */
    public Point3D shearBy(Point2D p) {
        return shearBy(p.getX(), p.getY());
    }

    /**
     * Get the distance between this point and the provided coordinates.
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public double distanceTo(double x, double y, double z) {
        double zAxisDistance = distanceTo(x, y);
        double zDifference = this.getZ() - z;

        return Math.hypot(zAxisDistance, zDifference);
    }

    /**
     * Get the distance between this point and the provided point.
     *
     * @param p
     * @return
     */
    public double distanceTo(Point3D p) {
        return distanceTo(p.getX(), p.getY(), p.getZ());
    }

    /**
     * Get the distance between this point and the provided point and z
     * coordinate.
     *
     * @param p
     * @param z
     * @return
     */
    public double distanceTo(Point2D p, double z) {
        return distanceTo(p.getX(), p.getY(), z);
    }

    /**
     * Get the distance between this point and the provided coordinates,
     * ignoring the z axis. To compute the distance from this point to another
     * point in 3D space, use `distanceTo(double x, double y, double z)`,
     * `distanceTo(Point3D p)`, or distanceTo(Point2D p, double z)`.
     *
     * @param x
     * @param y
     * @return
     */
    public double distanceTo(double x, double y) {
        // Computes only the x/y distance
        return Math.hypot(this.getX() - x, this.getY() - y);
    }

    /**
     * Get the distance between this point and the provided point, ignoring the
     * z axis. To compute the distance from this point to another point in 3D
     * space, use `distanceTo(double x, double y, double z)`,
     * `distanceTo(Point3D p)`, or distanceTo(Point2D p, double z)`.
     *
     * @param p
     * @return
     */
    public double distanceTo(Point2D p) {
        return distanceTo(p.getX(), p.getY());
    }

    /**
     * Get the vector from this point to the given coordinates.
     *
     * @param x
     * @param y
     * @param z
     * @return
     */
    public Point3D vectorTo(double x, double y, double z) {
        double xVector = x - this.getX();
        double yVector = y - this.getY();
        double zVector = z - this.getZ();

        return new Point3D(xVector, yVector, zVector);
    }

    /**
     * Get the vector from this point to the given point.
     *
     * @param p
     * @return
     */
    public Point3D vectorTo(Point3D p) {
        return vectorTo(p.getX(), p.getY(), p.getZ());
    }

    /**
     * Get the vector from this point to the given coordinates, assuming a value
     * of 0 for the z axis.
     *
     * @param x
     * @param y
     * @return
     */
    public Point3D vectorTo(double x, double y) {
        return vectorTo(x, y, 0);
    }

    /**
     * Get the vector from this point to the given point, assuming a value of 0
     * for the z axis.
     *
     * @param p
     * @return
     */
    public Point3D vectorTo(Point2D p) {
        return vectorTo(p.getX(), p.getY());
    }

    /**
     * Get the angles, for each axis, of this point from the origin, in degrees.
     *
     * @return
     */
    public Point3D getAnglesFromOrigin() {
        double xAngle = Math.toDegrees(Math.atan2(this.getZ(), this.getY()));
        double yAngle = Math.toDegrees(Math.atan2(this.getX(), this.getZ()));
        double zAngle = Math.toDegrees(Math.atan2(this.getX(), this.getY()));

        return new Point3D(xAngle, yAngle, zAngle);
    }

    /**
     * Get the angle of this point from the origin on the z axis, in degrees.
     *
     * @return
     */
    public double getZAngleFromOrigin() {
        return Math.toDegrees(Math.atan2(this.getX(), this.getY()));
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.z) ^ (Double.doubleToLongBits(this.z) >>> 32));
        return hash;
    }

    /**
     * Check the equality of this point with another object. See
     * `Object.equals(Object obj)` for more details.
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point3D other = (Point3D) obj;
        if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x)) {
            return false;
        }
        if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y)) {
            return false;
        }
        if (Double.doubleToLongBits(this.z) != Double.doubleToLongBits(other.z)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return x + ", " + y + ((z != 0) ? ", " + z : "");
    }

}
