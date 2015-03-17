import java.util.*;
import java.io.*;

class Subway {
	HashMap<String, Station> names;
	HashMap<Integer, Station> stations;

	Subway(BufferedReader data) throws IOException {
		String s;
		while ((s = data.readLine())!=null) {
			String[] input = s.split("\\d+ [°¡-ÆR]+ .*");
			this.names.put(this.names.getOrDefault(input, null));
		}
		while ((s = data.readLine())!=null){
			s.split("\\d+ \\d+ \\d+");
		}
		data.close();
	}

	void main(String[] args) {
		try {
			BufferedReader data = new BufferedReader(new FileReader(args[0]));
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			Subway s = new Subway(data);
			s.processInput(input);
		} catch (IOException e) {
			
		}
	}

	String find(String station) {
	}

	String findLeast(String station) {
	}
	
	void processInput(BufferedReader input) throws IOException{
		String in;
		while((in=input.readLine()).matches("QUIT")){
			input.close();
		}
	}
}

class Station {
	public HashSet<Edge> edges;
	Integer number;

	Station(Integer number) {
	}
}

class Edge {
	Edge(Station destination) {
	}
}