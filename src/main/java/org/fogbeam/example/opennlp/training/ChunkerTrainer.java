package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import opennlp.tools.chunker.ChunkSample;
import opennlp.tools.chunker.ChunkSampleStream;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.chunker.DefaultChunkerContextGenerator;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * @file ChunkerTrainer.java
 * @brief Clase para entrenar un modelo de análisis sintáctico (Chunking) utilizando Apache OpenNLP.
 *
 * Esta clase entrena un modelo de análisis sintáctico basado en un conjunto de datos de entrenamiento
 * y genera un archivo del modelo que puede ser utilizado para realizar tareas de chunking en texto.
 */
public class ChunkerTrainer {

	/**
	 * @brief Metodo principal para entrenar un modelo de chunking.
	 *
	 * Este metodo realiza las siguientes tareas:
	 * - Lee datos de entrenamiento desde un archivo.
	 * - Entrena un modelo de chunking utilizando OpenNLP.
	 * - Guarda el modelo entrenado en un archivo de salida.
	 *
	 * @param args Argumentos de la línea de comandos (no utilizados).
	 * @throws Exception Si ocurre algún error durante el proceso.
	 */
	public static void main(String[] args) throws Exception {
		// Configuración de codificación
		Charset charset = Charset.forName("UTF-8");

		// Flujo de datos de entrenamiento desde un archivo
		ObjectStream<String> lineStream = new PlainTextByLineStream(
				new FileInputStream("training_data/conll2000-chunker.train"), charset);

		// Crear el flujo de muestras de chunking
		ObjectStream<ChunkSample> sampleStream = new ChunkSampleStream(lineStream);
		ChunkerModel model;

		// Entrenamiento del modelo de chunking
		try {
			model = ChunkerME.train(
					"en",
					sampleStream,
					new DefaultChunkerContextGenerator(),
					TrainingParameters.defaultParams()
			);
		} finally {
			sampleStream.close(); // Cierre del flujo de datos
		}

		OutputStream modelOut = null;
		String modelFile = "models/en-chunker.model";

		// Guardar el modelo entrenado en un archivo
		try {
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
