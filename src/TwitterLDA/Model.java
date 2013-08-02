package TwitterLDA;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import java.lang.Object;

import Common.ComUtil;
import Common.FileUtil;

public class Model {

	public int A; // all topics
	// public int T; //length of time stamp
	public int U; // user number
	public int V; // vocabulary size
	public int nIter; // iteration number

	public float[] alpha_general;
	public float alpha_general_sum = 0;
	public float[] beta_word;
	public float beta_word_sum = 0;
	public float[] beta_background;
	public float beta_background_sum = 0;
	public float[] gamma;

	public float[][] theta_general;
	public float[][] phi_word;
	public float[] phi_background;
	public float[] rho;

	public short[][] z; // all hidden variables
	public boolean[][][] x;

	// public short[][][] iterZ;
	// public boolean[][][][] iterX;

	public int[][] C_ua;
	public long[] C_lv;
	public int[][] C_word;
	public int[] C_b;

	public int[] countAllWord; // # of words which are general topic a

	// public int[][] CountTopicsTime;

	public Model(int A_all, int t, int u, int v, int niter, float alpha_g,
			float beta, float beta_b, float gamm) {

		this.A = A_all;
		// this.T = t;
		this.U = u;
		this.V = v;
		this.nIter = niter;
		// //////////////////////////////////////////////////////////////////////////////////////////
		this.alpha_general = new float[A];
		for (int i = 0; i < A; i++) {
			alpha_general[i] = alpha_g;
			alpha_general_sum += alpha_general[i];
		}

		this.gamma = new float[2];
		for (int i = 0; i < 2; i++) {
			gamma[i] = gamm;
		}

		this.beta_background = new float[V];
		this.beta_word = new float[V];
		for (int i = 0; i < V; i++) {
			beta_background[i] = beta_b;
			beta_background_sum += beta_background[i];
			beta_word[i] = beta;
			beta_word_sum += beta_word[i];
		}
		// ////////////////////////////////////////////////////////////////////////////////
		C_ua = new int[U][A];
		theta_general = new float[U][A];
		for (int i = 0; i < U; i++) {
			for (int j = 0; j < A; j++) {
				C_ua[i][j] = 0;
				theta_general[i][j] = 0;
			}
		}

		C_lv = new long[2];
		C_lv[0] = 0;
		C_lv[1] = 0;

		rho = new float[2];
		rho[0] = 0;
		rho[1] = 0;

		C_word = new int[A][V];
		phi_word = new float[A][V];
		for (int i = 0; i < A; i++) {
			for (int j = 0; j < V; j++) {
				C_word[i][j] = 0;
				phi_word[i][j] = 0;
			}
		}

		C_b = new int[V];
		phi_background = new float[V];
		for (int i = 0; i < V; i++) {
			C_b[i] = 0;
			phi_background[i] = 0;
		}

		countAllWord = new int[A];
		for (int i = 0; i < A; i++) {
			countAllWord[i] = 0;
		}

		// CountTopicsTime = new int[A][T];
		// for(int i=0; i<A; i++)
		// {
		// for(int j=0; j<T; j++)
		// {
		// CountTopicsTime[i][j] = 0;
		// }
		// }
	}

