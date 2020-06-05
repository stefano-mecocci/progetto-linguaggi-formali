public class NumberTok extends Token {
  private String numberString;
  private int value;

  public NumberTok() {
    super(Tag.NUM);
    this.numberString = "";
  }

  public void addCipher(char cipher) {
    numberString += cipher;
  }

  public int getValue() {
    return this.value;
  }

  /* Ho finito di leggere le cifre del numero, lo salvo come int */
  public void done() {
    this.value = Integer.parseInt(numberString);
  }
}