import java.io.*;

public class Translator {
  private Lexer lex;
  private BufferedReader pbr;
  private Token look;

  public SymbolTable st = new SymbolTable();
  public CodeGenerator code = new CodeGenerator();
  public int count = 0;

  public Translator(Lexer l, BufferedReader br) {
    lex = l;
    pbr = br;
    move();
  }

  void move() {
    look = lex.lexical_scan(pbr);
    System.out.println("token = " + look);
  }

  void error(String s) {
    throw new Error("ERROR: " + s + " near line " + lex.line);
  }

  void match(int t) {
    if (look.tag == t) {
      if (look.tag != Tag.EOF)
        move();
    } else
      error("syntax error at " + getTokenString(look));
  }

  public void prog() {
    int lnext_prog = code.newLabel();

    stat(lnext_prog);
    code.emitLabel(lnext_prog);
    match(Tag.EOF);

    try {
      code.toJasmin();
    } catch (IOException e) {
      System.out.println("ERROR: IO error\n");
    }
  }

  private String getTokenString(Token t) {
    String token;

    if (look instanceof Word) {
      token = ((Word)t).lexeme;
    } else {
      token = Character.toString((char)t.tag);
    }

    return token;
  }

  private void stat(int lnext_prog) {
    match('(');
    statp(lnext_prog);
    match(')');
  }

  private void statlist(int lnext) {
    stat(lnext);
    statlistp(lnext);
  }

  private void statlistp(int lnext) {
    if (look.tag == '(') {
      stat(lnext);
      statlistp(lnext);
    }
  }

  private void statp(int lnext) {
    switch (look.tag) {
    case '=':
      match('=');

      if (look.tag == Tag.ID) {
        int id_addr = st.lookupAddress(((Word)look).lexeme);

        if (id_addr == -1) {
          id_addr = count;
          st.insert(((Word)look).lexeme, count++);
        }

        match(Tag.ID);
        expr();
        code.emit(OpCode.istore, id_addr);
      } else {
        error("ERROR: error in grammar (statp) after ID with " + look);
      }
      break;
    case Tag.DO:
      match(Tag.DO);
      statlist(lnext);
      break;
    case Tag.READ:
      match(Tag.READ);

      if (look.tag == Tag.ID) {
        int read_id_addr = st.lookupAddress(((Word)look).lexeme);

        if (read_id_addr == -1) {
          read_id_addr = count;
          st.insert(((Word)look).lexeme, count++);
        }

        match(Tag.ID);
        code.emit(OpCode.invokestatic, 0);
        code.emit(OpCode.istore, read_id_addr);
      } else
        error("ERROR: error in grammar (statp) after read with " + look);
      break;
    case Tag.PRINT:
      match(Tag.PRINT);
      exprlist(true, null);
      break;
    case Tag.COND:
      int L_then = code.newLabel();
      int L_else = code.newLabel();
      int L_endif = code.newLabel();

      match(Tag.COND);
      bexpr(L_then);
      code.emit(OpCode.GOto, L_else);

      code.emitLabel(L_then);
      stat(code.newLabel());
      code.emit(OpCode.GOto, L_endif);

      code.emitLabel(L_else);
      if (look.tag == '(') {
        elseopt(L_endif);
      }
      code.emitLabel(L_endif);
      break;
    case Tag.WHILE:
      int L_start = code.newLabel(); /* inizio ciclo */
      int L_body = code.newLabel();  /* inizio del corpo del ciclo */
      int L_endwhile = code.newLabel();

      match(Tag.WHILE);
      code.emitLabel(L_start);
      bexpr(L_body);
      code.emit(OpCode.GOto, L_endwhile);
      code.emitLabel(L_body);
      stat(lnext);
      code.emit(OpCode.GOto, L_start);
      code.emitLabel(L_endwhile);

      break;
    default:
      error("in grammar (statp), found " + getTokenString(look));
    }
  }

  private void elseopt(int lnext) {
    match('(');
    match(Tag.ELSE);
    stat(lnext);
    match(')');
  }

  private void exprlist(boolean print, OpCode associativeOp) {
    expr();

    if (print)
      code.emit(OpCode.invokestatic, 1);

    exprlistp(print, associativeOp);
  }

  private void exprlistp(boolean print, OpCode associativeOp) {
    if (look.tag == Tag.NUM || look.tag == Tag.ID || look.tag == '(') {
      expr();

      if (associativeOp != null) {
        code.emit(associativeOp);
      }

      if (print)
        code.emit(OpCode.invokestatic, 1);

      exprlistp(print, associativeOp);
    }
  }

  private void exprp() {
    switch (look.tag) {
    case '+':
      match('+');
      exprlist(false, OpCode.iadd);
      break;
    case '-':
      match('-');
      expr();

      /* supporto per - come operatore unario */
      if (look.tag == ')') {
        code.emit(OpCode.ldc, -1);
        code.emit(OpCode.imul);
      } else {
        expr();
        code.emit(OpCode.isub);
      }
      break;
    case '/':
      match('/');
      expr();
      expr();
      code.emit(OpCode.idiv);
      break;
    case '*':
      match('*');
      exprlist(false, OpCode.imul);
      break;
    default:
      error("missing operation");
    }
  }

  private void expr() {
    switch (look.tag) {
    case '(':
      match('(');
      exprp();
      match(')');
      break;
    case Tag.ID:
      int id_addr = st.lookupAddress(((Word)look).lexeme);

      if (id_addr == -1) {
        error("No ID declared as " + ((Word)look).lexeme);
      }

      code.emit(OpCode.iload, id_addr);
      match(Tag.ID);
      break;
    case Tag.NUM:
      code.emit(OpCode.ldc, ((NumberTok)look).val);
      match(Tag.NUM);
      break;
    default:
      error("missing operand, found " + getTokenString(look));
    }
  }

  private void bexpr(int jump) {
    match('(');
    bexprp(jump);
    match(')');
  }

  private void bexprp(int jump) {
    String symbol = ((Word)look).lexeme;

    match(Tag.RELOP);
    expr();
    expr();

    switch (symbol) {
    case ">":
      code.emit(OpCode.if_icmpgt, jump);
      break;
    case "<=":
      code.emit(OpCode.if_icmple, jump);
      break;
    case "<":
      code.emit(OpCode.if_icmplt, jump);
      break;
    case ">=":
      code.emit(OpCode.if_icmpge, jump);
      break;
    case "==":
      code.emit(OpCode.if_icmpeq, jump);
      break;
    case "<>":
      code.emit(OpCode.if_icmpne, jump);
      break;
    }
  }

  public static void main(String[] args) {
    Lexer lex = new Lexer();
    String path = "hello.lft";

    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      Translator t = new Translator(lex, br);

      t.prog();

      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}