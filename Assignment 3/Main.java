package edu.iastate.cs228.hw3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author raghavk
 */

/**
 * contains the main method to store the encoding string in a tree and decode the message
 */
public class Main {

	public static void main(String[] args) {

		String message = "";
		boolean check = true;
		Scanner s = new Scanner(System.in);

		while (check) {
			try {
				
				System.out.print("Please enter filename to decode: ");
				File f = new File(s.next());
				System.out.println();
				s = new Scanner(f);
				check = false;
				String encoding = s.nextLine();
				StringBuilder e = new StringBuilder(encoding);
				String line2 = s.nextLine();
				
				for (int i = 0; i < line2.length(); i++) {
					if (line2.charAt(i) != '1' || line2.charAt(i) != '0') {
						e.append("\n").append(line2);
						if (s.hasNextLine()) {
							message = s.nextLine();
						} else {
							message = line2;
						}
						break;
					}
				}

				encoding = e.toString();
				s.close();
				MsgTree tree = new MsgTree(encoding);

				System.out.println("character  code");
				System.out.println("-----------------------");
				MsgTree.printCodes(tree, "");
				System.out.println();
				System.out.println("MESSAGE: ");

				tree.decode(tree, message);
				System.out.println();
				MsgTree.statistics(message);

			} catch (FileNotFoundException e) {
				System.out.println("Enter a valid filename. Try again.");
			}
		}
	}

}
