package com.dani.game2.Levels;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.utils.Array;
import com.dani.game2.tools.PhysicsCalculator;


/**
 * Created by root on 04.08.16.
 */
public final class ShapeFactory {
    private static Array<Vector2> oldPoints = new Array<Vector2>();
    public static Vector2 startPoint;

    private ShapeFactory() {
        oldPoints = new Array<Vector2>();
    }

    public static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        return getRectangle(rectangleObject.getRectangle());
    }

    public static PolygonShape getRectangle(Rectangle rectangle) {
        PolygonShape polygon = new PolygonShape();



        Vector2 size = new Vector2(
                PhysicsCalculator.toUnits(rectangle.x + rectangle.width * 0.5f),
                PhysicsCalculator.toUnits(rectangle.y + rectangle.height * 0.5f)
        );

        polygon.setAsBox(
                PhysicsCalculator.toUnits(rectangle.width * 0.5f),
                PhysicsCalculator.toUnits(rectangle.height * 0.5f),
                size,
                0.0f
        );


        return polygon;
    }

    public static CircleShape makeCircle(EllipseMapObject object){
        ////System.out.print("making circle Shape \n");
        Ellipse ellipse = object.getEllipse();


        //Vector2 position = new Vector2(PhysicsCalculator.toUnits(ellipse.x + ellipse.width/2), PhysicsCalculator.toUnits(ellipse.y + ellipse.height/2));
        Vector2 position = new Vector2(0,0);

        CircleShape circleShape = new CircleShape();
        circleShape.setPosition(position);
        circleShape.setRadius(PhysicsCalculator.toUnits(ellipse.height/2));
        ////System.out.print("circle data: \n  position: " + circleShape.getPosition());
        return circleShape;
    }

    public static CircleShape getCircle(CircleMapObject circleObject) {
       // //System.out.print("making of CircleMapObject Circleshape\n");
        return getCircle(circleObject.getCircle());
    }

    public static CircleShape getCircle(Circle circle) {
       // //System.out.print("making of Circle CircleShape\n");
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(PhysicsCalculator.toUnits(circle.radius));
        //circleShape.setPosition(new Vector2(PhysicsCalculator.toUnits(circle.x), PhysicsCalculator.toUnits(circle.y)));
        circleShape.setPosition(new Vector2(1,1));

        return circleShape;
    }

    public static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        ////System.out.print("making of PolygonMapObject PolygonShape\n");
        return getPolygon(polygonObject.getPolygon());
    }

    public static PolygonShape getPolygon(Polygon polygon) {
        ////System.out.print("making of Polygon PolygonShape\n");
        PolygonShape polygonShape = new PolygonShape();



        float vertice;
        float[] vertices = polygon.getTransformedVertices();
        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            vertice = vertices[i];
            worldVertices[i] = PhysicsCalculator.toUnits(vertices[i]);
            ////System.out.print("worldVertice: " + worldVertices[i] + "\n");

        }
        polygonShape.set(worldVertices);

        return polygonShape;
    }

    public static ChainShape getPolyline(PolylineMapObject polylineObject, boolean setToOldPoint) {
        return getPolyline(polylineObject.getPolyline(), setToOldPoint);
    }

    public static ChainShape getPolyline(Polyline polyline, boolean setToOldPoint) {
        ChainShape chain = new ChainShape();

        float[] vertices = polyline.getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = PhysicsCalculator.toUnits(vertices[i * 2]);
            worldVertices[i].y = PhysicsCalculator.toUnits(vertices[i * 2 + 1]);
        }

        if (setToOldPoint)
            worldVertices = set_to_old_point(worldVertices, startPoint);


        chain.createChain(worldVertices);

        return chain;
    }

    public static Shape getShape(MapObject object, boolean setToOldPoint) {
        if (object instanceof TextureMapObject) {
            return null;
        }

        Shape shape;

        if (object instanceof RectangleMapObject) {
            shape = ShapeFactory.getRectangle((RectangleMapObject) object);
        } else if (object instanceof PolygonMapObject) {
            shape = ShapeFactory.getPolygon((PolygonMapObject) object);
        } else if (object instanceof PolylineMapObject) {
            shape = ShapeFactory.getPolyline((PolylineMapObject) object, setToOldPoint);
        } else if (object instanceof CircleMapObject) {
            shape = ShapeFactory.getCircle((CircleMapObject) object);
        } else if(object instanceof  EllipseMapObject){
            ////System.out.print("object instance of EllipseMapObject\n");
            shape = ShapeFactory.makeCircle((EllipseMapObject) object);
        }else {
            return  null;
        }
        return shape;
    }





    private static Vector2[] set_to_old_point(Vector2[] points, Vector2 startPosition){



        //polygon points have to be scaled by chainsawrun.ppm and startPosition has to be added to them

        float pd1;
        float pd2;
        boolean setToOldpoint = false;
        Vector2 setOldPoint = new Vector2();

        //fin point width smallest x
        float smallestX = points[0].x;
        Vector2 beginningPoint = points[0];
        int beginnPointIndex = 0;
        int i = -1;
        for (Vector2 point: points){
            i ++;
            if (point.x < smallestX){
                ////System.out.println("                new smallest x found");
                smallestX = point.x;
                beginningPoint = point;
                beginnPointIndex = i;
            }
        }

        if (beginningPoint == null){
            beginningPoint = new Vector2(0,0);
        }
        if (startPosition == null){
            startPosition = new Vector2(0,0);
        }

        for (Vector2 oldPoint : oldPoints) {
            pd1 = oldPoint.x - (beginningPoint.x + startPosition.x);
            pd2 = oldPoint.y - (beginningPoint.y + startPosition.y);
            if(pd1 < 0){ pd1 *= (-1);}
            if(pd2 < 0){ pd2 *=(-1);}


            if (pd1 < 0.2 && pd2 < .2) {
                ////System.out.println("      set to oldPoint    beginPoint: " + beginningPoint + "   |  oldPint: " + oldPoint + "  |  startPos: " + startPosition);
                setToOldpoint = true;
                setOldPoint = new Vector2(oldPoint);
                setOldPoint.x -= startPosition.x;
                setOldPoint.y -= startPosition.y;

                points[beginnPointIndex].set(setOldPoint);
            }
            ////System.out.println();

        }

        Vector2 oldPoint;
        if (startPosition == null){
            startPosition = new Vector2(0,0);
        }

        for (Vector2 point : points){
            oldPoint = new Vector2(point);
            oldPoint.x += startPosition.x;
            oldPoint.y += startPosition.y;
            oldPoints.add(oldPoint);

        }

        Array<Vector2> poly_points = new Array<Vector2>();
        poly_points.add(new Vector2(points[0].x, points[0].y -10));
        poly_points.addAll(points);
        poly_points.add(new Vector2(points[points.length - 1].x, points[points.length - 1].y -10));

        //polyline.setPolyline(new Polyline(convert_to_floatList(polypoints)));

        //polyline.setPolyline(new Polyline(list));

        Vector2[] finalPoints = new Vector2[poly_points.size];

        i = 0;
        for (Vector2 point : poly_points){
            finalPoints[i] = point;
            i++;
        }


        return finalPoints;

    }


    private static float[] convert_to_floatList(Array<Vector2> array){
        float[] list = new float[array.size*2];
        int count = 0;
        for (Vector2 point: array){
            list[count] = point.x;
            count++;
            list[count] = point.y;
            count++;
        }



        return list;
    }



    private static Vector2[] convert_to_Vector2(PolylineMapObject object){
        float[] shape_vetecies = object.getPolyline().getTransformedVertices();
        Vector2[] points = new Vector2[shape_vetecies.length/2];
        Vector2 currentVec2 = new Vector2();



        for (int index = 0; index < object.getPolyline().getTransformedVertices().length/2; index++){
            currentVec2 = new Vector2();
            currentVec2.x = PhysicsCalculator.toUnits(shape_vetecies[index * 2]);
            currentVec2.y = PhysicsCalculator.toUnits(shape_vetecies[index * 2 + 1]);

            // //System.out.print(currentVec2.x + "    " + currentVec2.y + "      ");


            points[index] = currentVec2;

            /*if (index >= 1)
                //System.out.print(points.get(index-1));
            //System.out.print("\n");*/
        }
        return points;
    }


}


