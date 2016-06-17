package com.xjp.AlgPrj;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Handle {
	public List<Sensor> sensors;
	public List<Point> points;
	public int[] d;  //the shift array of the sensors
	public double L;
	public double r;
	
	//set the total length of the boundary
	public void setLength(double L){
		this.L = L;
	}
	//set the sensing range of each sensor
	public void setRange(double r){
		this.r = r;
	}
	//get the total number of sensors
	public int getNumOfSensor(){
		return sensors.size();
	}
	//create several sensors
	public void createSensor(){
		int n = (int)(L/r)+2;
		sensors = new ArrayList<Sensor>();
		for(int i = 0; i < n; i++){
			Sensor sensor = new Sensor();
			double x = Math.random() * (L - 2 * r) +  r;
			double y = Math.random() * 800;
			sensor = new Sensor();
			sensor.x = x;
			sensor.y = y;
			sensor.range = r;
			sensor.shift = 0;
			sensors.add(sensor);
		}
	}
	//print the information of each sensor
	public void print(){
		for(int i = 0; i < sensors.size(); i++){
			Sensor sensor = sensors.get(i);
			System.out.println("=============sensor  " + (i+1) +" =============");
			System.out.println("location:  (" + sensor.x + "," + sensor.y + ")");
			System.out.println("sensing range:   " + sensor.range);
		}
	}
	public void draw(){
		JFrame frame = new JFrame("Sensor");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(null);
		SensorPanel spanel = new SensorPanel();
		spanel.setBounds(0, 0, 1500, 1500);
		spanel.addSensor(sensors);
		spanel.addPoint(getPoints());
		frame.add(spanel);
		frame.setSize(1500, 1500);
		frame.setVisible(true);
	}
	//sort all the sensors
	public void sortSensor(){
		Collections.sort(this.sensors);
	}
	public void shiftSensor(){
		for(int i = 0; i < sensors.size(); i++){
			int shift = d[i];
			sensors.get(i).shift = shift;
			sensors.get(i).x += shift;
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
	//find all gaps!!
	public List<Gap> findGap(){
		List<Gap> gaps = new ArrayList<Gap>();
		List<Point> points = new ArrayList<Point>();
		points = getPoints();
		sortSensor();
//		for(int i = 1; i < points.size() - 1; i++){
//			Point left = points.get(i);
//			Point right = points.get(i+1);
//			if(left.sign == 1 && right.sign == 0){
//				Gap gap = new Gap();
//				gap.left = left.x;
//				gap.right = right.x;
//				gap.size = gap.right - gap.left;
//				gaps.add(gap);
//			}
//		}
		for(int i = 0; i < sensors.size() - 1; i++){
			Sensor left_sensor = sensors.get(i);
			Sensor right_sensor = sensors.get(i+1);
			if(!left_sensor.isOverlap(right_sensor)){
				Gap gap = new Gap();
				gap.left = left_sensor.getRight();
				gap.right = right_sensor.getLeft();
				gap.size = Math.abs(gap.right - gap.left);
				gaps.add(gap);
			}
		}
		return gaps;
	}
	//find all overlaps!!
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
		handle.L = 1200;
		handle.r = 60;
		handle.createSensor();
		System.out.println(handle.sensors.size());
		handle.draw();
//		List<Point> points = handle.getPoints();
//		System.out.println(points.size());
//		for(int i = 0; i < points.size(); i++){
//			System.out.println(points.get(i).x);
//		}
		List<Gap> gaps = handle.findGap();
		for(int i = 0; i < gaps.size(); i++){
			System.out.println(gaps.get(i).left + " , " + gaps.get(i).right);
		}
		System.out.println(gaps.size());
	}
}
class SensorPanel extends JPanel{
	List<Sensor> sensors;
	List<Point> points;
	public void addPoint(List<Point> points){
		this.points = points;
	}
	public void addSensor(List<Sensor> sensors){
		this.sensors = sensors;
	}
	public void paint(Graphics g){
		g.drawLine(0, 850, 1500, 850);
		for(int i = 0; i < sensors.size(); i++){
			Sensor sensor = sensors.get(i);
			int x = (int)sensor.getX();
			int y = (int)sensor.getY();
			int r = (int)sensor.getRange();
			g.drawOval(x - r, y - r, 2 * r, 2 * r);
//			g.drawLine(x - r, 850, x - r, 0);
//			g.drawLine(x + r, 850, x + r, 0);
		}
		for(int i = 0; i < points.size(); i++){
			Point point = points.get(i);
			int x = (int)point.x;
			if(point.sign == 0){
				g.setColor(Color.black);
				g.drawLine(x, 850, x, 0);
			}else{
				g.setColor(Color.RED);
				g.drawLine(x, 850, x, 0);
			}
			
		}
	}
}