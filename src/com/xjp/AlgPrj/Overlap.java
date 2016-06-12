package com.xjp.AlgPrj;

import java.util.List;

public class Overlap {
	public double left;
	public double right;
	public double size; 
	public List<Sensor> coverSensors;
	
	public double getLeft(){
		return left;
	}
	public double getRight(){
		return right;
	}
	public double getSize(){
		return size;
	}
	public int getNum(){
		return coverSensors.size();
	}
	public void addSensor(Sensor sensor){
		coverSensors.add(sensor);
	}
	public List<Sensor> getCoverSensor(){
		return coverSensors;
	}
}
