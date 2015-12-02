package com.textocat.textokit.eval;

import com.google.common.base.Objects;
import com.textocat.textokit.eval.event.EvaluationListener;
import org.apache.commons.lang3.event.EventListenerSupport;
import org.apache.uima.cas.text.AnnotationFS;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author Rinat Gareev
 */
public class EvaluationContext {

    @Autowired
    private List<EvaluationListener> listeners;

    private EventListenerSupport<EvaluationListener> listenerSupport =
            new EventListenerSupport<EvaluationListener>(EvaluationListener.class);

    // state
    private String currentDocUri;

    @PostConstruct
    protected void init() {
        if (listeners != null) {
            for (EvaluationListener curListener : listeners) {
                listenerSupport.addListener(curListener);
            }
        }
    }

    public void addListener(EvaluationListener newListener) {
        listenerSupport.addListener(newListener);
    }

    public void reportMissing(AnnotationFS goldAnno) {
        listenerSupport.fire().onMissing(goldAnno);
    }

    public void reportExactMatch(AnnotationFS goldAnno,
                                 AnnotationFS sysAnno) {
        listenerSupport.fire().onExactMatch(goldAnno, sysAnno);
    }

    public void reportPartialMatch(AnnotationFS goldAnno,
                                   AnnotationFS sysAnno) {
        listenerSupport.fire().onPartialMatch(goldAnno, sysAnno);
    }

    public void reportSpurious(AnnotationFS sysAnno) {
        listenerSupport.fire().onSpurious(sysAnno);
    }

    public void reportEvaluationComplete() {
        listenerSupport.fire().onEvaluationComplete();
    }

    public void setCurrentDocUri(String docUri) {
        if (!Objects.equal(docUri, currentDocUri)) {
            currentDocUri = docUri;
            listenerSupport.fire().onDocumentChange(currentDocUri);
        }
    }

    public String getCurrentDocUri() {
        return currentDocUri;
    }
}