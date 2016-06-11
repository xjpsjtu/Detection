package com.xjp.AlgPrj;

public class Point implements Comparable{
	public double x;
	int sign; //sign=0代表起始端点，sign=1代表终点
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Point point = (Point)o;
		return (int)(this.x - point.x);
	}
}
