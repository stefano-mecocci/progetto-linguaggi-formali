public class NumberTok extends Token {
  private String numberString;
  public int val;

  public NumberTok() {
    super(Tag.NUM);
    this.numberString = "";
  }

  public void addCipher(char cipher) {
    numberString += cipher;
  }

  public void convert() {
    this.val = Integer.parseInt(numberString);
  }
}