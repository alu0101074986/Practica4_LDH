package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.sentdetect.SentenceSample;
import opennlp.tools.sentdetect.SentenceSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * @file SentenceDetectionTrainer.java
 * @brief Entrena un modelo de detección de oraciones usando Apache OpenNLP.
 */
public class SentenceDetectionTrainer {

	/**
	 * @brief Ejecuta el entrenamiento del modelo de detección de oraciones.
	 *
	 * Este metodo carga datos de entrenamiento, entrena un modelo y lo guarda en un archivo.
	 *
	 * @param args Argumentos de la línea de comandos (no usados).
	 * @throws Exception Si ocurre un error durante el entrenamiento o manejo de archivos.
	 */
	public static void main(String[] args) throws Exception {
		Charset charset = Charset.forName("UTF-8");

		// Cargar datos de entrenamiento desde un archivo
		ObjectStream<String> lineStream = new PlainTextByLineStream(
				new FileInputStream("training_data/en-sent.train"), charset);

		// Convertir los datos de entrenamiento en muestras de oraciones
		ObjectStream<SentenceSample> sampleStream = new SentenceSampleStream(lineStream);

		SentenceModel model;

		try {
			// Entrenar el modelo de detección de oraciones
			model = SentenceDetectorME.train("en", sampleStream, true, null, TrainingParameters.defaultParams());
		} finally {
			sampleStream.close();
		}

		// Guardar el modelo entrenado en un archivo
		try (OutputStream modelOut = new BufferedOutputStream(new FileOutputStream("models/en-sent.model"))) {
			model.serialize(modelOut);
		}

		// Imprimir mensaje de finalización
		System.out.println("done");
	}
}