package com.textocat.textokit.eval.event;

import org.apache.uima.cas.text.AnnotationFS;

import java.util.EventListener;

/**
 * The are following assertions:
 * <ul>
 * <li>before each document (i.e., CAS) {@link #onDocumentChange(String)} is
 * invoked
 * <li>'Missing', 'ExactMatch', 'PartialMatch' events are raised in order that
 * corresponds to the default ordering of gold annotations in a text
 * <li>for each gold annotation corresponding events are raised in the following
 * order:
 * <ul>
 * <li>ExactMatch
 * <li>PartialMatch
 * <li>Missing
 * </ul>
 * <li>Missing and ExactMatch events are mutually exclusive per gold annotation
 * <li>'Spurious' events are raised last per document
 * </ul>
 *
 * @author Rinat Gareev
 */
public interface EvaluationListener extends EventListener {

    /**
     * Invoked before processing each document. After processing a document
     * invoked with docUri = = null.
     *
     * @param docUri
     */
    public void onDocumentChange(String docUri);

    public void onMissing(AnnotationFS goldAnno);

    public void onExactMatch(AnnotationFS goldAnno, AnnotationFS sysAnno);

    /**
     * Report partial matching on given goldAnno. Note that the same sysAnno may
     * be reported as partially matched for multiple 'gold' annotations.
     *
     * @param docUri
     * @param goldAnno
     * @param sysAnno
     */
    public void onPartialMatch(AnnotationFS goldAnno, AnnotationFS sysAnno);

    /**
     * Events of this type are raised last for each document, i.e., after
     * exactMatch, partialMatch & missing.
     *
     * @param sysAnno
     */
    public void onSpurious(AnnotationFS sysAnno);

    public void onEvaluationComplete();
}