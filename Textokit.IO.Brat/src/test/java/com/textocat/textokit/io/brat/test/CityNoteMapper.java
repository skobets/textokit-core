/**
 *
 */
package com.textocat.textokit.io.brat.test;

import com.textocat.textokit.io.brat.BratNoteMapper;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationFS;

import static com.textocat.textokit.commons.util.AnnotatorUtils.annotationTypeExist;
import static com.textocat.textokit.commons.util.AnnotatorUtils.featureExist;

/**
 * @author Rinat Gareev
 */
public class CityNoteMapper implements BratNoteMapper {

    private static final String CITY_TYPE_NAME = "issst.test.HL_City";

    private Feature latFeat;
    private Feature longFeat;

    @Override
    public void typeSystemInit(TypeSystem ts) throws AnalysisEngineProcessException {
        Type cityType = ts.getType(CITY_TYPE_NAME);
        annotationTypeExist(CITY_TYPE_NAME, cityType);
        longFeat = featureExist(cityType, "longitude");
        latFeat = featureExist(cityType, "latitude");
    }

    @Override
    public String makeNote(AnnotationFS uAnno) {
        int latitude = uAnno.getIntValue(latFeat);
        int longitude = uAnno.getIntValue(longFeat);
        if (latitude == 0 || longitude == 0) {
            return null;
        }
        return String.format("%s:%s", latitude, longitude);
    }

    @Override
    public void parseNote(AnnotationFS uAnno, String noteContent) {
        if (noteContent == null) {
            return;
        }
        String[] coords = noteContent.split(":");
        int latitude = Integer.valueOf(coords[0]);
        int longitude = Integer.valueOf(coords[1]);
        uAnno.setIntValue(latFeat, latitude);
        uAnno.setIntValue(longFeat, longitude);
    }
}