import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {

    private Point[] points;
    private LineSegment[] segments;
    private int size;

    public BruteCollinearPoints(Point[] points){
        if (points == null) {
            throw new IllegalArgumentException("The argument to the constructor is null.");
        }
        Point[] pointsCopy = new Point[points.length];
        for (int i = 0; i < points.length; i++){
            if (points[i] == null) {
                throw new IllegalArgumentException("A point in the array is null.");
            }
            pointsCopy[i] = points[i];
        }

        Arrays.sort(pointsCopy);
        for (int i = 0; i < pointsCopy.length - 1; i++){
            if (pointsCopy[i].compareTo(pointsCopy[i+1]) == 0){
                throw new IllegalArgumentException("The argument to the constructor contains a repeated point.");
            }
        }

        segments = new LineSegment[1];
        size = 0;

        for (int i = 0; i < pointsCopy.length - 3; i++){
            Point tempPoint1 = pointsCopy[i];
            for (int j = i + 1; j < pointsCopy.length - 2; j++){
                Point tempPoint2 = pointsCopy[j];
                for (int k = j + 1; k < pointsCopy.length - 1; k++){
                    Point tempPoint3 = pointsCopy[k];
                    for (int l = k + 1; l < pointsCopy.length; l++){
                        Point tempPoint4 = pointsCopy[l];
                        double tempSlope1 = tempPoint1.slopeTo(tempPoint2);
                        double tempSlope2 = tempPoint1.slopeTo(tempPoint3);
                        double tempSlope3 = tempPoint1.slopeTo(tempPoint4);
                        if (tempSlope1 == tempSlope2 && tempSlope2 == tempSlope3){
                            LineSegment tempLineSegment = new LineSegment(tempPoint1, tempPoint4);
                            enqueue(tempLineSegment);
                        }
                    }
                }
            }
        }        
    }

    private void enqueue(LineSegment line){
        if (line == null){
            throw new IllegalArgumentException("The argument to the constructor is null.");
        }
        if (size == segments.length){
            resize(2 * segments.length);
        }
        segments[size++] = line;
    }

    private void resize(int capacity){
        LineSegment[] lines = new LineSegment[capacity];
        for (int i = 0; i < size; i++) lines[i] = segments[i];
        segments = lines;
    }

    public int numberOfSegments(){
        return size;
    }

    public LineSegment[] segments(){
        LineSegment[] segmentsRemoveNull = new LineSegment[size];
        for (int i = 0; i < size; i++) segmentsRemoveNull[i] = segments[i];
        return segmentsRemoveNull;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
    
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
    
        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
