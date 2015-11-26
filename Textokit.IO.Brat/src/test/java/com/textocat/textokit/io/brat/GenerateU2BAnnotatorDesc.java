/**
 *
 */
package com.textocat.textokit.io.brat;

import org.apache.commons.io.IOUtils;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

/**
 * @author Rinat Gareev
 */
public class GenerateU2BAnnotatorDesc {

    public static void main(String[] args) throws Exception {
        AnalysisEngineDescription anDesc = createEngineDescription(UIMA2BratAnnotator.class);
        FileOutputStream fout = new FileOutputStream(
                "src/main/resources/ru/kfu/itis/issst/uima/brat/UIMA2BratAnnotator.xml");
        BufferedOutputStream out = new BufferedOutputStream(fout);
        try {
            anDesc.toXML(out);
        } finally {
            IOUtils.closeQuietly(out);
        }
        System.out.println("Done");
    }

}