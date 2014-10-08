package retest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Regexer {
    private final List<RegexEntry> list = new ArrayList<>();

    public Regexer(final File fromFile) throws IOException {
        final BufferedReader r = new BufferedReader(new FileReader(fromFile));
        try {
            String regex;
            while (null != (regex = r.readLine())) {
                final String replacement = Objects.requireNonNull(r.readLine());
                list.add(new RegexEntry(regex, replacement));
            }
        } finally {
            r.close();
        }
        System.out.println("Regexer initialized with "
            + list.size() + " rules.");
    }

    public String process(final String arg) {
        String curr = arg;
        for (final RegexEntry entry : list) {
            curr = curr.replaceAll(entry.regex, entry.replacement);
        }
        return curr;
    }

    private static final class RegexEntry {
        public final String regex;

        public final String replacement;

        public RegexEntry(final String regex, final String replacement) {
            this.regex = regex;
            this.replacement = replacement;
        }
    }
}
