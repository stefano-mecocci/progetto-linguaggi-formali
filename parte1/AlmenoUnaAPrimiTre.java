/* ESERCIZIO 1.7 di laboratorio */
public class AlmenoUnaAPrimiTre {
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
            state = 4;
          else
            state = -1;
          break;
        
        case 1:
          if (ch == 'b')
            state = 1;
          else if (ch == 'a')
            state = 2;
          else
            state = -1;
          break;
        
        case 2:
          if (ch == 'a')
            state = 3;
          else if (ch == 'b')
            state = 2;
          else
            state = -1;
          break;
        
        case 3:
          if (ch == 'a' || ch == 'b')
            state = 3;
          else
            state = -1;
          break;

        case 4:
          if (ch == 'a')
            state = 1;
          else if (ch == 'b')
            state = 5;
          else
            state = -1;
          break;

        case 5:
          if (ch == 'a')
            state = 2;
          else if (ch == 'b')
            state = 6;
          else
            state = -1;
          break;

        case 6:
          if (ch == 'a' || ch == 'b')
            state = 6;
          else
            state = -1;
          break;
      }
    }

    return state == 1 || state == 2 || state == 3;
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
    test("abbbbbb");
    test("bbaba");
    test("baaaaaaa");
    test("aaaaaaa");
    test("a");
    test("ba");
    test("bba");
    test("aa");
    test("aaa");
    test("aba");
    test("bbabbbbbbbb");

    System.out.println();

    test("bbbababab");
    test("b");
    test("");
    test("bb");
  }
}
