package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.Collections;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * @file NameFinderTrainer.java
 * @brief Clase para entrenar un modelo de reconocimiento de nombres (Named Entity Recognition - NER) utilizando Apache OpenNLP.
 *
 * Esta clase entrena un modelo para identificar entidades nombradas, como nombres de personas,
 * a partir de un conjunto de datos de entrenamiento, y guarda el modelo entrenado en un archivo.
 */
public class NameFinderTrainer {

	/**
	 * @brief Método principal para entrenar un modelo de reconocimiento de nombres.
	 *
	 * Este método realiza las siguientes tareas:
	 * - Lee un archivo de entrenamiento con datos etiquetados para reconocimiento de nombres.
	 * - Entrena un modelo NER utilizando OpenNLP.
	 * - Guarda el modelo entrenado en un archivo para uso posterior.
	 *
	 * @param args Argumentos de la línea de comandos (no utilizados en este caso).
	 * @throws Exception Si ocurre algún error durante la ejecución.
	 */
	public static void main(String[] args) throws Exception {
		// Configuración de codificación
		Charset charset = Charset.forName("UTF-8");

		// Flujo de datos de entrenamiento desde un archivo
		ObjectStream<String> lineStream = new PlainTextByLineStream(
				new FileInputStream("training_data/en-ner-person.train"), charset);

		// Crear un flujo de muestras de NameSample
		ObjectStream<NameSample> sampleStream = new NameSampleDataStream(lineStream);
		TokenNameFinderModel model;

		// Entrenamiento del modelo
		try {
			model = NameFinderME.train(
					"en", // Idioma
					"person", // Tipo de entidad (nombres de personas en este caso)
					sampleStream,
					TrainingParameters.defaultParams(),
					(byte[]) null,
					Collections.<String, Object>emptyMap()
			);
		} finally {
			sampleStream.close(); // Cerrar el flujo de muestras
		}

		BufferedOutputStream modelOut = null;

		// Guardar el modelo entrenado en un archivo
		try {
			String modelFile = "models/en-ner-person.model";
			modelOut = new BufferedOutputStream(new FileOutputStream(modelFile));
			model.serialize(modelOut); // Serializar el modelo en el archivo
		} finally {
			if (modelOut != null) {
				modelOut.close(); // Cerrar el flujo de salida
			}
		}

		// Mensaje de finalización
		System.out.println("done");
	}
}
