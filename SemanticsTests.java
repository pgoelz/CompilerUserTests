package prog2.tests.user;

import org.junit.Test;
import prog2.tests.CompilerTests;
import prog2.tests.FatalCompilerError;
import tinycc.diagnostic.Locatable;
import tinycc.diagnostic.Location;
import tinycc.parser.Lexer;

import java.io.StringReader;

import static org.junit.Assert.fail;

public class SemanticsTests extends CompilerTests {
    private void runIntegrationTest(final String name, final String code) {
        final StringReader r = new StringReader(code);
        final Lexer        l = new Lexer(diagnostic, r, name);
        compiler.parseTranslationUnit(l);
        compiler.checkSemantics();
    }

    private void runExpectSemanticException(final String name, final String code, Locatable loc) {
        final StringReader r = new StringReader(code);
        final Lexer        l = new Lexer(diagnostic, r, name);
        compiler.parseTranslationUnit(l);
        try {
            compiler.checkSemantics();
            fail("Did not fail.");
        } catch (final FatalCompilerError fce) {
            checkLocation(fce, loc);
        }
    }

    @Test
    public void testRedeclareFunction() {
        final String code = ("int foo(char);\n" +
                             "int foo(char);\n" +
                             "int main() {}");
        runIntegrationTest("redec", code);
    }

    @Test
    public void testBadRedeclareFunction() {
        final String code = ("int foo(char);\n" +
                             "int foo(int);\n" +
                             "int main() {}");
        runExpectSemanticException("badredec", code, new Location("badredec", 2, 5));
    }

    @Test
    public void testBadFunctionDefinition() {
        final String code = ("int foo(char);\n" +
                             "int foo(int a) {\n" +
                             "return a;\n" +
                             "}\n" +
                             "int main() {}");
        runExpectSemanticException("badredec", code, new Location("badredec", 2, 5));
    }

    @Test
    public void testFaculty() {
        final String code = ("int fac(int x) {\n" +
                             "if (x < 1) return 1;\n" +
                             "else return x * (fac (x - 1));\n" +
                             "}\n" +
                             "int main() {}");
        runIntegrationTest("fac", code);
    }

    @Test
    public void testFacultyChar() {
        final String code = ("int fac(char x) {\n" +
                             "if (x < 1) return 1;\n" +
                             "else return x * (fac (x - 1));\n" +
                             "}\n" +
                             "int main() {}");
        runIntegrationTest("facc", code);
    }

    @Test
    public void testBadRecursion() {
        final String code = ("int fac(char x) {\n" +
                             "int a = 3;\n" +
                             "return fac(&a);\n" +
                             "}\n" +
                             "int main() {}");
        runExpectSemanticException("badrec", code, new Location("badrec", 3, 11));
    }

    @Test
    public void testBadReturnType() {
        final String code = ("char* fac(char x) {\n" +
                             "int a = 3;\n" +
                             "return a;\n" +
                             "}\n" +
                             "int main() {}");
        runExpectSemanticException("name", code, new Location("name", 3, 1));
    }

    @Test
    public void testReturnEmpty() {
        final String code = ("char fac(char x) {\n" +
                             "int a = 3;\n" +
                             "return;\n" +
                             "}\n" +
                             "int main() {}");
        runExpectSemanticException("name", code, new Location("name", 3, 1));
    }

    @Test
    public void testIntegerRange1() {
        final String code = ("int main() {\n" +
                             "int a = 2147483648;\n" + // 2^31
                             "}");
        runExpectSemanticException("name", code, new Location("name", 2, 9));
    }

    @Test
    public void testIntegerRange2() {
        final String code = ("int main() {\n" +
                             "int a = 2147483647;\n" + // 2^31 - 1
                             "}");
        runIntegrationTest("name", code);
    }

    @Test
    public void testIntegerRange3() {
        final String code = ("int main() {\n" +
                             "int a = -2147483648;\n" + // -2^31
                             "}");
        runIntegrationTest("name", code);
    }

    @Test
    public void testIntegerRange4() {
        final String code = ("int main() {\n" +
                             "int a = -2147483649;\n" + // -2^31 - 1
                             "}");
        runExpectSemanticException("name", code, new Location("name", 2, 10)); // Possibly 2, 9
    }

    @Test
    public void testIntegerRange5() {
        // Check what happens if the integer constant is too big to fit into a java long
        final String code = ("int main() {\n" +
                             "int a = 18446744073709551616;\n" + // 2^64
                             "}");
        runExpectSemanticException("name", code, new Location("name", 2, 9));
    }

    @Test
    public void testIntegerRange6() {
        // If the constant integer value is stored in a java integer, this might be seen as zero and therefor ignored
        final String code = ("int main() {\n" +
                             "int a = 4294967296;\n" + // 2^32
                             "}");
        runExpectSemanticException("name", code, new Location("name", 2, 9));
    }
    @Test
    public void testExampleSpec1() {
        final String code = ("int globalVariable;\n" +
                             "int foo(void);\n" +
                             "int main(void) {\n" +
                             "\tglobalVariable = 0;\n" +
                             "\treturn foo();\n" +
                             "}\n" +
                             "int foo(void) {\n" +
                             "\treturn globalVariable = globalVariable + 1;\n" +
                             "}");
        runIntegrationTest("name", code);
    }

