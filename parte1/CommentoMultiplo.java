/* ESERCIZIO 1.11 di laboratorio */
public class CommentoMultiplo {
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

  public static boolean scan(String s) {
    int state = 0;
    int i = 0;

    while (state >= 0 && i < s.length()) {
      final char ch = s.charAt(i++);

      switch (state) {
        case 0:
          if (ch == '/')
            state = 1;
          else if (ch == 'a' || ch == '*')
            state = 0;
          else
            state = -1;
          break;

        case 1:
          if (ch == '*')
            state = 2;
          else if (ch == 'a' || ch == '/')
            state = 0;
          else
            state = -1;
          break;

        case 2:
          if (ch == '/' || ch == 'a')
            state = 3;
          else if (ch == '*')
            state = 4;
          else
            state = -1;
          break;

        case 3:
          if (ch == '/' || ch == 'a')
            state = 3;
          else if (ch == '*')
            state = 4;
          else
            state = -1;
          break;

        case 4:
          if (ch == '*')
            state = 4;
          else if (ch == 'a')
            state = 2;
          else if (ch == '/')
            state = 5;
          else
            state = -1;
          break;

        case 5:
          if (ch == '/')
            state = 1;
          else if (ch == '*' || ch == 'a')
            state = 5;
          else
            state = -1;
          break;
      }
    }

    return state == 5 || state == 0;
  }

  public static void main(String[] args) {
    test("aaa/****/aa");
    test("aa/*a*a*/");
    test("aaaa");
    test("/****/");
    test("/*aa*/");
    test("*/a");
    test("a/**/***a");
    test("a/**/***/a");
    test("a/**/aa/***/a");
    test("");

    System.out.println();

    test("/**/***/");
    test("/*/");
    test("aaa/*/aa");
    test("a/**//***a");
    test("aa/*aa");
  }
}
