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
 * based on the paper.
 * 
 * Feel free to contact the following people if you find any
 * problems in the package.
 * 
 * qiming.diao.2010@smu.sg or minghui.qiu.2010@smu.edu.sg
 *
 */

package DataProcessing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Common.Stopwords;
import Common.FileUtil;
import Common.JC;

public class DataPro {

	public static void main(String args[]) throws Exception {
		String base = "D:/Research/Projects/NTUproject/TLDA/data/";
		String name = "ori_batch 2_May 2011";
		// String name = "ori_batch 3_ 2012 May 25-June 25";
		// String name = "ori_batch 4_2012 June 01-30";

		// process tokenized data for applying the model
		String input = base + "Tokens/" + name + "/";
		String output = base + "Data4Model/" + name + "/";
		String filelist = base + "filelist_" + name + ".txt";

		// FileUtil.deleteDirectory(new File(output));
		// FileUtil.mkdir(new File(output));
		//
		// process4appyModel(filelist, input, output);
		// System.out.println("Final Done");
	}

	private static void process4appyModel(String filelist, String input,
			String output) throws Exception {
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<String> lines = new ArrayList<String>();
		FileUtil.readLines(filelist, files);

		for (int i = 0; i < files.size(); i++) {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					output + files.get(i))));

			lines.clear();
			FileUtil.readLines(input + files.get(i), lines);
			for (int j = 0; j < lines.size(); j++) {
				String line = lines.get(j);

				int index = indexOf(Pattern.compile(" 201[0123]"), line);
				if (index > -1) {
					String newline = line.substring(index + 5).trim();
					writer.write(newline + "\n");
				} else {
					if (indexOf(
							Pattern.compile("[\\d]+ . [\\w]+ [\\w]+ [\\d]+ "),
							line) > -1) {
						String newline = line.replaceAll(
								"[\\d]+ . [\\w]+ [\\w]+ [\\d]+ ", "");
						writer.write(newline + "\n");
					} else {
						System.err.println(input + files.get(i));
						System.err.println(line);
					}
				}
			}

			System.out.println(input + files.get(i));
			writer.close();
			// if(i > 1)
			// System.exit(0);
		}
	}

	private static int indexOf(Pattern pattern, String s) {
		Matcher matcher = pattern.matcher(s);
		return matcher.find() ? matcher.start() : -1;
	}
}
