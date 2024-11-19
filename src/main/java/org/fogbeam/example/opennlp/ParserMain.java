
package org.fogbeam.example.opennlp;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;

import java.util.logging.Level;
import java.util.logging.Logger;


public class ParserMain {

	private static final Logger LOGGER = Logger.getLogger(ParserMain.class.getName());

	public static void main(String[] args) throws Exception {
		InputStream modelIn = new FileInputStream("models/en-parser-chunking.bin");

		try {
			ParserModel model = new ParserModel(modelIn);

			Parser parser = ParserFactory.create(model);

			String sentence = "The quick brown fox jumps over the lazy dog .";

			Parse topParses[] = ParserTool.parseLine(sentence, parser, 1);

			Parse parse = topParses[0];

			System.out.println(parse.toString());

			parse.showCodeTree();

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error al procesar el archivo del modelo o analizar la frase.", e);
		} finally {
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Error al cerrar el archivo del modelo.", e);
				}
			}
		}

		System.out.println("done");
	}
}
