/*
 * @author Nicholas Eng <nicholas.eng22@trnityschoolnyc.org>
 * @since  2021-2-16
 */

import java.awt.*;

public class DisplayWindow {

    /* Global Variables */

    public static int maxDisplayWindowLength = 800;
    public static int maxDisplayWindowWidth = 800;

    public static int numberWindows = 2;
    public static double maxComplexSpaceX = 2.0;
    public static double maxComplexSpaceY = 1.5;

    private int leftX, bottomY; //The position of the DisplayWindow
    private int rightX, topY; // The upper right corner of the displayWindow
    private double length, width; //The length along the x-axis and width along the y-axis of the DisplayWindow
    private double rMax, rMin; //The min and max values on the real axis. Starts at -2 ≤ a ≤ 2
    private double iMax, iMin; //The min and max values on the imaginary axis. Starts at-1.5 ≤ a ≤ 1.5
    private double zoomFactor; //Keeps track of the zoom ratio. Starts at 1. Increases/decreases according to “i”/“I” and “o”/“O” key presses.

    private ComplexNumber center;  //starts at 0+0i. Changes according to mouse clicks on the DisplayWindow
    private ComplexNumber juliaConstant;
    private String title; //title for each window

    public int screenID;

    /* Constructor */

    /**
    * The standard constructor for class Line. A Line object is a line defined by the equation ax + by + c = 0, where a is not equal to zero, b is not equal to zero, and a, b, and c are all integers.
    * @param leftX  an integer value indicating the left x coordinate
    * @param bottomY  an integer value indicating the bottom y coordinate
    * @param length  an integer value indicating the length of the screen
    * @param width  an integer value indicating the width of the screen
    * @param screenID  an integer value indicating which screen is being used
    *
    */
    public DisplayWindow(final int leftX, final int bottomY, final int length, final int width, final int screenID) {
        this.leftX = leftX;
        this.bottomY = bottomY;
        this.rightX = leftX + length;
        this.topY = bottomY + width;

        this.screenID = screenID;

        this.juliaConstant = ComplexNumber.C_ZERO;
        this.resetCenter();
        this.resetView();
    }

    /* Access Methods */

    /**
    * The Accessor method for the the minimum value of the real axis
    * @return the minimum value on the real axis, a double value
    *
    */
    public double getRMin(){
        return this.rMin;
    }

    /**
    * The Accessor method for the the maximum value of the real axis
    * @return the maximum value on the real axis, a double value
    *
    */
    public double getRMax(){
        return this.rMax;
    }

    /**
    * The Accessor method for the the minimum value of the imaginary axis
    * @return the minimum value on the imaginary axis, a double value
    *
    */
    public double getIMin(){
        return this.iMin;
    }

    /**
    * The Accessor method for the the maximum value of the imaginary axis
    * @return the maximum value on the imaginary axis, a double value
    *
    */
    public double getIMax(){
        return this.iMin;
    }

    /**
    * The Accessor method for the the length
    * @return the length value, a double value
    *
    */
    public double getLength(){
        return this.length;
    }

    /**
    * The Accessor method for the the width
    * @return the width value, a double value
    *
    */
    public double getWidth(){
        return this.width;
    }

    /**
    * The Accessor method for the the center of a ComplexNumber
    * @return the center, a ComplexNumber
    *
    */
    public ComplexNumber getCenter(){
      return this.center;
    }

    /**
    * The Accessor method for the the pixels on the display
    * @return the leftX, an integer value
    *
    */
    public int getStartX(){
      return this.leftX;
    }

    /**
    * The Accessor method for the the pixels on the display
    * @return the bottomY, an integer value
    *
    */
    public int getStartY(){
      return this.bottomY;
    }

    /**
    * The Accessor method for the the pixels on the display
    * @return the rightX, an integer value
    *
    */
    public int getEndX(){
      return this.rightX;
    }

    /**
    * The Accessor method for the the pixels on the display
    * @return the topY, an integer value
    *
    */
    public int getEndY(){
      return this.topY;
    }

    /* Calculator Methods */

