package com.xjp.AlgPrj;

import java.util.ArrayList;
import java.util.List;

public class test {
	List<Sensor> sensors;
	public void create(){
		sensors = new ArrayList<Sensor>();
		for(int i = 0; i < 10; i++){
			Sensor sensor = new Sensor();
			sensor.x = 20;
			sensors.add(sensor);
		}
	}
	public void print(){
		for(int i = 0; i < sensors.size(); i++){
			Sensor sensor = sensors.get(i);
			System.out.println("===========sensor  " + (i+1) +" =============");
			System.out.println(sensor.x);
		}
	}
	public void moveSensor(){
		for(int i = 0; i < sensors.size(); i++){
			Sensor sensor = sensors.get(i);
			sensor.x += 20;
		}
	}
	
	public static void main(String[] args){
		test t = new test();
		t.create();
		t.print();
		t.moveSensor();
		t.print();
	}
}
