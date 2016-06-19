package com.xjp.AlgPrj;

import java.util.List;

public class OverlapK {
	public double left;
	public double right;
	public double size; 
	public List<Sensor> coverSensors;
	public int num;
	public int l;
	
	public double getLeft(){
		return left;
	}
	public double getRight(){
		return right;
	}
	public double getSize(){
		return size;
	}
	public void addSensor(Sensor sensor){
		coverSensors.add(sensor);
	}
	public List<Sensor> getCoverSensor(){
		return coverSensors;
	}
	public int getNum(){
		return num;
	}
	public int getL(){
		return l;
	}
}
