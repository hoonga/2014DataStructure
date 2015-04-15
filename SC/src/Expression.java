import java.util.regex.*;
import java.util.*;

public class Expression {
	static final private Pattern PARENTHESIS = Pattern
			.compile("[(]([^()]*?)[)]");
	static final private Pattern ADDSUB = Pattern.compile("(E)([+-])(E)");
	static final private Pattern MULDIVMOD = Pattern.compile("(E)([*/%])(E)");
	static final private Pattern EXP = Pattern.compile("(E)(\\^)(E)");
	static final private Pattern NEG = Pattern
			.compile("[(]-(E)|^-(E)|[+^*%/-]-(E)");
	static final private Pattern SPACES = Pattern
			.compile("^E\\s+|\\s+E\\s+|\\s+E$");
	static final public int PREFIX = 0;
	static final public int INFIX = 1;
	static final public int POSTFIX = 2;

	Node<Symbol> root;

	Expression(String expression, int fixation) {
		expression = expression.replaceAll("E", "");
		switch (fixation) {
		case PREFIX:
			this.root = fromPrefix(expression);
			break;
		case INFIX:
			this.root = fromInfix(expression);
			break;
		case POSTFIX:
			this.root = fromPostfix(expression);
			break;
		}
	}

	static public boolean isExpression(String expression) {
		expression = expression.replaceAll("\\d+", "E");
		Matcher m;
		while (!expression.equals("E")) {
			if ((m = PARENTHESIS.matcher(expression)).find()) {
				if (isExpression(m.group(1))) {
					expression = expression.replace(m.group(), "E");
					continue;
				} else
					return false;
			}
			if ((m = SPACES.matcher(expression)).find()
					|| (m = NEG.matcher(expression)).find()
					|| (m = EXP.matcher(expression)).find()
					|| (m = MULDIVMOD.matcher(expression)).find()
					|| (m = ADDSUB.matcher(expression)).find()) {
				expression = expression.replace(m.group(), "E");
				continue;
			}
			return false;
		}
		return true;
	}

	static private Node<Symbol> fromPrefix(String prefix) {
		return null;
	}

	// shunting-yard algorithm. the patterns for the token parsing
	static private Pattern p = Pattern.compile("(\\d+|[()+^*%/-])");

	static private Node<Symbol> fromInfix(String infix) {
		if (!isExpression(infix))
			throw new IllegalArgumentException();
		Stack<Node<Symbol>> operands = new Stack<Node<Symbol>>();
		Stack<Node<Symbol>> operators = new Stack<Node<Symbol>>();
		Matcher m;
		Node<Symbol> n = null;
		while (!infix.isEmpty()) {
			if ((m = p.matcher(infix)).find()) {
				infix = infix.substring(infix.indexOf(m.group(1)) + 1,
						infix.length());
				if (m.group(1).matches("\\d+")) {
					n = new Node<Symbol>(new Operand(m.group(1)));
					operands.push(n);
					continue;
				}
				if (m.group(1).matches("[(+^*/%-]")) {
					if (m.group(1).equals("-")) {
						if (!(n != null && n.item instanceof Operand))
							n = new Node<Symbol>(new Operator("~"));
						else
							n = new Node<Symbol>(new Operator("-"));
					} else
						n = new Node<Symbol>(new Operator(m.group(1)));
					if (!operators.isEmpty()) {
						if (((Operator) operators.peek().item)
								.compareTo((Operator) n.item) >= 0) {
							Node<Symbol> op = operators.pop();
							if (op.item.symbol.equals("~")) {
								Node<Symbol> ee = operands.pop();
								op.link(ee, null);
							} else {
								Node<Symbol> er = operands.pop();
								Node<Symbol> ee = operands.pop();
								op.link(ee, er);
							}
							operands.push(op);
						}
					}
					operators.push(n);
				}
				if (m.group(1).equals(")")) {
					while (!(n = operators.pop()).item.symbol.equals("(")) {
						Node<Symbol> er = operands.pop();
						Node<Symbol> ee = operands.pop();
						n.link(ee, er);
						operands.push(n);
					}
					continue;
				}
			}
		}
		while (!operators.isEmpty()) {
			n = operators.pop();
			if (n.item.symbol.equals("~")) {
				Node<Symbol> ee = operands.pop();
				n.link(ee, null);
			} else {
				Node<Symbol> er = operands.pop();
				Node<Symbol> ee = operands.pop();
				n.link(ee, er);
			}
			operands.push(n);
		}
		return operands.pop();
	}

	static private Node<Symbol> fromPostfix(String postfix) {
		return null;
	}

	public String toPrefix() {
		String result = "";
		return result;
	}

	public String toInfix() {
		String result = "";
		return result;
	}

	public String toPostfix() {
		String result = "";
		Stack<Node<Symbol>> visited = new Stack<Node<Symbol>>();
		visited.push(this.root);
		Node<Symbol> prev = null;
		Node<Symbol> current;
		while (!visited.isEmpty()) {
			current = visited.peek();
			if (prev == null || prev.ee == current || prev.er == current) {
				if (current.ee != null) {
					visited.push(current.ee);
				} else if (current.er != null) {
					visited.push(current.er);
				} else {
					result += current.item.symbol + " ";
					current = visited.pop();
				}
			} else if (current.ee == prev) {
				if (current.er != null)
					visited.push(current.er);
				else {
					result += current.item.symbol + " ";
					current = visited.pop();
				}
			} else if (current.er == prev) {
				result += current.item.symbol + " ";
				current = visited.pop();
			}
			prev = current;
		}
		return result;
	}
}

class Node<T> {
	public T item;
	public Node<T> Parent;
	public Node<T> ee = null;
	public Node<T> er = null;

	Node(T item) {
		this.item = item;
	}

	void link(Node<T> ee, Node<T> er) {
		this.ee = ee;
		ee.Parent = this;
		if (er != null) {
			this.er = er;
			er.Parent = this;
		}
	}
}