    /**
    * The calculator method for finding length of window
    * @return length of the window, an integer value
    *
    */
    public int getScreenLength() {
      return (this.rightX - this.leftX);
    }

    /**
    * The calculator method for finding width of window
    * @return width of the window, an integer value
    *
    */
    public int getScreenWidth() {
        return (this.topY - this.bottomY);
    }

    /**
    * The calculator method for finding the screenID
    * @return screenID, an integer value
    *
    */
    public int getScreenID() {
        return this.screenID;
    }

    /**
    * The calculator method for a bileanear map
    * @param value, a double
    * @param oldMin, a double
    * @param oldMax. a double
    * @param newMin, a double
    * @param newMax, a double
    * @return a converted double value
    *
    */
    public double map(double value, double oldMin, double oldMax, double newMin, double newMax){
        return (value - oldMin) * (newMax - newMin) / (oldMax - oldMin) + newMin;
    }

    /**
    * The calculator method for a map from ComplexNumber to Coordinate (x value))
    * @param real, a double
    * @return a converted integer value
    *
    */
    public int mapX(final double real) {
        double xmax = this.rMax;
        double xmin = this.rMin;
        return (int) ((real - xmin) / (xmax - xmin) * this.getScreenLength());
    }

    /**
    * The calculator method for a map from ComplexNumber to Coordinate (Y value)
    * @param img, a double
    * @return a converted integer value
    *
    */
    public int mapY(final double img) {
        double ymax = this.iMax;
        double ymin = this.iMin;
        return (int) ((img - ymin) / (ymax - ymin) * this.getScreenWidth());
    }

    /**
    * The calculator method for shifting the center of a mandelbrot set
    * @param shiftC. a ComplexNumber
    *
    */

    public void shift(ComplexNumber shiftC) {
        this.center = this.center.add(shiftC);
    }

    /**
    * The calculator method for translating DisplayWindow coordinate values into ComplexNumbers
    * @param windowX, an integer
    * @return a real converted double value
    *
    */
    public double mapA(int windowX){ //translate DisplayWindow values to values from the current Complex plane
      int xmin = this.getStartX();
      int xmax = this.getScreenLength() + xmin;

      double scaler = (windowX - xmin) * 1.0 / (xmax - xmin);
      double maxdist = ( this.rMax - this.rMin );
      return scaler * maxdist + this.rMin;
    }

    /**
    * The calculator method for translating DisplayWindow coordinate values into ComplexNumbers
    * @param windowY, an integer
    * @return imaginary converted double value
    *
    */
    public double mapB(final int windowY) {
        int ymin = this.getStartY();
        int ymax = this.getScreenWidth() + ymin;

        double scaler = (windowY - ymin) * 1.0 / (ymax - ymin);
        double maxdist = ( this.iMax - this.iMin );
        return scaler * maxdist + this.iMin;
    }

    /**
    * The Accessor method for getting the ZoomFactor
    * @return ZoomFactor, a double value
    *
    */
    public double getZoomFactor() {
        return this.zoomFactor;
    }

    /**
    * The Accessor method for setting the ZoomFactor (>1)
    * @param zoomFactor, a double
    *
    */
    public void setZoomFactor(final double zoomFactor) {
        if ( zoomFactor > 1.0 ) {
            this.zoomFactor = zoomFactor;
        }
    }

    /**
    * The calculator method for change the Complex boundary space
    *
    */
    public void zoomIn(){
      this.rMax = ( this.rMax - this.center.getReal()) / this.zoomFactor + this.center.getReal();
      this.rMin = ( this.rMin - this.center.getReal()) / this.zoomFactor + this.center.getReal();
      this.iMax = ( this.iMax - this.center.getImaginary()) / this.zoomFactor + this.center.getImaginary();
      this.iMin = ( this.iMin - this.center.getImaginary()) / this.zoomFactor + this.center.getImaginary();
    }

    /**
    * The calculator method for change the Complex boundary space
    *
    */
    public void zoomOut(){
      this.rMax = ( this.rMax - this.center.getReal()) * this.zoomFactor + this.center.getReal();
      this.rMin = ( this.rMin - this.center.getReal()) * this.zoomFactor + this.center.getReal();
      this.iMax = ( this.iMax - this.center.getImaginary()) * this.zoomFactor + this.center.getImaginary();
      this.iMin = ( this.iMin - this.center.getImaginary()) * this.zoomFactor + this.center.getImaginary();
    }

