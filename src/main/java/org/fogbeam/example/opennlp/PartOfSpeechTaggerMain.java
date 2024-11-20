
package org.fogbeam.example.opennlp;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @file PartOfSpeechTaggerMain.java
 * @brief Realiza etiquetado gramatical (POS tagging) sobre un conjunto de tokens usando OpenNLP.
 *
 * @author Marcelo Daniel Choque
 * @date 2024-11-19
 */
public class PartOfSpeechTaggerMain {

	private static final Logger logger = Logger.getLogger(PartOfSpeechTaggerMain.class.getName());

	public static void main(String[] args) {
		// Uso de try-with-resources para manejar el InputStream
		try (InputStream modelIn = new FileInputStream("models/en-pos-maxent.bin")) {

			// Carga el modelo POS
			POSModel model = new POSModel(modelIn);
			POSTaggerME tagger = new POSTaggerME(model);

			// Tokens de entrada
			String[] sent = {
					"Most", "large", "cities", "in", "the", "US", "had",
					"morning", "and", "afternoon", "newspapers", "."
			};

			// Realizar el etiquetado POS
			String[] tags = tagger.tag(sent);
			double[] probs = tagger.probs();

			// Registrar los resultados
			for (int i = 0; i < sent.length; i++) {
				logger.info("Token [" + sent[i] + "] tiene POS [" + tags[i] + "] con probabilidad = " + probs[i]);
			}

		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error al cargar el modelo o procesar los datos de entrada.", e);
		} finally {
			logger.info("Procesamiento completado.");
		}
	}
}
