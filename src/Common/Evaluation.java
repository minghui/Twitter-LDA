package Common;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Evaluation {

	@SuppressWarnings("deprecation")
	public static void main(String args[]) throws IOException {

		String[] descrp = { "Filelist ", "File direction(/) ", "OuptDir(/) " };
		// String[] directory = { "/data/filelistNew-p2.txt",
		// "/data/tweets-tokenized/", "/data/output/" };
		String[] directory = { "/data/filelist-400.txt",
				"C:/Ubuntu/Study/Data/tweets/tweets-Eng/tweetsNoT/",
				"/data/output/" };
		char[] options = { 'f', 'i', 'o' };
		new JC();
		JC.setInputOptions(descrp, directory, options, args, "010", 0);
		String filelist = JC.getARG(0);
		String dataDir = JC.getARG(1);
		String outputdir = JC.getARG(2);
		JC.close();

		// FileUtil.test();

		ArrayList<String> files = new ArrayList<String>();
		ArrayList<String> inputlines = new ArrayList<String>();
		ArrayList<String> outputlines = new ArrayList<String>();
		ArrayList<String> outputRaw = new ArrayList<String>();
		//ArrayList<String> irregular = new ArrayList<String>();
		//FileUtil.readLines2Lowcase(filelist, files);
		FileUtil.readLines(filelist, files);

		// dataDir = JC.getCD() + "/data/@data/";
		//outputRelData(files, dataDir, outputdir);
		String outputdirRet = outputdir + "/@data/";
		String outputdirRaw = outputdir + "/Raw/";
		FileUtil.mkdir(new File(outputdirRet));
		FileUtil.mkdir(new File(outputdirRaw));

		for (int i = 0; i < files.size(); i++) {
			FileUtil.readLines(dataDir + files.get(i), inputlines);
			for (int j = 0; j < inputlines.size(); j++) {
				if (ISeng(inputlines.get(j))) {
					Pattern MY_PATTERN = Pattern.compile(".*@.+.*");
					Matcher m = MY_PATTERN.matcher(inputlines.get(j));
					if (m.matches())
						outputlines.add(inputlines.get(j));
					else
						outputRaw.add(inputlines.get(j));
				} else {
					System.out.println(inputlines.get(j));
					//irregular.add(files.get(i) + "\t" + j + "\t"
					//	+ inputlines.get(j));
					}
			}
			// output files
			if (outputlines.size() > 0)
				FileUtil.writeLines(outputdirRet + files.get(i), outputlines);
			if (outputRaw.size() > 0)
				FileUtil.writeLines(outputdirRaw + files.get(i), outputRaw);
			//FileUtil.writeLines(outputdir + files.get(i) + ".irr", irregular);
			inputlines.clear();
			outputlines.clear();
			//irregular.clear();
			outputRaw.clear();
		}
		System.out.println("done");
	}

	private static void outputRelData(ArrayList<String> files, String dataDir,
			String outputdir) {
		ArrayList<String> inputlines = new ArrayList<String>();
		ArrayList<String> outputlines = new ArrayList<String>();
		ArrayList<String> newFiles = new ArrayList<String>();
		ArrayList<String> logs = new ArrayList<String>();

		for (int i = 0; i < files.size(); i++) {
			newFiles.add("@" + files.get(i).toLowerCase());
		}
		// dataDir = dataDir + "/@data/";
		outputdir = outputdir + "/user/";
		// FileUtil.deleteDirectory(new File(outputdir));
		//FileUtil.mkdir(new File(outputdir));

		// for(int i = 0; i < 1; i++) {
		for (int i = 0; i < files.size(); i++) {
			System.out.println(files.get(i));
			FileUtil.readLines(dataDir + files.get(i), inputlines);
			outputlines = inputlines;
			ArrayList<String> mentions = new ArrayList<String>();
			ArrayList<String> tweets = new ArrayList<String>();
			ArrayList<String> tokens = new ArrayList<String>();
			// group inputlines by @ mentions
			for (int j = 0; j < inputlines.size(); j++) {
				// String filteredTweet = inputlines.get(j).concat(" ").
				// replaceAll("@[^ ]+ ", " ");
				FileUtil.tokenize(inputlines.get(j), tokens);
				for (int m = 0; m < tokens.size(); m++) {
					if (tokens.get(m).matches("@[.\\w.]+")) {
						// System.out.println(inputlines.get(j));
						// System.out.println(tokens.get(m));

						if (!mentions.isEmpty()
								&& mentions
										.indexOf(tokens.get(m).toLowerCase()) > -1) {
							int index = mentions.indexOf(tokens.get(m)
									.toLowerCase());
							tweets.set(index, tweets.get(index) + "\n"
									+ inputlines.get(j));
						} else {
							mentions.add(tokens.get(m).toLowerCase());
							tweets.add(inputlines.get(j));
						}

						tokens.remove(m);
						m--;
					}
				}
				tokens.clear();
			}

			// output files
			//String outdir = outputdir + "/" + files.get(i) + "/";
			for (int mSize = 0; mSize < mentions.size(); mSize++) {
				if (newFiles.contains(mentions.get(mSize))) {
					//FileUtil.mkdir(new File(outdir));
					logs.add(files.get(i) + mentions.get(mSize) + "\t"
							+ (ComUtil.count(tweets.get(mSize), "\n") + 1));
					FileUtil.writeLine(
							outputdir + files.get(i) + mentions.get(mSize),
							tweets.get(mSize));
				}
			}
			mentions.clear();
			tweets.clear();
			inputlines.clear();
			outputlines.clear();
			tokens.clear();
		}
		if (logs.size() > 0)
			FileUtil.writeLines(outputdir + "all.log", logs);
		logs.clear();
	}

	private static boolean ISeng(String string) {
		Pattern MY_PATTERN = Pattern.compile("[\\w\\p{Punct} ]+");
		Matcher m = MY_PATTERN.matcher(string);
		if (m.matches())
			return true;
		else {
			return false;
		}
	}
}