package prog2.tests.pub;

import org.junit.Test;

import tinycc.implementation.expression.Expression;
import tinycc.parser.TokenKind;
import prog2.tests.CompilerTests;

public class BinaryExprTests extends CompilerTests {
	public static String getCategory() {
		return "public";
	}

	public static String getExercise() {
		return "AST";
	}

	private void testBinarySimple(final TokenKind kind) {
		final ASTMaker   m = new ASTMaker("testBinary" + kind.name());
		final Expression a = m.createNumber("42");
		final Expression b = m.createNumber("1337");
		final Expression e = m.createBinary(kind, a, b);
		assertEqualsNormalized("(42" + kind + "1337)", e.toString());
	}

	@Test
	public void testPlus() {
		testBinarySimple(TokenKind.PLUS);
	}

	@Test
	public void testMinus() {
		testBinarySimple(TokenKind.MINUS);
	}

	@Test
	public void testMultiply() {
		testBinarySimple(TokenKind.ASTERISK);
	}

	@Test
	public void testDivide() {
		testBinarySimple(TokenKind.SLASH);
	}

	@Test
	public void testEqual() {
		testBinarySimple(TokenKind.EQUAL_EQUAL);
	}

	@Test
	public void testNotEqual() {
		testBinarySimple(TokenKind.BANG_EQUAL);
	}

	@Test
	public void testLessThan() {
		testBinarySimple(TokenKind.LESS);
	}

	@Test
	public void testGreaterThan() {
		testBinarySimple(TokenKind.GREATER);
	}

	@Test
	public void testAssign() {
		final ASTMaker   m   = new ASTMaker("testAssignment");
		m.createExternalDeclaration(m.createInt(), "a");
		final Expression lhs = m.createIdentifier("a");
		final Expression rhs = m.createNumber("42");
		final Expression e   = m.createBinary(TokenKind.EQUAL, lhs, rhs);
		assertEqualsNormalized("(a=42)", e.toString());
	}
}
