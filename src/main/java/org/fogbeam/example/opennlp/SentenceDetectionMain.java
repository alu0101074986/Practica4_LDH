
package org.fogbeam.example.opennlp;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @file SentenceDetectionMain.java
 * @brief Detecta oraciones en un texto utilizando OpenNLP.
 * @details Este programa carga un modelo de detección de oraciones y procesa un archivo de entrada
 *          para dividir el texto en oraciones.
 *
 * @author Marcelo Daniel Choque
 * @date 2024-11-19
 */
public class SentenceDetectionMain {

	private static final Logger logger = Logger.getLogger(SentenceDetectionMain.class.getName());

	public static void main(String[] args) {
		// Uso de try-with-resources para asegurar el cierre de los recursos
		try (InputStream modelIn = new FileInputStream("models/en-sent.model");
			 InputStream demoDataIn = new FileInputStream("demo_data/en-sent1.demo")) {

			// Cargar el modelo de detección de oraciones
			SentenceModel model = new SentenceModel(modelIn);
			SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);

			// Leer los datos de entrada
			String demoData = convertStreamToString(demoDataIn);

			// Detectar oraciones
			String[] sentences = sentenceDetector.sentDetect(demoData);

			// Registrar las oraciones detectadas
			for (String sentence : sentences) {
				logger.info("Oración detectada: " + sentence);
			}

		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error al cargar el modelo o procesar los datos de entrada.", e);
		}

		logger.info("Procesamiento completado.");
	}

	/**
	 * Convierte un InputStream a una cadena.
	 *
	 * @param is InputStream de datos.
	 * @return Contenido del InputStream como String.
	 */
	static String convertStreamToString(InputStream is) {
		try (Scanner scanner = new Scanner(is).useDelimiter("\\A")) {
			return scanner.hasNext() ? scanner.next() : "";
		}
	}
}
