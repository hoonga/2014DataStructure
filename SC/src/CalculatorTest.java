import java.util.*;
import java.util.regex.*;
import java.io.*;

public class CalculatorTest {
	public static final String QUIT_COMMAND = "q";
	public static final String MSG_ERROR = "ERROR";
	public static final Pattern EXPRESSION = Pattern.compile("([(].*[)]|-?\\d+)([*^%/+-])([(].*[)]|-?\\d+)");
	public static final Pattern PARENTHESIS = Pattern.compile("");
	
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
						System.err.println(e);
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
			input = input.replaceAll(" ", "");
			input = toPostfix(input);
			System.out.println(input);
			System.out.println(String.valueOf(calculate(input)));
			return false;
		}
	}

	static long calculate(String input){
		String[] expression = input.split(" ");
		Stack<Long> stack = new Stack<Long>();
		for (int i = 0; i < expression.length; i++){
			if (expression[i].matches("\\d+"))
				stack.push(Long.valueOf(expression[i]));
			else {
				Long ee, er;
				switch("^~*/%+-".indexOf(expression[i])){
				case 0:
					er = stack.pop();
					ee = stack.pop();
					stack.push((long)Math.pow(ee, er));
					break;
				case 1:
					ee = stack.pop();
					stack.push(-ee);
					break;
				case 2:
					er = stack.pop();
					ee = stack.pop();
					stack.push(er*ee);
					break;
				case 3:
					er = stack.pop();
					ee = stack.pop();
					stack.push(ee/er);
					break;
				case 4:
					er = stack.pop();
					ee = stack.pop();
					stack.push(ee%er);
					break;
				case 5:
					er = stack.pop();
					ee = stack.pop();
					stack.push(ee+er);
					break;
				case 6:
					er = stack.pop();
					ee = stack.pop();
					stack.push(ee-er);
					break;
				}
			}
		}
		return stack.pop();
	}
	
	static String toPostfix(String input){
		Stack<Operator> opstack = new Stack<Operator>();
		String result = "";
		while (input.length()!=0){
			Matcher m = EXPRESSION.matcher(input);
			if(m.find()){
				input = input.replace(m.group(), "");
				if (m.group(1).contains("(")){
					m = PARENTHESIS.matcher(m.group(1));
					result += (toPostfix(m.group()) + " ");
				} else
					result += m.group(1);
				Operator op = new Operator(m.group(2));
				if (!opstack.isEmpty()){
					Operator last = opstack.peek();
					if (last.compareTo(op)>=0)
						opstack.push(op);
					else
						result += opstack.pop().toString() + " ";
				}
				if (m.group(3).contains("(")){
					m = PARENTHESIS.matcher(m.group(3));
					result += (toPostfix(m.group()) + " ");
				} else
					result += m.group(3) + " ";
			}
		}
		while (!opstack.isEmpty()){
			result += opstack.pop().toString() + " ";
		}
		return result.trim();
	}

	static boolean isQuitCmd(String input) {
		return input.equalsIgnoreCase(QUIT_COMMAND);
	}
}	

class Operator implements Comparable<Operator>{
	byte hierarchy;
	String symbol;
	Operator(String in){
		if (in.equals("^")){
			this.hierarchy = 4;
			this.symbol = in;
		}
		if (in.equals("~")){
			this.hierarchy = 3;
			this.symbol = in;
		}
		if ("*%/".contains(in)){
			this.hierarchy = 2;
			this.symbol = in;
		}
		if ("+-".contains(in)){
			this.hierarchy = 1;
			this.symbol = in;
		}
	}

	@Override
	public int compareTo(Operator o) {
		// TODO Auto-generated method stub
		return this.hierarchy - o.hierarchy;
	}
	
	@Override
	public String toString(){
		return this.symbol;
	}
}