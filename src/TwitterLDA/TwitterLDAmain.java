/*
 * Copyright (C) 2012 by
 * 
 * 	SMU Text Mining Group
 *	Singapore Management University
 *
 * TwitterLDA is distributed for research purpose, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * The original paper is as follows:
 * Wayne Xin Zhao, Jing Jiang et al., Comparing Twitter and traditional media using topic models. 
 * ECIR'11.
 * 
 * Note that the package here is not developed by the authors
 * in the paper, nor used in the original papers. It's an implementation
 * based on the paper, where most of the work is done by qiming.diao.2010@smu.sg.
 * 
 * Feel free to contact the following people if you find any
 * problems in the package.
 * 
 * minghui.qiu.2010@smu.edu.sg
 *
 */

package TwitterLDA;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import Common.Stopwords;
import Common.FileUtil;
import Common.JC;

public class TwitterLDAmain {

	static ArrayList<String> stopWords;

	public static void main(String args[]) throws Exception {
		String base = System.getProperty("user.dir") + "/data/";
		String name = "test";
		char[] options = { 'f', 'i', 'o', 'p', 's' };
		String filelist = base + "/filelist_" + name + ".txt";
		String dataDir = base + "/Data4Model/" + name + "/";
		String outputDir = base + "/ModelRes/" + name + "/";
		String modelParas = base + "/modelParameters-" + name + ".txt";
		String stopfile = base + "/stoplist.txt";
		
		// create output folder
		FileUtil.mkdir(new File(base + "/ModelRes/"));
		FileUtil.mkdir(new File(outputDir));

		ArrayList<String> files = new ArrayList<String>();
		FileUtil.readLines(filelist, files);

		// 1. get model parameters
		ArrayList<String> modelSettings = new ArrayList<String>();
		getModelPara(modelParas, modelSettings);
		int A_all = Integer.parseInt(modelSettings.get(0));
		float alpha_g = Float.parseFloat(modelSettings.get(1));
		float beta_word = Float.parseFloat(modelSettings.get(2));
		float beta_b = Float.parseFloat(modelSettings.get(3));
		float gamma = Float.parseFloat(modelSettings.get(4));
		int nIter = Integer.parseInt(modelSettings.get(5));
		System.err.println("Topics:" + A_all + ", alpha_g:" + alpha_g
				+ ", beta_word:" + beta_word + ", beta_b:" + beta_b
				+ ", gamma:" + gamma + ", iteration:" + nIter);
		modelSettings.clear();

		new Stopwords();
		Stopwords.addStopfile(stopfile);
		int outputTopicwordCnt = 30;
		int outputBackgroundwordCnt = 50;

		String outputWordsInTopics = outputDir + "WordsInTopics.txt";
		String outputBackgroundWordsDistribution = outputDir
				+ "BackgroundWordsDistribution.txt";
		String outputTextWithLabel = outputDir + "/TextWithLabel/";

		if (!new File(outputTextWithLabel).exists())
			FileUtil.mkdir(new File(outputTextWithLabel));

		
		// 2. get documents (users)
		HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
		ArrayList<user> users = new ArrayList<user>();
		ArrayList<String> uniWordMap = new ArrayList<String>();

		for (int i = 0; i < files.size(); i++) {
			user tweetuser = new user(dataDir + files.get(i), files.get(i),
					wordMap, uniWordMap);
			users.add(tweetuser);
		}

		// ComUtil.printHash(wordMap);
		if (uniWordMap.size() != wordMap.size()) {
			System.out.println(wordMap.size());
			System.out.println(uniWordMap.size());
			System.err
					.println("uniqword size is not the same as the hashmap size!");
			System.exit(0);
		}

		// output wordMap and itemMap
		FileUtil.writeLines(outputDir + "wordMap.txt", wordMap);
		FileUtil.writeLines(outputDir + "uniWordMap.txt", uniWordMap);
		int uniWordMapSize = uniWordMap.size();
		wordMap.clear();
		uniWordMap.clear();
		// uniItemMap.clear();

		// 3. run the model
		Model model = new Model(A_all, users.size(), uniWordMapSize, nIter,
				alpha_g, beta_word, beta_b, gamma);
		model.intialize(users);
		// model.fake_intialize(users);
		model.estimate(users, nIter);

		// 4. output model results
		System.out.println("Record Topic Distributions/Counts");
		model.outputTopicDistributionOnUsers(outputDir, users);
		System.out.println("read uniwordmap");
		FileUtil.readLines(outputDir + "uniWordMap.txt", uniWordMap);

		try {
			model.outputTextWithLabel(outputTextWithLabel, users, uniWordMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("write text with labels done");
		// model.outputTopicCountOnTime(outputTopicsCountOnTime);
		users.clear();

		try {
			model.outputWordsInTopics(outputWordsInTopics, uniWordMap,
					outputTopicwordCnt);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			model.outputBackgroundWordsDistribution(
					outputBackgroundWordsDistribution, uniWordMap,
					outputBackgroundwordCnt);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		System.out.println("Record Background done");

		System.out.println("Final Done");
	}

	private static void getModelPara(String modelParas,
			ArrayList<String> modelSettings) {
		modelSettings.clear();
		// T , alpha , beta , gamma , iteration , saveStep, saveTimes
		modelSettings.clear();
		// add default parameter settings
		modelSettings.add("40");
		modelSettings.add("1.25");
		modelSettings.add("0.01");
		modelSettings.add("0.01");
		modelSettings.add("20");
		modelSettings.add("20");

		ArrayList<String> inputlines = new ArrayList<String>();
		FileUtil.readLines(modelParas, inputlines);
		for (int i = 0; i < inputlines.size(); i++) {
			int index = inputlines.get(i).indexOf(":");
			String para = inputlines.get(i).substring(0, index).trim()
					.toLowerCase();
			String value = inputlines.get(i)
					.substring(index + 1, inputlines.get(i).length()).trim()
					.toLowerCase();
			switch (ModelParas.valueOf(para)) {
			case topics:
				modelSettings.set(0, value);
				break;
			case alpha_g:
				modelSettings.set(1, value);
				break;
			case beta_word:
				modelSettings.set(2, value);
				break;
			case beta_b:
				modelSettings.set(3, value);
				break;
			case gamma:
				modelSettings.set(4, value);
				break;
			case iteration:
				modelSettings.set(5, value);
				break;
			default:
				break;
			}
		}
	}

	public enum ModelParas {
		topics, alpha_g, beta_word, beta_b, gamma, iteration;
	}
}