    @Test
    public void testAssignPointer() {
        final String code = ("void fac(void) {\n" +
                             "int a = 3;\n" +
                             "int* b = &a;\n" +
                             "*b = 4;" +
                             "return;\n" +
                             "}\n" +
                             "int main() {}");
        runIntegrationTest("name", code);
    }

    @Test
    public void testLateInitialization() {
        final String code = ("void foo(void) {\n" +
                             "\tbar();\n" +
                             "\treturn;\n" +
                             "}\n" +
                             "void bar(void) {}\n" +
                             "int main() {}");
        runExpectSemanticException("name", code, new Location("name", 2, 2));
    }

    @Test
    public void testDeclareVoidFunction() {
        final String code = ("int foo(void);\n" +
                             "int foo() {\n" +
                             "\treturn 3;\n" +
                             "}\n" +
                             "int main() {}");
        runIntegrationTest("name", code);
    }

    @Test
    public void testNullAsInt() {
        final String code = ("int main() {\n" +
                             "\treturn 0;\n" +
                             "}");
        runIntegrationTest("name", code);
    }

    @Test
    public void testNullAsIntCompute() {
        final String code = ("int main() {\n" +
                             "\tint a = 3;\n" +
                             "\treturn a + 0;\n" +
                             "}");
        runIntegrationTest("name", code);
    }

    @Test
    public void testNullAsPointer() {
        final String code = ("int* foo() {\n" +
                             "\treturn 0;\n" +
                             "}\n" +
                             "int main() {}");
        runIntegrationTest("name", code);
    }

    @Test
    public void testNullIntAsPointer() {
        final String code = ("int* foo() {\n" +
                             "\tint a = 0;\n" +
                             "\treturn a;\n" +
                             "}\n" +
                             "int main() {}");
        runExpectSemanticException("name", code, new Location("name", 3, 2));
    }

    @Test
    public void testNullNullCompare() {
        final String code = ("int main() {\n" +
                             "\tif (0 == 0) {\n" +
                             "\t\treturn 1;\n" +
                             "\t}\n" +
                             "}");
        runIntegrationTest("name", code);
    }

    @Test
    public void compareIntToPointer() {
        final String code = ("int main() {\n" +
                             "\tint a = 3;\n" +
                             "\tint* b = &a;\n" +
                             "\treturn (3 < b);\n" +
                             "}");
        runExpectSemanticException("name", code, new Location("name", 4, 12));
    }

    @Test
    public void testBadMainType() {
        final String code = ("char main() {}");
        runExpectSemanticException("name", code, new Location("name", 1, 6));
    }

    @Test
    public void testMainBadArgumentTypes() {
        final String code = ("int main(int* a) {}");
        runExpectSemanticException("name", code, new Location("name", 1, 5));
    }

    @Test
    public void testMainLongArgumentTypes() {
        final String code = ("int main(int a, char** b) {}");
        runIntegrationTest("name", code);
    }

    @Test
    public void testParamnameEqualsFunctionname() {
        // Unfortunately, this is legal C.
        final String code = ("int foo(int foo) {}\n" +
                             "int main() {}");
        runIntegrationTest("name", code);
    }

    @Test
    public void testDuplicateArgumentName() {
        final String code = ("int main(int a, char** a) {}");
        runExpectSemanticException("name", code, new Location("name", 1, 24));
    }

    @Test
    public void testUseInAssignment() {
        // This is valid C code. The result is not specified, but the behaviour is not undefined.
        final String code = ("int main() {\n" +
                             "  int c = c + 3;\n" +
                             "  return c;\n" +
                             "}");
        runIntegrationTest("name", code);
    }

    @Test
    public void testUseInAssignment2() {
        // This is valid C code. The result is not specified, but the behaviour is not undefined.
        final String code = ("int main() {\n" +
                             "  int c = sizeof(c);\n" +
                             "  return c;\n" +
                             "}");
        runIntegrationTest("name", code);
    }

    @Test
    public void testNonScalarCondition() {
        final String code = ("void foo() {}\n" +
                             "int main() {\n" +
                             " if (foo()) {\n" +
                             "  return 0;\n" +
                             " }\n" +
                             "}");
        runExpectSemanticException("name", code, new Location("name", 3, 2)); // Or 3, 6?
    }

    @Test
    public void testNonScalarConditionWhile() {
        final String code = ("void foo() {}\n" +
                             "int main() {\n" +
                             " while (foo()) {}\n" +
                             "}");
        runExpectSemanticException("name", code, new Location("name", 3, 2)); // Or 3, 9?
    }

    @Test
    public void testInnerReturn() {
        final String code = ("int main() {\n" +
                             " while (1) {\n" +
                             "  return 0;\n" +
                             " }}");
        runIntegrationTest("name", code);
    }
}
