public class NumberTok extends Token {
  private String numberString;
  private int number;

  public NumberTok() {
    super(Tag.NUM);
    this.numberString = "";
  }

  public void addCipher(char cipher) {
    numberString += cipher;
  }

  public int getNumber() {
    return this.number;
  }

  public void convert() {
    this.number = Integer.parseInt(numberString);
  }
}