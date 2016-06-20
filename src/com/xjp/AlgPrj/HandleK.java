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
	public int K;
	public double ratio;
	
	public void createSensor(){
		int n = (int)(1.5 * L/(2 * r)) * (K + 2);
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
			x = Math.rint(x);
			double y = Math.random() * 300 + 300;
			if(i%4==0)y=400;
			else if(i%4==1)y=300;
			else if(i%4==2) y=200;
			else y=100;
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
	public void draw(){
		JFrame frame = new JFrame("Sensor");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(null);
		SensorPanelK spanel = new SensorPanelK();
		spanel.setBounds(0, 0, 800, 800);
		spanel.addSensor(sensors);
		frame.add(spanel);
		frame.setSize(800, 800);
		frame.setVisible(true);
	}
	public void sortSensor(){
		Collections.sort(this.sensors);
	}
	public int isIntersect(Sensor sensor1, Sensor sensor2){
		double x = sensor1.getRight();
		double y = sensor2.getLeft();
		if(x < y)return 1;
		else if(x > y)return -1;
		else return 0;
		
	}
	public void findGap(){
		gaps = new ArrayList<GapK>();
		sortSensor();
		for(int k = 1; k <= K; k++){
			for(int i = 0; i < sensors.size() - k; i++){
				Sensor sensor = sensors.get(i);
				Sensor sensor2 = sensors.get(i + k);
				if(isIntersect(sensor, sensor2) <= 0)continue;
				else if(isIntersect(sensor, sensor2) == 1){
					GapK gap = new GapK();
					gap.num = k;
					gap.left = sensor.getRight();
					gap.right = sensor2.getLeft();
					gap.l = i;
					gap.size = Math.abs(gap.right - gap.left);
					gaps.add(gap);
				}
			}
		}
	}
	public void findOverLap(){
		overlaps = new ArrayList<OverlapK>();
		sortSensor();
		for(int k = 1; k <= K; k++){
			for(int i = 0; i < sensors.size() - k; i++){
				Sensor sensor = sensors.get(i);
				Sensor sensor2 = sensors.get(i + k);
				if(isIntersect(sensor, sensor2) < 0){
					OverlapK overlap = new OverlapK();
					overlap.left = sensor2.getLeft();
					overlap.right = sensor.getRight();
					overlap.size = Math.abs(overlap.right - overlap.left);
					overlap.num = k;
					overlap.l = i;
					overlap.coverSensors = new ArrayList<Sensor>();
					for(int j = i; j <= i + k; j++){
						overlap.addSensor(sensors.get(j));
					}
					overlaps.add(overlap);
				}else{
					continue;
				}
				
			}
		}
	}
	public int calCost(OverlapK overlap, GapK gap, int flag){
		int k = overlap.getNum();
		int ol = overlap.getL();
		int gl = gap.getL();
		if(flag == 0){
			int pos = 0;
			int neg = 0;
			if(ol+k < gl){
				for(int i = ol+k; i <= gl; i += k){
					Sensor sensor = sensors.get(i);
					double shift = sensor.getShift();
					if(shift < 0)neg++;
					else if(shift > 0)pos++;
					return pos-neg;
				}
			}else{
				Sensor sensor1 = sensors.get(ol + k);
				Sensor sensor2 = sensors.get(gl);
				double shift1 = sensor1.getShift();
				double shift2 = sensor2.getShift();
				if(shift1 < 0)neg++;
				else if(shift1 > 0)pos++;
				if(shift2 < 0)neg++;
				else if(shift2 > 0)pos++;
				return pos-neg;
			}
		}else if(flag == 1){
			int sum = 0;
			if(ol < gl+k){
				sum = 2;
			}else{
				for(int i = ol; i >= gl + k; i-=k){
					sum++;
				}
			}
			return sum;
		}
		return -1;
	}
	public double calDis(OverlapK overlap, GapK gap, int flag){
		int k = overlap.getNum();
		int ol = overlap.getL();
		double osize = overlap.getSize();
		int gl = gap.getL();
		double gsize = gap.getSize();
		if(flag == 0){
			double min = Double.MAX_VALUE;
			for(int i = ol+k; i <= gl; i+=k){
				Sensor sensor = sensors.get(i);
				if(sensor.shift >= 0)continue;
				double shift = Math.abs(sensor.getShift());
				if(shift <= min)min=shift;
			}
			System.out.println("minimal distance" + Math.min(Math.min(osize, gsize), min) + " is from " + osize + "," + gsize + "min");
			return Math.min(Math.min(osize, gsize), min);
		}
		else if(flag == 1){
			return Math.min(osize, gsize);
		}
		return -1;
	}
	public boolean isCovered(int i, int k){
		if(i+k>sensors.size()){
			Sensor sensor = sensors.get(i);
			double x = sensor.getRight();
			return (x>=L);
		}
		Sensor sensor = sensors.get(i);
		Sensor sensor2 = sensors.get(i + k);
		double x = sensor.getRight();
		double y = sensor2.getLeft();
		if(x < y)return false;
		else return true;
	}
	public GapK gapK(int i, int k){
		findGap();
		System.out.println("in GapK");
		GapK gap = null;
		for(int j = 0; j < gaps.size(); j++){
			gap = gaps.get(j);
			System.out.println("When j is " + j + ", test this one in GapK(), with location " + gap.getLeft() + "," + gap.getRight() + "," + gap.getL() + "," + gap.getNum());
			if(gap.getL() == i && gap.getNum() == k){
				System.out.println("Found one in GapK(), with location " + gap.getLeft() + "," + gap.getRight());
				break;
			}else{
				continue;
			}
		}
		return gap;
	}
	public OverlapK overlapK(int i, int k){
		findOverLap();
		OverlapK overlap = new OverlapK();
		for(int j = 0; j < gaps.size(); j++){
			overlap = overlaps.get(j);
			if(overlap.getL() == i && overlap.getNum() == k){
				break;
			}else{
				continue;
			}
		}
		return overlap;
	}
	public int weakdectionk(){
		sortSensor();
		findGap();
		System.out.println("We have totally " + sensors.size() + " sensors");
		for(int k = 1; k <= K; k++){
			System.out.println("------------We start the layer " + k + "------------");
			for(int i = 0; i < sensors.size() - k; i++){
				System.out.println("Test a new gap");
				while(!isCovered(i,k)){
					findGap();
					findOverLap();
					System.out.println("Focus on a gap begins at " + i + " when k is " + k);
					GapK gap = gapK(i,k);
					System.out.println("This gap's location is " + gap.getLeft() + "," + gap.getRight());
					int gl = gap.getL();
					System.out.println("This gap is from " + gl);
					OverlapK overlap_left = new OverlapK();
					OverlapK overlap_right = new OverlapK();
					int lindex = -1;
					int rindex = -1;
					System.out.println("Now we have "+ overlaps.size() + " overlaps");
					for(int t = overlaps.size() - 1; t >= 0; t--){
//						System.out.println("~~~~~");
						OverlapK overlap = overlaps.get(t);
						if(overlap.getNum() != k){
//							System.out.println("(Left)Not " + k + ", pass~");
							continue;
						}
						int ol = overlap.getL();
						if(ol > gl){
//							System.out.println("Not left, pass~");
							continue;
						}
						else{
							lindex = t;
							System.out.println("--Find an left overlap--");
							break;
						}
					}
					if(lindex == -1){
						System.out.println("Fail at the left");
					}
					for(int t = 0; t < overlaps.size(); t++){
						OverlapK overlap = overlaps.get(t);
						if(overlap.getNum() != k){
//							System.out.println("(Right)Not " + k + ", pass~");
							continue;
						}
						int ol = overlap.getL();
						if(ol < gl){
//							System.out.println("Not right, pass~");
							continue;
						}
						else{
							rindex = t;
							System.out.println("--Find a right overlap--" + ", from" + ol);
							break;
						}
					}
					if(rindex == -1){
						System.out.println("Fail at the right");
					}
					int lcost = 0;
					int rcost = 0;
					if(lindex != -1){
						if(rindex != -1){
							overlap_left = overlaps.get(lindex);
							overlap_right = overlaps.get(rindex);
							lcost = calCost(overlap_left, gap, 0);
							rcost = calCost(overlap_right, gap, 1);
						}else{
							overlap_left = overlaps.get(lindex);
							lcost = calCost(overlap_left, gap, 0);
							rcost = Integer.MAX_VALUE;
						}
					}else{
						if(rindex != -1){
							overlap_right = overlaps.get(rindex);
							rcost = calCost(overlap_right, gap, 1);
							lcost = Integer.MAX_VALUE;
						}else{
							System.out.println("No solution");
							return -1;
						}
					}
					if(lcost <= rcost){
						int or = overlap_left.getL() + k;
						System.out.println("The bound is (" + or + "," + gl + ")");
						double dis = calDis(overlap_left, gap, 0);
						System.out.println("Left distance is " + dis);
						if(or > gl){
							Sensor sensor = sensors.get(gl);
							sensor.x += dis;
							sensor.shift += dis;
							sensor = sensors.get(or);
							sensor.x += dis;
							sensor.shift += dis;
							System.out.println("A left sensor " + or + "," + gl + " is moved " + dis + " !!!");
						}
						for(int t = or; t <= gl; t++){
							Sensor sensor = sensors.get(t);
							sensor.x += dis;
							sensor.shift += dis;
							System.out.println("A left sensor " + t + "(" + or + "," + gl + ")" + " is moved " + dis + " !!!");
						}
					}else{
						int ol = overlap_right.getL();
						double dis = calDis(overlap_right, gap, 1);
						System.out.println("Right distance is " + dis);
						System.out.println("From " + (gl+k) + " to " + ol);
						if(gl + k > ol){
							Sensor sensor = sensors.get(gl + k);
							sensor.x -= dis;
							sensor.shift -= dis;
							sensor = sensors.get(ol);
							sensor.x -= dis;
							sensor.shift -= dis;
							System.out.println("A right sensor " + (gl+k) + "," + ol + "is moved " + dis + " !!!");
						}
						for(int t = ol; t >= (gl + k); t--){
							Sensor sensor = sensors.get(t);
							sensor.x -= dis;
							sensor.shift -= dis;
							System.out.println("A right sensor " + t + "is moved " + dis + " !!!");
						}
					}
				}
			}
		}
		int sum = 0;
		for(int i = 0; i < sensors.size(); i++){
			sum += Math.abs(sensors.get(i).shift);
		}
		return sum;
	}
	public static void main(String[] args){
		
//		File file = new File("D:\\cost.txt");
//			for(int r = 10; r <= 50; r+=10){
//				for(int K = 1; K < 10; K+=2){
//					double cost = 0;
//					HandleK handle = new HandleK();
//					handle.L = 800;
//					handle.ratio = 1.2;
//					handle.r = r;
//					handle.K = K;
//					for(int i = 0; i < 30; i++){
//						handle.createSensor();
//						handle.sortSensor();
//						cost += handle.weakdectionk();
//						cost += handle.weakdectionk();
//					}
//					cost /= 30;
//					try {
//						FileWriter fileWriter = new FileWriter(file,true);
////						fileWriter.write(r + "\r\n");
//						String c ="1.5" + " " + handle.L + " " + handle.r + " " + handle.K + " " + cost + "\r\n";
//						fileWriter.write(c);
//						fileWriter.flush();
//						fileWriter.close();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
		HandleK handle = new HandleK();
		handle.L = 800;
		handle.r = 30;
		handle.K = 4;
		handle.createSensor();
		handle.sortSensor();
		handle.print1();
		handle.draw();
		for(int i = 0; i < 20; i++){
			handle.weakdectionk();
		}
		handle.draw();
		handle.print2();
	}
}
class SensorPanelK extends JPanel{
	List<Sensor> sensors;
	List<Point> points;
	List<Gap> gaps;
	List<Overlap> overlaps;
	public void addPoint(List<Point> points){
		this.points = points;
	}
	public void addSensor(List<Sensor> sensors){
		this.sensors = sensors;
	}
	public void addGaps(List<Gap> gaps){
		this.gaps = gaps;
	}
	public void addOverlaps(List<Overlap> overlaps){
		this.overlaps = overlaps;
	}
	public void paint(Graphics g){
		g.drawLine(0, 650, 800, 650);
		for(int i = 0; i < sensors.size(); i++){
			Sensor sensor = sensors.get(i);
			int x = (int)(sensor.getX()+0.5);
			int y = (int)(sensor.getY()+0.5);
			int r = (int)(sensor.getRange()+0.5);
			g.setColor(Color.red);
			g.drawOval(x - r, y - r, 2 * r, 2 * r);
		}
	}
}