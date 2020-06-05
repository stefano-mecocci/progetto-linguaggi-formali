import java.io.*;

public class Parser {
  private Lexer lex;
  private BufferedReader pbr;
  private Token look;

  public Parser(Lexer l, BufferedReader br) {
    lex = l;
    pbr = br;
    move();
  }

  void move() {
    look = lex.lexical_scan(pbr);
    System.out.println("token = " + look);
  }

  void error(String s) { throw new Error("near line " + lex.line + ": " + s); }

  void match(int t) {
    if (look.tag == t) {
      if (look.tag != Tag.EOF)
        move();
    } else {
      error("syntax error");
    }
  }

  public void start() {
    expr();
    match(Tag.EOF);
  }

  private void expr() {
    term();
    exprp();
  }

  private void exprp() {
    switch (look.tag) {
    case '+':
      match('+');
      term();
      exprp();
      break;
    case '-':
      match('-');
      term();
      exprp();
      break;
    }
  }

  private void term() {
    fact();
    termp();
  }

  private void termp() {
    switch (look.tag) {
    case '*':
      match('*');
      fact();
      termp();
      break;
    case '/':
      match('/');
      fact();
      termp();
      break;
    }
  }

  private void fact() {
    if (look.tag == '(') {
      match('(');
      expr();
      match(')');
    } else if (look.tag == Tag.NUM) {
      match(Tag.NUM);
    } else {
      error(look.toString());
    }
  }

  public static void main(String[] args) {
    Lexer lex = new Lexer();
    String path = "hello.lft";

    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      Parser parser = new Parser(lex, br);

      parser.start();
      System.out.println("\nInput OK!");

      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}