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
    } else
      error("syntax error");
  }

  public void prog() {
    stat();
    match(Tag.EOF);
  }

  private void stat() {
    match('(');
    statp();
    match(')');
  }

  private void statp() {
    switch (look.tag) {
    case '=':
      match('=');
      match(Tag.ID);
      expr();
      break;
    case Tag.DO:
      match(Tag.DO);
      statlist();
      break;
    case Tag.READ:
      match(Tag.READ);
      match(Tag.ID);
      break;
    case Tag.PRINT:
      match(Tag.PRINT);
      exprlist();
      break;
    case Tag.WHILE:
      match(Tag.WHILE);
      bexpr();
      stat();
      break;
    case Tag.COND:
      match(Tag.COND);
      bexpr();
      stat();
      elseopt();
      break;
    default:
      error(look.toString());
    }
  }

  private void elseopt() {
    if (look.tag == '(') {
      match('(');
      match(Tag.ELSE);
      stat();
      match(')');
    }
  }

  private void bexpr() {
    match('(');
    bexprp();
    match(')');
  }

  private void bexprp() {
    match(Tag.RELOP);
    expr();
    expr();
  }

  private void statlist() {
    stat();
    statlistp();
  }

  private void statlistp() {
    if (look.tag == '(') {
      stat();
      statlistp();
    }
  }

  private void exprlist() {
    expr();
    exprlistp();
  }

  private void expr() {
    switch (look.tag) {
    case '(':
      match('(');
      exprp();
      match(')');
      break;
    case Tag.ID:
      match(Tag.ID);
      break;
    case Tag.NUM:
      match(Tag.NUM);
      break;
    }
  }

  private void exprp() {
    switch (look.tag) {
    case '+':
      match('+');
      exprlist();
      break;
    case '-':
      match('-');
      expr();
      expr();
      break;
    case '*':
      match('*');
      exprlist();
      break;
    case '/':
      match('/');
      expr();
      expr();
      break;
    }
  }

  private void exprlistp() {
    boolean isExpr =
        Character.isDigit(look.tag) || Character.isLetter(look.tag);
    isExpr = isExpr || look.tag == '_' || look.tag == '(';

    if (isExpr) {
      expr();
      exprlistp();
    }
  }

  public static void main(String[] args) {
    Lexer lex = new Lexer();
    String path = "hello.lft";

    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      Parser parser = new Parser(lex, br);

      parser.prog();
      System.out.println("\nInput OK!");

      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}