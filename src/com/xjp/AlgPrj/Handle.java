package com.xjp.AlgPrj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Handle {
	public List<Sensor> sensors;
	public double L;
	public double r;
	
	//设置区域总长度
	public void setLength(double L){
		this.L = L;
	}
	//设置传感器的半径
	public void setRange(double r){
		this.r = r;
	}
	//获取当前传感器的数量
	public int getNumOfSensor(){
		return sensors.size();
	}
	//创建传感器
	public void createSensor(){
		int n = (int)(L/r) + 5;
		sensors = new ArrayList<Sensor>();
		for(int i = 0; i < n; i++){
			Sensor sensor = new Sensor();
			double x = Math.random() * (2 * r + L)  - r;
			double y = Math.random() * (5 * r);
			sensor = new Sensor();
			sensor.x = x;
			sensor.y = y;
			sensor.range = r;
			sensors.add(sensor);
		}
	}
	//输出传感器
	public void print(){
		for(int i = 0; i < sensors.size(); i++){
			Sensor sensor = sensors.get(i);
			System.out.println("=============第   " + (i+1) +" 个传感器的信息=============");
			System.out.println("原点为：   (" + sensor.x + "," + sensor.y + ")");
			System.out.println("半径为：   " + sensor.range);
		}
	}
	//对传感器排序
	public void sortSensor(){
		Collections.sort(this.sensors);
	}
	//寻找Gap,需要传感器按照序号升序!!
	public List<Gap> findGap(){
		List<Gap> gaps = new ArrayList<Gap>();
		for(int i = 0; i < sensors.size() - 1; i++){
			Sensor sensorLeft = sensors.get(i);
			Sensor sensorRight = sensors.get(i + 1);
			if(Math.abs(sensorLeft.getX() - sensorRight.getX()) > 2 * r){
				Gap gap = new Gap();
				gap.left = sensorLeft.getRight();
				gap.right = sensorLeft.getLeft();
				gaps.add(gap);
			}
		}
		return gaps;
	}
	//寻找overlap
	public List<Overlap> findOverLap(){
		int n = sensors.size();
		List<Overlap> overlaps = new ArrayList<Overlap>();
		List<Point> points = new ArrayList<Point>();
		for(int i = 0; i < sensors.size(); i++){
			Sensor sensor = sensors.get(i);
			double left = sensor.getLeft();
			double right = sensor.getRight();
			Point pleft = new Point();
			pleft.x = left;
			pleft.sign = 0;
			Point pright = new Point();
			pright.x = right;
			pright.sign = 1;
			points.add(pleft);
			points.add(pright);
		}
		Collections.sort(points);
		for(int i = 0; i < points.size() - 1; i++){
			Point left = points.get(i);
			Point right = points.get(i+1);
			if(left.sign == 1 && right.sign == 0)continue;
			else{
				Overlap overlap = new Overlap();
				overlap.left = left.x;
				overlap.right = right.x;
				overlaps.add(overlap);
			}
		}
		return overlaps;
	}
	//移动传感器的位置使其能够覆盖区域
	public void WeakDetection(){
		
	}
	
	public static void main(String[] args){
		Handle handle = new Handle();
		handle.L = 20;
		handle.r = 5;
		handle.createSensor();
		handle.print();
		Collections.sort(handle.sensors);
		handle.print();
	}
}
