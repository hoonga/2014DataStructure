import java.util.*;
import java.util.regex.*;
import java.io.*;

public class CalculatorTest {
	public static final String QUIT_COMMAND = "q";
	public static final String MSG_ERROR = "ERROR";
	public static final Pattern EXPRESSION = Pattern.compile("([(].*[)]|\\d+|[+^*/%-])");
	public static final Pattern PARENTHESIS = Pattern.compile("[(](.*)[)]");
	
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
		String previous = "a";
		while (input.length()!= 0){
			Matcher m = EXPRESSION.matcher(input);
			if(!m.find())
				throw new IllegalArgumentException();
			else{
				input = input.substring(input.indexOf(m.group())+m.group().length(),input.length());
				if(m.group().matches("\\d+")){
					if (previous.matches("\\d+"))
						throw new IllegalArgumentException();
					result += m.group() + " ";
					previous = m.group();
				}
				else if (m.group().matches("[+^*/%-]")){
					if (previous.matches("[+^*/%-]")&&m.group().equals("-")){
						result += "~ ";
						previous = m.group();
					}
					else if (previous.matches("[+^*/%-]")&&!m.group().equals("-"))
						throw new IllegalArgumentException();
					else if (previous.matches("\\d+")){
						Operator op = new Operator(m.group());
						if (opstack.isEmpty()){
							opstack.push(op);
							previous = m.group();
						} else {
							if(opstack.peek().compareTo(op)>0){
								result += opstack.pop().toString() + " ";
								opstack.push(op);
							} else
								opstack.push(op);
							previous = m.group();
						}
					}
				} else if (m.group().matches("[(].*[)]")){
					m = PARENTHESIS.matcher(m.group(1));
					if (m.matches()){
						result += toPostfix(m.group(1)) + " ";
						previous = "0";
					}
					else
						throw new IllegalArgumentException();
				} else
					throw new IllegalArgumentException();
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