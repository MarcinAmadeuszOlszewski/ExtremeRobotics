package com.mo.extremerobotics.trendlinelib;

public interface TrendLine {
	public void setValues(double[] y, double[] x); // y ~ f(x)

	public double predict(double x); // get a predicted y for a given x
}
