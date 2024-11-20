
package org.fogbeam.example.opennlp.training;


import java.io.*;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.WordTagSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Entrena un modelo de etiquetado gramatical (POS) con datos de entrenamiento.
 * Este modelo puede ser utilizado para etiquetar texto con etiquetas gramaticales.
 */


import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class PartOfSpeechTaggerTrainer {

	private static final Logger logger = Logger.getLogger(PartOfSpeechTaggerTrainer.class.getName());

	public static void main(String[] args) {
		POSModel model = null;

		// Leer los datos de entrenamiento y entrenar el modelo
		try (InputStream dataIn = new FileInputStream("training_data/en-pos.train")) {
			ObjectStream<String> lineStream = null;
			ObjectStream<POSSample> sampleStream = null;

			try {
				lineStream = new PlainTextByLineStream(new InputStreamReader(dataIn, StandardCharsets.UTF_8));
				sampleStream = new WordTagSampleStream(lineStream);

				// Entrenar el modelo
				model = POSTaggerME.train("en", sampleStream, TrainingParameters.defaultParams(), null, null);
				logger.info("Modelo de etiquetado POS entrenado exitosamente.");
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error al leer o procesar los datos de entrenamiento.", e);
			} finally {
				// Cerrar manualmente los ObjectStreams
				if (sampleStream != null) {
					try {
						sampleStream.close();
					} catch (IOException e) {
						logger.log(Level.WARNING, "Error al cerrar sampleStream.", e);
					}
				}
				if (lineStream != null) {
					try {
						lineStream.close();
					} catch (IOException e) {
						logger.log(Level.WARNING, "Error al cerrar lineStream.", e);
					}
				}
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error al abrir el archivo de entrenamiento.", e);
		}

		// Guardar el modelo entrenado
		if (model != null) {
			String modelFile = "models/en-pos.model";
			try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelFile))) {
				model.serialize(modelOut);
				logger.info("Modelo de etiquetado POS guardado en: " + modelFile);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error al guardar el modelo entrenado.", e);
			}
		} else {
			logger.warning("El modelo no fue entrenado correctamente, no se guardará ningún archivo.");
		}

		logger.info("Proceso de entrenamiento completado.");
	}
}
