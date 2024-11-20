package org.fogbeam.example.opennlp.training;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import opennlp.tools.tokenize.TokenSample;
import opennlp.tools.tokenize.TokenSampleStream;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.TrainingParameters;

/**
 * @file TokenizerTrainer.java
 * @brief Clase para entrenar un modelo de tokenización utilizando Apache OpenNLP.
 *
 * Esta clase entrena un modelo de tokenización basado en un conjunto de datos de entrenamiento
 * y genera un archivo del modelo que puede ser usado posteriormente para procesar texto.
 */
public class TokenizerTrainer
{
	/**
	 * @brief Metodo principal para entrenar un modelo de tokenización.
	 *
	 * Este metodo realiza las siguientes tareas:
	 * - Lee datos de entrenamiento desde un archivo.
	 * - Entrena un modelo de tokenización utilizando OpenNLP.
	 * - Guarda el modelo entrenado en un archivo de salida.
	 *
	 * @param args Argumentos de la línea de comandos (no utilizados).
	 * @throws Exception Si ocurre algún error durante el proceso.
	 */
	public static void main(String[] args) throws Exception
	{
		// Configuración de codificación
		Charset charset = Charset.forName("UTF-8");

		// Creación del flujo de datos de entrenamiento
		ObjectStream<String> lineStream = new PlainTextByLineStream(
				new FileInputStream("training_data/en-token.train"), charset);

		ObjectStream<TokenSample> sampleStream = new TokenSampleStream(lineStream);

		TokenizerModel model;

		// Entrenamiento del modelo de tokenización
		try
		{
			model = TokenizerME.train("en", sampleStream, true, TrainingParameters.defaultParams());
		}
		finally
		{
			sampleStream.close(); // Cierre del flujo de datos
		}

		OutputStream modelOut = null;

		// Guardar el modelo entrenado en un archivo
		try
		{
			modelOut = new BufferedOutputStream(new FileOutputStream("models/en-token.model"));
			model.serialize(modelOut); // Serializar el modelo en el archivo
		}
		finally
		{
			if (modelOut != null)
			{
				modelOut.close(); // Cerrar el flujo de salida
			}
		}

		// Mensaje de finalización
		System.out.println("done");
	}
}