	public Model(int A_all, int u, int v, int niter, float alpha_g, float beta,
			float beta_b, float gamm) {
		System.out.println("reading data...");

		this.A = A_all;
		this.U = u;
		this.V = v;
		this.nIter = niter;
		// //////////////////////////////////////////////////////////////////////////////////////////
		this.alpha_general = new float[A];
		for (int i = 0; i < A; i++) {
			alpha_general[i] = alpha_g;
			alpha_general_sum += alpha_general[i];
		}

		this.gamma = new float[2];
		for (int i = 0; i < 2; i++) {
			gamma[i] = gamm;
		}

		this.beta_background = new float[V];
		this.beta_word = new float[V];
		for (int i = 0; i < V; i++) {
			beta_background[i] = beta_b;
			beta_background_sum += beta_background[i];
			beta_word[i] = beta;
			beta_word_sum += beta_word[i];
		}
		// ////////////////////////////////////////////////////////////////////////////////
		C_ua = new int[U][A];
		theta_general = new float[U][A];
		for (int i = 0; i < U; i++) {
			for (int j = 0; j < A; j++) {
				C_ua[i][j] = 0;
				theta_general[i][j] = 0;
			}
		}

		C_lv = new long[2];
		C_lv[0] = 0;
		C_lv[1] = 0;

		rho = new float[2];
		rho[0] = 0;
		rho[1] = 0;

		C_word = new int[A][V];
		phi_word = new float[A][V];
		for (int i = 0; i < A; i++) {
			for (int j = 0; j < V; j++) {
				C_word[i][j] = 0;
				phi_word[i][j] = 0;
			}
		}

		C_b = new int[V];
		phi_background = new float[V];
		for (int i = 0; i < V; i++) {
			C_b[i] = 0;
			phi_background[i] = 0;
		}

		countAllWord = new int[A];
		for (int i = 0; i < A; i++) {
			countAllWord[i] = 0;
		}
	}

	public void intialize(ArrayList<user> users) {
		System.out.println("initializing...");

		int u, d, w = 0;
		// int t = 0;

		z = new short[users.size()][];
		x = new boolean[users.size()][][];

		// iterZ = new short[users.size()][][];
		// iterX = new boolean[users.size()][][][];

		for (u = 0; u < users.size(); u++) {
			user buffer_user = users.get(u);
			z[u] = new short[buffer_user.tweets.size()];
			x[u] = new boolean[buffer_user.tweets.size()][];

			// iterZ[u] = new short[buffer_user.tweets.size()][40];
			// iterX[u] = new boolean[buffer_user.tweets.size()][][];

			for (d = 0; d < buffer_user.tweets.size(); d++) {
				// t = buffer_user.tweets.get(d).time;
				tweet tw = buffer_user.tweets.get(d);
				x[u][d] = new boolean[tw.tweetwords.length];
				// iterX[u][d] = new boolean[tw.tweetwords.length][40];

				double randgeneral = Math.random();
				double thred = 0;
				short a_general = 0;
				for (short a = 0; a < A; a++) {
					thred += (double) 1 / A;
					if (thred >= randgeneral) {
						a_general = a;
						break;
					}
				}
				z[u][d] = a_general;
				C_ua[u][a_general]++;
				for (w = 0; w < tw.tweetwords.length; w++) {
					int word = tw.tweetwords[w];
					double randback = Math.random();
					boolean buffer_x;
					if (randback > 0.5) {
						buffer_x = true;
					} else {
						buffer_x = false;
					}

					if (buffer_x == true) {
						C_lv[1]++;
						C_word[a_general][word]++;
						countAllWord[a_general]++;
						x[u][d][w] = buffer_x;
					} else {
						C_lv[0]++;
						C_b[word]++;
						x[u][d][w] = buffer_x;
					}
				}
			}
		}
		System.out.println("Intialize Done");
	}

	public void estimate(ArrayList<user> users, int nIter) {

		int niter = 0;
		// int step = 0;
		while (true) {
			niter++;
			System.out.println("iteration" + " " + niter + " ...");
			sweep(users);
			// check();

			// ignore the following to only get results from the last iteration
			// if(niter > nIter-200 && niter%5 == 0)
			// {
			// recordMatrix(step,users);
			// step++;
			// System.out.println("record matrix done");
			// }

			if (niter >= nIter) {
				update_distribution();
				break;
			}
		}
	}

