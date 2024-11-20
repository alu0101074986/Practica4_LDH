package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;

/**
 * @file ChunkerMain.java
 * @brief Clase principal para realizar el chunking de tokens y etiquetas POS utilizando OpenNLP.
 *
 * @author Marcelo Daniel Choque
 * @date 2024-11-19
 */
public class ChunkerMain {

	private static final Logger logger = Logger.getLogger(ChunkerMain.class.getName());

	/**
	 * Método principal que ejecuta el chunking sobre tokens y etiquetas POS.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados).
	 */
	public static void main(String[] args) {
		try (InputStream modelIn = new FileInputStream("models/en-chunker.model")) {
			ChunkerModel model = new ChunkerModel(modelIn);
			ChunkerME chunker = new ChunkerME(model);

			// Tokens de entrada
			String[] sent = {
					"Rockwell", "International", "Corp.", "'s", "Tulsa", "unit", "said", "it",
					"signed", "a", "tentative", "agreement", "extending", "its", "contract",
					"with", "Boeing", "Co.", "to", "provide", "structural", "parts", "for",
					"Boeing", "'s", "747", "jetliners", "."
			};

			// Etiquetas POS de entrada
			String[] pos = {
					"NNP", "NNP", "NNP", "POS", "NNP", "NN", "VBD", "PRP", "VBD", "DT",
					"JJ", "NN", "VBG", "PRP$", "NN", "IN", "NNP", "NNP", "TO", "VB",
					"JJ", "NNS", "IN", "NNP", "POS", "CD", "NNS", "."
			};

			// Chunking y cálculo de probabilidades
			String[] tags = chunker.chunk(sent, pos);
			double[] probs = chunker.probs();

			// Registrar los resultados
			for (int i = 0; i < sent.length; i++) {
				logger.info("Token [" + sent[i] + "] tiene chunk tag [" + tags[i] +
						"] con probabilidad = " + probs[i]);
			}

		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error al cargar el modelo o procesar los datos de entrada.", e);
		}

		logger.info("Procesamiento completado.");
	}
}
