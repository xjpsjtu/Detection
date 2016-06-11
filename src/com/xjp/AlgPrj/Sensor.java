package com.xjp.AlgPrj;

public class Sensor implements Comparable{
	public double x;
	public double y;
	public double range;
	public int initial;
	
	public double getX(){
		return x;
	}
	public double getY(){
		return y;
	}
	public double getRange(){
		return range;
	}
	//得到左边界
	public double getLeft(){
		return x - range;
	}
	
	//得到右边界
	public double getRight(){
		return x + range;
	}
	
	//判断一个点是否在它的范围内
	public boolean isInRange(double x, double y){
		double dis = (this.x - x) * (this.x - x) + (this.y - y) * (this.y - y);
		if(dis <= range * range)return true;
		else return false;
	}
	//对传感器的横坐标排序
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Sensor sensor = (Sensor)o;
		return (int)(this.x - sensor.x);
	}
	
}
