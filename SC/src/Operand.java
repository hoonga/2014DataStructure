
public class Operand extends Symbol {
	long value;
	Operand(String input){
		this.symbol = input;
		this.value = Long.valueOf(input); 
	}
}
