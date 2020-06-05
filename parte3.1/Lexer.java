import java.io.*;
import java.util.*;

public class Lexer {
  public static int line = 1;
  private char peek = ' ';

  private void readch(BufferedReader br) {
    try {
      peek = (char)br.read();
    } catch (IOException exc) {
      peek = (char)-1; // ERROR
      System.out.println(((int)peek));
    }
  }

  public Token lexical_scan(BufferedReader br) {
    while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
      if (peek == '\n')
        line++;

      readch(br);
    }

    switch (peek) {
    case '+':
      peek = ' ';
      return Token.plus;

    case '-':
      peek = ' ';
      return Token.minus;
      
    case '*':
      peek = ' ';
      return Token.mult;
      
    case '/':
      peek = ' ';
      return Token.div;
      
    case '(':
      peek = ' ';
      return Token.lpt;
      
    case ')':
      peek = ' ';
      return Token.rpt;

    case (char)-1:
      return new Token(Tag.EOF);

    default:
      if (Character.isDigit(peek)) {
        NumberTok number = new NumberTok();

        while (Character.isDigit(peek)) {
          number.addCipher(peek);
          readch(br);
        }

        number.convert();
        return number;
      } else {
        System.err.println("Erroneous character: " + peek);
        return null;
      }
    }
  }

  public static void main(String[] args) {
    Lexer lexer = new Lexer();
    String path = "hello.lft";

    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      Token tok;

      do {
        tok = lexer.lexical_scan(br);
        System.out.printf("Scan: %s\n", tok);
      } while (tok.tag != Tag.EOF);

      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}