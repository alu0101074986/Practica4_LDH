
package org.fogbeam.example.opennlp.training;


import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase para entrenar un modelo de clasificación de documentos (Document Categorization).
 */
public class DocumentClassifierTrainer {

	private static final Logger logger = Logger.getLogger(DocumentClassifierTrainer.class.getName());

	public static void main(String[] args) {
		DoccatModel model = null;

		// Leer datos de entrenamiento y entrenar el modelo
		try (InputStream dataIn = new FileInputStream("training_data/en-doccat.train")) {
			ObjectStream<String> lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
			ObjectStream<DocumentSample> sampleStream = new DocumentSampleStream(lineStream);

			model = DocumentCategorizerME.train("en", sampleStream);
			logger.info("Modelo de clasificación de documentos entrenado exitosamente.");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error al leer o procesar los datos de entrenamiento.", e);
		}

		// Guardar el modelo entrenado
		String modelFile = "models/en-doccat.model";
		try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream(modelFile))) {
			if (model != null) {
				model.serialize(modelOut);
				logger.info("Modelo de clasificación guardado en: " + modelFile);
			} else {
				logger.warning("El modelo no fue entrenado correctamente. No se guardará nada.");
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error al guardar el modelo entrenado.", e);
		}

		logger.info("Proceso de entrenamiento completado.");
	}
}
