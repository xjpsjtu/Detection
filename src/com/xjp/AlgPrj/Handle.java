package com.xjp.AlgPrj;

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	//public double[] d;  //the shift array of the sensors
	public List<Gap> gaps;
	public List<Overlap> overlaps;
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
		int n = (int)(L/(2 * r)) + 5;
		sensors = new ArrayList<Sensor>();
		for(int i = 0; i < n; i++){
			Sensor sensor = new Sensor();
			double x = Math.random() * (L - 2 * r) +  r;
//			double y = Math.random() * 300 + 300;
			double y = 300;
			sensor = new Sensor();
			sensor.x = x;
			sensor.y = y;
			sensor.range = r;
			sensor.shift = 0;
			sensors.add(sensor);
		}
	}
	//print the information of each sensor
	public void print1(){
//		for(int i = 0; i < sensors.size(); i++){
//			Sensor sensor = sensors.get(i);
//			System.out.println("=============sensor  " + (i+1) +" =============");
//			System.out.println("location:  (" + sensor.x + "," + sensor.y + ")");
//			System.out.println("sensing range:   " + sensor.range);
//		}
		FileWriter fileWriter;
		try{
			File f = new File("D:\\before.txt");
			fileWriter = new FileWriter(f);
			for(int i = 0; i < sensors.size(); i++){
				Sensor sensor = sensors.get(i);
				fileWriter.write(sensor.getX() + " " + sensor.getY() + "\r\n");
			}
			fileWriter.flush();
			fileWriter.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void print2(){
//		for(int i = 0; i < sensors.size(); i++){
//			Sensor sensor = sensors.get(i);
//			System.out.println("=============sensor  " + (i+1) +" =============");
//			System.out.println("location:  (" + sensor.x + "," + sensor.y + ")");
//			System.out.println("sensing range:   " + sensor.range);
//		}
		FileWriter fileWriter;
		try{
			File f = new File("D:\\after.txt");
			fileWriter = new FileWriter(f);
			for(int i = 0; i < sensors.size(); i++){
				Sensor sensor = sensors.get(i);
				fileWriter.write(sensor.getX() + " " + sensor.getY() + "\r\n");
			}
			fileWriter.flush();
			fileWriter.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	public void draw(){
		JFrame frame = new JFrame("Sensor");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(null);
		SensorPanel spanel = new SensorPanel();
		spanel.setBounds(0, 0, 800, 800);
		spanel.addSensor(sensors);
//		System.out.println("The first sensor is: " + sensors.get(0).getX());
		findGap();
		findOverLap();
		spanel.addGaps(gaps);
		spanel.addOverlaps(overlaps);
		spanel.addPoint(getPoints());
		frame.add(spanel);
		frame.setSize(800, 800);
		frame.setVisible(true);
	}
	//sort all the sensors
	public void sortSensor(){
		Collections.sort(this.sensors);
	}
//	public void shiftSensor(){
//		for(int i = 0; i < sensors.size(); i++){
//			double shift = d[i];
//			sensors.get(i).shift = shift;
//			sensors.get(i).x += shift;
//		}
//	}
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
	public void findGap(){
		gaps = new ArrayList<Gap>();
//		List<Point> points = new ArrayList<Point>();
//		points = getPoints();
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
		Sensor sensor = sensors.get(0);
		if(sensor.getLeft() > 0){
			Gap gap = new Gap();
			gap.left = 0;
			gap.right = sensor.getLeft();
			gap.size = Math.abs(gap.right - gap.left);
			gaps.add(gap);
		}
		for(int i = 0; i < sensors.size() - 1; i++){
			Sensor left_sensor = sensors.get(i);
			Sensor right_sensor = sensors.get(i+1);
			double left_x = left_sensor.getX();
			double right_x = right_sensor.getX();
			if(Math.abs(left_x - right_x) > 2 * r){
				Gap gap = new Gap();
				gap.left = left_sensor.getRight();
				gap.right = right_sensor.getLeft();
//				System.out.println("Left of the new gap is " + gap.left);
//				System.out.println("Right of the new gap is " + gap.right);
				gap.size = gap.right - gap.left;
				gaps.add(gap);
			}
		}
		
		Sensor sensor2 = sensors.get(sensors.size() - 1);
		if(sensor2.getRight() < L){
			Gap gap = new Gap();
			gap.left = sensor2.getRight();
			gap.right = L;
			gap.size = Math.abs(gap.right - gap.left);
			gaps.add(gap);
		}
	}
	//find all overlaps!!
	public void findOverLap(){
		overlaps = new ArrayList<Overlap>();
//		List<Point> points = new ArrayList<Point>();
//		points = getPoints();
//		for(int i = 1; i < points.size() - 1; i++){
//			Point left = points.get(i);
//			Point right = points.get(i+1);
//			if(left.sign == 1 && right.sign == 0){
//				i++;
//				continue;
//			}
//			else{
//				Overlap overlap = new Overlap();
//				overlap.left = left.x;
//				overlap.right = right.x;
//				overlaps.add(overlap);
//			}
//		}
//		for(int i = 0; i < overlaps.size(); i++){
//			Overlap overlap = overlaps.get(i);
//			if(overlap.left <=0 ){
//				Overlap ov = new Overlap();
//				ov.left = overlap.left;
//				ov.right = 0;
//				overlaps.add(ov);
//				overlap.left = 0;
//			}
//		}
//		for(int i = 0; i < overlaps.size(); i++){
//			Overlap overlap = overlaps.get(i);
//			if(overlap.right >= L){
//				Overlap ov = new Overlap();
//				ov.right = overlap.right;
//				ov.left = L;
//				overlaps.add(ov);
//				overlap.right = L;
//			}
//		}
//		for(int i = 0; i < overlaps.size(); i++){
//			Overlap overlap = overlaps.get(i);
//			double left = overlap.getLeft();
//			double right = overlap.getRight();
//			overlap.size = right - left;
//			for(int j = 0; j < sensors.size(); j++){
//				Sensor sensor = new Sensor();
//				double sleft = sensor.getLeft();
//				double sright = sensor.getRight();
//				if(left >= sleft && left <= sright){
//					overlap.addSensor(sensor);
//				}
//			}
//		}
		sortSensor();
		for(int i = 0; i < sensors.size() - 1; i++){
			Sensor left_sensor = sensors.get(i);
			Sensor right_sensor = sensors.get(i + 1);
			if(left_sensor.isOverlap(right_sensor)){
//				System.out.println("Find one!");
//				System.out.println("Left sensor of overlap is " + left_sensor.getX());
//				System.out.println("Right sensor of overlap is " + right_sensor.getX());
				Overlap overlap = new Overlap();
				overlap.left = right_sensor.getLeft();
				overlap.right = left_sensor.getRight();
				overlap.size = Math.abs(overlap.right - overlap.left);
				overlap.left_sensor = left_sensor;
				overlap.right_sensor = right_sensor;
				overlaps.add(overlap);
			}
		}
	}
	public int calCost(Overlap overlap, Gap gap, int flag){
		int sum = 0;
//		List<Sensor> sensors = new ArrayList<Sensor>();
//		sensors = overlap.getCoverSensor();
//		for(int i = 0; i < sensors.size(); i++){
//			Sensor sensor = sensors.get(i);
//			if(sensor.getRight() <= gap.left || sensor.getLeft() >= gap.right){
//				sum++;
//			}
//		}
		//overlap is to the left of the gap
		if(flag == 0){
			sum = 0;
			Sensor sensor = overlap.right_sensor;
			sortSensor();
			double start = sensor.x;
			double end = gap.left - r;
			int negative = 0;
			int positive = 0;
			for(int i = 0; i < sensors.size(); i++){
				Sensor now_sensor = sensors.get(i);
				if(now_sensor.x >= start && now_sensor.x <= end){
					if(now_sensor.shift > 0){
						positive++;
					}else{
						negative++;
					}
				}else{
					continue;
				}
			}
			sum = Math.abs(positive - negative);
		}
		//overlap is to the right of the gap
		else{
			sum = 0;
			Sensor sensor = overlap.left_sensor;
			sortSensor();
			double start = gap.right + r;
			double end = sensor.x;
			for(int i = 0; i < sensors.size(); i++){
				Sensor now_sensor = sensors.get(i);
				if(now_sensor.x >= start && now_sensor.x <= end){
					sum++;
				}
			}
		}
		return sum;
	}
	
	public boolean WeakDetection(){
		int n = sensors.size();
//		int[] d = new int[n];  //the array of sensor shifts
//		for(int i = 0; i < n; i++){
//			d[i] = 0;
//		}
		sortSensor();
		findOverLap();
		int k = overlaps.size();
		findGap();
		int l = gaps.size();
		for(int i = 0; i < l; i++){
			Gap gap = gaps.get(i);
			System.out.println("We try a new gap, its size is now: " + gap.size);
			while(gap.getSize() > 0){
				findOverLap();
				double gleft = gap.getLeft();
				double gright = gap.getRight();
				System.out.println("Now the gap is in: (" + gleft + "  ,  " + gright + ")");
				int oleft = -1, oright = k;
				for(int j = 0; j < overlaps.size(); j++){
					Overlap overlap = overlaps.get(j);
					double right = overlap.getRight();
					if(right <= gleft){
						oleft = j;
					}else{
//						System.out.println("The left break at: " + oleft);
						break;
					}
				}
				for(int j = overlaps.size() - 1; j > -1; j--){
					Overlap overlap = overlaps.get(j);
					double left = overlap.getLeft();
					if(left >= gright){
						oright = j;
					}else{
//						System.out.println("The right break at: " + oright);
						break;
					}
				}
				int left_cost, right_cost;
				Overlap overlap_left, overlap_right;
				if(oleft != -1){
					overlap_left = overlaps.get(oleft);
//					System.out.println("The left overlap is: (" + overlap_left.getLeft() + "," + overlap_left.getRight() + ")");
					left_cost = calCost(overlap_left, gap, 0);
//					System.out.println("The left cost is: " + left_cost);
				}else{
					left_cost = Integer.MAX_VALUE;
					overlap_left = null;
				}
				if(oright != k){
					overlap_right = overlaps.get(oright);
//					System.out.println("The right overlap is: (" + overlap_right.getLeft() + "," + overlap_right.getRight() + ")");
					right_cost = calCost(overlap_right, gap, 1);
					System.out.println("The right cost is: " + right_cost);
				}else{
					right_cost = Integer.MAX_VALUE;
					overlap_right = null;
				}
				if(oleft == -1 && oright == k){
					System.out.println("No solution");
					return false;
				}
				double c;
				if(left_cost <= right_cost){
					if(overlap_left.getSize() < gap.getSize()){
						c = overlap_left.getSize();
					}else{
						c = gap.getSize();
					}
					System.out.print("Gap size decrease from " + gap.getSize());
					gap.size -= c;
					System.out.println("  to  " + gap.size);
					overlap_left.size -= c;
					overlap_left.left += c;
//					for(int j = 0; j < sensors.size(); j++){
//						Sensor sensor = sensors.get(j);
//						double sensor_left = sensor.getLeft();
//						double sensor_right = sensor.getRight();
//						if(sensor_left >= overlap_left.getRight() && sensor_right <= gap.getLeft()){
//							sensor.x += c;
//							sensor.shift = c;
//						}
//					}
					Sensor sensor = overlap_left.right_sensor;
					sortSensor();
					double start = sensor.x;
					double end = gap.left - r;
					System.out.println("Left start and end: " + start + "," + end);
					for(int t = 0; t < sensors.size(); t++){
						Sensor now_sensor = sensors.get(t);
						if(now_sensor.x >= start && now_sensor.x <= end){
//							System.out.println("MOVE ONE");
							System.out.println("A left sensor moved !!!");
							now_sensor.x += c;
							now_sensor.shift = c;
						}
					}
					gap.left += c;
					System.out.println("The gap's left is put right by: " + c);
				}else{
					if(overlap_right.getSize() < gap.getSize()){
						c = overlap_right.getSize();
					}else{
						c = gap.getSize();
					}
					System.out.print("Gap size decrease from " + gap.getSize());
					gap.size -= c;
					System.out.println("  to  " + gap.size);
					overlap_right.size -= c;
					overlap_right.right -= c;
//					for(int j = 0; j < sensors.size(); j++){
//						Sensor sensor = sensors.get(j);
//						double sensor_left = sensor.getLeft();
//						double sensor_right = sensor.getRight();
//						if(sensor_left >= gap.right && sensor_right <= overlap_right.getLeft()){
//							d[j] += c;
//						}
//					}
					Sensor sensor = overlap_right.left_sensor;
					sortSensor();
					double start = gap.right + r;
					double end = sensor.x;
					System.out.println("Right start and end: " + start + "," + end);
					for(int t = 0; t < sensors.size(); t++){
//						System.out.println("MOVE ONE");
						Sensor now_sensor = sensors.get(t);
						if(now_sensor.x >= start && now_sensor.x <= end){
							System.out.println("A right sensor moved !!!");
							now_sensor.x -= c;
							now_sensor.shift = -c;
						}
					}
					for(int t = 1; t < gaps.size(); t++){
						Gap now_gap = gaps.get(t);
						double left = now_gap.left;
						double right = now_gap.right;
						if(left >= gleft && right <= sensor.getLeft()){
							now_gap.left -= c;
							now_gap.right -= c;
						}
					}
					gap.right -= c;
					System.out.println("The gap's right is put left by: " + c);
				}
				if(gap.size == 0){
					System.out.println("Eliminate a gap");
				}
			}
		}
		return true;
//		shiftSensor();
	}
	
	public void moveSensor(){
		for(int i = 0; i < sensors.size(); i++){
			sensors.get(i).x += 60;
		}
	}
	public static void main(String[] args){
		Handle handle = new Handle();
		handle.L = 800;
		handle.r = 5;
		handle.createSensor();
		handle.sortSensor();
		handle.findOverLap();
		List<Overlap> overlaps = handle.overlaps;
		for(int i = 0; i < overlaps.size(); i++){
			System.out.println(overlaps.get(i).left + "  ,  " + overlaps.get(i).right);
		}
		handle.findGap();
		List<Gap> gaps = handle.gaps;
		System.out.println("The number of gaps is : " +gaps.size());
		for(int i = 0; i < gaps.size(); i++){
			System.out.println("Gap: " + gaps.get(i).left + " , " + gaps.get(i).right);
		}
		handle.draw();
//		handle.print1();
		handle.WeakDetection();
		handle.sortSensor();
		handle.findGap();
		handle.findOverLap();
		handle.WeakDetection();
		handle.WeakDetection();
//		handle.moveSensor();
//		handle.print2();
		handle.draw();
//		List<Point> points = handle.getPoints();
//		System.out.println(points.size());
//		for(int i = 0; i < points.size(); i++){
//			System.out.println(points.get(i).x);
//		}
//		handle.print();
		
		System.out.println("The number of overlaps is : " +overlaps.size());
		
	}
}
class SensorPanel extends JPanel{
	List<Sensor> sensors;
	List<Point> points;
	List<Gap> gaps;
	List<Overlap> overlaps;
	public void addPoint(List<Point> points){
		this.points = points;
	}
	public void addSensor(List<Sensor> sensors){
		this.sensors = sensors;
//		System.out.println("Add a sensor: " + sensors.get(0).getX());
	}
	public void addGaps(List<Gap> gaps){
		this.gaps = gaps;
	}
	public void addOverlaps(List<Overlap> overlaps){
		this.overlaps = overlaps;
	}
	public void paint(Graphics g){
		g.drawLine(0, 650, 800, 650);
//		System.out.println("Draw a sensor: " + sensors.get(0).getX());
		for(int i = 0; i < sensors.size(); i++){
			Sensor sensor = sensors.get(i);
			int x = (int)sensor.getX();
			int y = (int)sensor.getY();
			int r = (int)sensor.getRange();
			g.setColor(Color.red);
			g.fillOval(x - r, y - r, 2 * r, 2 * r);
//			g.drawLine(x - r, 850, x - r, 0);
//			g.drawLine(x + r, 850, x + r, 0);
		}
//		for(int i = 0; i < points.size(); i++){
//			Point point = points.get(i);
//			int x = (int)point.x;
//			if(point.sign == 0){
//				g.setColor(Color.black);
//				g.drawLine(x, 650, x, 0);
//			}else{
//				g.setColor(Color.RED);
//				g.drawLine(x, 650, x, 0);
//			}
//			
//		}
//		for(int i = 0; i < gaps.size(); i++){
//			Gap gap = gaps.get(i);
//			int left = (int)gap.getLeft();
//			int right = (int)gap.getRight();
//			g.setColor(Color.black);
//			g.drawLine(left, 650, left, 0);
//			g.setColor(Color.RED);
//			g.drawLine(right, 650, right, 0);
//		}
//		for(int i = 0; i < overlaps.size(); i++){
//			Overlap overlap = overlaps.get(i);
//			int left = (int)overlap.getLeft();
//			int right = (int)overlap.getRight();
//			g.setColor(Color.black);
//			g.drawLine(left, 650, left, 0);
//			g.setColor(Color.RED);
//			g.drawLine(right, 650, right, 0);
//		}
	}
}