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

  /* prova a convertire la stringa in un token variabile */
  private Word convertToIdentifier(String word) {
    boolean allUnderscores = true;

    for (int i = 0; i < word.length(); i++) {
      allUnderscores = allUnderscores && (word.charAt(i) == '_');
    }

    if (!allUnderscores) {
      return new Word(Tag.ID, word);
    } else {
      System.err.println("Erroneous character: invalid identifier");
      return null;
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
      w = convertToIdentifier(word);
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
      readch(br);

      if (peek == '/') {
        while (peek != '\n') {
          readch(br);
        }

        readch(br);
        return lexical_scan(br);
      } else if (peek == '*') {
        while (true) {
          do {
            readch(br);
          } while (peek != '*');

          readch(br);
          if (peek == '/') {
            readch(br);
            return lexical_scan(br);
          }
        }
      } else {
        return Token.div;
      }
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
      } else if (peek == '>') {
        peek = ' ';
        return Word.ne;
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
      if (Character.isLetter(peek) || peek == '_') {
        String word = "";

        while (Character.isLetter(peek) || Character.isDigit(peek) ||
               peek == '_') {
          word += peek;
          readch(br);
        }

        /* peek = ' '; */
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