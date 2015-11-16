package com.textocat.textokit.morph.lemmatizer

import java.io.File

import com.textocat.textokit.commons.io.IoUtils
import com.textocat.textokit.commons.util.PipelineDescriptorUtils
import com.textocat.textokit.morph.dictionary.MorphDictionaryAPIFactory
import com.textocat.textokit.postagger.PosTaggerAPI
import com.textocat.textokit.segmentation.SentenceSplitterAPI
import com.textocat.textokit.tokenizer.TokenizerAPI
import org.apache.uima.resource.metadata.MetaDataObject
import org.apache.uima.resource.metadata.impl.Import_impl

object DescriptionGenerator {

  def getDescriptionWithDep() = {
    val aeDescriptions = scala.collection.mutable.Map.empty[String, MetaDataObject]
    aeDescriptions("tokenizer") = TokenizerAPI.getAEImport()
    aeDescriptions("sentenceSplitter") = SentenceSplitterAPI.getAEImport()
    //
    val posTaggerDescImport = new Import_impl()
    posTaggerDescImport.setName("pos_tagger")
    aeDescriptions("pos-tagger") = posTaggerDescImport
    //
    aeDescriptions("lemmatizer") = Lemmatizer.createDescription()
    //
    import scala.collection.JavaConversions._
    val aggrDesc = PipelineDescriptorUtils.createAggregateDescription(aeDescriptions)
    //
    // bind MorphDictionary
    val morphDictDesc = MorphDictionaryAPIFactory.getMorphDictionaryAPI().getResourceDescriptionForCachedInstance()
    morphDictDesc.setName(PosTaggerAPI.MORPH_DICTIONARY_RESOURCE_NAME)
    PipelineDescriptorUtils.getResourceManagerConfiguration(aggrDesc).addExternalResource(morphDictDesc)
    aggrDesc
  }

  def serializeDescriptionWithDep(pathToXML: String) = {
    val description = getDescriptionWithDep()
    // TODO in JAVA 7
    // val fileWriter = Files.newBufferedWriter(Paths.get(pathToXML), StandardCharsets.UTF_8)
    val fileWriter = IoUtils.openBufferedWriter(new File(pathToXML))
    description.toXML(fileWriter)
    fileWriter.close()
  }
}