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
	//get the left bound of the sensor
	public double getLeft(){
		return x - range;
	}
	
	//get the right bound of the sensor
	public double getRight(){
		return x + range;
	}
	//Get the shift of this sensor
	public double getShift(){
		return shift;
	}
	
	//test if (x,y) is in the range of the sensor
	public boolean isInRange(double x, double y){
		double dis = (this.x - x) * (this.x - x) + (this.y - y) * (this.y - y);
		if(dis <= range * range)return true;
		else return false;
	}
	public boolean isOverlap(Sensor sensor){
		double x = sensor.getX();
		double y = sensor.getY();
		if(Math.abs(this.x - x) < 2 * range - 0.1)return true;
		else return false;
	}
	//compare the sensors based on their x value
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Sensor sensor = (Sensor)o;
		if(this.x > sensor.x){
			return 1;
		}else{
			return -1;
		}
	}
	
}
