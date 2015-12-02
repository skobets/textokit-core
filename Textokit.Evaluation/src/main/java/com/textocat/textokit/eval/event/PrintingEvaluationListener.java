package com.textocat.textokit.eval.event;

import javax.annotation.PreDestroy;
import java.io.*;

/**
 * @author Rinat Gareev
 */
public abstract class PrintingEvaluationListener extends DocumentUriHoldingEvaluationListener {

    // config
    private File outputFile;
    // derived
    protected PrintWriter printer;

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    protected void init() throws Exception {
        // init printer
        Writer writer;
        if (outputFile != null) {
            File outputDir = outputFile.getParentFile();
            if (outputDir != null) {
                outputDir.mkdirs();
            }
            OutputStream os = new FileOutputStream(outputFile);
            writer = new BufferedWriter(new OutputStreamWriter(os, "utf-8"));
        } else {
            writer = new OutputStreamWriter(System.out);
        }
        printer = new PrintWriter(writer, true);
    }

    @PreDestroy
    protected void clean() {
        // do not close stdout!
        if (outputFile != null && printer != null) {
            printer.close();
            printer = null;
        }
    }
}