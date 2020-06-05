/* ESERCIZIO 1.1 di laboratorio */
public class TreZeri {
  public static boolean scan(String s) {
    int state = 0;
    int i = 0;

    while (state >= 0 && i < s.length()) {
      final char ch = s.charAt(i++);

      switch (state) {
        case 0:
          if (ch == '0')
            state = 1;
          else if (ch == '1')
            state = 0;
          else
            state = -1;
          break;

        case 1:
          if (ch == '0')
            state = 2;
          else if (ch == '1')
            state = 0;
          else
            state = -1;
          break;

        case 2:
          if (ch == '0')
            state = 3;
          else if (ch == '1')
            state = 0;
          else
            state = -1;
          break;

        case 3:
          if (ch == '0' || ch == '1')
            state = 3;
          else
            state = -1;
          break;
      }
    }

    return state == 3;
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
    test("000");
    test("10001");

    System.out.println();

    test("");
    test("0100");
  }
}
