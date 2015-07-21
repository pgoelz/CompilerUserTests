package prog2.tests.pub;

import org.junit.Test;

import tinycc.implementation.expression.Expression;
import tinycc.parser.TokenKind;
import prog2.tests.CompilerTests;

public class UnaryExprTests extends CompilerTests {
	public static String getCategory() {
		return "public";
	}

	public static String getExercise() {
		return "AST";
	}

	private void testUnarySimple(final TokenKind kind) {
		final ASTMaker   m = new ASTMaker("testUnarySimple");
		final Expression o = m.createNumber("42");
		final Expression e = m.createUnary(kind, o);
		assertEqualsNormalized("(" + kind + "42)", e.toString());
	}

	@Test
	public void testMinus() {
		testUnarySimple(TokenKind.MINUS);
	}

	@Test
	public void testSizeof() {
		final ASTMaker   m = new ASTMaker("testSizeof");
		final Expression a = m.createNumber("42");
		final Expression e = m.createUnary(TokenKind.SIZEOF, a);
		assertEqualsNormalized("(sizeof42)", e.toString());
	}

	@Test
	public void testAddress() {
		final ASTMaker   m = new ASTMaker("testAddress");
		m.createExternalDeclaration(m.createInt(), "a");
		final Expression a = m.createIdentifier("a");
		final Expression e = m.createUnary(TokenKind.AND, a);
		assertEqualsNormalized("(&a)", e.toString());
	}

	@Test
	public void testIndirection() {
		final ASTMaker   m = new ASTMaker("testIndirection");
		m.createExternalDeclaration(m.createInt(), "a");
		final Expression a = m.createIdentifier("a");
		final Expression e = m.createUnary(TokenKind.ASTERISK, a);
		assertEqualsNormalized("(*a)", e.toString());
	}
}
