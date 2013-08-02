package Common;
import java.util.ArrayList;
import jargs.gnu.CmdLineParser;

/* ****************************  JC  *************************************
 * 	This is a class for input parameters. A demo usage is as follows.
 * 	Relative Path: path in System.getProperty("user.dir")
 *  Usage:
 *  1.  JC.setInputOptions(Descry, directory, options, args, property, int i);
 *  	i = 1 for input options; others for specified directory;
 *  property.charAt[i] = 0 means Relative Path; 1 means absolute path
 *  
 *  Demo:
 *  String [] descry = {"Filelist ", "TagMap ", "File direction ", "OuptDir "};
 *  String [] directory = {"/filelist_data.txt", "/TagMap.txt","/sentence/","/output/"};
 *  char [] options = {'f','t','i','o'};
 *  String property = "1111";
 *  new JC();
 *	JC.setInputOptions(Descry, directory, options, args, property, 1);
 *  String fileName = JC.getARG(0);
 *  String TagList = JC.getARG(1);
 *  String dataDir = JC.getARG(2);
 *  String outputFileName = JC.getARG(3);
 *  JC.close();
 * 
 *	JC.setInputOptions(Descry, directory, options, args, property, 1);
 *  Call the func: java -jar [name].jar -f filelist -t tagmap -i dir -o outputDir
 *  
 *	JC.setInputOptions(Descry, directory, options, args, property, 0);
 *  Just execute the program !
 *
 * ************************************************************************/

public class JC {

	public static CmdLineParser clp;
	
	public static String CD;

	public static ArrayList<String> Argums; // 1: description 2: option (-f)

	public JC() {
		CD = System.getProperty("user.dir");
		clp = new CmdLineParser();
		Argums = new ArrayList<String>();
	}
	
	public static ArrayList<String> getArgums() {
		return Argums;
	}

	public static void setArgums(ArrayList<String> argums) {
		Argums = argums;
	}

	public static void setSinArgums(String argums) {
		Argums.add(argums);
	}
	
	public static String getCD() {
		return CD;
	}

	public void setCD(String cD) {
		CD = cD;
	}

	public static void close() {

		for (int i = 0; i < Argums.size(); i += 2)
			System.err.println(Argums.get(i) + " is:   "
				+ Argums.get(i + 1));
		Argums.clear();
	}

	static void setOption(char [] options) {

		for(int i = 0; i < options.length; i++) {
			clp.addStringOption(options[i], options[i]+"");
		}
	}
	
	public static String getARG(int i) {
		return Argums.get(2*i+1);
	}

	private static void printHelp(String[] descrp, char[] options) {
		System.err.println("\nPlease run this file in the following way:");
		System.err.println("java -jar [name].jar -" + options[0] + " " + descrp[0]);
		for(int m = 1; m < descrp.length; m++) {
		System.err.println("                    -" + options[m] + " " + descrp[m]);
		}
	}

	public static void setInputOptions(String[] descrp, String[] directory,
			char[] options, String[] args, String string, int i) {
		if( i == 1) 
			setInputOptions(descrp, options, args, string);
		else
			setInputOptions(descrp, directory, string);
	}
	

	public static void setInputOptions(String[] descrp, char[] options,
			String[] args, String property) {
		if(descrp.length != options.length | descrp.length != property.length()) {
			System.err.println("\n Length of input parameters is not equal ! ");
			System.exit(1); 
		} else {
			setParemeter(descrp, options, args, property);
		}		
	}

	private static void setParemeter(String[] descrp, char[] options,
			String[] a, String property) {
		
		setOption(options);	
		try { 
			clp.parse(a); 
			} catch (CmdLineParser.OptionException e) {
				System.err.println(e.getMessage()); e.printStackTrace();
				printHelp(descrp, options);
				System.exit(1); 
				}
			for(int i = 0; i < descrp.length; i++) {
				CmdLineParser.Option tmp = clp.addStringOption(options[i], options[i]+"");
				Argums.add(descrp[i]);
				// i = 1 means p2 is absolute path, others relative path
				Argums.add((String)clp.getOptionValue(tmp));
				
				if(Argums.get(Argums.size()-1) == null) {
					System.err.print("-" + options[i] + " option is missing !");
					printHelp(descrp, options);
					System.exit(1); 
				} else {
					if(Integer.parseInt(property.charAt(i)+"") == 0)
						Argums.set(Argums.size()-1, getCD() + "/" + Argums.get(Argums.size()-1));
				}
			}
	}

	public static void setInputOptions(String[] descrp, String[] directory,
			String property) {
		if(descrp.length != directory.length | descrp.length != property.length()) {
			System.err.println("\n Length of input parameters is not equal ! ");
			System.exit(1); 
		} else {
			for(int i = 0; i < descrp.length; i++) {
				setParemeter(descrp[i], directory[i], 
						Integer.parseInt(property.charAt(i)+""));
			}
		}		
	}

	public static void setParemeter(String p1, String p2, int i) {
		
		// i = 1 means p2 is absolute path, others relative path
		if (i == 1) {
			setSinArgums(p1);
			setSinArgums(p2);
		} else {
			setSinArgums(p1);
			setSinArgums(getCD() + "/" + p2);
		}
	}
	
}
