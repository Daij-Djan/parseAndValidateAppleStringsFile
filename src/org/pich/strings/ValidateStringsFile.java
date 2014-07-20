package org.pich.strings;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class ValidateStringsFile {

	public static void main(String[] args) {
		try {
			if(args.length!=2) {
				System.out.println("Usage: program PATH_TO_STRING_FILE ENCODING_OF_FILE");
				System.out.println("-- PATH_TO_STRING_FILE is a windows/posix filepath");
				System.out.println("-- ENCODING_OF_FILE is UTF-16, UTF-8, ASCII or DEFAULT");
				return;
			}
			
			//encoding	
			Charset encoding;
			if(args[1].equals("UTF-16")) 
				encoding = StandardCharsets.UTF_16;
			else if(args[1].equals("UTF-8"))
				encoding = StandardCharsets.UTF_8;
			else if(args[1].equals("ASCII"))
				encoding = StandardCharsets.US_ASCII;			
			else //if(args[1].equals("DEFAULT"))
				encoding = Charset.defaultCharset();			
				
			//parse it to a hashmap
			StringsFile stringsFile = new StringsFile(args[0], encoding);
			HashMap<String,String> entries = stringsFile.getEntries();

			System.out.println(args[0] + " parsed ok. " + entries.size() + " entries");
		}
		catch(Exception e) {
			System.out.println(args[0] + " failed to parse");
			System.out.println(e.getMessage());
		}		
	}
}
