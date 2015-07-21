package prog2.tests.pub;

import org.junit.Test;

import tinycc.implementation.expression.Expression;
import prog2.tests.CompilerTests;

public class PrimaryExprTests extends CompilerTests {
	public static String getCategory() {
		return "public";
	}

	public static String getExercise() {
		return "AST";
	}

	@Test
	public void testNumber() {
		final ASTMaker   m = new ASTMaker("testNumber");
		final Expression e = m.createNumber("1337");
		assertEqualsNormalized("1337", e.toString());
	}

	@Test
	public void testCharacter() {
		final ASTMaker   m = new ASTMaker("testCharacter");
		final Expression e = m.createCharacter("y");
		assertEqualsNormalized("'y'", e.toString());
	}

	@Test
	public void testString() {
		final ASTMaker   m = new ASTMaker("testString");
		final Expression e = m.createString("Hallo Welt!");
		assertEqualsNormalized("\"Hallo Welt!\"", e.toString());
	}

	@Test
	public void testIdentifier() {
		final ASTMaker   m = new ASTMaker("testIdentifier");
		m.createExternalDeclaration(m.createInt(), "a");
		final Expression e = m.createIdentifier("a");
		assertEqualsNormalized("a", e.toString());
	}
}
