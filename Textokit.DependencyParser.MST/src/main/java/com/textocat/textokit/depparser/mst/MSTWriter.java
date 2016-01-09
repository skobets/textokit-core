package com.textocat.textokit.depparser.mst;

import com.google.common.collect.Lists;
import com.textocat.textokit.commons.io.IoUtils;
import com.textocat.textokit.depparser.Dependency;
import com.textocat.textokit.morph.fs.Word;
import com.textocat.textokit.segmentation.SentenceSplitterAPI;
import com.textocat.textokit.segmentation.fstype.Sentence;
import org.apache.commons.io.IOUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.fit.descriptor.OperationalProperties;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import static com.google.common.collect.Lists.newArrayListWithExpectedSize;
import static com.textocat.textokit.commons.cas.AnnotationUtils.coveredTextFunction;
import static com.textocat.textokit.morph.commons.TagUtils.tagFunction;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;
import static org.apache.uima.fit.factory.TypeSystemDescriptionFactory.createTypeSystemDescription;

/**
 * @author Rinat Gareev
 */
@OperationalProperties(multipleDeploymentAllowed = false)
public class MSTWriter extends JCasAnnotator_ImplBase {

    public static final AnalysisEngineDescription createDescription(File outputFile)
            throws ResourceInitializationException {
        TypeSystemDescription tsDesc = createTypeSystemDescription(
                SentenceSplitterAPI.TYPESYSTEM_SENTENCES,
                "com.textocat.textokit.depparser.dependency-ts");
        return createEngineDescription(MSTWriter.class, tsDesc,
                PARAM_OUTPUT_FILE, outputFile.getPath());
    }

    public static final String PARAM_OUTPUT_FILE = "outputFile";

    @ConfigurationParameter(name = PARAM_OUTPUT_FILE, mandatory = true)
    private File outputFile;
    // state fields
    private BufferedWriter outWriter;

    @Override
    public void initialize(UimaContext ctx) throws ResourceInitializationException {
        super.initialize(ctx);
        //
        try {
            outWriter = IoUtils.openBufferedWriter(outputFile);
        } catch (IOException e) {
            throw new ResourceInitializationException(e);
        }
    }

    @Override
    public void process(JCas jCas) throws AnalysisEngineProcessException {
        for (Sentence sent : JCasUtil.select(jCas, Sentence.class)) {
            writeSentence(outWriter, jCas, sent);
        }
    }

    static void writeSentence(Writer outWriter, JCas jCas, Sentence sent)
            throws AnalysisEngineProcessException {
        List<Word> words = JCasUtil.selectCovered(jCas, Word.class, sent);
        List<Dependency> deps = JCasUtil.selectCovered(jCas, Dependency.class, sent);
        if (words.size() != deps.size()) {
            throw new IllegalStateException("Words.size != Deps.size");
        }
        List<String> forms = Lists.transform(words, coveredTextFunction());
        List<String> tags = Lists.transform(words, tagFunction());
        List<Integer> heads = newArrayListWithExpectedSize(deps.size());
        for (Dependency dep : deps) {
            Word head = dep.getHead();
            if (head == null) {
                heads.add(0);
            } else {
                int headIndex = words.indexOf(head);
                if (headIndex < 0) {
                    throw new IllegalStateException();
                }
                heads.add(headIndex + 1);
            }
        }
        // sanity check
        if (heads.size() != forms.size() || tags.size() != forms.size()) {
            throw new IllegalStateException();
        }
        //
        try {
            MSTFormat.writeInstance(outWriter, forms, tags, heads);
        } catch (IOException e) {
            throw new AnalysisEngineProcessException(e);
        }
    }

    @Override
    public void collectionProcessComplete() throws AnalysisEngineProcessException {
        super.collectionProcessComplete();
        IOUtils.closeQuietly(outWriter);
        outWriter = null;
    }

    @Override
    public void destroy() {
        super.destroy();
        IOUtils.closeQuietly(outWriter);
        outWriter = null;
    }
}
