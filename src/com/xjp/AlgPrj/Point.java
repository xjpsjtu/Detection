package com.xjp.AlgPrj;

public class Point implements Comparable{
	public double x;
	int sign; //sign=0������ʼ�˵㣬sign=1�����յ�
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Point point = (Point)o;
		return (int)(this.x - point.x);
	}
}