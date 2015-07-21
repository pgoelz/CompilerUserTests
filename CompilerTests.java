package prog2.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.Timeout;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import tinycc.diagnostic.Locatable;
import tinycc.diagnostic.Location;
import tinycc.implementation.Compiler;
import tinycc.implementation.expression.Expression;
import tinycc.implementation.statement.Statement;
import tinycc.implementation.type.Type;
import tinycc.parser.ASTFactory;
import tinycc.parser.Token;
import tinycc.parser.TokenKind;
import tinycc.parser.Lexer;

public class CompilerTests {
	public static final String INPUT_NAME = "test.c";

	/* set these to true, if you need some help while debugging */
	public static final boolean PRINT_C_CODE = false;
	public static final boolean PRINT_ASM_CODE = false;

	protected FatalDiagnostic diagnostic;
	protected Compiler compiler;
	protected ASTFactory astFactory;
	private final String NORMALIZE_WHITESPACE_TO = "";

	@Rule
	public TestRule timeout = new Timeout(DEFAULT_TIMEOUT);

	protected static final int DEFAULT_TIMEOUT = 25 * 1000;

	@Before
	public void setUp() {
		diagnostic = new FatalDiagnostic();
		compiler = new Compiler(diagnostic);
		astFactory = compiler.getASTFactory();
	}

	private String normalizeString(final String s) {
		return s.replaceAll("\\s+", NORMALIZE_WHITESPACE_TO);
	}

	protected void assertEqualsNormalized(final String expected,
			final String got) {
		final String expected_normalized = normalizeString(expected);
		final String got_normalized = normalizeString(got);
		assertEquals(expected_normalized, got_normalized);
	}

	protected void checkLocation(final FatalCompilerError e,
			final Locatable expected) {
		final Locatable l = e.getLocatable();
		if (!equals(expected, l))
			fail("expected <" + toString(expected) + ">, got <"
					+ e.getMessage() + "> (the actual message is ignored)");
	}

	protected void checkLocation(final FatalCompilerError e,
			final String inputName, final int line, final int column) {
		checkLocation(e, new Location(inputName, line, column));
	}

	public static boolean equals(final Locatable a, final Locatable b) {
		return a.getInputName().equals(b.getInputName())
				&& a.getLine() == b.getLine() && a.getColumn() == b.getColumn();
	}

	public static String toString(final Locatable l) {
		return l.getInputName() + ':' + l.getLine() + ':' + l.getColumn();
	}

	protected class ASTMaker {
		private final Locatable  l;

		public ASTMaker(final String name) {
			l = new Location(name);
		}

		public void createExternalDeclaration(final Type type, final String name) {
			astFactory.createExternalDeclaration(type, createIdentifierToken(name));
		}

		public Expression createBinary(final TokenKind operator, final Expression left, final Expression right) {
			return astFactory.createBinaryExpression(createToken(operator), left, right);
		}

		public Expression createIdentifier(final String name) {
			return astFactory.createPrimaryExpression(createIdentifierToken(name));
		}

		Token createToken(final TokenKind kind) {
			return new Token(l, kind);
		}

		public Expression createCall(final Expression callee, final Expression... arguments) {
			return astFactory.createCallExpression(createToken(TokenKind.LPAREN), callee, Arrays.asList(arguments));
		}

		public Expression createCharacter(final String c) {
			return astFactory.createPrimaryExpression(new Token(l, TokenKind.CHARACTER, c));
		}

		public Expression createConditional(final Expression cond, final Expression con, final Expression alt) {
			return astFactory.createConditionalExpression(createToken(TokenKind.QUESTION_MARK), cond, con, alt);
		}

		public Expression createNumber(final String value) {
			return astFactory.createPrimaryExpression(new Token(l, TokenKind.NUMBER, value));
		}

		public Expression createString(final String c) {
			return astFactory.createPrimaryExpression(new Token(l, TokenKind.STRING, c));
		}

