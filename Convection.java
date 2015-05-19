/*************************************************************************
 *  Compilation:  javac Convection.java
 *  Execution:    java Convection
 *  Dependencies: Picture.java                
 *
 *  Visualization of convective flow in a cylinder induced by 
 *  horizontal temperature gradient. This aids in examining 
 *  the thermal convection effect in electrophoretic NMR 
 *  experiments. 
 *
 *  % java Potential
 *
 *
 *************************************************************************/

import java.awt.Color;

public class Convection {

	public void plot() {
		// initialize plot parameters
		final int SIZE = 1600; // size of the image
		final double xMax = 4E-3; // maximum x value of the plot
		final double zMax = 1.3E-4; // maximum z value of the plot
		double maxV = 0.0; // maximum vertical flow velocity
		Picture image = new Picture(SIZE, SIZE);

		// compute the vertical flow velocity at each point and plot
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				double velocity = getV(i, j, SIZE, xMax);
				if (velocity > maxV) {
					maxV = velocity;
				}
				Color c = getColor(velocity, zMax);
				image.set(i, j, c);
			}
		}

		System.out.println(maxV);
		//image.save("/Users/hahooy1/Desktop/flow.png");
		image.show();
	}

	// compute the velocity at given x and y values
	private double getV(int col, int row, int size, double xMax) {
		assert (col / size <= 1 && row / size <= 1);
		// initialize parameters
		double gradient = 84.0; // horizontal temperature gradient
		double R = 1.5E-3; // internal radius of the cylinder
		double g = 9.80; // gravity acceleration
		double beta = 1.3E-3; // thermal expansion coefficient of the fluid
		double rho = 1.3E3; // fluid density
		double eta = 3.9E-3; // fluid dynamic viscosity

		// compute Cartesian coordinate
		double x = ((double) col / (double) size) * xMax - xMax / 2;
		double y = ((double) row / (double) size) * xMax - xMax / 2;
		assert x >= -xMax / 2 && x < xMax / 2 && y >= -xMax / 2 && y < xMax / 2;

		// compute polar coordinate
		// radial coordinate
		double r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		double theta = Math.atan(x / y); // angular coordinate
		assert r <= Math.sqrt(Math.pow(xMax / 2, 2) + Math.pow(xMax / 2, 2));

		// calculate the velocity based on flow equation
		double velocity = gradient * (Math.pow(R, 3) * g * beta * rho)
				/ (4 * eta) * (r / R) * (1 - Math.pow((r / R), 2))
				* Math.cos(theta);

		return velocity;
	}

	// compute the color based on the value of velocity
	private Color getColor(double v, double zMax) {
		assert v / zMax <= 1;
		double zColor = (v / zMax) * 255.0;
		int z = 0;
		if (zColor > 255) {
			z = 255;
		} else if (zColor < 0) {
			z = 0;
		} else {
			z = (int) zColor;
		}
		Color c = Color.getHSBColor(z / 255.0f, .9f, .9f);
		return c;
	}

	// unit testing
	public static void main(String[] args) {
		Convection c = new Convection();
		c.plot();
	}
}
