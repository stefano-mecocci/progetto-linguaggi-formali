/* ESERCIZIO 1.2 di laboratorio */
public class Identificatore {
  public static boolean isLetter(char ch) {
    boolean isLowercase = ch >= 97 && ch <= 122;
    boolean isUppercase = ch >= 65 && ch <= 90;

    return isLowercase || isUppercase;
  }

  public static boolean scan(String s) {
    int state = 0;
    int i = 0;

    while (state >= 0 && i < s.length()) {
      final char ch = s.charAt(i++);

      switch (state) {
        case 0:
          if (isLetter(ch))
            state = 1;
          else if (ch == '_')
            state = 2;
          else if (Character.isDigit(ch))
            state = 3;
          else
            state = -1;
          break;

        case 1:
          if (isLetter(ch) || ch == '_' || Character.isDigit(ch))
            state = 1;
          else
            state = -1;
          break;

        case 2:
          if (isLetter(ch) || Character.isDigit(ch))
            state = 1;
          else if (ch == '_')
            state = 2;
          else
            state = -1;
          break;

        case 3:
          if (isLetter(ch) || ch == '_' || Character.isDigit(ch))
            state = 3;
          else
            state = -1;
          break;
      }
    }

    return state == 1;
  }

  public static void test(String input) {
    String result = scan(input) ? "OK" : "NOPE";

    if (input.equals("")) {
      System.out.printf("%4s ε\n", result);
    } else if (input.endsWith(" ") || input.startsWith(" ")) {
      System.out.printf("%4s \"%s\"\n", result, input);
    } else {
      System.out.printf("%4s %s\n", result, input);
    }
  }

  public static void main(String[] args) {
    test("x");
    test("flag1");
    test("x2y2");
    test("x_1");
    test("lft_lab");
    test("_temp");
    test("x_1_y_2");
    test("x___");
    test("__5");

    System.out.println();
    
    test("5");
    test("");
    test("221B");
    test("123");
    test("9_to_5");
    test("___");
  }
}
