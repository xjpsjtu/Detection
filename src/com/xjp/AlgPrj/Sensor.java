package com.xjp.AlgPrj;

public class Sensor implements Comparable{
	public double x;
	public double y;
	public double range;
	public int initial;
	public double shift;
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public double getRange(){
		return range;
	}
	//�õ���߽�
	public double getLeft(){
		x = x + shift;
		return x - range;
	}
	
	//�õ��ұ߽�
	public double getRight(){
		x = x + shift;
		return x + range;
	}
	//�õ���������ƫ��
	public double getShift(){
		return shift;
	}
	
	//�ж�һ�����Ƿ������ķ�Χ��
	public boolean isInRange(double x, double y){
		double dis = (this.x - x) * (this.x - x) + (this.y - y) * (this.y - y);
		if(dis <= range * range)return true;
		else return false;
	}
	//�Դ������ĺ���������
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Sensor sensor = (Sensor)o;
		return (int)(this.x - sensor.x);
	}
	
}