	// private void recordMatrix(int step, ArrayList<user> users){
	// user buffer_user = null;
	// tweet buffer_tweet = null;
	// for(int u=0; u<users.size(); u++)
	// {
	// buffer_user = users.get(u);
	// for(int d=0; d<buffer_user.tweetCnt; d++)
	// {
	// buffer_tweet = buffer_user.tweets.get(d);
	// // iterZ[u][d][step] = z[u][d];
	// // for(int w=0; w<buffer_tweet.tweetwords.length; w++)
	// // {
	// // iterX[u][d][w][step] = x[u][d][w];
	// // }
	// }
	// }
	//
	// if(step == 39)
	// {
	// for(int i=0; i<U; i++)
	// {
	// for(int j=0; j<A; j++)
	// {
	// C_ua[i][j] =0;
	// }
	// }
	//
	// C_lv[0] = 0;
	// C_lv[1] = 0;
	//
	// for(int i=0; i<A; i++)
	// {
	// for(int j=0; j<V; j++)
	// {
	// C_word[i][j] =0;
	// }
	// }
	//
	//
	// for(int i=0; i<V; i++)
	// {
	// C_b[i] = 0;
	// }
	//
	//
	// for(int u=0; u<users.size(); u++)
	// {
	// buffer_user = users.get(u);
	// for(int d=0; d<buffer_user.tweetCnt; d++)
	// {
	// buffer_tweet = buffer_user.tweets.get(d);
	// int t = buffer_tweet.time;
	//
	// HashMap<Short,Integer> ContList = new HashMap<Short,Integer>();
	// for(int i=0; i<40; i++)
	// {
	// // Short bufferline = iterZ[u][d][i];
	// if(!ContList.containsKey(bufferline))
	// {
	// ContList.put(bufferline, 1);
	// }
	// else
	// {
	// ContList.put(bufferline, ContList.get(bufferline)+1);
	// }
	// }
	//
	// HashMap rankedList = ComUtil.sortByValue(ContList);
	//
	// Set s = rankedList.entrySet();
	// Iterator it = s.iterator();
	//
	// Map.Entry m = (Map.Entry) it.next();
	// z[u][d] = (Short) m.getKey();
	//
	// rankedList.clear();
	// ContList.clear();
	//
	// C_ua[u][z[u][d]]++;
	// // CountTopicsTime[z[u][d]][t]++;
	//
	// for(int n=0; n<buffer_tweet.tweetwords.length; n++)
	// {
	// int word = buffer_tweet.tweetwords[n];
	// x[u][d][n] = ComUtil.frequentBoolean(iterX[u][d][n]);
	// if(x[u][d][n] == false)
	// {
	// C_lv[0]++;
	// C_b[word]++;
	// }
	// else
	// {
	// C_lv[1]++;
	// C_word[z[u][d]][word]++;
	// }
	// }
	// }
	// }
	// }
	// }
	//
	private void sweep(ArrayList<user> users) {
		for (int cntuser = 0; cntuser < users.size(); cntuser++) {
			user buffer_user = users.get(cntuser);
			for (int cnttweet = 0; cnttweet < buffer_user.tweetCnt; cnttweet++) {
				tweet tw = buffer_user.tweets.get(cnttweet);
				sample_z(cntuser, cnttweet, buffer_user, tw);
				for (int cntword = 0; cntword < tw.tweetwords.length; cntword++) {
					int word = tw.tweetwords[cntword];
					sample_x(cntuser, cnttweet, cntword, word);
				}
			}
		}
	}

	private void sample_x(int u, int d, int n, int word) {

		boolean binarylabel = x[u][d][n];
		int binary;
		if (binarylabel == true) {
			binary = 1;
		} else {
			binary = 0;
		}

		C_lv[binary]--;
		if (binary == 0) {
			C_b[word]--;
		} else {
			C_word[z[u][d]][word]--;
			countAllWord[z[u][d]]--;
		}

		binarylabel = draw_x(u, d, n, word);

		x[u][d][n] = binarylabel;

		if (binarylabel == true) {
			binary = 1;
		} else {
			binary = 0;
		}

		C_lv[binary]++;
		if (binary == 0) {
			C_b[word]++;
		} else {
			C_word[z[u][d]][word]++;
			countAllWord[z[u][d]]++;
		}
	}