    /**
    * The calculator method for resetting the center location
    * @param c, a ComplexNumber
    *
    */
    public void setCenter(final ComplexNumber c) {
      this.rMax += c.getReal() - this.center.getReal();
      this.rMin += c.getReal() - this.center.getReal();
      this.iMax += c.getImaginary() - this.center.getImaginary();
      this.iMin += c.getImaginary() - this.center.getImaginary();
      this.center = c;
  }

    /* Helper Methods */

    /**
    * The Helper method for sets a title for the window
    * @param title, a String
    */
    public void setTitle(final String title) {
        if ( title != null ) {
            this.title = title;
        }
    }

    /**
    * The Accesspr method for getting the Julia Constant
    *
    *
    */
    public ComplexNumber getJuliaConstant() {
        return this.juliaConstant;
    }

    /**
    * The Accesspr method for setting the Julia Constant
    * @param C, a ComplexNumber
    *
    */
    public void setJuliaConstant(final ComplexNumber C) {
        this.juliaConstant = C;
    }

    /**
    * The Helper method for resetting the boundaries in the complex window
    *
    */

    public void resetView() {
        this.rMax = maxComplexSpaceX;
        this.rMin = -this.rMax;
        this.iMax = maxComplexSpaceY;
        this.iMin = -this.iMax;
    }

    /**
    * The Helper method for resetting the center
    *
    */
    public void resetCenter() {
        this.center = new ComplexNumber();
    }

    /**
    * The Helper method for plotting a coordinate
    * @param c, a coordinate
    *
    */
    public void plot(final Coordinate c) {
        this.plot(c.getX(), c.getY(), StdDraw.BLACK);
    }

  /**
   * The Helper method for plotting a coordinate
   *
   * @param x position. an integer
   * @param y position, an integer
   *
   */
    public void plot(final int x, final int y) {
        this.plot(x, y, StdDraw.BLACK);
    }

    /**
     * The Helper method for plotting a coordinate
     * @param x position. an integer
     * @param y position, an integer
     * @param penColor, a Color
     *
     */

    public void plot(final int x, final int y, final Color penColor) {
        if (this.withinWindow(x, y)) {
            StdDraw.setPenColor(penColor);
            StdDraw.point(x, y);
        }
    }

    /**
    * The Helper method for plotting a coordinate
    * @param c, a ComplexNumber
    *
    */
    public void plot(ComplexNumber c) {
        this.plot(c, StdDraw.BLACK);
    }

    /**
    * The Helper method for plotting a coordinate
    * @param c, a ComplexNumber
    * @param penColor, a color
    *
    */
    public void plot(ComplexNumber c, Color penColor) {
        if (c == null) {
            return;
        }
        int coordX = this.mapX(c.getReal());
        int coordY = this.mapY(c.getImaginary());

        StdDraw.setPenColor(penColor);
        StdDraw.point(coordX, coordY);
    }

    /**
    * The Helper method for drawing the axis'
    *
    */
    public void makeQuadrantsInScreen() {
          StdDraw.setPenColor(StdDraw.RED);

          double oldPenRadius = StdDraw.getPenRadius();
          StdDraw.setPenRadius(0.001);
          double xmin = this.getStartX();
          double xmax = this.getEndX();
          double ymin = this.getStartY();
          double ymax = this.getEndY();

          // y-axis
          StdDraw.line((xmin + this.getScreenLength()/2), ymin, (xmin + this.getScreenLength()/2), ymax);

          // x-axis
          StdDraw.line(xmin, (ymin + this.getScreenWidth()/2), xmax, (ymin + this.getScreenWidth()/2));
          StdDraw.setPenRadius(oldPenRadius);
    }

    /**
    * The Helper method for drawing lines
    *
    */
    public void drawLine(final Coordinate c_begin, final Coordinate c_end) {
        this.drawLine(c_begin.getX(), c_begin.getY(), c_end.getX(), c_end.getY());
    }

