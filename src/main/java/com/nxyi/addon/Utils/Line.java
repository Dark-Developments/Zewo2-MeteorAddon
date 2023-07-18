package com.nxyi.addon.Utils;

import net.minecraft.util.math.Vec3d;

public class Line {
    public double k,b;
    public Line setKx(double k, double b)
    {
        this.k = k;
        this.b = b;
        return this;
    }
    public double getY(double x){
        return k*x+b;
    }
    public Line kxPlusBFromCoords(double x, double y, double degree){
        degree+=90;
        k=getKFromDegree(degree);
        var zeroB = new Line().setKx(k,0);
        var zeroBY = zeroB.getY(x);

        this.setKx(k,y-zeroBY);
        return this;
    }
    static double getKFromDegree(double degree){
        return Math.tan(degree*3.14159/180);
    }
    static double findIntersectionX(Line a, Line b){
        var k1=a.k;
        var k2=b.k;
        var b1=a.b;
        var b2=b.b;
        return (b2-b1)/(k1-k2);
    }
    static double findIntersectionY(Line a, Line b){
        return a.getY(findIntersectionX(a,b));
    }
    public static Vec3d getIntersection(double x1,double y1,double deg1,double x2,double y2,double deg2){
        var v1 = new Line().kxPlusBFromCoords(x1,y1,deg1);
        var v2 = new Line().kxPlusBFromCoords(x2,y2,deg2);
        return new Vec3d(Line.findIntersectionX(v1,v2),0, Line.findIntersectionY(v1,v2));
    }
}
