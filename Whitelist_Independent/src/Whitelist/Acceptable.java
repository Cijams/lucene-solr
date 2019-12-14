// --------------------------Whitelist_Interface.java-------------------------
// Author: ahnaka
// December 12, 2019
// ---------------------------------------------------------------------------
// Purpose: The utility class that interacts with Whitelist. This object will
// generate the accepted files from 'whitelist.txt'. In addition, it will give
// the results for the comparison (of input with the whitelist), it can add
// accepted files to the whitelist, or remove accepted files from the whitelist. 
// ---------------------------------------------------------------------------
package Whitelist;

import java.io.*;
import java.util.*;

// A utility class Acceptable: this class adds the accepted content that the
// whitelist will accept. Overall, the actual check is too simplistic 
// for a whitelist, we would need to in reality have some kind of check
// for the inside of the file to detect either legitimate, or have a
// blacklist to outright prevent malicious scripts.
public class Acceptable {
	
	// A List of initially acceptable content that lucene solr would
	// view as legitimate input, checks the back appendages of a file
	List<String> accept = new LinkedList<String>();
	
	// Builds the list of acceptable content
	public List<String> generateAcceptable() throws IOException {
		
		File whitelist_content = new File("src/accept/whitelist.txt");
		BufferedReader br = new BufferedReader(new FileReader(whitelist_content));
		
		// Utilize a reader to test the content 
		String input;
		while ((input = br.readLine()) != null) {
			accept.add(input);
		}
		br.close();
		
		return accept; // return a copy of accepted values for whitelist_interface
	} // end of generateAcceptable
	
	// For the user's benefit, show the list of acceptable files
	public void showAcceptables() {
		System.out.print("Acceptable Files: ");
		for (int i = 0; i < accept.size(); i++) {
			String end = accept.get(i);
			System.out.print(end + " ");
		}
		System.out.println();
	} // end of showAcceptables
	
	// Compare the Whitelist input files with the user's input files
	public boolean compare(String input) {
		
		// Variables for comparison
		String input_sub = "";
		boolean file_ext = false;
		
		// Obtain the end of text file
		for(int length = (input.length() - 1); length >= 0; length--) {
			if (input.charAt(length) == '.') {
				file_ext = true;
			}
			if (file_ext == true) {
				input_sub = input.substring(length);
				break;
			}
		}
		
		// Compare the extension with the whitelist accepted values
		for(int i = 0; i < accept.size(); i++) {
			if(accept.get(i).contentEquals(input_sub)) {
				System.out.println(input + " is a valid file.");
				return true;
			}
		}
		System.out.println(input + " is an invalid file.");
		return false;
	} // end of compare
	
	// Add on a back appendage to the whitelist
	public String addToList(String extension) {
		accept.add(extension);
		return extension + " added to whitelist";
	} // end of addToList
	
	// Remove a back appendage from the whitelist, check
	// if its a viable remove option
	public String removeFromList(String extension) {
		boolean removal = false;
		for (int i = 0; i < accept.size(); i++) {
			
			// At the first instance of removal, return the result
			if (accept.get(i).contentEquals(extension)) {
				accept.remove(i);
				removal = true;
			}
			if (removal == true) { break; }
		}
		
		// Inform interface of removal (or no removal)
		if (removal == true) {
			return (extension + " removed from whitelist");
		} else {
			return (extension + " not in whitelist");
		}
	} // end of removeFromList
	
} // End of Acceptable
