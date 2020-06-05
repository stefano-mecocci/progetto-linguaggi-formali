import java.io.*;

public class Valutatore {
  private Lexer lex;
  private BufferedReader pbr;
  private Token look;

  public Valutatore(Lexer l, BufferedReader br) {
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

  public void start() {
    int expr_val;

    expr_val = expr();
    match(Tag.EOF);

    System.out.println("\nRisultato: " + expr_val);
  }

  private int expr() {
    int exprp_val, term_val;

    term_val = term();
    exprp_val = exprp(term_val);

    return exprp_val;
  }

  private int exprp(int exprp_i) {
    int term_val, exprp_val = exprp_i;

    switch (look.tag) {
    case '+':
      match('+');
      term_val = term();
      exprp_val = exprp(exprp_i + term_val);
      break;
    case '-':
      match('-');
      term_val = term();
      exprp_val = exprp(exprp_i - term_val);
      break;
    }

    return exprp_val;
  }

  private int term() {
    int fact_val = fact();

    return termp(fact_val); // termp_val
  }

  private int termp(int termp_i) {
    int termp_val, fact_val;
    termp_val = termp_i;

    switch (look.tag) {
    case '*':
      match('*');
      fact_val = fact();
      termp_val = termp(termp_i * fact_val);
      break;
    case '/':
      match('/');
      fact_val = fact();
      termp_val = termp(termp_i / fact_val);
      break;
    }

    return termp_val;
  }

  private int fact() {
    int fact_val = 0;
    // Java richiede che nel return ci sia un valore certo alla fine del metodo

    if (look.tag == '(') {
      match('(');
      fact_val = expr();
      match(')');
    } else if (look.tag == Tag.NUM) {
      fact_val = ((NumberTok) look).getValue();
      match(Tag.NUM);
    } else {
      error(look.toString());
    }

    return fact_val;
  }

  public static void main(String[] args) {
    Lexer lex = new Lexer();
    String path = "hello.lft";

    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      Valutatore valutatore = new Valutatore(lex, br);

      valutatore.start();

      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}