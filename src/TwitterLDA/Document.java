package TwitterLDA;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Common.ComUtil;
import Common.FileUtil;
import Common.Stopwords;

//import edu.mit.jwi.item.Word;

/**
 * A Document object represents a user
 */

public class Document {
	
	// the ID or title of this document
	private String id;

	//private ArrayList<String> DataLines;
	
	// irregular array to store Words and Items
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private int [][] docWords;
	
	private int [][] docItems;
	
	//public HashMap<String, Integer> wordMap;
	
	//public HashMap<String, Integer> itemMap;
	
	// Init. Document -> get DataLines from Document
	public Document(String Dir, String id, HashMap<String, Integer> wordMap, 
			HashMap<String, Integer> itemMap, ArrayList<String> uniWordMap,
			ArrayList<String> uniItemMap, ArrayList<Integer> uniWordMapCounts, 
			ArrayList<Integer> uniItemMapCounts) {
		
		this.id = id;
		String line;
		int number = wordMap.size();
		int numberItem = itemMap.size();
		ArrayList<String> datalines = new ArrayList<String>();
		Pattern MY_PATTERN = Pattern.compile(" *#PB#.*\\w+.*");
		FileUtil.readLines(Dir, datalines);
		
		this.docWords = new int[datalines.size()][];
		this.docItems = new int[datalines.size()][];
		ArrayList<Integer> words = new ArrayList<Integer>();
		ArrayList<Integer> items = new ArrayList<Integer>();
		
		for(int lineNo = 0; lineNo < datalines.size(); lineNo++) {
			//System.out.println(datalines.get(lineNo));
			//line = datalines.get(lineNo).replaceAll(":", " : ");
			line = datalines.get(lineNo);
			
			ArrayList<String> tokens = new ArrayList<String>();
			ComUtil.tokenize(line, tokens);
			for(int i = 0; i < tokens.size(); i++) {
				// RT is also stop word // keep stop words force them to background words
				if(!Stopwords.isStopword(tokens.get(i)) && !isNoisy(tokens.get(i))) {
//				if(!isNoisy(tokens.get(i))) {
					if(tokens.get(i).contains("#PB#")) {
						Matcher m = MY_PATTERN.matcher(tokens.get(i));
						//if (m.matches() && tokens.get(i).length() > 18 && tokens.get(i).startsWith("http://t.co")) {
						if (true) {
							if(!itemMap.containsKey(tokens.get(i))) {
								items.add(numberItem);
								itemMap.put(tokens.get(i), numberItem++);
								uniItemMap.add(tokens.get(i));
								uniItemMapCounts.add(1);
							} else {
								items.add(itemMap.get(tokens.get(i)));
								uniItemMapCounts.set(itemMap.get(tokens.get(i)), 
										uniItemMapCounts.get(itemMap.get(tokens.get(i))) + 1);
							}
						}
					} else {
						String newWord = tokens.get(i).toLowerCase();
						if(!wordMap.containsKey(newWord)) {
							words.add(number);
							wordMap.put(newWord, number++);
							uniWordMap.add(newWord);
							uniWordMapCounts.add(1);
						} else {
							words.add(wordMap.get(newWord));
							uniWordMapCounts.set(wordMap.get(newWord), 
									uniWordMapCounts.get(wordMap.get(newWord)) + 1);
						}
//						if(Stopwords.isStopword(newWord)) {
//							PBmodel.stopList.add(wordMap.get(newWord));
////							System.out.println(newWord + " id:" + wordMap.get(newWord));
//						}
					}
				}
			}
			
//			ComUtil.uniqe(items);
			docWords[lineNo] = new int[words.size()];
			for(int w = 0; w < words.size(); w++)
				docWords[lineNo][w] = words.get(w);
			docItems[lineNo] = new int[items.size()];
			for(int w = 0; w < items.size(); w++)
				docItems[lineNo][w] = items.get(w);
			words.clear();
			items.clear();
			tokens.clear();
		}
		datalines.clear();
	}

