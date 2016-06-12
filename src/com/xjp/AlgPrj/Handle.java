package com.xjp.AlgPrj;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Handle {
	public List<Sensor> sensors;
	public List<Point> points;
	public int[] d;
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
			sensor.shift = 0;
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
	public void shiftSensor(){
		for(int i = 0; i < sensors.size(); i++){
			int shift = d[i];
			sensors.get(i).shift = shift;
		}
	}
	public List<Point> getPoints(){
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
		return points;
	}
	//寻找Gap,需要传感器按照序号升序!!
	public List<Gap> findGap(){
		List<Gap> gaps = new ArrayList<Gap>();
		List<Point> points = new ArrayList<Point>();
		points = getPoints();
		for(int i = 1; i < points.size(); i++){
			Point left = points.get(i);
			Point right = points.get(i+1);
			if(left.sign == 1 && right.sign == 0){
				Gap gap = new Gap();
				gap.left = left.x;
				gap.right = right.x;
				gap.size = gap.right - gap.left;
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
		points = getPoints();
		for(int i = 1; i < points.size() - 1; i++){
			Point left = points.get(i);
			Point right = points.get(i+1);
			if(left.sign == 1 && right.sign == 0){
				i++;
				continue;
			}
			else{
				Overlap overlap = new Overlap();
				overlap.left = left.x;
				overlap.right = right.x;
				overlaps.add(overlap);
			}
		}
		for(int i = 0; i < overlaps.size(); i++){
			Overlap overlap = overlaps.get(i);
			if(overlap.left <=0 ){
				Overlap ov = new Overlap();
				ov.left = overlap.left;
				ov.right = 0;
				overlaps.add(ov);
				overlap.left = 0;
			}
		}
		for(int i = 0; i < overlaps.size(); i++){
			Overlap overlap = overlaps.get(i);
			if(overlap.right >= L){
				Overlap ov = new Overlap();
				ov.right = overlap.right;
				ov.left = L;
				overlaps.add(ov);
				overlap.right = L;
			}
		}
		for(int i = 0; i < overlaps.size(); i++){
			Overlap overlap = overlaps.get(i);
			double left = overlap.getLeft();
			double right = overlap.getRight();
			overlap.size = right - left;
			for(int j = 0; j < sensors.size(); j++){
				Sensor sensor = new Sensor();
				double sleft = sensor.getLeft();
				double sright = sensor.getRight();
				if(left >= sleft && left <= sright){
					overlap.addSensor(sensor);
				}
			}
		}
		return overlaps;
	}
	public int calCost(Overlap overlap, Gap gap){
		int sum = 0;
		List<Sensor> sensors = new ArrayList<Sensor>();
		sensors = overlap.getCoverSensor();
		for(int i = 0; i < sensors.size(); i++){
			Sensor sensor = sensors.get(i);
			if(sensor.getRight() <= gap.left || sensor.getLeft() >= gap.right){
				sum++;
			}
		}
		return sum;
	}
	//移动传感器的位置使其能够覆盖区域
	public void WeakDetection(){
		int n = sensors.size();
		int[] d = new int[n];  //the array of sensor shifts
		for(int i = 0; i < n; i++){
			d[i] = 0;
		}
		sortSensor();
		List<Overlap> overlaps = new ArrayList<Overlap>();
		overlaps = findOverLap();
		int k = overlaps.size();
		List<Gap> gaps = new ArrayList<Gap>();
		gaps = findGap();
		int l = gaps.size();
		for(int i = 0; i < l; i++){
			Gap gap = gaps.get(i);
			while(gap.getSize() != 0){
				double gleft = gap.getLeft();
				double gright = gap.getRight();
				int oleft = -1, oright = -1;
				for(int j = 0; j < k; j++){
					Overlap overlap = overlaps.get(j);
					double right = overlap.getRight();
					if(right > gleft){
						oleft = j - 1;
						break;
					}
				}
				for(int j = k - 1; j > -1; j++){
					Overlap overlap = overlaps.get(j);
					double left = overlap.getLeft();
					if(left < gright){
						oright = j + 1;
						break;
					}
				}
				Overlap overlap_left = overlaps.get(oleft);
				Overlap overlap_right = overlaps.get(oright);
				int left_cost = calCost(overlap_left, gap);
				int right_cost = calCost(overlap_right, gap);
				double c;
				if(left_cost <= right_cost){
					if(overlap_left.getSize() < gap.getSize()){
						c = overlap_left.getSize();
					}else{
						c = gap.getSize();
					}
					gap.size -= c;
					overlap_left.size -= c;
					for(int j = 0; j < sensors.size(); j++){
						Sensor sensor = sensors.get(j);
						double sensor_left = sensor.getLeft();
						double sensor_right = sensor.getRight();
						if(sensor_left >= overlap_left.getRight() && sensor_right <= gap.getLeft()){
							d[j] += c;
						}
					}
				}else{
					if(overlap_right.getSize() < gap.getSize()){
						c = overlap_right.getSize();
					}else{
						c = gap.getSize();
					}
					gap.size -= c;
					overlap_right.size -= c;
					for(int j = 0; j < sensors.size(); j++){
						Sensor sensor = sensors.get(j);
						double sensor_left = sensor.getLeft();
						double sensor_right = sensor.getRight();
						if(sensor_left >= gap.right && sensor_right <= overlap_right.getLeft()){
							d[j] += c;
						}
					}
				}

			}
		}
		shiftSensor();
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
