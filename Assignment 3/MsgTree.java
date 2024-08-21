package edu.iastate.cs228.hw3;

/**
 * @author raghavk 
*/

/**
 * class that models a MsgTree data structure that stores characters in its leaf
 * nodes
 */
public class MsgTree {

	/**
	 * stores the char as described by the encoding scheme
	 */

	public char payloadChar;

	/**
	 * left child of tree
	 */

	public MsgTree left;

	/**
	 * right child of tree
	 */

	public MsgTree right;

	/**
	 * string to store the decoded message after a call to decode()
	 */

	private static String decoded = "";

	/*
	 * static char idx to the tree string for recursive solution, but it is not
	 * strictly necessary
	 */

	private static int staticCharIdx = 0;

	/**
	 * Constructor building the tree from a string
	 * 
	 * @param encodingString
	 */
	public MsgTree(String encodingString) {

		if (staticCharIdx < encodingString.length()) {
			char current = encodingString.charAt(staticCharIdx);
			staticCharIdx++;

			if (current == '^') {
				this.payloadChar = '^';
				left = new MsgTree(encodingString);
				right = new MsgTree(encodingString);
			} else {
				this.payloadChar = current;
				left = null;
				right = null;
			}
		}
	}

	/**
	 * Constructor for a single node with null children
	 * 
	 * @param payloadChar
	 */
	public MsgTree(char payloadChar) {
		this.payloadChar = payloadChar;
		left = null;
		right = null;
	}

	/**
	 * method to print characters and their binary codes
	 * 
	 * @param root
	 * @param code
	 */
	public static void printCodes(MsgTree root, String code) {

		if (root == null) {
			return;
		}

		if (root.payloadChar != '^') {
			System.out.printf("%-11s%s%n", root.payloadChar, code);
		}

		printCodes(root.left, code + "0");
		printCodes(root.right, code + "1");
	}

	/**
	 * method that decodes and prints the message
	 * 
	 * @param codes
	 * @param msg
	 */
	public void decode(MsgTree codes, String msg) {

		if (codes == null) {
			return;
		}

		MsgTree current = codes;
		for (int i = 0; i < msg.length(); i++) {
			char c = msg.charAt(i);
			if (c == '0') {
				current = current.left;
			} else if (c == '1') {
				current = current.right;
			}

			if (current.left == null && current.right == null) {
				decoded += current.payloadChar;
				current = codes;
			}
		}

		System.out.println(decoded);
	}

	/**
	 * method prints message specific statistics after the decoded message is
	 * printed
	 * 
	 * @param encoding
	 */
	public static void statistics(String encoding) {

		int numChars = decoded.length();
		int compressedBits = encoding.length();
		int uncompressedBits = numChars * 16;
		double avgBits = (double) compressedBits / numChars;
		double spaceSavings = (1 - (double) compressedBits / uncompressedBits) * 100;

		System.out.println("STATISTICS:");
		System.out.printf("%-20s%.1f%n", "Avg bits/char:", avgBits);
		System.out.printf("%-20s%d%n", "Total characters:", numChars);
		System.out.printf("%-20s%.1f%%%n", "Space savings:", spaceSavings);

	}

}
