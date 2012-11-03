package es.astroide.csvconverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

import org.mozilla.universalchardet.UniversalDetector;

/**
 * 
 * 	CSVconverter.jar
 * 
 * 	CSV file conversion
 * 	
 * 	Any csv file is converted to a '#' separated file
 * 
 * 
 * 	USAGE: csvimporter.jar inputFile outputFile
 * 	
 * 
 * @author Jose Gomez Castaño
 * 			jgcasta@gmail.com
 * 			
 * 			www.astroide.es
 * 			www.meridi.es
 * 
 * 	v.1.0
 *
 */
public class CSVconverter {

	
	/**
	 * @param args inputFile outputFile 
	 * @throws  			
	 * 
	 * Exit Codes: 	0 Success
	 * 				1 FileNotFoundException
	 * 				2 UnsupportedEncodingException
	 * 				3 IOException	
	 */
	public static void main(String[] args) {
	
		
		String path = args[0];
		BufferedReader br = null;
		FileInputStream is = null;
		String line = "";
		
		String pathW = args[1]; 
		
		// get the enconding and character separator
		
		String encoding = getEncoding(path);
		String separator = getSeparator(path);	
		
		try {
			is = new java.io.FileInputStream(path);
			br = new BufferedReader(new InputStreamReader(is,encoding));
			
			BufferedWriter brW = new BufferedWriter(new FileWriter(pathW));
			
			String[] fields = null;


			String tmpLine = "";
			String inLine = "";
			
			while (line != null && !line.equalsIgnoreCase("null")){
				int pos = 0;
				int pos1 = 0;
				int pos2 = 0;
				
				// read lines
				line = br.readLine();
				line = tmpLine + line;
				inLine = line;
				
				// "," char substitution by " " in each field
				if (!separator.equalsIgnoreCase(",")){
					line = line.replaceAll(",", " ");
				}else{
					if (line.indexOf("\"") > -1){

						// checking "," between "\""
						while (line.indexOf("\"") > -1){
							
							pos1 = line.indexOf("\"", pos1);
							pos2 = line.indexOf("\"", pos1 + 1);
	
							if (pos2 > 0){
								String part1 = "";
								String part2 = "";
								String part3 = "";
								
								part1 = line.substring(0, pos1);
								part2 = line.substring(pos1 + 1, pos2);
								part2 = part2.replaceAll(",", " ");
								part3 = line.substring(pos2 + 1, line.length());
								
								line = part1 + part2 + part3;	
								tmpLine = "";
							}else{
								pos1 = 0;
								pos2 = 0;
								tmpLine =  inLine;
								line = "";
							} // from if
						} // from while
					}
				}
				
				if(line.length() > 0 && !line.equalsIgnoreCase("null")){
					fields = line.split(separator);				
					
					// String replacement of 
					String newLine = "";
					newLine = line.replaceAll("\"", "").replaceAll("\\`", "").replaceAll("#", "num");
					newLine = newLine.replaceAll(separator, "#");
					brW.write(newLine);
					brW.write("\n");
					
				}
			}
			brW.close();
			br.close();
			
			System.exit(0);

			
		} catch (FileNotFoundException e1) {
			
			e1.printStackTrace();
			System.exit(1);
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
			System.exit(2);
		} catch (IOException e) {
			
			e.printStackTrace();
			System.exit(3);
		}		
		
	}
	
	/**
	 * 
	 * @param path
	 * @return String -> character separator
	 */
	private static String getSeparator(String path){
		
		String separator = "";
		// tokens you need to parse. Specify more characters if you need
		String[] tok = new String[4];
		
		tok[0] = ",";
		tok[1] = ";";
		tok[2] = "#";
		tok[3] = "\\t";
		
		BufferedReader br = null;
		FileInputStream is = null;
		String encoding = "";
		String lines = "";
		String linea = "";
		
		try {
			is = new java.io.FileInputStream(path);
			br = new BufferedReader(new InputStreamReader(is)); 
			
			String[] fields = null;
			Integer[] numtokens = new Integer[4];
			
			// searching the token
			int temp = 0;
			String parser = "";
			for (int idTok = 0; idTok < tok.length; idTok++){
			
				linea = br.readLine();
				
				fields = linea.split(tok[idTok]);
				numtokens[idTok] = fields.length;
				
				if (numtokens[idTok] > temp){
					temp = numtokens[idTok];
					parser = tok[idTok];
				}
				
			}
			separator = parser;
			br.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
 		return separator;
         
	}

	
	/**
	 * 
	 * @param fileName
	 * @return String encoding, UTF-8 by default
	 */
	private static String getEncoding(String fileName){
	    byte[] buf = new byte[4096];
	    String encod = "";
	    
	    java.io.FileInputStream fis;
		try {
			fis = new java.io.FileInputStream(fileName);
			
			UniversalDetector detector = new UniversalDetector(null);
			
			int nread;
			while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
				  detector.handleData(buf, 0, nread);
			}
			detector.dataEnd();
			String encoding = detector.getDetectedCharset();
		    if (encoding != null) {
		      encod = encoding;
		    } else {
		     encod = "UTF-8";
		    }
		    detector.reset();
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return encod;
	}
}
