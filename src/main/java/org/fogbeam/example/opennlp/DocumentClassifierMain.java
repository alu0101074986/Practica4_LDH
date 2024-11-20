package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.InputStream;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class for document classification using Apache OpenNLP.
 * This class demonstrates how to load a pre-trained document categorization model
 * and classify an input text into a specific category.
 */
public class DocumentClassifierMain {

	/**
	 * Logger instance for logging messages and errors.
	 */
	private static final Logger LOGGER = Logger.getLogger(DocumentClassifierMain.class.getName());

	/**
	 * Main method to execute the document classification process.
	 *
	 * @param args Command-line arguments (not used in this example).
	 */
	public static void main(String[] args) {
		// Use try-with-resources to manage the InputStream automatically
		try (InputStream is = new FileInputStream("models/en-doccat.model")) {

			/**
			 * Load the document categorization model.
			 *
			 * @throws IOException if the model file is not found or cannot be loaded.
			 */
			DoccatModel m = new DoccatModel(is);

			/**
			 * Input text to classify.
			 */
			String inputText = "What happens if we have declining bottom-line revenue?";

			/**
			 * Categorize the input text using the model.
			 */
			DocumentCategorizerME myCategorizer = new DocumentCategorizerME(m);
			double[] outcomes = myCategorizer.categorize(inputText);

			/**
			 * Retrieve the best category based on the model's predictions.
			 */
			String category = myCategorizer.getBestCategory(outcomes);

			// Log the classification result
			LOGGER.info("Input classified as: " + category);

		} catch (IOException e) {
			/**
			 * Handle exceptions related to file input or model processing.
			 *
			 * @param e The exception thrown during file handling or model operations.
			 */
			LOGGER.log(Level.SEVERE, "Error processing the document categorization model or input text.", e);
		}

		// Log the end of the process
		LOGGER.info("Process completed.");
	}
}
