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

  /* prova a convertire una stringa in keyword o variabile */
  private Word convertToEffectiveWord(String word) {
    Word w;

    if (word.equals(Word.whiletok.lexeme)) {
      w = new Word(Tag.WHILE, word);
    } else if (word.equals(Word.cond.lexeme)) {
      w = new Word(Tag.COND, word);
    } else if (word.equals(Word.when.lexeme)) {
      w = new Word(Tag.WHEN, word);
    } else if (word.equals(Word.elsetok.lexeme)) {
      w = new Word(Tag.ELSE, word);
    } else if (word.equals(Word.then.lexeme)) {
      w = new Word(Tag.THEN, word);
    } else if (word.equals(Word.dotok.lexeme)) {
      w = new Word(Tag.DO, word);
    } else if (word.equals(Word.seq.lexeme)) {
      w = new Word(Tag.SEQ, word);
    } else if (word.equals(Word.print.lexeme)) {
      w = new Word(Tag.PRINT, word);
    } else if (word.equals(Word.read.lexeme)) {
      w = new Word(Tag.READ, word);
    } else {
      w = new Word(Tag.ID, word);
    }

    return w;
  }

  public Token lexical_scan(BufferedReader br) {
    while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
      if (peek == '\n')
        line++;

      readch(br);
    }

    switch (peek) {
    case '!':
      peek = ' ';
      return Token.not;

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
    case ';':
      peek = ' ';
      return Token.semicolon;
    case '(':
      peek = ' ';
      return Token.lpt;
    case ')':
      peek = ' ';
      return Token.rpt;
    case '{':
      peek = ' ';
      return Token.lpg;
    case '}':
      peek = ' ';
      return Token.rpg;

    case '&':
      readch(br);
      
      if (peek == '&') {
        peek = ' ';
        return Word.and;
      } else {
        System.err.println("Erroneous character"
                           + " after & : " + peek);
        return null;
      }

    case '|':
      readch(br);

      if (peek == '|') {
        peek = ' ';
        return Word.or;
      } else {
        System.err.println("Erroneous character after \"|\" : " + peek);
        return null;
      }

    case '=':
      readch(br);

      if (peek == '=') {
        peek = ' ';
        return Word.eq;
      } else if (peek != '=') {
        return Token.assign;
      } else {
        System.err.println("Erroneous character after \"=\" : " + peek);
        return null;
      }

    case '<':
      readch(br);

      if (peek == '=') {
        peek = ' ';
        return Word.le;
      } else {
        return Word.lt;
      }

    case '>':
      readch(br);

      if (peek == '=') {
        peek = ' ';
        return Word.ge;
      } else {
        return Word.gt;
      }

    case (char)-1:
      return new Token(Tag.EOF);
    default:
      if (Character.isLetter(peek)) {
        String word = "";

        while (Character.isLetter(peek) || Character.isDigit(peek)) {
          word += peek;
          readch(br);
        }

        return convertToEffectiveWord(word);
      } else if (Character.isDigit(peek)) {
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