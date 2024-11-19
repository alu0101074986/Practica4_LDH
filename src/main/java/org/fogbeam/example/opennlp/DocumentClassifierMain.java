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

		InputStream is = null;
		try {
			is = new FileInputStream("models/en-doccat.model");

			DoccatModel m = new DoccatModel(is);

			String inputText = "What happens if we have declining bottom-line revenue?";
			DocumentCategorizerME myCategorizer = new DocumentCategorizerME(m);
			double[] outcomes = myCategorizer.categorize(inputText);
			String category = myCategorizer.getBestCategory(outcomes);

			System.out.println("Input classified as: " + category);

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error processing the document categorization model or input text.", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Error closing the model input stream.", e);
				}
			}
		}

		System.out.println("done");
	}
}