		public Expression createUnary(final TokenKind operator, final Expression operand) {
			return astFactory.createUnaryExpression(createToken(operator), false, operand);
		}

		public Expression createPostfix(final TokenKind operator, final Expression operand) {
			return astFactory.createUnaryExpression(createToken(operator), true, operand);
		}

		public Statement createBlock(final Statement... body) {
			return astFactory.createBlockStatement(l, Arrays.asList(body));
		}

		public Statement createBreak() {
			return astFactory.createBreakStatement(l);
		}

		public Statement createContinue() {
			return astFactory.createContinueStatement(l);
		}

		public Statement createDeclaration(final Type type, final String name) {
			return astFactory.createDeclarationStatement(type, createIdentifierToken(name), null);
		}

		public Statement createEmpty() {
			return astFactory.createEmptyStatement(l);
		}

		public Statement createExpressionStatement(final Expression e) {
			return astFactory.createExpressionStatement(l, e);
		}

		public Statement createIf(final Expression cond, final Statement con, final Statement alt) {
			return astFactory.createIfStatement(l, cond, con, alt);
		}

		public Statement createReturn(final Expression expression) {
			return astFactory.createReturnStatement(l, expression);
		}

		public Statement createWhile(final Expression cond, final Statement body) {
			return astFactory.createWhileStatement(l, cond, body);
		}

		public Type createChar() {
			return astFactory.createBaseType(TokenKind.CHAR);
		}

		public Type createFunctionType(final Type returnType, final Type... paramTypes) {
			return astFactory.createFunctionType(returnType, Arrays.asList(paramTypes));
		}

		public Type createInt() {
			return astFactory.createBaseType(TokenKind.INT);
		}

		public Type createPointer(final Type type) {
			return astFactory.createPointerType(type);
		}

		public Type createVoid() {
			return astFactory.createBaseType(TokenKind.VOID);
		}

		private Token createIdentifierToken(final String name) {
			return new Token(l, TokenKind.IDENTIFIER, name);
		}
	}

	public void checkCode(final String code) {
		final StringReader r = new StringReader(code);
		final Lexer lexer = new Lexer(diagnostic, r, INPUT_NAME);
		compiler.parseTranslationUnit(lexer);
		compiler.checkSemantics();
	}

	private static String excToString(final Exception e) {
		final String stack = Arrays.toString(e.getStackTrace()).replace(",",
				"\n\t\t  at ");
		return "\t " + e + ":\n\t\t  at "
				+ stack.substring(1, stack.length() - 1);
	}

	public void checkCode(final String code, final int line, final int column) {
		try {
			checkCode(code);
			fail("Semantics check didn't produce any errors");
		} catch (final FatalCompilerError e) {
			checkLocation(e, INPUT_NAME, line, column);
		}
	}

	public MarsUtil prepareCode(final String cCode) {
		final StringReader r = new StringReader(cCode);

		if (PRINT_C_CODE) {
			System.out.println("C code:");
			final String[] lines = cCode.split("\n");
			for (int i = 0; i < lines.length; i++) {
				System.out.printf("%03d: %s\n", i + 1, lines[i]);
				//System.out.println(lines[i]);
			}
		}
		
		final Lexer lexer = new Lexer(diagnostic, r, INPUT_NAME);
		try {
			compiler.parseTranslationUnit(lexer);
		} catch (Exception e) {
			fail("Failed to parseTranslationUnit:\n" + excToString(e));
		}
		try {
			compiler.checkSemantics();
		} catch (Exception e) {
			fail("Failed to checkSemantics:\n" + excToString(e));
		}

		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final PrintStream           ps   = new PrintStream(baos);
		try {
			compiler.generateCode(ps);
		} catch (Exception e) {
			fail("Failed to generateCode:\n" + excToString(e));
		}

		if (PRINT_ASM_CODE) {
			System.out.println("ASM code:");
			System.out.println(baos.toString());
		}

		return new MarsUtil(baos.toString());
	}
}
