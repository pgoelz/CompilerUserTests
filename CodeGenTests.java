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

    @Test
    public void testVariablesAfterReturn() throws MarsException {
        // Written by Julian Baldus, published with his kind permission
        final String code = "int test_variables_after_return()\n" +
                            "{\n" +
                            "  return 1;\n" +
                            "  int i;\n" +
                            "  int j;\n" +
                            "  int k;\n" +
                            "  int l;\n" +
                            "}\n" +
                            "int main() {\n" +
                            "  int b = 3;\n" +
                            "  return b + test_variables_after_return() + b;\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(7, mars.run());
    }

    @Test
    public void testNestedScopes() throws MarsException {
        // Written by Julian Baldus, published with his kind permission
        final String code = "int test_scopes()\n" +
                            "{\n" +
                            "  int x = 1;\n" +
                            "  if (1) {\n" +
                            "    int x = 2;\n" +
                            "  }\n" +
                            "  if (x != 1) return 0;\n" +
                            "  {\n" +
                            "    {\n" +
                            "      int x = 0;\n" +
                            "      {\n" +
                            "        int a = 1;\n" +
                            "        x = 2;\n" +
                            "      }\n" +
                            "      {\n" +
                            "        if (x != 2) return 0;\n" +
                            "        int x = 42;\n" +
                            "      }\n" +
                            "      int b = 2;\n" +
                            "      if (x != 2) return 0;\n" +
                            "    }\n" +
                            "    int c = 3;\n" +
                            "  }\n" +
                            "  int d = 4;\n" +
                            "  return x == 1;\n" +
                            "}\n" +
                            "int main() {\n" +
                            "  return test_scopes();\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(1, mars.run());
    }

    @Test
    public void testSizeOf() throws MarsException {
        // Written by Julian Baldus, published with his kind permission
        final String code = "int modify(int* i)\n" +
                            "{\n" +
                            "  return *i = 42;\n" +
                            "}\n" +
                            "int test_sizeof()\n" +
                            "{\n" +
                            "  int i = 0;\n" +
                            "  if (sizeof modify(&i) != 4) return 2;\n" +
                            "  if (i) return 3;\n" +
                            "  if (sizeof i != 4) return 4;\n" +
                            "  if (sizeof &i != 4) return 5;\n" +
                            "  char c;\n" +
                            "  if (sizeof c != 1) return 6;\n" +
                            "  if (sizeof 'x' != 1) return 7; //TODO: what is the type of 'x'?\n" +
                            "  if (sizeof \"Hello, World!\" != 14) return 8;\n" +
                            "  return 1;\n" +
                            "}\n" +
                            "int main() {\n" +
                            "  return test_sizeof();\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(1, mars.run());
    }

    @Test
    public void testRecursion() throws MarsException {
        // Written by Julian Baldus, published with his kind permission
        final String code = "int fac(int i)\n" +
                            "{\n" +
                            "  if (i > 1) return i * fac(i - 1);\n" +
                            "  else return 1;\n" +
                            "}\n" +
                            "\n" +
                            "int fib(int i)\n" +
                            "{\n" +
                            "  if (i) {\n" +
                            "    if (i > 2) return fib(i - 2) + fib(i - 1);\n" +
                            "    else return 1;\n" +
                            "  }\n" +
                            "  return 0;\n" +
                            "}\n" +
                            "int test_recursion()\n" +
                            "{\n" +
                            "  int i = (2 * fac(10)) / 2;\n" +
                            "  if (i != 3628800) return 0;\n" +
                            "  int j = fib(10);\n" +
                            "  if (j != 55) return 0;\n" +
                            "  return 1;\n" +
                            "}" +
                            "int main() {\n" +
                            "  return test_recursion();\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(1, mars.run());
    }

    @Test
    public void testComparison() throws MarsException {
        // Written by Julian Baldus, published with his kind permission
        final String code = "int test_comparison()\n" +
                            "{\n" +
                            "  if (3 > 3) return 0;\n" +
                            "  if (3 < 3) return 0;\n" +
                            "  if ((0 > -1) != 1) return 0;\n" +
                            "  if ((-1 < 0) != 1) return 0;\n" +
                            "  int i = 4 == 4;\n" +
                            "  int j = 4 != 4;\n" +
                            "  if (i != 1) return 0;\n" +
                            "  if (j != 0) return 0;\n" +
                            "  char* a = \"Hi :)\";\n" +
                            "  void* b = a;\n" +
                            "  if (a != b) return 0;\n" +
                            "  if (-1);\n" +
                            "  else return 0;\n" +
                            "  return 1;\n" +
                            "}\n" +
                            "int main() {\n" +
                            "  return test_comparison();\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(1, mars.run());
    }

    @Test
    public void testAssignment() throws MarsException {
        // Written by Julian Baldus, published with his kind permission
        final String code = "int test_assignment()\n" +
                            "{\n" +
                            "  int* a;\n" +
                            "  int* b;\n" +
                            "  a = b = 0;\n" +
                            "  if (a) return 0;\n" +
                            "  if (b) return 0;\n" +
                            "  int c = 0;\n" +
                            "  a = b = &c;\n" +
                            "  if (a) *a = 42;\n" +
                            "  else return 0;\n" +
                            "  if (*b == 0) return 0;\n" +
                            "  return 1;\n" +
                            "}\n" +
                            "int main() {\n" +
                            "  return test_assignment();\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(1, mars.run());
    }

    @Test
    public void testBreakContinue() throws MarsException {
        // Written by Julian Baldus, published with his kind permission
        final String code = "int test_break_continue()\n" +
                            "{\n" +
                            "  int runs = 0;\n" +
                            "  while(\":P\") {\n" +
                            "    if (runs) return 2;\n" +
                            "    runs = runs + 1;\n" +
                            "    while (0) return 3;\n" +
                            "    int i = 10;\n" +
                            "    while (i) {\n" +
                            "      i = i - 1;\n" +
                            "      if (i < 0) return 4;\n" +
                            "      else continue;\n" +
                            "      i = -1;\n" +
                            "    }\n" +
                            "    i = 1;\n" +
                            "    while ((i / 47) * 47 != i) i = i + 1;\n" +
                            "    if (i != 47) return 5;\n" +
                            "    while (i > 0) {\n" +
                            "      int j = i / 2;\n" +
                            "      if (j == 10) break;\n" +
                            "      i = i - 1;\n" +
                            "    }\n" +
                            "    if (i != 21) return 6;\n" +
                            "    return 1;\n" +
                            "  }\n" +
                            "  return 7;\n" +
                            "}\n" +
                            "int main() {\n" +
                            "  return test_break_continue();\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(1, mars.run());
    }

    @Test
    public void testBinaryIdentities() throws MarsException {
        final String code = "void* get_scratch();\n" +
                            "int test_identities() {\n" +
                            "  int* a = get_scratch();\n" +
                            "  if (a != a) return 0;\n" +
                            "  if (((a + 3) - 3) != a) return 1;\n" +
                            "  if (((a - 5) + 5) != a) return 2;\n" +
                            "  if (a - a != 0) return 3;\n" +
                            "  if ((a + 11) - a != 11) return 4;\n" +
                            "  \n" +
                            "  char* b = \"Wuerzburg\";\n" +
                            "  if (((b + 3) - 3) != b) return 5;\n" +
                            "  if (((b - 5) + 5) != b) return 6;\n" +
                            "  if (b - b != 0) return 7;\n" +
                            "  if ((b - 4) - b != -4) return 8;\n" +
                            "  \n" +
                            "  int c = 13;\n" +
                            "  int d = -423;\n" +
                            "  if (c + d != d + c) return 9;\n" +
                            "  if (c - d != -(d - c)) return 10;\n" +
                            "  if ((c * d) / d != c) return 11;\n" +
                            "  if (c < d) return 12;\n" +
                            "  if (d < c) {\n" +
                            "    if (c == d) return 14;\n" +
                            "    if (c != c) return 15;\n" +
                            "    if (d != d) return 16;\n" +
                            "    return -1;\n" +
                            "  }\n" +
                            "  return 13;\n" +
                            "}\n" +
                            "int main() {\n" +
                            "  return test_identities();\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(-1, mars.run());
    }

    @Test
    public void testTripleAssignment() throws MarsException {
        // GCC says: implicit conversion from 'int' to 'char' changes value from 256 to 0
        final String code = "int main() {\n" +
                            "  char c;\n" +
                            "  int a = c = 256;\n" +
                            "  return (a - c);\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(0, mars.run());
    }

    @Test
    public void testContinue() throws  MarsException {
        final String code = "int test_continue() {\n" +
                            "  int i = 0;\n" +
                            "  while (1) {\n" +
                            "    char a;\n" +
                            "    int* b;\n" +
                            "    if (i < 37) {\n" +
                            "      i = i + 1;\n" +
                            "      continue;\n" +
                            "    }\n" +
                            "    return i; \n" +
                            "  }\n" +
                            "  return 2;\n" +
                            "}\n" +
                            "int main() {\n" +
                            "  return test_continue();\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(37, mars.run());
    }

    @Test
    public void testBreak() throws  MarsException {
        final String code = "int test_break() {\n" +
                            "  int i = 20;\n" +
                            "  while (i = i - 1) {\n" +
                            "    if (i == 13) {\n" +
                            "      char c = 'a';\n" +
                            "      break;\n" +
                            "    }\n" +
                            "  }\n" +
                            "  return i;\n" +
                            "}\n" +
                            "int main() {\n" +
                            "  int b = 5;\n" +
                            "  return b + test_break() + 5;\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(23, mars.run());
    }

    @Test
    public void testNestedBreak() throws  MarsException {
        final String code = "int test_nested_break(int max) {\n" +
                            "  int c = 0;\n" +
                            "  int i = 1;\n" +
                            "  while (i < max + 1) {\n" +
                            "    int j = 1;\n" +
                            "    while (1) {\n" +
                            "      c = c + 1;\n" +
                            "      if (j == i) break;\n" +
                            "      j = j + 1;\n" +
                            "    }\n" +
                            "    if (j != i) return -1;\n" +
                            "    if (i == max) break;\n" +
                            "    int y = 3;\n" +
                            "    i = i + 1;\n" +
                            "  }\n" +
                            "  if (i != max) return -2;\n" +
                            "  return c;\n" +
                            "}\n" +
                            "int main() {\n" +
                            "  return test_nested_break(15);\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(120, mars.run());
    }
}
