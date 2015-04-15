import java.util.*;
import java.io.*;

public class CalculatorTest {
	public static final String QUIT_COMMAND = "q";
	public static final String MSG_ERROR = "ERROR";

	public static void main(String args[]) throws Exception {
		try (InputStreamReader isr = new InputStreamReader(System.in)) {
			try (BufferedReader reader = new BufferedReader(isr)) {
				boolean done = false;
				while (!done) {
					String input = reader.readLine();
					try {
						done = processInput(input);
					} catch (IllegalArgumentException e) {
						System.err.println(MSG_ERROR);
					}
				}
			}
		}
	}

	static boolean processInput(String input) throws IllegalArgumentException {
		boolean quit = isQuitCmd(input);
		if (quit) {
			return true;
		} else {
			Expression e = new Expression(input,Expression.INFIX);
			String s = e.toPostfix();
			System.out.println(s);
			System.out.println(calculate(s));
			return false;
		}
	}

	static long calculate(String input) {
		if (input==null)
			return 0;
		String[] expression = input.split(" ");
		Stack<Long> stack = new Stack<Long>();
		for (int i = 0; i < expression.length; i++) {
			if (expression[i].matches("\\d+"))
				stack.push(Long.valueOf(expression[i]));
			else {
				Long ee, er;
				switch ("^~*/%+-".indexOf(expression[i])) {
				case 0:
					er = stack.pop();
					ee = stack.pop();
					stack.push((long) Math.pow(ee, er));
					break;
				case 1:
					ee = stack.pop();
					stack.push(-ee);
					break;
				case 2:
					er = stack.pop();
					ee = stack.pop();
					stack.push(er * ee);
					break;
				case 3:
					er = stack.pop();
					ee = stack.pop();
					stack.push(ee / er);
					break;
				case 4:
					er = stack.pop();
					ee = stack.pop();
					stack.push(ee % er);
					break;
				case 5:
					er = stack.pop();
					ee = stack.pop();
					stack.push(ee + er);
					break;
				case 6:
					er = stack.pop();
					ee = stack.pop();
					stack.push(ee - er);
					break;
				}
			}
		}
		return stack.pop();
	}

	static boolean isQuitCmd(String input) {
		return input.equalsIgnoreCase(QUIT_COMMAND);
	}
}