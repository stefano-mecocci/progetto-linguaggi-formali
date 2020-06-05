/* ESERCIZIO 1.4 di laboratorio */
public class NumeroCognomeConSpazi {
  public static boolean isNumber(char ch) {
    return Character.isDigit(ch);
  }

  public static boolean isEvenNumber(char ch) {
    if (isNumber(ch)) {
      return ((int) ch) % 2 == 0;
    } else {
      return false;
    }
  }

  public static boolean isOddNumber(char ch) {
    if (isNumber(ch)) {
      return ((int) ch) % 2 == 1;
    } else {
      return false;
    }
  }

  public static boolean isLetter(char ch) {
    boolean isLowercase = ch >= 97 && ch <= 122;
    boolean isUppercase = ch >= 65 && ch <= 90;

    return isLowercase || isUppercase;
  }

  public static boolean isAtoK(char ch) {
    boolean isLowercase = ch >= 97 && ch <= 107;
    boolean isUppercase = ch >= 65 && ch <= 75;

    return isLowercase || isUppercase;
  }

  public static boolean isLtoZ(char ch) {
    boolean isLowercase = ch >= 108 && ch <= 122;
    boolean isUppercase = ch >= 76 && ch <= 90;

    return isLowercase || isUppercase;
  }

  public static boolean scan(String s) {
    int state = 0;
    int i = 0;

    while (state >= 0 && i < s.length()) {
      final char ch = s.charAt(i++);

      switch (state) {
        case 0:
          if (isEvenNumber(ch))
            state = 1;
          else if (isOddNumber(ch))
            state = 2;
          else if (isLetter(ch))
            state = 6;
          else if (ch == ' ')
            state = 0;
          else
            state = -1;
          break;

        case 1:
          if (isEvenNumber(ch))
            state = 1;
          else if (isOddNumber(ch))
            state = 2;
          else if (ch == ' ')
            state = 3;
          else if (isAtoK(ch))
            state = 5;
          else if (isLtoZ(ch))
            state = 6;
          else
            state = -1;
          break;

        case 2:
          if (isEvenNumber(ch))
            state = 1;
          else if (isOddNumber(ch))
            state = 2;
          else if (isLtoZ(ch))
            state = 5;
          else if (isAtoK(ch))
            state = 6;
          else
            state = -1;
          break;

        case 3:
          if (ch == ' ')
            state = 3;
          else if (isAtoK(ch))
            state = 5;
          else if (isLtoZ(ch) || isNumber(ch))
            state = 6;
          else
            state = -1;
          break;

        case 4:
          if (ch == ' ')
            state = 4;
          else if (isLtoZ(ch))
            state = 5;
          else if (isAtoK(ch) || isNumber(ch))
            state = 6;
          else
            state = -1;
          break;

        case 5:
          if (isLetter(ch) || ch == ' ')
            state = 5;
          else if (isNumber(ch))
            state = 6;
          else
            state = -1;
          break;

        case 6:
          if (isLetter(ch) || isNumber(ch) || ch == ' ')
            state = 6;
          else
            state = -1;
          break;
      }
    }

    return state == 5; // state == 6 indica errore
  }

  public static void test(String input) {
    String result = scan(input) ? "OK" : "NOPE";

    if (input.endsWith(" ") || input.startsWith(" ")) {
      System.out.printf("%4s \"%s\"\n", result, input);
    } else {
      System.out.printf("%4s %s\n", result, input);
    }
  }

  public static void main(String[] args) {
    test("2Bianchi");
    test("122B");
    test("884579Mecocci");
    test("123456De Gasperi");
    test("  123456De Gasperi  ");

    System.out.println();

    test("1234 56Bianchi");
    test("Rossi");
    test("654322");
    test("884579Fecocci");
  }
}
