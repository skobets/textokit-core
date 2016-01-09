package com.textocat.textokit.depparser.mst;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import mstparser.DependencyInstance;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

/**
 * @author Rinat Gareev
 */
public class MSTFormat {

    static final Joiner mstTokenJoiner = Joiner.on('\t');
    static final Joiner mstTagJoiner = Joiner.on('\t').useForNull("null");

    public static void writeInstance(Writer out,
                                     Iterable<String> forms, Iterable<String> tags, Iterable<Integer> heads)
            throws IOException {
        mstTokenJoiner.appendTo(out, forms);
        out.write("\n");
        mstTagJoiner.appendTo(out, tags);
        out.write("\n");
        mstTokenJoiner.appendTo(out, heads);
        out.write("\n\n");
    }

    public static void writeInstance(Writer out, DependencyInstance instance)
            throws IOException {
        writeInstance(out,
                Arrays.asList(instance.forms),
                Arrays.asList(instance.postags),
                Lists.newArrayList(ArrayUtils.toObject(instance.heads)));
    }

    private MSTFormat() {
    }
}
