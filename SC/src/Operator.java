
public class Operator extends Symbol implements Comparable<Operator> {
	byte hierarchy;

	Operator(String in) {
		this.symbol = in;
		if (in.equals("(")) {
			this.hierarchy = 5; 
		}
		if (in.equals("^")) {
			this.hierarchy = 4;
		}
		if (in.equals("~")) {
			this.hierarchy = 3;
		}
		if ("*%/".contains(in)) {
			this.hierarchy = 2;
		}
		if ("+-".contains(in)) {
			this.hierarchy = 1;
		}
	}

	@Override
	public int compareTo(Operator o) {
		// TODO Auto-generated method stub
		if (this.symbol.equals("^")&&o.symbol.equals("^"))
			return -1;
		if (this.symbol.equals("("))
			return -1;
		return this.hierarchy - o.hierarchy;
	}

	@Override
	public String toString() {
		return this.symbol;
	}
}
