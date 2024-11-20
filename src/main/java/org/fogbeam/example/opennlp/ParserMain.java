
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

	public static void main(String[] args) {
		// Uso de try-with-resources para manejar el InputStream
		try (InputStream modelIn = new FileInputStream("models/en-parser-chunking.bin")) {

			// Cargar el modelo del parser
			ParserModel model = new ParserModel(modelIn);

			// Crear el parser con el modelo
			Parser parser = ParserFactory.create(model);

			// Frase a analizar
			String sentence = "The quick brown fox jumps over the lazy dog .";

			// Parsear la frase
			Parse[] topParses = ParserTool.parseLine(sentence, parser, 1);

			// Obtener el primer resultado
			Parse parse = topParses[0];

			// Loggear el resultado
			LOGGER.info("Parse result: " + parse.toString());

			// Mostrar el árbol de análisis
			parse.showCodeTree();

		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error al procesar el archivo del modelo o analizar la frase.", e);
		}

		// Loggear el final del proceso
		LOGGER.info("Proceso completado.");
	}
}
