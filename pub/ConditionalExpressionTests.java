package prog2.tests.pub;

import org.junit.Test;

import tinycc.implementation.expression.Expression;
import prog2.tests.CompilerTests;

public class ConditionalExpressionTests extends CompilerTests {
	public static String getCategory() {
		return "public";
	}

	public static String getExercise() {
		return "AST";
	}

	@Test
	public void testConditional() {
		final ASTMaker   m    = new ASTMaker("testConditional");
		final Expression cond = m.createNumber("1");
		final Expression con  = m.createNumber("42");
		final Expression alt  = m.createNumber("13");
		final Expression e    = m.createConditional(cond, con, alt);
		assertEqualsNormalized("(1?42:13)", e.toString());
	}
}
