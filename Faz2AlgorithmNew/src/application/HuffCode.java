package application;

public class HuffCode implements Comparable<HuffCode> {
	private char character;
	private int counter;
	private String huffCode;
	private int codeLength;

	public HuffCode(char character) {
		this.character = character;
	}

	public HuffCode(char character, int counter) {
		this.character = character;
		this.counter = counter;
	}

	

	public char getCharacter() {
		return character;
	}

	public int getCounter() {
		return counter;
	}

	public String getHuffCode() {
		return huffCode;
	}

	public int getCodeLength() {
		return codeLength;
	}

	@Override
	public String toString() {
		return "HuffCode{" + "character=" + (int) character + ", counter=" + counter + ", huffCode=" + huffCode
				+ ", codeLength=" + codeLength + '}';
	}

	@Override
	public int compareTo(HuffCode t) {
		return huffCode.compareTo(t.huffCode);
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public void setHuffCode(String huffCode) {
		this.huffCode = huffCode;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}

	public void setCodeLength(int codeLength) {
		this.codeLength = codeLength;
	}
	
}