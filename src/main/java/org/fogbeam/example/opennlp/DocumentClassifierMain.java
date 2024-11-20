package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.InputStream;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DocumentClassifierMain {

	private static final Logger LOGGER = Logger.getLogger(DocumentClassifierMain.class.getName());

	public static void main(String[] args) {
		// Uso de try-with-resources para manejar el InputStream automáticamente
		try (InputStream is = new FileInputStream("models/en-doccat.model")) {

			// Cargar el modelo
			DoccatModel m = new DoccatModel(is);

			// Entrada para clasificar
			String inputText = "What happens if we have declining bottom-line revenue?";

			// Categorizar la entrada
			DocumentCategorizerME myCategorizer = new DocumentCategorizerME(m);
			double[] outcomes = myCategorizer.categorize(inputText);
			String category = myCategorizer.getBestCategory(outcomes);

			// Loggear el resultado
			LOGGER.info("Input classified as: " + category);

		} catch (IOException e) {
			// Manejo de excepciones
			LOGGER.log(Level.SEVERE, "Error processing the document categorization model or input text.", e);
		}

		// Loggear el final del proceso
		LOGGER.info("Process completed.");
	}
}
