/**
 * @author      Nicholas Eng
 * @since       2021-2-25
 */

import java.awt.*;

public class JuliaSetGenerator_Eng_Nicholas {

    public static int leftWindow = 0;
    public static int rightWindow = 1;
    public static int numberWindows = 2;
    public static int lastWindow = leftWindow;
    public static int maxDisplayWindowLength = 800;
    public static int maxDisplayWindowWidth = 600;

    public static double unboundLimit2 = 4.0; // This is the 2 squared to make computation of sqrt less slow
    public static int maxIteration = 255;
    public static int maxRotation = 360;
    public static int maxHue = 11;
    public static double hueOffset = 45;
    public static int indent = 20;

    public static int juliaSet(ComplexNumber seed, final ComplexNumber offset, final int maxIterations) {
        // z_{n+1} = (z_{n})^2 + C
        int iter = 0;
        if ( offset.sqrmag() < unboundLimit2 ) {
            ComplexNumber z_n = seed; // Here we seed the value for the original iteration
            for (int i = 0; i <= maxIterations; i++) {
                z_n = z_n.square().add(offset); // recursive formula due to the for-loop re-using the same variable
                if ( z_n.sqrmag() >= unboundLimit2 ) {
                    break;
                }
                iter = i;
            }
        }
        return iter;
    }

    /**
    * The Calculator method for drawing the mandelbrot set
    * @param window, a DisplayWindow
    *
    */
    public static void drawMandelbrot(final DisplayWindow window) {
        int iterationCutoff = maxIteration;

        for (int i = window.getStartX(); i < window.getEndX(); ++i) {
            for (int j = window.getStartY(); j < window.getEndY(); ++j) {
                // Transform the i,j into a Coordinate...
                Coordinate C_window = new Coordinate(i, j); // This represents a pixel on the window

                // Transform the coordinate into a complex number based on the window being used
                ComplexNumber C = window.transformCoordinate(C_window);

                // Compute if the point from the window converted to a complex number is in the set
                int iterationCount = juliaSet(ComplexNumber.C_ZERO, C, iterationCutoff);

                //Create HSB color scheme
                Color hsb;
                if (iterationCount == iterationCutoff) {
                    // Stays in the "orbit" of the set the Fatou section
                    hsb = StdDraw.BLACK;
                } else {
                    // Leaves the orbit before the maximum iterations have been done
                    double hue = window.map(maxIteration - iterationCount, 0, maxRotation, 0, maxHue) + hueOffset;
                    hsb = Color.getHSBColor((float) hue, (float) 1.0, (float) 1.0);
                }

                // Save the iteration count to a 2D array for "smoothing"
                //window.setPoint(i, j, iterationCount);

                // Plot the raw-data converted to a HSB color value
                window.plot(i, j, hsb);
            }
        }
        //window.makeQuadrantsInScreen();
        window.drawTitle();
        StdDraw.show();
    }

    /**
    * The Calculator method for drawing a Julia Set
    * @param window, a DisplayWindow
    *
    */
    public static void drawJuliaSet(final DisplayWindow window) {
        drawJuliaSet(window, window.getJuliaConstant());
    }

    /**
    * The Calculator method for drawing a Julia Set
    * @param window, a DisplayWindow
    * @param seed, a ComplexNumber
    *
    */
    public static void drawJuliaSet(final DisplayWindow window, final ComplexNumber seed) {
        int iterationCutoff = maxIteration;

        for (int i = window.getStartX(); i < window.getEndX(); ++i) {
            for (int j = window.getStartY(); j < window.getEndY(); ++j) {
                // Transform the i,j into a Coordinate...
                Coordinate C_window = new Coordinate(i, j); // This represents a pixel on the window

                // Transform the coordinate into a complex number based on the window being used
                ComplexNumber C = window.transformCoordinate(C_window);

                // Compute if the point from the window converted to a complex number is in the set
                int iterationCount = juliaSet(C, seed, iterationCutoff);

                //Create HSB color scheme
                Color hsb;
                int rgbColorCode = 0;

                if (iterationCount == iterationCutoff) {
                    hsb = StdDraw.BLACK;
                } else {
                    // Leaves the orbit before the maximum iterations have been done
                    double hue = window.map(maxIteration - iterationCount,
                            0, maxRotation,
                            0, 2 * maxHue) + hueOffset;
                    hsb = Color.getHSBColor((float) hue, (float) 1.0, (float) 1.0);
                }
                //window.setPoint(i, j, iterationCount);

                // Plot the raw-data converted to a HSB color value
                window.plot(i, j, hsb);
                //window.plot(i, j, rgbColorCode);
            }
        }
        //window.makeQuadrantsInScreen();
        window.drawTitle();
        StdDraw.show();
    }


