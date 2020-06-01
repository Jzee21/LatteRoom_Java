package network.server.test.datatype;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FloatSensorDatas {

	public static void main(String[] args) {
		
		File file = new File("C:\\Jzee\\0530_0601_copy.txt");
		File file2 = new File("C:\\Jzee\\0530_0601_deff.txt");
		BufferedReader br = null;
		PrintWriter pw = null;
		ArrayList<Float> list = new ArrayList<Float>();
		ArrayList<Float> deff = new ArrayList<Float>();
		
		try {
			br = new BufferedReader(new FileReader(file));
			pw = new PrintWriter(file2);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(br != null) {
			try {
				String line = "";
				while((line = br.readLine()) != null) {
					list.add(Float.parseFloat(line));
				}
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
//		int i=0;
//		for(Float data : list) {
//			System.out.println(++i + " : " + data);
//		}
		
		if(pw != null) {
			try {
				double all = 0.0;
				float val = 0.0f;
				float plus = 0.0f;
				float minus = 0.0f;
				float max = 0.0f;
				int pcount = 0;
				int mcount = 0;
				for (int i = 0; i < list.size()-1; i++) {
					all += list.get(i);
					val = list.get(i+1) - list.get(i);
					if(max < val) {
						max = val;
					}
					if(val == 0.0f) {
						pw.append(String.format("%3.2f\t", list.get(i)) + String.format("%3.2f", val) + "\n");
					} else {
						if(val>0.0) {
							plus += val;
							pcount++;
							pw.append(String.format("%3.2f\t", list.get(i)) + String.format("\t %3.2f", val) + "\n");
						} else {
							minus += val;
							mcount++;
							pw.append(String.format("%3.2f\t", list.get(i)) + String.format("\t%3.2f", val) + "\n");							
						}
					}
//					pw.append(String.format("avg : %.2f\n", all/list.size()));
//					pw.append(String.format("최대 : %.2f\n", max));
//					pw.append(String.format("증가 : %d, %.2f\n", pcount, plus));
//					pw.append(String.format("감소 : %d, %.2f\n", mcount, minus));
//					pw.append(String.format("변화량 : %d/%d, %.2f\n", pcount + mcount, list.size(), plus + minus));
//					System.out.println();
				}
				System.out.println(String.format("avg : %.2f\n", all/list.size()));
				System.out.println(String.format("최대 : %.2f\n", max));
				System.out.println(String.format("증가 : %d, %.2f\n", pcount, plus));
				System.out.println(String.format("감소 : %d, %.2f\n", mcount, minus));
				System.out.println(String.format("변화량 : %d/%d, %.2f\n", pcount + mcount, list.size(), plus + minus));
				
			} catch (Exception e) {
			}
		}
		
	}

}