	public Document(List<String> datalines, String id,
			HashMap<String, Integer> wordMap, HashMap<String, Integer> itemMap) {
		this.id = id;
		this.docWords = new int[datalines.size()][];
		this.docItems = new int[datalines.size()][];
		ArrayList<Integer> words = new ArrayList<Integer>();
		ArrayList<Integer> items = new ArrayList<Integer>();

		Pattern MY_PATTERN = Pattern.compile(" *#PB#.*\\w+.*");
		
		for(int lineNo = 0; lineNo < datalines.size(); lineNo++) {
			String line = datalines.get(lineNo);
			
			ArrayList<String> tokens = new ArrayList<String>();
			ComUtil.tokenize(line, tokens);
			for(int i = 0; i < tokens.size(); i++) {
				// RT is also stop word // keep stop words force them to background words
				if(!Stopwords.isStopword(tokens.get(i)) && !isNoisy(tokens.get(i))) {
					if(tokens.get(i).contains("#PB#")) {
						Matcher m = MY_PATTERN.matcher(tokens.get(i));
//						String tmpNewItem = tokens.get(i).toLowerCase().trim();
						String tmpNewItem = tokens.get(i);
						//if (m.matches() && tokens.get(i).length() > 18 && tokens.get(i).startsWith("http://t.co")) {
						if (true) {
							if(!itemMap.containsKey(tmpNewItem)) {
								System.out.println(tokens.get(i) + " is not contained!");
							} else {
								items.add(itemMap.get(tmpNewItem));
							}
						}
					} else {
						String newWord = tokens.get(i).toLowerCase().trim();
						if(!wordMap.containsKey(newWord)) {
//							System.out.println(newWord + " is not contained!");
						} else {
							words.add(wordMap.get(newWord));
						}
					}
				}
			}
			
//			ComUtil.uniqe(items);
			docWords[lineNo] = new int[words.size()];
			for(int w = 0; w < words.size(); w++)
				docWords[lineNo][w] = words.get(w);
			docItems[lineNo] = new int[items.size()];
			for(int w = 0; w < items.size(); w++)
				docItems[lineNo][w] = items.get(w);
			words.clear();
			items.clear();
			tokens.clear();
		}
	}

	public Document(String Dir, String id,
			HashMap<String, Integer> wordMap, int [][]z, boolean[][][] y, 
			int userNo) {
		this.id = id;
		String line;
		ArrayList<String> datalines = new ArrayList<String>();
		FileUtil.readLines(Dir, datalines);
		
		this.docWords = new int[datalines.size()][];
		this.docItems = new int[datalines.size()][];
		
		// z and y
		System.out.println(Dir + " lines:" + datalines.size());
		z[userNo] = new int[datalines.size()];
		y[userNo] = new boolean[datalines.size()][];
		
		for(int lineNo = 0; lineNo < datalines.size(); lineNo++) {
			line = datalines.get(lineNo);
//			System.out.println(line);
			ArrayList<String> tokens = new ArrayList<String>();
			FileUtil.tokenize(line, tokens);
			
			// get topic
			int topic = Integer.parseInt(tokens.get(1).replaceAll(".*=", "").replace(":", "").trim());
			z[userNo][lineNo] = topic;
			
			// get words (tolowercase) and items
			ArrayList<Integer> words = new ArrayList<Integer>();
			ArrayList<Boolean> ylabel = new ArrayList<Boolean>();
			ArrayList<Integer> items = new ArrayList<Integer>();
			if(tokens.size() > 2) {
				for(int tokenno = 2; tokenno < tokens.size(); tokenno++) {
					String word = tokens.get(tokenno).toLowerCase();
					int index = word.lastIndexOf("/");
					String wordToken = word.substring(0, index);
					if(wordMap.containsKey(wordToken)) {
						words.add(wordMap.get(wordToken));
						// add y value
						if(word.substring(index+1).contains("false"))
							ylabel.add(false);
						else
							ylabel.add(true);
					}
					else
						System.err.println("Doesn't contains word: " + word);				
				}
			} else
				System.err.println(line);

			y[userNo][lineNo] = new boolean[words.size()];
			docWords[lineNo] = new int[words.size()];
			for(int w = 0; w < words.size(); w++) {
				docWords[lineNo][w] = words.get(w);
				y[userNo][lineNo][w] = ylabel.get(w);
			}
			docItems[lineNo] = new int[items.size()];
			for(int w = 0; w < items.size(); w++)
				docItems[lineNo][w] = items.get(w);
			
			words.clear();
			items.clear();
			tokens.clear();
		}
		datalines.clear();
	}

	private boolean isNoisy(String string) {
		// at least contains a word
		//Pattern MY_PATTERN = Pattern.compile(".*[a-zA-Z#@]+.*");
		Pattern MY_PATTERN = Pattern.compile(".*[a-zA-Z]+.*");
		Matcher m = MY_PATTERN.matcher(string.toLowerCase());
		// filter @xxx and URL
		if (!m.matches() || string.contains("@") || string.startsWith("http://")) {
			return true;
		}
		else
			return false;
	}

	public int[][] getDocWords() {
		return docWords;
	}

	public void setDocWords(int[][] docWords) {
		this.docWords = docWords;
	}

	public int[][] getDocItems() {
		return docItems;
	}

	public void setDocItems(int[][] docItems) {
		this.docItems = docItems;
	}

}