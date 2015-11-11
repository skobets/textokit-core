package ru.kfu.itis.issst.uima.tokenizer;

import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngineDescription;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.xml.sax.SAXException;

import ru.kfu.itis.issst.uima.tokenizer.InitialTokenizer;
import ru.kfu.itis.issst.uima.tokenizer.TokenizerAPI;

/**
 * @author Rinat Gareev
 * 
 */
public class GenerateInitialTokenizerDescriptor {

	public static void main(String[] args) throws UIMAException, IOException, SAXException {
		String outputPath = "src/main/resources/ru/kfu/itis/issst/uima/tokenizer/InitialTokenizer.xml";
		TypeSystemDescription tsDesc = TokenizerAPI.getTypeSystemDescription();
		AnalysisEngineDescription desc = createEngineDescription(InitialTokenizer.class, tsDesc);
		FileOutputStream out = new FileOutputStream(outputPath);
		try {
			desc.toXML(out);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
}