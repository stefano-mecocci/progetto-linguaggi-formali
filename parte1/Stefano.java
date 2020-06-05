/* ESERCIZIO 1.9 di laboratorio */
public class Stefano {
  public static boolean scan(String s) {
    int state = 0;
    int i = 0;

    while (state >= 0 && i < s.length()) {
      final char ch = s.charAt(i++);

      switch (state) {
        case 0:
          if (ch == 'S' || ch == 's')
            state = 8;
          else
            state = 1;
          break;

        case 1:
          if (ch == 't' || ch == 'T')
            state = 2;
          else
            state = -1;
          break;

        case 2:
          if (ch == 'e' || ch == 'E')
            state = 3;
          else
            state = -1;
          break;

        case 3:
          if (ch == 'f' || ch == 'F')
            state = 4;
          else
            state = -1;
          break;

        case 4:
          if (ch == 'a' || ch == 'A')
            state = 5;
          else
            state = -1;
          break;

        case 5:
          if (ch == 'n' || ch == 'N')
            state = 6;
          else
            state = -1;
          break;

        case 6:
          if (ch == 'o' || ch == 'O')
            state = 7;
          else
            state = -1;
          break;

        case 7:
          state = -1;
          break;

        case 8:
          if (ch == 't' || ch == 'T')
            state = 9;
          else
            state = 2;
          break;

        case 9:
          if (ch == 'e' || ch == 'E')
            state = 10;
          else
            state = 3;
          break;

        case 10:
          if (ch == 'f' || ch == 'F')
            state = 11;
          else
            state = 4;
          break;

        case 11:
          if (ch == 'a' || ch == 'A')
            state = 12;
          else
            state = 5;
          break;

        case 12:
          if (ch == 'n' || ch == 'N')
            state = 13;
          else
            state = 6;
          break;

        case 13:
          state = 7;
          break;
      }
    }

    return state == 7;
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
    test("Stefano");
    test("@tefano");
    test("S@efano");
    test("St@fano");
    test("Ste@ano");
    test("Stef@no");
    test("Stefa@o");
    test("Stefan@");

    System.out.println();

    test("Ste@an@");
    test("");
  }
}