	private boolean draw_x(int u, int d, int n, int word) {

		// int t = time;

		boolean returnvalue = false;

		double[] P_lv;
		P_lv = new double[2];
		double Pb = 1;
		double Ptopic = 1;

		P_lv[0] = (C_lv[0] + gamma[0])
				/ (C_lv[0] + C_lv[1] + gamma[0] + gamma[1]); // part 1 from
																// counting C_lv

		P_lv[1] = (C_lv[1] + gamma[1])
				/ (C_lv[0] + C_lv[1] + gamma[0] + gamma[1]);

		Pb = (C_b[word] + beta_background[word])
				/ (C_lv[0] + beta_background_sum); // word in background part(2)
		Ptopic = (C_word[z[u][d]][word] + beta_word[word])
				/ (countAllWord[z[u][d]] + beta_word_sum);

		double p0 = Pb * P_lv[0];
		double p1 = Ptopic * P_lv[1];

		double sum = p0 + p1;
		double randPick = Math.random();

		if (randPick <= p0 / sum) {
			returnvalue = false;
		} else {
			returnvalue = true;
		}

		return returnvalue;
	}

	private void sample_z(int u, int d, user buffer_user, tweet tw) {

		short tweet_topic = z[u][d];
		// int t = tw.time;
		int w = 0;

		C_ua[u][tweet_topic]--;
		for (w = 0; w < tw.tweetwords.length; w++) {
			int word = tw.tweetwords[w];
			if (x[u][d][w] == true) {
				C_word[tweet_topic][word]--;
				countAllWord[tweet_topic]--;
			}
		}

		short buffer_z;
		buffer_z = draw_z(u, d, buffer_user, tw);

		tweet_topic = buffer_z;
		z[u][d] = tweet_topic;

		C_ua[u][tweet_topic]++;
		for (w = 0; w < tw.tweetwords.length; w++) {
			int word = tw.tweetwords[w];
			if (x[u][d][w] == true) {
				C_word[tweet_topic][word]++;
				countAllWord[tweet_topic]++;
			}
		}
	}

	private short draw_z(int u, int d, user buffer_user, tweet tw) { // return y
																		// then
																		// z

		int word;
		int w;

		double[] P_topic;
		int[] pCount;
		P_topic = new double[A];
		pCount = new int[A];

		HashMap<Integer, Integer> wordcnt = new HashMap<Integer, Integer>(); // store
																				// the
																				// topic
																				// words
																				// with
																				// frequency
		int totalWords = 0; // total number of topic words

		for (w = 0; w < tw.tweetwords.length; w++) {
			if (x[u][d][w] == true) {
				totalWords++;
				word = tw.tweetwords[w];
				if (!wordcnt.containsKey(word)) {
					wordcnt.put(word, 1);
				} else {
					int buffer_word_cnt = wordcnt.get(word) + 1;
					wordcnt.put(word, buffer_word_cnt);
				}
			}
		}

		for (int a = 0; a < A; a++) {
			P_topic[a] = (C_ua[u][a] + alpha_general[a])
					/ (buffer_user.tweetCnt - 1 + alpha_general_sum);

			double buffer_P = 1;

			// Set s = wordcnt.entrySet();
			// Iterator it = s.iterator();
			// while (it.hasNext()) {
			// Map.Entry m = (Map.Entry) it.next();
			// word = (Integer) m.getKey();
			// int buffer_cnt = (Integer) m.getValue();
			// for (int j = 0; j < buffer_cnt; j++) {
			// buffer_P *= (C_word[a][word] + beta_word[word] + j);
			// }
			// }
			//
			// for (int i = 0; i < totalWords; i++) {
			// buffer_P /= (countAllWord[a] + beta_word_sum + i);
			// }

			int i = 0;
			Set s = wordcnt.entrySet();
			Iterator it = s.iterator();
			while (it.hasNext()) {
				Map.Entry m = (Map.Entry) it.next();
				word = (Integer) m.getKey();
				int buffer_cnt = (Integer) m.getValue();
				for (int j = 0; j < buffer_cnt; j++) {
					double value = (double) (C_word[a][word] + beta_word[word] + j)
							/ (countAllWord[a] + beta_word_sum + i);
					i++;
					buffer_P *= value;
					buffer_P = isOverFlow(buffer_P, pCount, a);
				}
			}

			P_topic[a] *= Math.pow(buffer_P, (double) 1);
		}

		reComputeProbs(P_topic, pCount);

		double randz = Math.random();

		double sum = 0;

		for (int a = 0; a < A; a++) {
			sum += P_topic[a];
		}

		double thred = 0;

		short chosena = -1;

		for (short a = 0; a < A; a++) {
			thred += P_topic[a] / sum;
			if (thred >= randz) {
				chosena = a;
				break;
			}
		}
		if (chosena == -1) {
			System.out.println("chosena equals -1, error!");
		}

		wordcnt.clear();
		return chosena;
	}

