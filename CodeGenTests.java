package prog2.tests.user;

import org.junit.Test;
import prog2.tests.CompilerTests;
import prog2.tests.MarsException;
import prog2.tests.MarsUtil;

import static org.junit.Assert.assertEquals;

public class CodeGenTests extends CompilerTests {
	public static String getCategory() {
		return "public";
	}

	public static String getExercise() {
		return "Codegen";
	}

	@Test
	public void testInnerReturn() throws MarsException {
		final String code = "int foo(int x) {\n" +
							"  int c;\n" +
							"  while (x != 30) {\n" +
							"  int a = 5;\n" +
							"  char b;\n" +
							"  return x;\n" +
							"  }\n" +
							"}\n" +
							"\n" +
							"int main() {\n" +
							"  return foo(-37);\n" +
							"}";
		final MarsUtil mars = prepareCode(code);
		assertEquals(-37, mars.run());
	}

	@Test
	public void testEuklid() throws MarsException {
		// Adapted from https://de.wikibooks.org/wiki/Algorithmensammlung:_Zahlentheorie:_Euklidischer_Algorithmus#C
		final String code = "int euklid(int a, int b)\n" +
							"{\n" +
							"  if (a == 0) {\n" +
							"    return b;\n" +
							"  }\n" +
							"  while(b != 0) {\n" +
							"    if (a > b) {\n" +
							"      a = a - b; \n" +
							"    } else {\n" +
							"      b = b - a; \n" +
							"    }\n" +
							"  }\n" +
							"  return a; \n" +
							"}\n" +
							"int main() {\n" +
							"  return euklid(543, 2345);\n" +
							"}";
		final MarsUtil mars = prepareCode(code);
		assertEquals(1, mars.run());
	}

	@Test
	public void testEuklid2() throws MarsException {
		// Adapted from https://de.wikibooks.org/wiki/Algorithmensammlung:_Zahlentheorie:_Euklidischer_Algorithmus#C
		final String code = "int euklid(int a, int b)\n" +
							"{\n" +
							"  if (a == 0) {\n" +
							"    return b;\n" +
							"  }\n" +
							"  while(b != 0) {\n" +
							"    if (a > b) {\n" +
							"      a = a - b; \n" +
							"    } else {\n" +
							"      b = b - a; \n" +
							"    }\n" +
							"  }\n" +
							"  return a; \n" +
							"}\n" +
							"int main() {\n" +
							"  return euklid(10710, 17850);\n" +
							"}";
		final MarsUtil mars = prepareCode(code);
		assertEquals(3570, mars.run());
	}

	private String primeFactorCode(int c) {
		return "char divides(int a, int b);\n" +
			   "int smallestprimefactor(int c) {\n" +
			   "  int i = 2;\n" +
			   "  while (1 - (divides(i, c))) {\n" +
			   "    i = i + 1;\n" +
			   "  }\n" +
			   "  return i;\n" +
			   "}\n" +
			   "int main() {\n" +
			   "  return smallestprimefactor(" + c + ");\n" +
			   "}\n" +
			   "char divides(int a, int b) {\n" +
			   "  int d = b / a;\n" +
			   "  return (d * a == b);\n" +
			   "}";
	}

	@Test
	public void testPrimeFactor1() throws MarsException {
		final MarsUtil mars = prepareCode(primeFactorCode(2));
		assertEquals(2, mars.run());
	}

	@Test
	public void testPrimeFactor2() throws MarsException {
		final MarsUtil mars = prepareCode(primeFactorCode(37 * 37 * 47));
		assertEquals(37, mars.run());
	}

	@Test
	public void testPrimeFactor3() throws MarsException {
		final MarsUtil mars = prepareCode(primeFactorCode(1013 * 1009 * 997));
		assertEquals(997, mars.run());
	}
}
