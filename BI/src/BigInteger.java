import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BigInteger {
	public static final String QUIT_COMMAND = "quit";
	public static final String MSG_INVALID_INPUT = "입력이 잘못되었습니다.";
	public static final byte max = 10;
	byte[] number;
	byte sign = 1;

	public static final Pattern EXPRESSION_PATTERN = Pattern.compile("([+-]{0,1}[0-9]{1,})([+*-])([+-]{0,1}[0-9]{1,})");

	public BigInteger(byte[] num1, byte sign) { // an initializer with array of byte and its sign
		this.number = new byte[num1.length];
		this.sign = sign;
		for (int i = 0; i < num1.length; i++) {
			this.number[i] = (byte) (sign * num1[i]);
		}
	}

	public BigInteger(String s) { // initialize from string
		switch ("+-".indexOf(s.charAt(0))) {
		case 1:
			this.sign = (byte) -1;
		case 0:
			s = s.substring(1, s.length());
		}
		this.number = new byte[s.length()];
		for (int i = 0; i < s.length(); i++) {
			this.number[i] = Byte.valueOf(s.substring(s.length()-i-1, s.length()-i));
		}
	}

	public BigInteger add(BigInteger other) {
		byte sign;
		byte[] bigger = this.number.length > other.number.length ? this.number : other.number;
		byte[] smaller = this.number.length < other.number.length ? this.number : other.number;
		byte[] result = new byte[bigger.length];
		if (this.number.length == other.number.length) {
			sign = this.number[this.number.length - 1] > other.number[other.number.length - 1] ? this.sign : other.sign;
		} else {
			sign = this.number.length > other.number.length ? this.sign : other.sign;
		}
		for (int i = 0; i < bigger.length; i++) {
			for (; i < smaller.length; i++)
				result[i] = (byte) (this.sign * this.number[i] + other.sign * other.number[i]);
			if (bigger.length == i)
				break;
			result[i] = (byte) (sign * bigger[i]);
		}
		for (int i = 0; i < result.length - 1; i++) {
			if (sign * result[i] < 0) {
				result[i + 1] -= sign;
				result[i] += sign * max;
			} else if (sign * result[i] >= max) {
				result[i] -= sign * max;
				result[i + 1] += sign;
			}
		}
		return new BigInteger(result, sign);
	}

	public BigInteger multiply(BigInteger other) {
		byte sign = (byte) (this.sign * other.sign);
		byte[] result = new byte[this.number.length + other.number.length];
		for (int i = 0; i < this.number.length; i++) {
			for (int j = 0; j < other.number.length; j++){
				result[i + j] += sign*this.number[i] * other.number[j];
				if(result[i+j]*sign >= max){
					result[i + j + 1] += result[i + j] / max;
					result[i + j] %= max;
				}
			}
		}
		return new BigInteger(result, sign);
	}

	public BigInteger negative() {
		this.sign = (byte) -this.sign;
		return this;
	}

	@Override
	public String toString() {
		String result = "";
		for (int i = this.number.length - 1; i >= 0; i--) {
			result += String.valueOf(this.number[i]); 
		}
		while (result.indexOf("0") == 0 && result.length() > 1)
			result = result.substring(1, result.length());
		return (this.sign > 0 || result.equals("0") ? "" : "-") + result;
	}

	static BigInteger evaluate(String input) throws IllegalArgumentException {
		Matcher m = EXPRESSION_PATTERN.matcher(input);
		m.find();
		BigInteger left = new BigInteger(m.group(1)), right = new BigInteger(m.group(3));
		switch ("+-*".indexOf(m.group(2))) {
		case 0:
			left = left.add(right);
			break;
		case 1:
			left = left.add(right.negative());
			break;
		case 2:
			left = left.multiply(right);
			break;
		}
		return left;
	}

	public static void main(String[] args) throws Exception {
		try (InputStreamReader isr = new InputStreamReader(System.in)) {
			try (BufferedReader reader = new BufferedReader(isr)) {
				boolean done = false;
				while (!done) {
					String input = reader.readLine();
					try {
						done = processInput(input);
					} catch (IllegalArgumentException e) {
						System.err.println(MSG_INVALID_INPUT);
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
			BigInteger result = evaluate(input);
			System.out.println(result.toString());
			return false;
		}
	}

	static boolean isQuitCmd(String input) {
		return input.equalsIgnoreCase(QUIT_COMMAND);
	}
}