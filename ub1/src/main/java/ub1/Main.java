package ub1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Main {

	static final int ADD = 0;
	static final int MINUS = 1;
	static final int DIV = 2;
	static final int MULT = 3;
	static final int INT = 4;
	static final int VAR = 5;
	static final int ASSIGNMENT = 6;

	static HashMap<String, Integer> symbols = new HashMap<String, Integer>();

	static class Token {
		int Type;
		String str;

		Token(String str) {
			this.str = str;
			if (str.matches("-?\\d+")) this.Type = INT;
			if (str.equals("=")) this.Type = ASSIGNMENT;
			if (str.equals("+")) this.Type = ADD;
			if (str.equals("/")) this.Type = DIV;
			if (str.equals("-")) this.Type = MINUS;
			if (str.equals("*")) this.Type = MULT;
			if (str.matches("[a-zA-Z]+\\d*")) this.Type = VAR;
			if (str.equals(".")) System.exit(0);
		}

		int intVal() {
			return Integer.valueOf(str);
		}

		int doMath(int ia, int ib) {
			if (Type == ADD) return ia + ib;
			if (Type == MULT) return ia * ib;
			if (Type == DIV) return ia / ib;
			if (Type == MINUS) return ia - ib;
			throw new RuntimeException("Foo");
		}
	}

	static Integer AusdruckAuswerten3(List<Token> tokens) {
		Stack<Integer> stack = new Stack<Integer>();
		for (int i = tokens.size() - 1; i >= 0; i--) {
			Token t = tokens.get(i);
			if (t.Type == INT){
				stack.push(t.intVal());
			} else if (t.Type == VAR){
				if (symbols.containsKey(t.str)){
					stack.push(symbols.get(t.str));
				} else {
					System.out.println("Variable " + t.str + " undefiniert.");
					return null;
				}
			} else {
				if (stack.size() < 2) {
					System.out.println("Der Ausdruck konnte nicht interpretiert werden.");
					return null;
				}
				int val1 = stack.pop();
				int val2 = stack.pop();
				stack.push(t.doMath(val1, val2));
			}
		}
		return stack.pop();
	}

	public static String interprete2(List<Token> tokens) {
		if (tokens.isEmpty())
			return "";
		else if (tokens.size() > 2 && tokens.get(0).Type == VAR && tokens.get(1).Type == ASSIGNMENT) {
			Integer val = AusdruckAuswerten3(tokens.subList(2, tokens.size()));
			if (val != null)
				symbols.put(tokens.get(0).str, val);
			else
				return "";
		} else {
			Integer val = AusdruckAuswerten3(tokens);
			return val == null ? "" : val.toString();
		}
		return "";
	}

	public static List<Token> scan2(String line) {
		List<Token> tokens = new ArrayList<Token>();
		if (line == null)
			return tokens;
		for (String str : line.split(" "))
			if (!str.trim().isEmpty())
				tokens.add(new Token(str.trim()));
		return tokens;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		while(true)
			System.out.println(interprete2(scan2(s.nextLine())));
	}

}
