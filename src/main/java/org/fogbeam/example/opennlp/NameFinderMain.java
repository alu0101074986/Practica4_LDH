package org.fogbeam.example.opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @file NameFinderMain.java
 * @brief Encuentra nombres propios en un conjunto de tokens usando un modelo preentrenado de OpenNLP.
 *
 * Esta clase utiliza un modelo preentrenado de OpenNLP para detectar entidades nombradas (como personas)
 * en una secuencia de tokens.
 *
 * @author Marcelo Daniel Choque
 * @date 2024-11-19
 */
public class NameFinderMain {

	/**
	 * Logger para manejar mensajes de información, advertencias y errores.
	 */
	private static final Logger LOGGER = Logger.getLogger(NameFinderMain.class.getName());

	/**
	 * @brief Método principal que ejecuta la detección de nombres propios.
	 *
	 * Carga un modelo preentrenado para la detección de nombres, procesa un conjunto de tokens
	 * y muestra los nombres encontrados en la salida estándar.
	 *
	 * @param args Argumentos de línea de comandos (no utilizados en esta implementación).
	 */
	public static void main(String[] args) {

		InputStream modelIn = null;

		try {
			// Cargar el modelo de detección de nombres
			modelIn = new FileInputStream("models/en-ner-person.model");
			TokenNameFinderModel model = new TokenNameFinderModel(modelIn);

			// Crear la instancia de NameFinderME con el modelo cargado
			NameFinderME nameFinder = new NameFinderME(model);

			// Tokens de entrada
			String[] tokens = {
					"Phillip",
					"Rhodes",
					"is",
					"presenting",
					"at",
					"some",
					"meeting",
					"."
			};

			// Encontrar nombres en los tokens
			Span[] names = nameFinder.find(tokens);

			// Mostrar los resultados
			for (Span ns : names) {
				System.out.println("Nombre encontrado: " + ns.toString());
				// Aquí puedes realizar acciones adicionales con los nombres encontrados
			}

			// Limpiar los datos adaptativos del modelo
			nameFinder.clearAdaptiveData();

		} catch (IOException e) {
			// Manejar errores durante el procesamiento
			LOGGER.log(Level.SEVERE, "Error procesando el modelo Name Finder o los tokens de entrada.", e);
		} finally {
			// Asegurar el cierre del InputStream
			if (modelIn != null) {
				try {
					modelIn.close();
				} catch (IOException e) {
					LOGGER.log(Level.WARNING, "Error al cerrar el flujo de entrada del modelo.", e);
				}
			}
		}

		// Log de finalización del proceso
		System.out.println("Proceso completado.");
	}
}
