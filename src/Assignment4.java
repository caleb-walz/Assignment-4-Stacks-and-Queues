import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Assignment4 {

	// codons and corresponding proteins for Question 6
	public static final String[] CODONS = {
			"UUU", "UUC", "UUA", "UUG", "UCU", "UCC", "UCA", "UCG",
			"UAU", "UAC", "UAA", "UAG", "UGU", "UGC", "UGA", "UGG",
			"CUU", "CUC", "CUA", "CUG", "CCU", "CCC", "CCA", "CCG",
			"CAU", "CAC", "CAA", "CAG", "CGU", "CGC", "CGA", "CGG",
			"AUU", "AUC", "AUA", "AUG", "ACU", "ACC", "ACA", "ACG",
			"AAU", "AAC", "AAA", "AAG", "AGU", "AGC", "AGA", "AGG",
			"GUU", "GUC", "GUA", "GUG", "GCU", "GCC", "GCA", "GCG",
			"GAU", "GAC", "GAA", "GAG", "GGU", "GGC", "GGA", "GGG"
	};
	public static final char[] PROTEINS = {
			'F', 'F', 'L', 'L', 'S', 'S', 'S', 'S',
			'Y', 'Y', '.', '.', 'C', 'C', '.', 'W',
			'L', 'L', 'L', 'L', 'P', 'P', 'P', 'P',
			'H', 'H', 'Q', 'Q', 'R', 'R', 'R', 'R',
			'I', 'I', 'I', 'M', 'T', 'T', 'T', 'T',
			'N', 'N', 'K', 'K', 'S', 'S', 'R', 'R',
			'V', 'V', 'V', 'V', 'A', 'A', 'A', 'A',
			'D', 'D', 'E', 'E', 'G', 'G', 'G', 'G'
	};

	// Question 4: Balanced Brackets
	// time: O(n) where n = s.length()
	// space: O(n) where n = s.length()
	public static String isBalanced(String s) {
		// if s is empty, brackets are not balanced
		if (s.isEmpty()) return "NO";

		Stack<Character> stack = new Stack<>();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '{' || c == '[' || c == '(') {
				stack.push(c);
			} else {
				// if stack is empty, then we are missing an opening bracket for this closing bracket
				if (stack.empty()) return "NO";

				// pop the top char off the stack, check for invalid matches
				// if none of the below are true, the brackets match
				char top = stack.pop();
				if (top == '{' && c != '}') return "NO";
				if (top == '[' && c != ']') return "NO";
				if (top == '(' && c != ')') return "NO";
			}
		}

		// if the stack is not empty, we did not have enough closing braces
		//     to match with opening braces, so it is not balanced
		return stack.empty() ? "YES" : "NO";
	}

	// Question 5: DNA to RNA
	// time: O(n) where n = dna.length()
	// space: O(n) where n = dna.length()
	public static String dnaToRna(String dna) {
		String rna = "";
		Queue<Character> dnaQ = new LinkedList<>();
		for (char c : dna.toCharArray()) dnaQ.add(c);
		while (!dnaQ.isEmpty()) {
			if (dnaQ.peek() == 'T') {
				rna += 'U';
				dnaQ.remove();
			} else {
				rna += dnaQ.remove();
			}
		}

		return rna;
	}

	// Question 6: RNA to Proteins
	// time: O(n), where n = rna.length()
	// space: O(n), where n = rna.length()
	public static String rnaToProteins(String rna) {
		String proteins = "";
		Queue<Character> rnaQ = new LinkedList<>();
		for (char c : rna.toCharArray()) rnaQ.add(c);

		while (rnaQ.size() >= 3) {
			String codon = "";
			codon += rnaQ.remove();
			codon += rnaQ.remove();
			codon += rnaQ.remove();

			int index = -1;
			for (int i = 0; i < CODONS.length; i++) {
				if (CODONS[i].equals(codon)) {
					index = i;
					break;
				}
			}

			proteins += PROTEINS[index];
		}

		// add . if there were characters left in rnaQ (this means length was not multiple of 3)
		return rnaQ.isEmpty() ? proteins : proteins + ".";
	}

	// Question 7: Infix to Postfix
	public static String infixToPostfix(String in) {
		String post = "";
		Stack<Character> ops = new Stack<>();
		for (char c : in.toCharArray()) {
			if (c == '(') {
				ops.push(c);
			} else if (c == ')') {
				while (!ops.empty() && ops.peek() != '(') {
					post += ops.pop();
				}
				if (!ops.empty()) ops.pop(); // remove '(' from stack
			} else if (c == '^' || c == '*' || c == '/' || c == '+' || c == '-') {
				while (!ops.empty() && !(ops.peek() == '(') && !takesPrecedence(c, ops.peek())) {
					post += ops.pop();
				}
				ops.push(c);
			} else {
				post += c;
			}
		}

		// pop remaining operators into postfix
		while (!ops.empty()) post += ops.pop();
		return post;
	}

	// Question 7: Infix to Postfix - helper method
	// finds if c1 takes precedence over c2
	private static boolean takesPrecedence(char c1, char c2) {
		// we assume that c1 and c2 are operators
		if (c1 == '^') return true;
		if ((c1 == '*' || c1 == '/') && (c2 != '^' && c2 != '*' && c2 != '/')) return true;

		return false;
	}

	public static void main(String[] args) {
		System.out.println("Testing Balanced Brackets");
		String s = "{[()]}";
		System.out.println("Is \"" + s + "\" balanced: " + isBalanced(s));
		s = "{[(])}";
		System.out.println("Is \"" + s + "\" balanced: " + isBalanced(s));
		s = "{{[[(())]]}}";
		System.out.println("Is \"" + s + "\" balanced: " + isBalanced(s));
		s = "{{[](){}}}";
		System.out.println("Is \"" + s + "\" balanced: " + isBalanced(s));
		s = "]}}";
		System.out.println("Is \"" + s + "\" balanced: " + isBalanced(s));
		s = "{{[";
		System.out.println("Is \"" + s + "\" balanced: " + isBalanced(s));
		s = "";
		System.out.println("Is \"" + s + "\" balanced: " + isBalanced(s));
		System.out.println();

		System.out.println("Testing DNA to RNA");
		s = "AGCTGGGAAACGTAGGCCTA";
		System.out.println("DNA: " + s);
		System.out.println("RNA: " + dnaToRna(s));
		s = "TTTTTTTTTTGGCGCG";
		System.out.println("DNA: " + s);
		System.out.println("RNA: " + dnaToRna(s));
		s = "CTTTGGGACTAGTAACCCATTTCGGCT";
		System.out.println("DNA: " + s);
		System.out.println("RNA: " + dnaToRna(s));
		System.out.println();

		System.out.println("Testing RNA to Proteins");
		s = "AGCUGGGAAACGUAGGCCUA";
		System.out.println("RNA: " + s);
		System.out.println("Proteins: " + rnaToProteins(s));
		s = "UAAAGAGAAGCCAGC";
		System.out.println("RNA: " + s);
		// This prints ".REAS". The example says the output should be ".GREAT",
		//     but when I do the conversion manually I also get ".REAS".
		System.out.println("Proteins: " + rnaToProteins(s));
		System.out.println();

		System.out.println("Testing Infix to Postfix");
		s = "a+b*(c^d-e)^(f+g*h)-i";
		System.out.println("Infix: " + s);
		System.out.println("Postfix: " + infixToPostfix(s));
	}

}