    /**
    * The Helper method for drawing a line
    * @param x_begin value, an integer
    * @param y_begin value, an integer
    * @param x_end value, an integer
    * @param y_end value, an integer
    *
    */
    public void drawLine(final int x_begin, final int y_begin, final int x_end, final int y_end) {
        if ( x_begin < this.getStartX() || x_end > this.getEndX() ) { return; }
        if ( y_begin < this.getStartY() || y_end > this.getEndY() ) { return; }

        StdDraw.line(x_begin, y_begin, x_end, y_end);
    }

    /**
    * The Helper method for checking if a coordiante is within the given window
    * @param x value, an integer
    * @param y value, an integer
    * @return true or false, a boolean
    *
    */
    public boolean withinWindow(final int x, final int y) {
        boolean matchX = (x >= this.getStartX()) && (x <= this.getEndX());
        boolean matchY = (y >= this.getStartY()) && (y <= this.getEndY());
        return matchX && matchY;
    }

    /**
    * The Helper method for checking if a coordiante is within the given window
    * @param c, a Coordinate
    * @return true or false, a boolean
    *
    */
    public boolean withinWindow(final Coordinate c) {
        return this.withinWindow(c.getX(), c.getY());
    }

    /**
    * The Calculator method for getting displaying coordinates
    * @param obj, an object
    * @param showComplex, a boolean
    * @return a String
    *
    */
    public String getDisplayString(Object obj, final boolean showComplex) {
        String str = null;
        if (showComplex) {
            if ( obj instanceof ComplexNumber ) {
                str = ((ComplexNumber) obj).round(5);
            }
            if ( obj instanceof Coordinate ) {
                Coordinate coord = (Coordinate) obj;
                ComplexNumber c = this.transformCoordinate(coord);
                str = c.round(5);
            }
        } else {
            if ( obj instanceof ComplexNumber ) {
                ComplexNumber c = (ComplexNumber) obj;
                Coordinate coord = this.transformComplex(c);
            }

            if ( obj instanceof Coordinate ) {
                str = ((Coordinate) obj).toString();
            }
        }
        return str;
    }

    /**
    * The Calculator method for drawing the display coordinates
    *
    */
    public void drawCoordinateText() {
        String result = this.getDisplayString(this.getCenter(), true);
        if ( result != null ) {
            StdDraw.setPenColor(StdDraw.WHITE);
            Font font = new Font("Courier", Font.PLAIN, 12);
            StdDraw.setFont(font);
            StdDraw.textLeft(7, 10, "Center: " + result);
            StdDraw.show();
        }
    }

    /**
    * The Calculator method for drawing the Title of the window
    *
    */
    public void drawTitle() {
        if ( this.title != null ) {
            StdDraw.setPenColor(StdDraw.WHITE);
            Font font = new Font("Courier", Font.BOLD, 20);
            StdDraw.setFont(font);
            StdDraw.textLeft(this.getScreenID() * this.getScreenLength() + 50, this.getScreenWidth() - 20, this.title);
            StdDraw.show();
        }
    }

    /**
    * The Calculator method for transforming from pixel to complex space
    * @param a, an integers
    * @param b, an integer
    * @return an equicalent in the Complex complex space
    *
    */
    public ComplexNumber transformCoordinate(final int a, int b) {
        double x = this.mapA(a);
        double y = this.mapB(b);
        return new ComplexNumber(x, y);
    }

    /**
    * The Calculator method for transforming from pixel to complex space
    * @param c, a coordiante
    * @return an equicalent in the Complex complex space
    *
    */
    public ComplexNumber transformCoordinate(final Coordinate c) {
        return this.transformCoordinate(c.getX(), c.getY());
    }

    /**
    * The Calculator method for transforming from complex space to pixel space
    * @param c, a ComplexNumber
    * @return an equicalent in the pixel space
    *
    */
    public Coordinate transformComplex(final ComplexNumber c) {
        return new Coordinate(this.mapX(c.getReal()), this.mapY(c.getImaginary()));
    }
}