	private void reComputeProbs(double[] p_topic, int[] pCount) {
		int max = pCount[0];
		// System.out.print(max + " ");
		for (int i = 1; i < pCount.length; i++) {
			if (pCount[i] > max)
				max = pCount[i];
			// System.out.print(pCount[i] + " ");
		}
		if (max > 0)
			FileUtil.print(p_topic, "previous: ", " ", "\n");
		for (int i = 0; i < pCount.length; i++) {
			p_topic[i] = p_topic[i] * Math.pow(1e150, pCount[i] - max);
		}
		if (max > 0) {
			System.out.print(pCount[0] + " ");
			for (int i = 1; i < pCount.length; i++) {
				System.out.print(pCount[i] + " ");
			}
			System.out.println();
			FileUtil.print(p_topic, "current: ", " ", "\n");
			// System.exit(0);
		}
	}

	private double isOverFlow(double buffer_P, int[] pCount, int a2) {
		if (buffer_P > 1e150) {
			pCount[a2]++;
			return buffer_P / 1e150;
		}
		if (buffer_P < 1e-150) {
			pCount[a2]--;
			return buffer_P * 1e150;
		}
		return buffer_P;
	}

	public void update_distribution() {

		for (int u = 0; u < U; u++) {
			int c_u_a = 0;
			for (int a = 0; a < A; a++) {
				c_u_a += C_ua[u][a];
			}
			for (int a = 0; a < A; a++) {
				theta_general[u][a] = (C_ua[u][a] + alpha_general[a])
						/ (c_u_a + alpha_general_sum);
			}
		}

		for (int a = 0; a < A; a++) {
			int c_v = 0;
			for (int v = 0; v < V; v++) {
				c_v += C_word[a][v];
			}
			for (int v = 0; v < V; v++) {
				phi_word[a][v] = (C_word[a][v] + beta_word[v])
						/ (c_v + beta_word_sum);
			}
		}

		int c_b_v = 0;
		for (int v = 0; v < V; v++) {
			c_b_v += C_b[v];
		}
		for (int v = 0; v < V; v++) {
			phi_background[v] = (C_b[v] + beta_background[v])
					/ (c_b_v + beta_background_sum);
		}

		for (int l = 0; l < 2; l++) {
			rho[0] = (C_lv[0] + gamma[0])
					/ (C_lv[0] + C_lv[1] + gamma[0] + gamma[1]);
			rho[1] = (C_lv[1] + gamma[1])
					/ (C_lv[0] + C_lv[1] + gamma[0] + gamma[1]);
		}
	}

