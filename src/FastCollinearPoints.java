import java.util.Arrays;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

    private Point[] points;
    private LineSegment[] segments;
    private int size;

    public FastCollinearPoints(Point[] points){
        if (points == null) {
            throw new IllegalArgumentException("The argument to the constructor is null.");
        }
        Point[] pointsNaturalOrder = new Point[points.length];
        for (int i = 0; i < points.length; i++){
            if (points[i] == null) {
                throw new IllegalArgumentException("A point in the array is null.");
            }
            pointsNaturalOrder[i] = points[i];
        }

        Arrays.sort(pointsNaturalOrder);
        for (int i = 0; i < pointsNaturalOrder.length - 1; i++){
            if (pointsNaturalOrder[i].compareTo(pointsNaturalOrder[i+1]) == 0){
                throw new IllegalArgumentException("The argument to the constructor contains a repeated point.");
            }
        }

        Point[] pointsSlopeOrder = new Point[pointsNaturalOrder.length];
        for (int i = 0; i < pointsNaturalOrder.length; i++){
            pointsSlopeOrder[i] = pointsNaturalOrder[i];
        }

        segments = new LineSegment[1];
        size = 0;

        for (int i = 0; i < pointsNaturalOrder.length; i++){
            Arrays.sort(pointsSlopeOrder, pointsNaturalOrder[i].slopeOrder());
            int count = 1;
            Point[] collinearPoints = new Point[pointsSlopeOrder.length];
            collinearPoints[0] = pointsNaturalOrder[i];
            for (int j = 0; j < pointsSlopeOrder.length - 1; j++){
                double tempSlope1 = pointsNaturalOrder[i].slopeTo(pointsSlopeOrder[j]);
                double tempSlope2 = pointsNaturalOrder[i].slopeTo(pointsSlopeOrder[j+1]);
                if (tempSlope1 == tempSlope2 && j != (pointsSlopeOrder.length - 2)){
                    collinearPoints[count++] = pointsSlopeOrder[j];
                } else if (tempSlope1 == tempSlope2 && j == (pointsSlopeOrder.length - 2)){
                    collinearPoints[count++] = pointsSlopeOrder[j];
                    collinearPoints[count++] = pointsSlopeOrder[j+1];
                    if (count >= 4){
                        Point[] collinearPointsRemoveNull = new Point[count];
                        for (int k = 0; k < count; k++){
                            collinearPointsRemoveNull[k] = collinearPoints[k];
                        }
                        Arrays.sort(collinearPointsRemoveNull);
                        if (collinearPointsRemoveNull[0] == collinearPoints[0]){
                            LineSegment tempLineSegment = new LineSegment(collinearPointsRemoveNull[0], collinearPointsRemoveNull[count-1]);
                            enqueue(tempLineSegment);
                        }
                    }
                } else {
                    collinearPoints[count++] = pointsSlopeOrder[j];
                    if (count >= 4){
                        Point[] collinearPointsRemoveNull = new Point[count];
                        for (int k = 0; k < count; k++){
                            collinearPointsRemoveNull[k] = collinearPoints[k];
                        }
                        Arrays.sort(collinearPointsRemoveNull);
                        if (collinearPointsRemoveNull[0] == collinearPoints[0]){
                            LineSegment tempLineSegment = new LineSegment(collinearPointsRemoveNull[0], collinearPointsRemoveNull[count-1]);
                            enqueue(tempLineSegment);
                        }
                    }
                    count = 1;
                    collinearPoints = new Point[pointsSlopeOrder.length];
                    collinearPoints[0] = pointsNaturalOrder[i];
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
