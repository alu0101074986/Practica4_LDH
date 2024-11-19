
package org.fogbeam.example.opennlp.training;


import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
public class PartOfSpeechTaggerTrainer {

	private static final Logger logger = Logger.getLogger(PartOfSpeechTaggerTrainer.class.getName());

	public static void main(String[] args) {
		POSModel model = null;
		InputStream dataIn = null;

		// Leer los datos de entrenamiento y entrenar el modelo
		try {
			dataIn = new FileInputStream("training_data/en-pos.train");
			ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
			ObjectStream<POSSample> sampleStream = new WordTagSampleStream(lineStream);

			model = POSTaggerME.train("en", sampleStream, TrainingParameters.defaultParams(), null, null);
			logger.info("Modelo de etiquetado POS entrenado exitosamente.");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error al leer o procesar los datos de entrenamiento.", e);
		} finally {
			if (dataIn != null) {
				try {
					dataIn.close();
				} catch (IOException e) {
					logger.log(Level.WARNING, "Error al cerrar el flujo de entrada después del entrenamiento.", e);
				}
			}
		}

		// Guardar el modelo entrenado
		OutputStream modelOut = null;
		String modelFile = "models/en-pos.model";
		try {
			modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
			model.serialize(modelOut);
			logger.info("Modelo de etiquetado POS guardado en: " + modelFile);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error al guardar el modelo entrenado.", e);
		} finally {
			if (modelOut != null) {
				try {
					modelOut.close();
				} catch (IOException e) {
					logger.log(Level.WARNING, "Error al cerrar el flujo de salida al guardar el modelo.", e);
				}
			}
		}

		logger.info("Proceso de entrenamiento completado.");
	}
}
