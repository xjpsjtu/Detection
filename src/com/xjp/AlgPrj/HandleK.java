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

public class HandleK {
	public List<Sensor> sensors;
	public List<GapK> gaps;
	public List<OverlapK> overlaps;
	public double L;
	public double r;
	public int k;
	
	public void createSensor(){
		int n = (int)(L/(2 * r)) * (k + 2);
		sensors = new ArrayList<Sensor>();
		Sensor sensor = new Sensor();
		sensor.x = -r;
		sensor.y = 300;
		sensor.range = r;
		sensor.shift = 0;
		sensors.add(sensor);
		for(int i = 1; i < n - 1; i++){
			sensor = new Sensor();
			double x = Math.random() * (L - 2 * r) +  r;
//			double y = Math.random() * 300 + 300;
			double y = 300;
			sensor.x = x;
			sensor.y = y;
			sensor.range = r;
			sensor.shift = 0;
			sensors.add(sensor);
		}
		sensor = new Sensor();
		sensor.x = L + r;
		sensor.y = 300;
		sensor.range = r;
		sensor.shift = 0;
		sensors.add(sensor);
	}
	public void print1(){
		FileWriter fileWriter;
		try{
			File f = new File("D:\\beforek.txt");
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
		FileWriter fileWriter;
		try{
			File f = new File("D:\\afterk.txt");
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
	public void sortSensor(){
		Collections.sort(this.sensors);
	}
	public void findGap(){
		gaps = new ArrayList<GapK>();
		sortSensor();
		for(int i = 0; i < sensors.size(); i++){
			Sensor sensor = sensors.get(i);
			for(int j = i + 1; j < sensors.size(); j++){
				Sensor sensor2 = sensors.get(j);
				double x = sensor.getX();
				double y = sensor2.getX();
				if(Math.abs(x - y) <= 2 * r)continue;
				else{
					GapK gap = new GapK();
					gap.left = sensor.getRight();
					gap.right = sensor2.getLeft();
					gap.size = Math.abs(gap.right - gap.left);
					for(int t = i; t <= j; t++){
						Sensor sensor3 = sensors.get(t);
						gaps.add(gap);
					}
				}
			}
		}
	}
	public void findOverLap(){
		overlaps = new ArrayList<OverlapK>();
		sortSensor();
		for(int i = 0; i < sensors.size() - k; i++){
			Sensor sensor = sensors.get(i);
			Sensor sensor2 = sensors.get(i + k);
			double x = sensor.getX();
			double y = sensor2.getX();
			if(Math.abs(x - y) <= 2 * r){
				OverlapK overlap = new OverlapK();
				overlap.left = sensor2.getLeft();
				overlap.right = sensor.getRight();
				overlap.size = Math.abs(overlap.right - overlap.left);
				overlap.coverSensors = new ArrayList<Sensor>();
				for(int j = i; j <= i + k; j++){
					overlap.addSensor(sensors.get(j));
				}
			}else{
				continue;
			}
		}
	}
	public int calCost(Overlap overlap, Gap gap, int flag){
		int sum = 0;
//		overlap is to the left of the gap
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
	public boolean weakdectionk(){
		return false;
	}
}
