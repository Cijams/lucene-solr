// --------------------------Whitelist_Interface.java-------------------------
// Author: ahnaka
// December 12, 2019
// ---------------------------------------------------------------------------
// Purpose: The purpose of the whitelist is to prevent the DIH configuration
// from being a script when the user enters input into Lucene Solr. The
// code add-on should act as a gatekeeper for the solr side from the code
// location solr\build\solr-core\classes\java\org\apache\solr\handler of the file
// by modifying RequestHandleBase.java
// ---------------------------------------------------------------------------
package Whitelist;

import java.io.*;
import java.util.*;

/* The Whitelist_Interface class would act as the main in the QueryParser
 * When implemented properly with the handleRequestBody, it would check
 * check the string portion of SolrQueryRequest req. This main function
 * acts as a test case for the whitelist, compared with test.txt
 * */ 
public class Whitelist_Interface {
	
	// The main portion, generating the Whitelist
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		// Initialize the Whitelist
		Acceptable wList = new Acceptable();
		wList.generateAcceptable();
		
		// Show the accepted files lucene will allow
		wList.showAcceptables();
		
		// Insert the test inputs from a file as a way to simulate
		// an indexed file
		File user_input = new File("src/test/test.txt");
		BufferedReader br = new BufferedReader(new FileReader(user_input));
		
		// Utilize a reader to test the content 
		List<String> content = new LinkedList<String>();
		String input;
		while ((input = br.readLine()) != null) {
			content.add(input);
		}
		br.close();
		
		/* Test Case Part i:
		 * Use the compare method to say whether the file is valid or not
		 * The Acceptable helper method 
		 * */
		boolean verify_req;
		System.out.println("Whitelist Test Case 1: ");
		for (int i = 0; i < content.size(); i++) {
			verify_req = wList.compare(content.get(i));
		}
		System.out.println();
		
		/* Test Case Part ii:
		 * Add to the whitelist to allow .png .doc and .py files
		 * And rerun the comparison
		 * */
		System.out.println("Whitelist Test Case 2: ");
		String add1 = wList.addToList(".png");
		String add2 = wList.addToList(".py");
		String add3 = wList.addToList(".doc");
		System.out.println(add1 + "\n" + add2 + "\n" + add3);
	
		for (int i = 0; i < content.size(); i++) {
			verify_req = wList.compare(content.get(i));
		}
		System.out.println();
		
		/* Test Case Part iii:
		 * Removes from the whitelist .csv .json files
		 * .jetty is also checked in input but shoudln't remove anything 
		 * And rerun the comparison
		 * */
		System.out.println("Whitelist Test Case 3: ");
		String rv1 = wList.removeFromList(".csv");
		String rv2 = wList.removeFromList(".json");
		String rv3 = wList.removeFromList(".jetty");
		System.out.println(rv1 + "\n" + rv2 + "\n" + rv3);
		
		for (int i = 0; i < content.size(); i++) {
			verify_req = wList.compare(content.get(i));
		}
		System.out.println();
		
		// With this Whitelist Interface, normally it would send back
		// a single boolean value over to the object SolrQueryRequest req,
		// which would then either accept or decline the index file
		// from the RequestHandlerBase in line 185.
	}
} // End of Whitelist_Interface
