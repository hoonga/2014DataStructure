import java.util.*;
import java.io.*;

class Subway {
	HashMap<String, HashSet<Edge>> names;
	HashMap<String, HashSet<Edge>> numbers;
	HashMap<String, String> lines;

	Subway(BufferedReader data) throws IOException {
		String s;
		while ((s = data.readLine()) != null) {
			String[] in = s.split(" ");
			if (this.names.containsKey(in[1])) {
				this.numbers.put(in[0], this.names.get(in[1]));
			} else {
				HashSet<Edge> station = new HashSet<Edge>();
				this.names.put(in[1], station);
				this.numbers.put(in[0], station);
			}
			lines.put(in[0], in[2]);
		}
		while ((s = data.readLine()) != null) {
			String[] in = s.split(" ");
			Edge e = new Edge(in[1], new Integer(in[2]), this.lines.get(in[0]));
			this.numbers.get(new Integer(in[0])).add(e);
		}
		data.close();
	}

	void main(String[] args) {
		try {
			BufferedReader data = new BufferedReader(new FileReader(args[0]));
			BufferedReader input = new BufferedReader(new InputStreamReader(
					System.in));
			Subway s = new Subway(data);
			s.processInput(input);
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	String[] find(String start, String dest) {
		Queue<String> q = new LinkedList<String>();
		HashMap<String, Integer> d = new HashMap<String, Integer>();
		HashMap<String, String> comesbefore = new HashMap<String, String>();
		Stack<String> result = new Stack<String>();
		d.put(start, 0);
		q.offer(start);
		while (!q.isEmpty()) {
			String cur_station = q.poll();
			if (cur_station.equals(dest)) {
				while (comesbefore.containsKey(cur_station)) {
					result.push(cur_station);
					cur_station = comesbefore.get(cur_station);
				}
				return result.toArray(new String[result.size()]);
			} else {
				Iterator<Edge> i = this.names.get(cur_station).iterator();
				while (i.hasNext()) {
					Edge e = i.next();
					if (!d.containsKey(e.destination)) {
						comesbefore.put(e.destination, cur_station);
						d.put(e.destination, d.get(cur_station) + e.cost);
					}
				}
			}
		}
		return null;
	}

	String[] findLeast(String start, String dest) {
	}

	void processInput(BufferedReader input) throws IOException {
		String in;
		while ((in = input.readLine()).matches("QUIT")) {
			String[] s = in.split(" ");
			if (s.length == 3)
				s = this.findLeast(s[0], s[1]);
			else if (s.length == 2)
				s = this.find(s[0], s[1]);
			else
				System.exit(2);
			print(s);
		}
		input.close();
	}

	private static void print(String[] in) {
		String result = "";
		for (int i = in.length - 1; i > 0; i++) {
			result += (in + " ");
		}
		System.out.println(result + in[0]);
	}
}

class Edge {
	String destination;
	Integer cost;
	String line;

	Edge(String destination, Integer cost, String line) {
		this.destination = destination;
		this.cost = cost;
		this.line = line;
	}
}