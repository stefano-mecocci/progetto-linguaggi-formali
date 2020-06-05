/* ESERCIZIO 1.8 di laboratorio */
public class AlmenoUnaAUltimiTre {
  public static boolean scan(String s) {
    int state = 0;
    int i = 0;

    while (state >= 0 && i < s.length()) {
      final char ch = s.charAt(i++);
      
      switch (state) {
        case 0:
          if (ch == 'a')
            state = 1;
          else if (ch == 'b')
            state = 0;
          else
            state = -1;
          break;

        case 1:
          if (ch == 'a')
            state = 2;
          else if (ch == 'b')
            state = 4;
          else
            state = -1;
          break;

        case 2:
          if (ch == 'a')
            state = 3;
          else if (ch == 'b')
            state = 4;
          else
            state = -1;
          break;

        case 3:
          if (ch == 'a')
            state = 3;
          else if (ch == 'b')
            state = 4;
          else
            state = -1;
          break;

        case 4:
          if (ch == 'a')
            state = 2;
          else if (ch == 'b')
            state = 5;
          else
            state = -1;
          break;

        case 5:
          if (ch == 'a')
            state = 1;
          else if (ch == 'b')
            state = 6;
          else
            state = -1;
          break;

        case 6:
          if (ch == 'b')
            state = 6;
          else if (ch == 'a')
            state = 1;
          else
            state = -1;
          break;
      }
    }

    return state >= 1 && state <= 5;
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
    test("abb");
    test("aba");
    test("aaa");
    test("bba");
    test("bab");
    test("aab");
    test("baa");
    test("ab");
    test("bbaba");
    test("baaaaaa");
    test("a");
    test("ba");
    test("bba");
    test("aa");
    test("bbbababab");
    test("aaab");
    test("aaabb");
    test("babbba");

    System.out.println();

    test("aaabbb");
    test("abbbbbbb");
    test("bbabbbbbbb");
    test("b");
    test("babbb");
  }
}