    public static void main(String[] args) {
        // Design the windows into a window Manager (simple array here)
        DisplayWindow[] fullCanvas = new DisplayWindow[numberWindows];

        int fullLength = 0;
        int fullWidth = maxDisplayWindowWidth;

        // Construct each displayWindow Left display window -- fullCanvas[0], Right display window -- fullCanvas[1]
        for (int i = 0; i < numberWindows; ++i) {
            fullCanvas[i] = new DisplayWindow(maxDisplayWindowLength * i, 0, maxDisplayWindowLength, maxDisplayWindowWidth, i);
            fullLength += maxDisplayWindowLength;
        }

        // Shift the left window center and set the zoom in/out scale
        fullCanvas[leftWindow].setCenter(new ComplexNumber(-0.375, 0.0));
        fullCanvas[leftWindow].setZoomFactor(Math.sqrt(2));
        fullCanvas[rightWindow].setZoomFactor(Math.sqrt(2));
        fullCanvas[leftWindow].setTitle("Mandelbrot Set");

        // Design the canvas size
        StdDraw.enableDoubleBuffering(); //Implement double buffering
        StdDraw.setCanvasSize(fullLength, fullWidth);

        // set the scale of the coordinate system
        StdDraw.setXscale(0.0, fullLength);
        StdDraw.setYscale(0.0, fullWidth);

        // clear the background
        StdDraw.clear(StdDraw.WHITE);

        //Graph Separation
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(1.0/maxDisplayWindowLength); //Determines how pixelated

        drawMandelbrot(fullCanvas[leftWindow]); // Attractor for C = (0,0)
        fullCanvas[leftWindow].drawCoordinateText();
        drawJuliaSet(fullCanvas[rightWindow]); // Attractor for C = (0,0)

        /* Mouse and Keyboard Input */

        while (true) {
            /* Check for key input */
            if (StdDraw.hasNextKeyTyped()) {
                char keyInput = StdDraw.nextKeyTyped();

                if (keyInput == 'i' || keyInput == 'I') {
                    if (lastWindow == leftWindow) {
                        fullCanvas[leftWindow].zoomIn();
                        drawMandelbrot(fullCanvas[leftWindow]);
                        fullCanvas[leftWindow].drawCoordinateText();
                    } else {
                        fullCanvas[rightWindow].zoomIn();
                        drawJuliaSet(fullCanvas[rightWindow]);
                    }
                }

                if (keyInput == 'o' || keyInput == 'O') {
                    if (lastWindow == leftWindow) {
                        fullCanvas[leftWindow].zoomOut();
                        drawMandelbrot(fullCanvas[leftWindow]);
                        fullCanvas[leftWindow].drawCoordinateText();
                    } else {
                        fullCanvas[rightWindow].zoomOut();
                        drawJuliaSet(fullCanvas[rightWindow]);
                    }
                }

                if (keyInput == 'q' || keyInput == 'Q') {
                    System.exit(0);
                }

                if (keyInput == 'r' || keyInput == 'R') {
                    fullCanvas[leftWindow].resetCenter();
                    fullCanvas[leftWindow].resetView();
                    drawMandelbrot(fullCanvas[leftWindow]);
                    fullCanvas[leftWindow].drawCoordinateText();

                    fullCanvas[rightWindow].resetCenter();
                    fullCanvas[rightWindow].resetView();
                    fullCanvas[rightWindow].setJuliaConstant(ComplexNumber.C_ZERO);
                    drawJuliaSet(fullCanvas[rightWindow]);
                    fullCanvas[rightWindow].drawTitle();
                    lastWindow = leftWindow; //resets the last window to left
                }
                if (keyInput == 'w' || keyInput == 'W') {
                    if (lastWindow == leftWindow) {
                      ComplexNumber oldCenter = fullCanvas[leftWindow].getCenter();
                      Coordinate oldCenterPixels = fullCanvas[leftWindow].transformComplex(oldCenter);
                      Coordinate newCenterPixels = new Coordinate(oldCenterPixels.getX(),oldCenterPixels.getY() + indent );
                      ComplexNumber newCenter = fullCanvas[leftWindow].transformCoordinate(newCenterPixels);
                      fullCanvas[leftWindow].setCenter(newCenter);
                      drawMandelbrot(fullCanvas[leftWindow]);
                      fullCanvas[leftWindow].drawCoordinateText();
                    }
                    else {
                      ComplexNumber oldCenter = fullCanvas[rightWindow].getCenter();
                      Coordinate oldCenterPixels = fullCanvas[rightWindow].transformComplex(oldCenter);
                      Coordinate newCenterPixels = new Coordinate(oldCenterPixels.getX() + fullCanvas[rightWindow].getScreenLength(),oldCenterPixels.getY() + indent );
                      ComplexNumber newCenter = fullCanvas[rightWindow].transformCoordinate(newCenterPixels);
                      fullCanvas[rightWindow].setCenter(newCenter);
                      drawJuliaSet(fullCanvas[rightWindow]);
                    }
                }
                if (keyInput == 's' || keyInput == 'S') {
                    if (lastWindow == leftWindow) {
                      ComplexNumber oldCenter = fullCanvas[leftWindow].getCenter();
                      Coordinate oldCenterPixels = fullCanvas[leftWindow].transformComplex(oldCenter);
                      Coordinate newCenterPixels = new Coordinate(oldCenterPixels.getX(),oldCenterPixels.getY() - indent );
                      ComplexNumber newCenter = fullCanvas[leftWindow].transformCoordinate(newCenterPixels);
                      fullCanvas[leftWindow].setCenter(newCenter);
                      drawMandelbrot(fullCanvas[leftWindow]);
                      fullCanvas[leftWindow].drawCoordinateText();
                    }
                    else {
                      ComplexNumber oldCenter = fullCanvas[rightWindow].getCenter();
                      Coordinate oldCenterPixels = fullCanvas[rightWindow].transformComplex(oldCenter);
                      Coordinate newCenterPixels = new Coordinate(oldCenterPixels.getX()+ fullCanvas[rightWindow].getScreenLength(), oldCenterPixels.getY() - indent );
                      ComplexNumber newCenter = fullCanvas[rightWindow].transformCoordinate(newCenterPixels);
                      fullCanvas[rightWindow].setCenter(newCenter);
                      drawJuliaSet(fullCanvas[rightWindow]);
                    }
                }
                if (keyInput == 'a' || keyInput == 'A') {
                    if (lastWindow == leftWindow) {
                      ComplexNumber oldCenter = fullCanvas[leftWindow].getCenter();
                      Coordinate oldCenterPixels = fullCanvas[leftWindow].transformComplex(oldCenter);
                      Coordinate newCenterPixels = new Coordinate(oldCenterPixels.getX()-indent, oldCenterPixels.getY());
                      ComplexNumber newCenter = fullCanvas[leftWindow].transformCoordinate(newCenterPixels);
                      fullCanvas[leftWindow].setCenter(newCenter);
                      drawMandelbrot(fullCanvas[leftWindow]);
                      fullCanvas[leftWindow].drawCoordinateText();
                    }
                    else {
                      ComplexNumber oldCenter = fullCanvas[rightWindow].getCenter();
                      Coordinate oldCenterPixels = fullCanvas[rightWindow].transformComplex(oldCenter);
                      Coordinate newCenterPixels = new Coordinate(oldCenterPixels.getX()-indent + fullCanvas[rightWindow].getScreenLength(), oldCenterPixels.getY());
                      ComplexNumber newCenter = fullCanvas[rightWindow].transformCoordinate(newCenterPixels);
                      fullCanvas[rightWindow].setCenter(newCenter);
                      drawJuliaSet(fullCanvas[rightWindow]);
                    }
                }
                if (keyInput == 'd' || keyInput == 'D') {
                    if (lastWindow == leftWindow) {
                      ComplexNumber oldCenter = fullCanvas[leftWindow].getCenter();
                      Coordinate oldCenterPixels = fullCanvas[leftWindow].transformComplex(oldCenter);
                      Coordinate newCenterPixels = new Coordinate(oldCenterPixels.getX()+indent, oldCenterPixels.getY());
                      ComplexNumber newCenter = fullCanvas[leftWindow].transformCoordinate(newCenterPixels);
                      fullCanvas[leftWindow].setCenter(newCenter);
                      drawMandelbrot(fullCanvas[leftWindow]);
                      fullCanvas[leftWindow].drawCoordinateText();
                    }
                    else {
                      ComplexNumber oldCenter = fullCanvas[rightWindow].getCenter();
                      Coordinate oldCenterPixels = fullCanvas[rightWindow].transformComplex(oldCenter);
                      Coordinate newCenterPixels = new Coordinate(oldCenterPixels.getX()+indent + fullCanvas[rightWindow].getScreenLength(), oldCenterPixels.getY());
                      ComplexNumber newCenter = fullCanvas[rightWindow].transformCoordinate(newCenterPixels);
                      fullCanvas[rightWindow].setCenter(newCenter);
                      drawJuliaSet(fullCanvas[rightWindow]);
                    }
                }

            }
            /* Mouse Events */

            if (StdDraw.mousePressed()) {
                int coordX = (int) StdDraw.mouseX();
                int coordY = (int) StdDraw.mouseY();

                if (fullCanvas[leftWindow].withinWindow(coordX,coordY)) {
                    ComplexNumber newCenter = fullCanvas[leftWindow].transformCoordinate(coordX, coordY);

                    fullCanvas[leftWindow].setCenter(newCenter);
                    drawMandelbrot(fullCanvas[leftWindow]);
                    fullCanvas[leftWindow].drawCoordinateText();

                    fullCanvas[rightWindow].setJuliaConstant(newCenter);
                    fullCanvas[rightWindow].setTitle("Julia Set [ c = " + newCenter.round(5) + " ]");
                    drawJuliaSet(fullCanvas[rightWindow]);
                    lastWindow = leftWindow;

                } else { //right window
                    ComplexNumber newCenter = fullCanvas[rightWindow].transformCoordinate(coordX, coordY);
                    fullCanvas[rightWindow].setCenter(newCenter);

                    drawJuliaSet(fullCanvas[rightWindow]);
                    fullCanvas[rightWindow].drawCoordinateText();

                    lastWindow = rightWindow;
                }
            }
            StdDraw.pause(1);

        }
    }
}
