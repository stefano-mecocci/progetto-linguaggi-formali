public class DivisibilePerTre {

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
          state = 2;
        else
          state = -1;
        break;

      case 1:
        if (ch == '0')
          state = 1;
        else if (ch == '1')
          state = 2;
        else
          state = -1;
        break;

      case 2:
        if (ch == '0')
          state = 3;
        else if (ch == '1')
          state = 1;
        else
          state = -1;
        break;

      case 3:
        if (ch == '0')
          state = 2;
        else if (ch == '1')
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
      System.out.printf("%4s \"%s\" - %d\n", result, input,
                        Integer.parseInt(input, 2));
    } else {
      System.out.printf("%4s %s - %d\n", result, input,
                        Integer.parseInt(input, 2));
    }
  }

  public static void main(String[] args) {
    test("11");
    test("110");
    test("0");
    test("1001");
    test("100111");
    test("0001111");
    test("0110");
    test("0");

    System.out.println();

    test("1");
    test("");
    test("111");
    test("10000");
  }
}
