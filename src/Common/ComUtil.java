package Common;
/**
 * 	This class contains common operations for data structure like array, arrayList, HashMap etc.
 * 
 * */

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class ComUtil {
	/**
  	 *  String Operations
  	 * */
  public static void tokenize(String line, ArrayList<String> tokens) {
		StringTokenizer strTok = new StringTokenizer(line);
		while (strTok.hasMoreTokens()) {
			String token = strTok.nextToken();
			tokens.add(token);
		}
	} 
  	/**
  	 *  Print
  	 * */
	public static void print(ArrayList tokens) {
		for(int i = 0; i < tokens.size(); i++) {
			System.out.print(tokens.get(i) + " ");
		}
		System.out.print("\n");
	}
	public static void print(String[] files) {
	
		for(int i = 0; i < files.length; i++) {
			System.out.print(files[i] + " ");
		}
		System.out.print("\n");
	}

	/**
	 * HashMap Operations
	 * */ 
	public static void printHash(HashMap<String, Integer> hashMap) {
		System.out.println("Print HashMap");
		Set s = hashMap.entrySet();
		Iterator it = s.iterator();
		while (it.hasNext()) {
			Map.Entry m = (Map.Entry) it.next();
			System.out.println(m.getKey()+"\t"+m.getValue());
		}
	}
	
	public static ArrayList<String> getHashMap(HashMap<String, String> hm) {
		ArrayList<String> a = new ArrayList<String>();
		Set s = hm.entrySet();
		Iterator it = s.iterator();
		while (it.hasNext()) {
			Map.Entry m = (Map.Entry) it.next();
			a.add(m.getKey()+"\t"+m.getValue());
		}
		return a;
	}

	public static ArrayList<String> getHashMap2(HashMap<String, Integer> hm) {
		ArrayList<String> a = new ArrayList<String>();
		Set s = hm.entrySet();
		Iterator it = s.iterator();
		while (it.hasNext()) {
			Map.Entry m = (Map.Entry) it.next();
			a.add(m.getKey()+"\t"+m.getValue());
		}
		return a;
	}
	
	public static String getKeysFromValue(HashMap<Integer, String> hm,
			String value) {
		Set s = hm.entrySet();
		// Move next key and value of HashMap by iterator
		Iterator it = s.iterator();
		while (it.hasNext()) {
			// key=value separator this by Map.Entry to get key and value
			Map.Entry m = (Map.Entry) it.next();
			if (m.getValue().equals(value))
				return m.getKey() + "";
		}
		System.err.println("Error, can't find the data in Hashmap!");
		return null;
	}

	public static void readHash(String type_map, HashMap<String, String> typeMap) {

		ArrayList<String> types = new ArrayList<String>();
		ArrayList<String> tokens = new ArrayList<String>();
		
		if(type_map != null) {
			FileUtil.readLines(type_map, types);
			for (int i = 0; i < types.size(); i++) {
				if (!types.get(i).isEmpty()) {
					ComUtil.tokenize(types.get(i), tokens);
					if(tokens.size() != 0) {
						if (tokens.size() != 2) {
							for(int j = 0; j < tokens.size(); j++) {
								System.out.print(tokens.get(j)+" ");
							}
							System.err
									.println(type_map + " Error ! Not two elements in one line !");
							return;
						}
						if (!typeMap.containsKey(tokens.get(0)))
							typeMap.put(tokens.get(0), tokens.get(1));
						else {
							System.out.println(tokens.get(0)+" "+tokens.get(1));
							System.err
									.println(type_map + " Error ! Same type in first column !");
							return;
						}
					}
					tokens.clear();
				}
			}
		}
	}

	public static void readHash2(String type_map, HashMap<String, Integer> hashMap) {
		ArrayList<String> types = new ArrayList<String>();
		ArrayList<String> tokens = new ArrayList<String>();
		
		if(type_map != null) {
			FileUtil.readLines(type_map, types);
			for (int i = 0; i < types.size(); i++) {
				if (!types.get(i).isEmpty()) {
					ComUtil.tokenize(types.get(i), tokens);
					if(tokens.size() != 0) {
						if (tokens.size() != 2) {
							for(int j = 0; j < tokens.size(); j++) {
								System.out.print(tokens.get(j)+" ");
							}
							System.err
									.println(type_map + " Error ! Not two elements in one line !");
							return;
						}
						if (!hashMap.containsKey(tokens.get(0)))
							hashMap.put(tokens.get(0), new Integer(tokens.get(1)));
						else {
							System.out.println(tokens.get(0)+" "+tokens.get(1));
							System.err
									.println(type_map + " Error ! Same type in first column !");
							return;
						}
					}
					tokens.clear();
				}
			}
		}
	}

	public static void readHash3(String type_map, HashMap<String, Double> hashMap) {

		ArrayList<String> types = new ArrayList<String>();
		ArrayList<String> tokens = new ArrayList<String>();
		
		if(type_map != null) {
			FileUtil.readLines(type_map, types);
			for (int i = 0; i < types.size(); i++) {
				if (!types.get(i).isEmpty()) {
					ComUtil.tokenize(types.get(i), tokens);
					if(tokens.size() != 0) {
						if (tokens.size() != 2) {
							for(int j = 0; j < tokens.size(); j++) {
								System.out.print(tokens.get(j)+" ");
							}
							System.err
									.println(type_map + " Error ! Not two elements in one line !");
							return;
						}
						if (!hashMap.containsKey(tokens.get(0)))
							hashMap.put(tokens.get(0), new Double(tokens.get(1)));
						else {
							System.out.println(tokens.get(0)+" "+tokens.get(1));
							System.err
									.println(type_map + " Error ! Same type in first column !");
							return;
						}
					}
					tokens.clear();
				}
			}
		}
	}

	public static double readHashTopValue(HashMap<String, Integer> scores, int k) {
	     List list = new LinkedList(scores.entrySet());
		int count = 0;
		int value = 0;
		double res = 0;
	    for (Iterator it = list.iterator(); count < k && it.hasNext();) {
	        Map.Entry entry = (Map.Entry)it.next();
	        value = (Integer) entry.getValue();
	        res += (double) value*Math.log(2)/Math.log(count + 2);
	        //res += (Integer) entry.getValue();
	        count ++;
	    }
		return res;
	}
	/**
	 * Frequently used functions
	 * */
	static public int count(String a, String contains) {
		int i = 0;
		int count = 0;
		while(a.contains(contains)) {
			i = a.indexOf(contains);
			a = a.substring(0, i) + 
				a.substring(i + contains.length(), a.length());
			count++;
		}
		return count;
	}

	public static void uniqe(ArrayList<String> queryDoc) {
		// add elements to al, including duplicates
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(queryDoc);
		queryDoc.clear();
		queryDoc.addAll(hs);
	}
	
	public static HashMap sortByValue(
			HashMap map) {
	     List list = new LinkedList(map.entrySet());
	     Collections.sort(list, new Comparator() {
	          public int compare(Object o1, Object o2) {
	               return ((Comparable) ((Map.Entry) (o2)).getValue())
	              .compareTo(((Map.Entry) (o1)).getValue());
	          }
	     });

	     HashMap result = new LinkedHashMap();
	    for (Iterator it = list.iterator(); it.hasNext();) {
	        Map.Entry entry = (Map.Entry)it.next();
	        result.put(entry.getKey(), entry.getValue());
	    }
	    return result;
	}
	
	public static double getSumValue(HashMap<String, Double> map) {
		Double count = 0.0D;
	     List list = new LinkedList(map.entrySet());
	    for (Iterator it = list.iterator(); it.hasNext();) {
	        Map.Entry entry = (Map.Entry)it.next();
	        count += map.get(entry.getKey());
	    }
		return count;
	} 

	public static int getFrequentElement(int [] bcp) {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		ArrayList<Integer> count = new ArrayList<Integer>();
		ArrayList<Integer> uniId = new ArrayList<Integer>();
		int id = 0;
		
		for(int col = 0; col < bcp.length; col++) {
			//System.out.print(bcp[col] + "\t");
			int no = 0;
			if(!map.containsKey(bcp[col])) {
				map.put(bcp[col], id++);
				count.add(1);
				uniId.add(bcp[col]);
			} else {
				no = map.get(bcp[col]);
				count.set(no, count.get(no) + 1);
			}	
		}
		
		int maximum = Integer.MIN_VALUE;
		int maxId = Integer.MIN_VALUE;
		for(int i = 0; i < count.size(); i++) {
			//System.out.print(uniId.get(i) + ":" + count.get(i) + ",\t");
			if(maximum < count.get(i)) {
				maximum = count.get(i);
				maxId = uniId.get(i);
			}
		}
		//System.out.println();
		
		map.clear();
		uniId.clear();
		count.clear();
		return maxId;	
	}
	public static void getFrequentElement(int[][] bcp, int [] res, char flag) {
		if(flag == 'r') {
			for(int row = 0; row < bcp.length; row++) {
				res[row] = getFrequentElement(bcp[row]);
			}
		}
		 else {
			 int colL = bcp[0].length;
			 int [] column = new int [bcp.length];
			 for(int col = 0; col < colL; col++ ) {
				 for(int row = 0; row < bcp.length; row++) {
					 column[row] = bcp[row][col];
				 }
				 res[col] = getFrequentElement(column);
			 }
		 }
	}
	public static int[] CountElmt(ArrayList<Integer> newScores1,
			ArrayList<Integer> scores) {
		int a[] = new int[scores.size()];
		for(int i = 0; i < scores.size(); i++) {
			a[i] = 0;
		}
		for(int i = 0; i < newScores1.size(); i++) {
			int value = newScores1.get(i);
			int pos = scores.indexOf(value);
			a[pos]++;
		}
		return a;
	}
	public static int countCommElmtsEqSize(ArrayList<Integer> newScores1,
			ArrayList<Integer> newScores2) {
		int count = 0;
		for(int i = 0; i < newScores1.size(); i++) {
			if(newScores1.get(i).equals(newScores2.get(i)))
				count++;
		}
		return count;
	}
	 
	public static int countCommElmts(ArrayList<String> u, ArrayList<String> v) {
		ArrayList<String> listOne = new ArrayList<String> ();
		listOne.addAll(u);
		listOne.retainAll(v);
		return listOne.size();
	}

	public static int countDiffElmts(ArrayList<String> u, ArrayList<String> v) {
		ArrayList<String> listOne = new ArrayList<String> ();
		ArrayList<String> listTwo = new ArrayList<String> ();
		listOne.addAll(u);
		listOne.retainAll(v);	
		listTwo.addAll(u);
		listTwo.addAll(v);
		listTwo.removeAll(listOne);
		return listTwo.size();
	}
	
	public static <T> ArrayList<T> rand(ArrayList<T> population, int nSamplesNeeded) {
		Random r = new Random();		
  		ArrayList<T> ret = new ArrayList<T>();  
  		
  		if(nSamplesNeeded > population.size()/2) {
	  		ArrayList<T> original = new ArrayList<T>();  
	  		original = population;
	  		
	  		while (nSamplesNeeded > 0) {
				int rand = r.nextInt(original.size());
				if (rand < nSamplesNeeded) {
						ret.add(original.get(rand));
						original.remove(rand);
						nSamplesNeeded--;
				}
	  		}
	  		original.clear();
  		} else
  			ret = shuffle(population, nSamplesNeeded);
  		
  		return ret;
	}
	
	public static <T> ArrayList<T> shuffle(ArrayList<T> population, int sample) {
		ArrayList<T> newList= new ArrayList<T>();
  		ArrayList<T> ret = new ArrayList<T>(); 
  		newList.addAll(population);

		Collections.shuffle(newList);
  		ret.addAll(newList);
  		for(int i = sample; i < ret.size(); i++) {
  			ret.remove(i);
  			i--;
  		}
  		newList.clear();
		return ret;
	}
	/*
	 public static <T> T[] rand(T[] population, int nSamplesNeeded) {
		Random r = new Random();
		T[] ret = (T[]) Array.newInstance(population.getClass().getComponentType(),
	                               nSamplesNeeded);
		int nPicked = 0, i = 0, nLeft = population.length;
		while (nSamplesNeeded > 0) {
		int rand = r.nextInt(nLeft);
		if (rand < nSamplesNeeded) {
	 			ret[nPicked++] = population[i];
	 			nSamplesNeeded--;
		}
		nLeft--;
		i++;
		}
		return ret;
	}
	 */
	public static void split(String[] items, ArrayList<String> res, int number) {
		// split items to res1 and res2, where res2 has number items
		res.clear();
		String res1 = "";
		String res2 = "";
		ArrayList<String> newList= new ArrayList<String>();
		for(int i = 0; i < items.length; i++) {
			newList.add(items[i]);
		}
		Collections.shuffle(newList);
		for(int i = 0; i < newList.size(); i++) {
			if(i < number) {
				res1 += newList.get(i) + "\t";
			} else {
				res2 += newList.get(i) + "\t";
			}
		}
		res.add(res1);
		res.add(res2);
	}

	public static void getTop(double[] array, ArrayList<Integer> rankList, int i) {
		int index = 0;
		HashSet<Integer> scanned = new HashSet<Integer>();
		double max = Double.MIN_VALUE;
		for (int m = 0; m < i && m < array.length; m++) {
			max = Double.MIN_VALUE;
			for (int no = 0; no < array.length; no++) {
				if (!scanned.contains(no)) {
					if (array[no] > max) {
						index = no;
						max = array[no];
					} else if (array[no] == max && Math.random() > 0.5) {
						index = no;
						max = array[no];
					}
				}
			}
			if (!scanned.contains(index)) {
				scanned.add(index);
				rankList.add(index);
			}
			//System.out.println(m + "\t" + index);
		}
	}

	public static void getTop(ArrayList<Double> array, ArrayList<Integer> rankList, int i) {
		int index = 0;
		HashSet<Integer> scanned = new HashSet<Integer>();
		Double max = Double.MIN_VALUE;
		for (int m = 0; m < i && m < array.size(); m++) {
			max = Double.MIN_VALUE;
			for (int no = 0; no < array.size(); no++) {
				if (!scanned.contains(no)) {
					if(array.get(no) > max) {
						index = no;
						max = array.get(no);
					} else if(array.get(no).equals(max) && Math.random() > 0.5) {
						index = no;
						max = array.get(no);
					}
				}
			}
			if (!scanned.contains(index)) {
				scanned.add(index);
				rankList.add(index);
			}
			//System.out.println(m + "\t" + index);
		}
	}
	
	public static void test() {
    	double [] a = {0.1d, 0.2d, 0.01d, 0.001d, 0.3d};    	
    	ArrayList<Integer> ranklist = new ArrayList<Integer>();
		getTop(a, ranklist , 10);
		for(int i = 0; i < ranklist.size(); i++) {
			System.out.println(ranklist.get(i) + ":" + a[ranklist.get(i)]);
		}
	    System.exit(1);
	}
	
	 public static boolean frequentBoolean(boolean[] iter){
		
		int CntTrue = 0;
		int CntFalse = 0;
		boolean returnvalue;
		
		for(int i=0; i<iter.length; i++)
		{
			if(iter[i] == true)
			{
				CntTrue++;
			}
			else
			{
				CntFalse++;
			}
		}
		if(CntTrue>CntFalse)
		{
			returnvalue = true;
		}
		else
		{
			returnvalue = false;
		}
		return returnvalue;
	}
	
	public static void getTop(float[] array, ArrayList<Integer> rankList, int i) {
		int index = 0;
		int count = 0;
		HashSet<Integer> scanned = new HashSet<Integer>();
		float max = Float.MIN_VALUE;
		for (int m = 0; m < i && m < array.length; m++) {
			max = Float.MIN_VALUE;
			for (int no = 0; no < array.length; no++) {
				if (array[no] >= max && !scanned.contains(no)) {
					index = no;
					max = array[no];
				}
			}
			scanned.add(index);
			rankList.add(index);
			//System.out.println(m + "\t" + index);
		}
	}
}