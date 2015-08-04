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
    public void testConditional() throws MarsException {
        final String code = "int main() {\n" +
                            "  return (0 ? 1 : (53 ? 2 : 3));\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(2, mars.run());
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
    public void testWeakInequalities() throws MarsException {
        final String code = "int test_comparison()\n" +
                            "{\n" +
                            "  if ((3 >= 3) != 1) return 2;\n" +
                            "  if ((3 <= 3) != 1) return 3;\n" +
                            "  if ((0 >= -1) != 1) return 4;\n" +
                            "  if ((-1 <= 0) != 1) return 5;\n" +
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
    public void testContinue() throws MarsException {
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
    public void testBreak() throws MarsException {
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
    public void testNestedBreak() throws MarsException {
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

    @Test
    public void testLargeInteger() throws MarsException {
        final String code = "int main() {\n" +
                            "  return -2147483648;\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(-2147483648, mars.run());
    }

    @Test
    public void testReturnCharAsInt() throws MarsException {
        final String code = "int main() {\n" +
                            "  int a = 12345;\n" +
                            "  char c = a;\n" +
                            "  return c;\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(57, mars.run());
    }

    @Test
    public void testLargeArithmeticExpression() throws MarsException {
        final String code = "int main() {\n" +
                            "  int a = ((((((((((((((((((((1+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1)+1;\n" +
                            "  int b = (1+(1+(1+(1+(1+(1+(1+(1+(1+(1+(1+(1+(1+(1+(1+(1+(1+(1+(1+(1+(1+(1+1))))))))))))))))))))));\n" +
                            "  return (a - b);\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(-1, mars.run());
    }

    @Test
    public void testLargeArithmeticExpression1() throws MarsException {
        final String code = "int main() {\n" +
                            "  int a = (((((((-28)*(5))-((2)*(5)))+(((-3)+(0))+((28)-(19))))+((((18)+(-3))+((29)-(6)))-(((-4)+(15))*((22)-(-26)))))+(((((-13)*(-29))*((-23)*(6)))+(((3)-(-20))+((21)+(-19))))+((((12)*(-12))*((1)-(-4)))-(((16)-(14))-((0)*(-2))))))-((((((24)+(24))-((15)*(18)))+(((3)-(27))+((9)*(-28))))*((((5)+(10))+((-4)*(-11)))-(((23)*(-28))*((17)+(-11)))))+(((((2)-(4))*((-11)*(-30)))*(((-29)+(21))-((-10)*(6))))-((((27)*(-14))*((18)*(-3)))*(((17)*(-19))*((1)-(-16)))))));\n" +
                            "  return a;\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(-110147675, mars.run());
    }

    @Test
    public void testLargeArithmeticExpression2() throws MarsException {
        // Arithmetic expression with depth 10, should not produce overflows, but only contains +, -, * anyway
        final String code = "int main() {\n" +
                            "  int a = ((((((((((-9)*(5))*((8)-(-19)))+(((-10)*(-3))*((-25)+(-14))))+((((-16)*(27))*((-21)+(14)))+(((-4)+(-24))+((19)*(29)))))+(((((-15)*(19))-((26)*(7)))-(((-16)*(-7))-((-14)-(-5))))+((((28)+(-23))*((-10)-(-19)))-(((-11)+(29))-((-29)+(1))))))+((((((6)+(-10))+((-8)*(-19)))-(((4)*(29))*((1)+(2))))+((((5)+(6))+((18)-(-29)))*(((-15)+(17))+((0)-(-12)))))-(((((20)-(19))-((22)-(10)))*(((-28)*(26))+((-25)+(24))))+((((2)-(-10))+((-2)-(-10)))+(((11)-(-19))-((-10)-(-22)))))))+(((((((-14)+(21))*((22)-(-18)))-(((-2)+(23))+((-30)+(-1))))*((((-11)+(13))+((-3)-(-24)))-(((-9)-(-9))*((27)+(8)))))-(((((-4)*(28))+((18)*(-10)))+(((-30)-(7))-((19)+(-17))))+((((-8)*(-13))+((-25)*(-3)))-(((-3)+(25))*((-22)-(-26))))))+((((((-24)+(15))-((20)+(-20)))*(((-14)+(-14))-((11)*(19))))+((((4)*(12))-((0)+(11)))-(((-22)+(-16))*((-21)-(5)))))-(((((-19)-(11))-((16)+(11)))-(((-28)+(9))-((-13)-(-16))))+((((29)-(21))+((4)+(9)))*(((-22)+(1))*((-1)*(-28))))))))+((((((((-15)-(-19))+((2)*(21)))+(((-29)+(24))-((11)-(8))))-((((18)+(-10))*((-1)+(1)))*(((-16)-(2))+((-11)*(29)))))-(((((-11)*(26))*((-17)*(18)))-(((25)-(1))+((-13)-(-9))))-((((13)-(24))*((17)-(-21)))*(((-17)*(14))+((-29)*(25))))))+((((((27)-(20))+((-29)-(-21)))-(((3)-(1))-((10)+(25))))*((((-24)*(-5))+((-9)+(-30)))+(((-15)*(-3))*((4)*(27)))))+(((((-9)+(23))+((2)+(-14)))+(((-20)-(14))*((-13)+(19))))-((((26)-(-18))+((-25)+(-14)))*(((-21)*(-14))*((20)-(13)))))))+(((((((-21)+(28))-((14)*(-7)))-(((16)*(27))*((-6)*(28))))-((((-14)+(16))*((-11)+(20)))*(((-15)-(-3))*((-17)*(0)))))-(((((9)+(-3))-((-10)+(26)))*(((-29)-(-24))+((-2)*(0))))-((((-6)*(-26))+((-3)-(20)))-(((-6)*(1))-((27)*(22))))))-((((((3)*(-13))*((-3)-(-2)))*(((15)*(-21))-((15)*(20))))+((((-16)+(10))*((-5)*(-11)))-(((8)-(21))+((17)-(19)))))+(((((-26)+(7))+((23)-(9)))-(((29)*(-18))-((25)+(17))))+((((-14)*(-21))*((6)*(12)))*(((5)-(-6))-((13)+(0)))))))))+(((((((((-24)+(-6))-((-16)-(9)))-(((-8)-(13))-((11)-(-13))))*((((21)*(14))+((11)+(-6)))-(((25)+(29))*((15)*(20)))))-(((((-18)-(19))+((-28)-(-19)))*(((13)-(-24))+((10)*(-6))))+((((-23)*(-16))-((-6)+(-30)))-(((23)*(-4))*((-30)+(26))))))+((((((-20)+(17))+((-20)-(21)))+(((13)*(-25))-((26)-(-23))))-((((-24)-(-27))*((19)-(-7)))*(((1)-(7))*((-13)-(26)))))-(((((27)*(14))*((-13)-(27)))-(((-8)-(2))-((16)*(18))))+((((-28)-(-13))-((-12)+(-16)))-(((0)-(18))+((-28)+(-22)))))))+(((((((-26)-(22))*((26)*(-28)))-(((-15)+(21))-((-26)+(-29))))+((((21)+(6))*((28)-(11)))+(((-7)+(-4))*((-21)-(-2)))))+(((((-19)*(-29))*((-8)-(22)))+(((3)-(27))*((-22)*(-6))))*((((19)-(15))+((0)+(6)))-(((-1)-(19))-((10)*(-2))))))-((((((-23)+(4))+((17)+(-30)))*(((-14)+(13))-((18)+(-23))))-((((25)+(-12))-((-1)*(-19)))-(((-28)-(-19))*((1)+(15)))))*(((((-8)*(-1))-((29)*(-17)))*(((22)-(-23))-((13)-(17))))-((((-7)*(28))+((16)*(18)))*(((14)*(-8))+((-6)*(-13))))))))-((((((((-17)*(-2))-((16)+(-23)))*(((-26)+(-29))-((-11)+(18))))+((((11)+(5))-((-15)+(2)))*(((19)+(-2))+((16)+(26)))))-(((((0)-(23))*((18)+(-13)))+(((-8)-(-5))*((-26)+(-13))))+((((23)-(11))+((23)*(-18)))-(((-17)-(-25))*((5)-(10))))))+((((((15)+(13))+((16)+(11)))*(((3)-(-8))+((-24)-(-29))))+((((-7)*(3))+((6)*(-13)))-(((-12)-(-19))-((3)+(-28)))))+(((((-22)*(-16))*((8)-(16)))+(((1)-(-11))*((16)-(16))))-((((-22)-(6))-((5)*(-15)))*(((2)-(1))*((-3)+(10)))))))+(((((((28)-(-6))+((21)-(28)))*(((-7)+(11))*((-14)-(-27))))*((((-15)+(-22))+((-15)-(-24)))-(((-18)+(18))-((-24)+(-7)))))-(((((7)+(28))+((-14)*(-27)))*(((-6)-(-14))*((5)+(-24))))-((((17)+(14))-((-22)+(26)))-(((23)+(-25))*((-3)+(-15))))))-((((((-3)*(7))-((17)*(-25)))-(((-19)*(26))*((2)*(-20))))-((((-13)-(-22))-((11)-(4)))-(((10)*(-19))+((17)*(-25)))))*(((((-10)-(5))-((-17)*(-22)))+(((-7)*(-3))*((0)+(-16))))+((((-4)*(-8))*((-21)+(-1)))-(((4)-(23))*((-26)-(25))))))))));\n" +
                            "  return a;\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(55093520, mars.run());
    }

    @Test
    public void testLargeArithmeticExpression3() throws MarsException {
        // Arithmetic expression with depth 10, should not produce overflows, but only contains +, -, * anyway
        final String code = "int main() {\n" +
                            "  int a = ((((((((((-26)+(-20))-((-8)*(25)))-(((-22)*(15))+((-8)-(-25))))+((((23)+(-22))+((12)+(-8)))+(((-10)*(7))-((12)-(13)))))+(((((11)-(-29))+((5)+(17)))-(((-6)-(23))*((-10)+(20))))*((((28)*(13))-((29)*(12)))+(((-26)-(17))*((-8)*(-26))))))-((((((26)-(-6))-((-30)+(-4)))+(((12)+(0))*((-8)+(-23))))*((((24)*(0))+((6)-(-10)))+(((-9)-(8))-((-25)+(9)))))*(((((-3)-(-1))-((-28)+(11)))*(((22)+(-7))+((22)-(5))))+((((-9)-(10))*((-18)-(-9)))+(((-12)*(21))*((-28)+(-9)))))))+(((((((14)*(1))*((15)*(-6)))-(((1)-(-17))+((-15)-(-3))))-((((13)+(16))-((11)-(-10)))*(((-24)*(10))-((12)-(-14)))))-(((((-28)*(9))*((17)*(-19)))+(((19)-(13))*((-18)*(5))))-((((29)*(-16))*((-10)*(-8)))-(((22)*(23))*((-15)+(2))))))+((((((-14)*(-24))-((-28)*(24)))+(((-9)-(-2))*((13)-(4))))+((((21)*(-1))*((-14)*(11)))-(((-25)-(16))+((-22)*(25)))))*(((((-22)*(5))+((-8)+(1)))+(((24)+(-14))+((13)-(28))))-((((12)-(-28))-((18)*(1)))*(((12)*(27))-((2)+(-30))))))))+((((((((-5)*(-16))-((11)*(2)))-(((13)+(29))*((-17)*(13))))+((((-7)+(2))-((-2)+(2)))*(((-15)-(-18))*((27)*(-17)))))+(((((5)-(24))-((-12)*(5)))+(((5)*(-13))+((25)+(9))))+((((5)-(-7))-((-22)+(10)))*(((-10)+(25))-((-18)-(1))))))*((((((11)-(-25))+((-29)-(18)))*(((29)*(23))+((9)+(10))))*((((-18)-(-21))*((12)-(12)))*(((-20)*(3))+((2)-(-29)))))-(((((-28)+(-11))*((4)*(9)))-(((24)*(15))-((-22)-(29))))+((((11)*(-24))-((-9)*(4)))+(((1)*(-8))+((-17)-(17)))))))+(((((((-14)-(2))-((2)*(-17)))-(((-1)-(22))-((-5)*(24))))-((((-27)-(11))*((-27)+(8)))-(((-22)+(-10))+((-23)+(-11)))))*(((((25)*(25))*((8)-(-26)))*(((19)-(14))-((-21)*(3))))+((((-1)*(28))+((16)+(3)))-(((-28)-(-6))-((-28)-(-9))))))+((((((-29)*(-19))-((6)*(-17)))+(((23)-(-24))*((12)*(19))))*((((-18)+(19))-((-16)-(16)))+(((7)+(3))+((-18)*(10)))))+(((((-3)-(-12))*((-1)-(-23)))*(((-6)-(-7))+((5)+(-5))))*((((9)-(-14))+((29)+(-5)))-(((-8)-(6))*((28)*(13)))))))))-(((((((((12)*(-5))-((29)-(-22)))*(((-9)+(20))+((-22)+(15))))*((((18)+(-6))-((-9)+(12)))*(((-13)+(-25))+((11)+(12)))))-(((((-23)+(3))+((-24)*(-1)))-(((-28)+(17))*((-25)-(24))))-((((-27)*(-10))*((17)*(11)))+(((0)*(-23))-((26)+(24))))))+((((((-27)*(27))+((19)*(9)))-(((-13)+(-11))-((20)*(-7))))-((((7)*(-11))*((21)*(1)))+(((-20)-(11))+((-4)+(-4)))))*(((((-7)-(-26))+((-11)+(-26)))-(((-5)*(25))+((4)*(-16))))+((((-30)*(-15))+((-24)+(15)))+(((12)*(-30))+((-11)*(16)))))))-(((((((-8)*(26))*((22)-(-25)))*(((18)-(14))*((18)-(-26))))-((((8)+(16))+((0)-(17)))+(((14)*(29))+((-3)*(-28)))))+(((((-6)*(13))*((0)+(29)))-(((3)-(24))*((-7)+(-27))))-((((-6)+(-13))-((22)-(-7)))*(((-9)+(29))+((12)-(20))))))*((((((7)-(27))+((0)+(29)))+(((-16)*(-17))-((18)-(14))))+((((8)-(5))-((-16)*(24)))-(((-21)+(0))+((-4)*(12)))))+(((((-10)+(21))-((0)*(6)))*(((-5)-(24))+((0)*(-15))))-((((-11)+(-22))-((-25)+(-19)))+(((-20)-(6))-((-5)*(-16))))))))+((((((((-20)*(-12))*((16)-(3)))+(((16)+(29))-((10)-(18))))-((((-30)*(-6))*((-16)+(-24)))+(((-25)-(-3))+((10)-(17)))))-(((((-30)*(20))-((20)*(2)))+(((-11)*(27))-((29)-(-8))))-((((-24)+(-30))*((7)+(14)))-(((20)+(18))+((-20)*(-15))))))+((((((-3)*(12))+((-1)-(-28)))*(((3)-(23))+((-14)*(1))))-((((-14)+(-22))-((15)-(10)))*(((-18)-(29))*((10)-(-7)))))+(((((19)-(-17))-((20)*(-28)))-(((-17)-(-12))-((-20)-(-27))))-((((16)*(25))+((16)-(-20)))-(((7)+(-3))+((20)-(-30)))))))*(((((((21)-(5))-((27)-(-9)))+(((-20)+(-16))+((21)*(-26))))-((((0)-(5))-((27)*(13)))+(((11)+(23))+((-16)+(25)))))+(((((21)+(27))*((-17)+(17)))+(((7)+(-2))*((-15)-(5))))-((((8)+(19))+((22)+(29)))-(((8)+(3))-((5)+(-13))))))+((((((-4)+(-16))-((-13)-(18)))*(((10)*(10))-((-6)*(1))))-((((18)-(25))*((17)-(-8)))-(((9)+(-26))+((-19)*(-9)))))-(((((-29)+(25))*((13)+(-22)))+(((-26)-(-7))-((-17)+(-28))))-((((0)+(5))+((-6)*(0)))-(((1)-(29))*((0)-(15))))))))));\n" +
                            "  return a;\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(-2077495255, mars.run());
    }

    @Test
    public void testLargeArithmeticExpression4() throws MarsException {
        // Arithmetic expression with depth 10, should not produce overflows, but only contains +, -, * anyway
        final String code = "int main() {\n" +
                            "  int a = ((((((((((6)+(23))*((-10)+(-20)))+(((-5)-(28))+((-11)+(9))))+((((-24)-(5))*((-5)*(-18)))-(((22)+(-24))+((-8)+(12)))))+(((((24)-(-23))-((-21)-(-14)))*(((11)+(3))-((19)*(27))))-((((9)-(10))+((7)*(-28)))*(((27)*(-30))*((-6)-(23))))))-((((((-4)*(26))*((6)+(25)))*(((-9)*(24))+((-13)*(-3))))+((((16)-(3))+((-6)-(7)))*(((17)+(16))-((3)+(11)))))-(((((-4)+(-11))+((-23)-(15)))-(((-30)*(-5))-((15)*(4))))+((((-9)*(6))+((22)*(-11)))*(((-24)+(16))*((-9)-(-24)))))))+(((((((-15)*(1))+((6)-(-11)))*(((25)-(3))-((-4)-(-18))))+((((-1)+(-2))-((24)*(3)))-(((-8)*(-7))*((-7)+(-14)))))-(((((1)+(-20))*((21)+(-1)))*(((-15)*(-16))-((-19)-(-24))))*((((-2)*(11))*((6)-(-26)))+(((-1)-(19))*((-5)-(24))))))-((((((14)+(-21))-((18)+(-3)))-(((10)+(-1))+((-24)-(-9))))+((((-26)*(-17))-((-2)-(-20)))-(((-30)*(-10))*((-8)-(1)))))+(((((-15)+(-8))-((-8)*(-11)))+(((17)+(-21))-((4)*(14))))*((((-20)+(10))+((-15)-(-20)))+(((19)+(-25))*((-4)-(-3))))))))+((((((((26)+(3))*((-7)-(-2)))+(((20)-(-2))*((-1)*(-21))))+((((23)-(-27))*((11)*(-30)))+(((-11)+(-27))*((29)+(-5)))))*(((((-13)-(-26))-((-1)*(-11)))-(((7)+(1))*((18)*(1))))-((((-4)*(14))+((-26)-(18)))-(((0)+(-29))*((-24)*(-14))))))-((((((15)*(-25))*((10)+(-2)))-(((-29)*(-26))+((-10)+(-29))))-((((-11)*(15))*((-22)-(-11)))+(((-29)*(-21))-((17)-(16)))))+(((((14)-(26))+((-7)+(27)))*(((-25)+(-10))*((-30)+(11))))+((((14)+(-2))+((-14)-(21)))*(((0)+(21))-((-15)-(19)))))))-(((((((5)*(3))+((-10)*(28)))+(((18)-(-18))-((21)-(2))))-((((13)+(17))+((-6)+(-6)))-(((-12)*(22))-((28)*(15)))))*(((((29)+(-19))+((1)*(-21)))*(((-30)-(11))+((17)-(8))))*((((-22)-(-10))*((-15)-(2)))+(((-2)+(-16))+((13)-(8))))))-((((((-15)-(19))+((-5)*(1)))*(((5)-(-7))*((-21)*(4))))-((((-19)*(22))+((19)-(-3)))*(((-30)-(5))-((10)+(-21)))))+(((((-3)-(21))-((-9)-(10)))+(((-26)+(-29))+((-24)-(26))))*((((-28)*(-22))*((-2)-(27)))*(((-7)+(-15))*((-2)+(-7)))))))))-(((((((((-17)-(-9))*((1)-(-6)))*(((16)*(-12))+((20)*(-4))))+((((-9)*(-1))+((-30)-(-24)))*(((16)+(-7))-((-4)*(14)))))*(((((-22)+(-9))-((-28)+(4)))*(((27)+(-1))+((-12)*(20))))+((((18)*(-21))+((4)-(5)))-(((4)+(27))+((-12)*(-18))))))-((((((-13)+(-15))-((15)+(-8)))-(((-6)*(9))*((18)-(10))))*((((12)*(19))-((-2)+(8)))+(((3)-(5))-((-23)+(-30)))))+(((((-29)-(-28))+((14)*(-1)))-(((18)*(-3))+((-14)+(-15))))-((((11)+(24))*((16)-(-14)))-(((-8)+(-30))+((22)*(-30)))))))-(((((((-6)+(3))+((25)*(-4)))+(((-13)+(3))+((-22)*(-4))))-((((-29)*(13))+((25)+(-2)))*(((19)-(11))+((-20)-(10)))))+(((((23)*(-20))-((21)-(17)))+(((22)+(29))-((-11)-(24))))+((((14)-(-29))*((23)-(-1)))-(((8)*(-28))*((28)+(0))))))-((((((-5)+(21))-((0)-(-19)))*(((-7)-(-26))+((15)*(3))))+((((12)+(19))*((4)*(11)))-(((21)-(26))+((12)-(10)))))-(((((-20)+(-6))*((21)*(29)))*(((22)-(-27))-((-3)+(25))))*((((3)+(14))-((-30)*(16)))-(((10)+(28))+((28)-(19))))))))+((((((((-23)-(10))+((-2)*(21)))+(((29)-(1))*((-30)*(-5))))*((((10)-(-26))+((11)+(-23)))*(((-29)-(10))+((-12)-(4)))))-(((((-5)+(12))*((-5)-(3)))*(((24)-(-21))*((-19)+(-15))))*((((-16)-(-21))-((-9)-(-19)))+(((0)*(21))*((-30)*(2))))))-((((((-25)+(-27))-((-4)*(-21)))+(((7)*(21))-((3)+(21))))-((((28)+(-28))*((3)+(26)))-(((-4)*(-18))*((-15)*(-1)))))*(((((-27)-(-15))-((-25)*(-18)))-(((19)*(11))-((14)*(-3))))-((((21)*(21))-((-10)+(16)))-(((-18)*(-3))*((-24)+(-30)))))))-(((((((-15)-(-17))-((12)-(6)))*(((-9)+(20))-((-11)+(13))))-((((14)*(-23))+((-8)-(7)))-(((11)+(-27))*((-16)*(1)))))*(((((-7)+(11))-((15)*(2)))*(((-30)*(0))-((22)+(-5))))+((((23)-(-15))*((22)-(-17)))+(((-22)+(-13))*((5)*(6))))))-((((((-8)-(-14))*((0)-(5)))-(((-15)+(6))+((-4)+(-13))))-((((9)*(21))+((15)-(17)))*(((7)+(24))+((20)*(-15)))))+(((((-26)+(17))-((-27)*(-6)))*(((-23)+(22))*((-19)+(-16))))*((((29)+(-28))-((6)+(16)))*(((2)*(19))*((-11)-(-18))))))))));\n" +
                            "  return a;\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(375212473, mars.run());
    }

    @Test
    public void testDivision() throws MarsException {
        final String code = "int main() {\n" +
                            "  return 52534234/5235;\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(10035, mars.run());
    }

    @Test
    public void testDivision2() throws MarsException {
        // Round to zero
        final String code = "int main() {\n" +
                            "  return (-52534234)/5235;\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(-10035, mars.run());
    }

    @Test
    public void testNegation() throws MarsException {
        // BONUS
        final String code = "int main() {\n" +
                            "  char a = 0;\n" +
                            "  if ((!a) != 1) return 1;\n" +
                            "  if (!(!a)) return 2;\n" +
                            "  if (!a) return 3;\n" +
                            "  return 4;\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(3, mars.run());
    }

    @Test
    public void testNegation2() throws MarsException {
        // BONUS
        final String code = "int main() {\n" +
                            "  int a = -23423;\n" +
                            "  if ((!a) != 0) return 1;\n" +
                            "  if ((!(!a)) != 1) return 2;\n" +
                            "  if (!a) return 3;\n" +
                            "  return 4;\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(4, mars.run());
    }

    @Test
    public void testAndOr1() throws MarsException {
        // BONUS
        final String code = "int diverge() {\n" +
                            "  while (1);\n" +
                            "}\n" +
                            "int main() {\n" +
                            "  if (0 && diverge()) return 1;\n" +
                            "  if (1 || diverge()) return 2;\n" +
                            "  return 3;\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(2, mars.run());
    }

    @Test
    public void testAndOr2() throws MarsException {
        // BONUS
        final String code = "int main() {\n" +
                            "  if ((-453 && 538593) != 1) return 1;\n" +
                            "  if ((0 || -46934) != 1) return 2;\n" +
                            "  return 3;\n" +
                            "}";
        final MarsUtil mars = prepareCode(code);
        assertEquals(3, mars.run());
    }
}
