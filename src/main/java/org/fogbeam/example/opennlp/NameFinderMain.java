
package org.fogbeam.example.opennlp;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NameFinderMain {

	private static final Logger LOGGER = Logger.getLogger(NameFinderMain.class.getName());

	public static void main(String[] args) {

		InputStream modelIn = null;

		try {
			modelIn = new FileInputStream("models/en-ner-person.model");

			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);

			NameFinderME nameFinder = new NameFinderME(model);

			String[] tokens = {
					"Phillip",
					"Rhodes",
					"is",
					"presenting",
					"at",
					"some",
					"meeting",
					"."
			};

			Span[] names = nameFinder.find(tokens);

			for (Span ns : names) {
				System.out.println("ns: " + ns.toString());
				// Aquí puedes hacer algo con el nombre encontrado
			}

			nameFinder.clearAdaptiveData();

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error processing the Name Finder model or input tokens.", e);
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Error closing the model input stream.", e);
				}
			}
		}

		System.out.println("done");
	}
}
