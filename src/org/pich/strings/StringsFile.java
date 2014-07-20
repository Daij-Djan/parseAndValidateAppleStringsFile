//
//Copyright Dominik Pich ;)
//

package org.pich.strings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;

public class StringsFile {
	private Charset encoding;
	private String path;
	private HashMap<String, String> entries;

	public StringsFile(String path, Charset encoding) {
		this.path = path;
		this.encoding = encoding;
	}

	public HashMap<String, String> getEntries() throws Exception {
		if (entries == null) {
			entries = this.parseFile();
		}
		return entries;

	}

	private HashMap<String, String> parseFile() throws Exception {
		HashMap<String, String> entries = new HashMap<String, String>();

		File file = new File(path);
		FileInputStream is = new FileInputStream(file);
		InputStreamReader isReader = new InputStreamReader(is, encoding);
		BufferedReader br = new BufferedReader(isReader);
		int i = 0;

		String line;
		while ((line = br.readLine()) != null) {
			// count it
			i++;

			// trim whitespaces
			line = line.trim();

			parseLine(entries, i, line);
		}
		br.close();

		return entries;
	}

	private void parseLine(HashMap<String, String> entries, int i, String line)
			throws Exception {
		// do a BASIC integrity check of the line first
		if (!line.startsWith("\"")) {
			throw new Exception("Cant parse line " + i
					+ ": not seeing \" at the start");
		}
		if (!line.endsWith("\";")) {
			throw new Exception("Cant parse line " + i
					+ ": not seeing \"; at the end");
		}

		// the elements we are interested in
		StringBuffer key = new StringBuffer();
		StringBuffer value = new StringBuffer();
		int c = 1; // skip first "

		// parse key
		c = parseField(line, key, c);
		c++; // skip next "

		// skip till after next " BUT make sure there is only space and 1 space
		boolean haveSeenEquals = false;
		while (line.charAt(c) != '\"' && c < line.length()) {
			if (line.charAt(c) != ' ') {
				if (line.charAt(c) == '=') {
					if (!haveSeenEquals) {
						haveSeenEquals = true;
						c++;
						continue;
					}
				}
				throw new Exception("Cant parse line " + i
						+ ": found unexpected char " + line.charAt(c)
						+ " while only exspecing space and one = sign");
			}
			c++;
		}
		c++;
		; // skip next "

		// parse value
		c = parseField(line, value, c);
		c++; // skip next "

		// check if we DID reach the end ok
		if (line.charAt(c) != ';') {
			throw new Exception(
					"Cant parse line "
							+ i
							+ ": it isnt welformed, else I should end on a ; -- instead all I got was "
							+ line.substring(0, c));
		}

		//log it (just as a test)
		System.out.println("||" + key.toString() + "||" + value.toString() + "||");
		
		// add it to the table
		entries.put(key.toString(), value.toString());
	}

	private int parseField(String line, StringBuffer field, int c) {
		// just get all till we hit an unescaped "
		while (line.charAt(c) != '\"' && c < line.length()) {
			field.append(line.charAt(c));
			if (line.charAt(c) == '\\') {
				c++;
				field.append(line.charAt(c));
			}
			c++;
		}
		return c;
	}
}
