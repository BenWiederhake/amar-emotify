package retest;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public final class Tester {
    private static final String REGEX_FILE = "regexes.txt";

    private static final String TESTS_DIR = "tests/";

    private final Regexer regexer;

    private final File testFile;

    public Tester(final Regexer regexer, final File testFile) {
        this.regexer = regexer;
        this.testFile = testFile;
    }

    @Test
    public void test() throws IOException {
        System.out.print("Testing file " + testFile + " ... ");
        final BufferedReader r =
            new BufferedReader(new FileReader(testFile));
        try {
            int counter = 0;
            String challenge;
            while (null != (challenge = r.readLine())) {
                final String expected = Objects.requireNonNull(r.readLine());
                runTest(challenge, expected);
                ++counter;
            }
            System.out.print(" all " + counter + " tests passed!");
        } finally {
            System.out.println();
            r.close();
        }
    }

    private void runTest(final String challenge, final String expected) {
        assertEquals(expected, regexer.process(challenge));
    }

    @Parameterized.Parameters(name = "{1}")
    public static List<Object[]> data() {
        System.out.println("Loading regexes ...");
        final Regexer r;
        try {
            r = new Regexer(new File(REGEX_FILE));
        } catch (final IOException e) {
            throw new AssertionError("Regex file " + REGEX_FILE + " invalid", e);
        }

        System.out.println("Reading tests ...");
        final File[] files = new File(TESTS_DIR).listFiles();

        final Object[][] ret = new Object[files.length][2];
        for (int i = 0; i < files.length; ++i) {
            ret[i][0] = r;
            ret[i][1] = files[i];
        }

        return Arrays.asList(ret);
    }
}