	public void outputWordsInTopics(String output, ArrayList<String> list,
			int Cnt) throws Exception {

		// ArrayList<String> outLines = new ArrayList<String>();
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				output)));
		ArrayList<Integer> rankList = new ArrayList<Integer>();

		for (int a = 0; a < A; a++) {
			String topicline = "Topic " + a + ":";
			writer.write(topicline);

			// phi_word[a][]
			ComUtil.getTop(phi_word[a], rankList, Cnt);

			for (int i = 0; i < rankList.size(); i++) {
				String tmp = "\t" + list.get(rankList.get(i)) + "\t"
						+ phi_word[a][rankList.get(i)];
				writer.write(tmp + "\n");
			}

			rankList.clear();
		}
		writer.flush();
		writer.close();
	}

	public void outputTopicDistributionOnUsers(String outputDir,
			ArrayList<user> users) throws Exception {

		String outputfile = outputDir + "TopicsDistributionOnUsers.txt";
		BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter(new File(outputfile)));

		for (int u = 0; u < U; u++) {
			String bufferline1 = "";
			String name = users.get(u).userID;
			writer.write(name + "\t");
			for (int a = 0; a < A; a++) {
				bufferline1 += theta_general[u][a] + "\t";
			}
			writer.write(bufferline1 + "\n");
		}
		writer.flush();
		writer.close();

		outputfile = outputDir + "TopicCountsOnUsers.txt";
		writer = new BufferedWriter(new FileWriter(new File(outputfile)));

		for (int u = 0; u < U; u++) {
			String bufferline1 = "";
			String name = users.get(u).userID;
			writer.write(name + "\t");
			for (int a = 0; a < A; a++) {
				bufferline1 += C_ua[u][a] + "\t";
			}
			writer.write(bufferline1 + "\n");
		}
		writer.flush();
		writer.close();
	}

	public void outputTopicDistributionOnUsers_old(String output,
			ArrayList<user> users) {

		ArrayList<String> outLines = new ArrayList<String>();

		for (int u = 0; u < U; u++) {
			String bufferline1 = "General topics distribution:" + "\t";
			String name = users.get(u).userID;
			outLines.add("UserName: " + name);
			for (int a = 0; a < A; a++) {
				bufferline1 += theta_general[u][a] + "\t";
			}
			outLines.add("\t" + bufferline1);
		}

		FileUtil.writeLines(output, outLines);
		outLines.clear();
	}

	public void outputBackgroundWordsDistribution(String output,
			ArrayList<String> list, int Cnt) throws Exception {

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
				output)));
		ArrayList<Integer> rankList = new ArrayList<Integer>();

		// phi_background
		ComUtil.getTop(phi_background, rankList, Cnt);

		for (int i = 0; i < rankList.size(); i++) {
			String tmp = "\t" + list.get(rankList.get(i)) + "\t"
					+ phi_background[rankList.get(i)];
			writer.write(tmp + "\n");
		}

		rankList.clear();

		writer.flush();
		writer.close();
	}

	public void outputBackgroundWordsDistribution_old(String output,
			ArrayList<String> list, int Cnt) {

		ArrayList<String> outLines = new ArrayList<String>();

		outLines.add("Background Words Distribution");

		HashMap<Integer, Float> wordList = new HashMap<Integer, Float>();
		for (int v = 0; v < V; v++) {
			wordList.put(v, phi_background[v]);
		}

		HashMap rankedWordList = ComUtil.sortByValue(wordList);

		Set s = rankedWordList.entrySet();
		Iterator it = s.iterator();
		int i = 0;
		while (i < Cnt) {
			Map.Entry m = (Map.Entry) it.next();
			String BufferLine = list.get((Integer) m.getKey());
			outLines.add("\t" + BufferLine + "\t" + m.getValue());
			i++;
		}
		wordList.clear();
		rankedWordList.clear();

		FileUtil.writeLines(output, outLines);
		outLines.clear();
	}

	public void outputTextWithLabel(String output, ArrayList<user> users,
			ArrayList<String> uniWordMap) throws Exception {
		BufferedWriter writer = null;

		// ArrayList<String> outlines = new ArrayList<String>();

		for (int u = 0; u < users.size(); u++) {
			user buffer_user = users.get(u);

			writer = new BufferedWriter(new FileWriter(new File(output + "/"
					+ buffer_user.userID)));
			// outlines.add(buffer_user.userID + " " + buffer_user.tweetCnt);
			for (int d = 0; d < buffer_user.tweetCnt; d++) {
				tweet buffer_tweet = buffer_user.tweets.get(d);
				String line = "z=" + z[u][d] + ":  ";
				for (int n = 0; n < buffer_tweet.tweetwords.length; n++) {
					int word = buffer_tweet.tweetwords[n];
					if (x[u][d][n] == true) {
						line += uniWordMap.get(word) + "/" + z[u][d] + " ";
					} else {
						line += uniWordMap.get(word) + "/" + "false" + " ";
					}
				}
				int buffertime = buffer_tweet.time + 1;
				if (buffertime <= 30) {
					if (buffertime < 10) {
						line = "2011-09-0" + buffertime + ":\t" + line;
					} else {
						line = "2011-09-" + buffertime + ":\t" + line;
					}
				} else if (buffertime <= 61 && buffertime > 30) {
					int buffer_time = buffertime - 30;
					if (buffertime - 30 < 10) {
						line = "2011-10-0" + buffer_time + ":\t" + line;
					} else {
						line = "2011-10-" + buffer_time + ":\t" + line;
					}
				} else if (buffertime > 61) {
					int buffer_time = buffertime - 61;
					if (buffertime - 61 < 10) {
						line = "2011-11-0" + buffer_time + ":\t" + line;
					} else {
						line = "2011-11-" + buffer_time + ":\t" + line;
					}
				}
				// outlines.add(line);
				writer.write(line + "\n");
			}
			writer.flush();
			writer.close();
		}

		// FileUtil.writeLines(output, outlines);
		// outlines.clear();
	}

	public void outputTextWithLabel_old(String output, ArrayList<user> users,
			ArrayList<String> uniWordMap) {
		ArrayList<String> outlines = new ArrayList<String>();

		for (int u = 0; u < users.size(); u++) {
			user buffer_user = users.get(u);
			outlines.add(buffer_user.userID + " " + buffer_user.tweetCnt);
			for (int d = 0; d < buffer_user.tweetCnt; d++) {
				tweet buffer_tweet = buffer_user.tweets.get(d);
				String line = "z=" + z[u][d] + ":  ";
				for (int n = 0; n < buffer_tweet.tweetwords.length; n++) {
					int word = buffer_tweet.tweetwords[n];
					if (x[u][d][n] == true) {
						line += uniWordMap.get(word) + "/" + z[u][d] + " ";
					} else {
						line += uniWordMap.get(word) + "/" + "false" + " ";
					}
				}
				int buffertime = buffer_tweet.time + 1;
				if (buffertime <= 30) {
					if (buffertime < 10) {
						line = "2011-09-0" + buffertime + ":\t" + line;
					} else {
						line = "2011-09-" + buffertime + ":\t" + line;
					}
				} else if (buffertime <= 61 && buffertime > 30) {
					int buffer_time = buffertime - 30;
					if (buffertime - 30 < 10) {
						line = "2011-10-0" + buffer_time + ":\t" + line;
					} else {
						line = "2011-10-" + buffer_time + ":\t" + line;
					}
				} else if (buffertime > 61) {
					int buffer_time = buffertime - 61;
					if (buffertime - 61 < 10) {
						line = "2011-11-0" + buffer_time + ":\t" + line;
					} else {
						line = "2011-11-" + buffer_time + ":\t" + line;
					}
				}
				outlines.add(line);
			}
		}

		FileUtil.writeLines(output, outlines);
		outlines.clear();
	}

	// public void outputTopicCountOnTime(String output){
	//
	// ArrayList<String> outlines = new ArrayList<String>();
	// for(int a=0; a<A; a++)
	// {
	// String line = "";
	// for(int t=0; t<T; t++)
	// {
	// line += CountTopicsTime[a][t] + "\t";
	// }
	// line = line.substring(0, line.length()-1);
	// outlines.add(line);
	// }
	// FileUtil.writeLines(output, outlines);
	// outlines.clear();
	//
	// }
}
