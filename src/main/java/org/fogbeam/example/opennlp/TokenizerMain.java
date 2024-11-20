
package org.fogbeam.example.opennlp;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.util.*;

public class TokenizerMain {

	public static void main(String[] args) {
		// Directorio de entrada, modelo de tokenización y archivo de salida
		String inputDirectory = "src/main/java/org/fogbeam/example/opennlp/input";
		String modelFilePath = "models/en-token.bin";
		String outputFilePath = "output/tokens_output.txt";

		try {
			// Obtener el contenido combinado de los archivos de entrada
			List<String> inputText = gatherInputText(inputDirectory);

			// Tokenizar el contenido de los archivos
			List<String> tokens = processTokens(inputText, modelFilePath);

			// Guardar los tokens en el archivo de salida
			saveTokensToFile(tokens, outputFilePath);

			System.out.println("Procesamiento completado. Tokens guardados en: " + outputFilePath);
		} catch (IOException e) {
			System.err.println("Ocurrió un error durante el procesamiento: " + e.getMessage());
		}
	}

	/**
	 * Método para recopilar el contenido de todos los archivos de texto en el directorio especificado.
	 *
	 * @param directory Ruta del directorio de entrada.
	 * @return Lista de cadenas con el contenido de los archivos.
	 * @throws IOException Si ocurre un error al leer los archivos.
	 */
	private static List<String> gatherInputText(String directory) throws IOException {
		List<String> fileContents = new ArrayList<>();

		// Usar try-with-resources para cerrar automáticamente el stream
		try (var stream = Files.list(Paths.get(directory))) {
			stream.filter(Files::isRegularFile)
					.filter(path -> path.toString().endsWith(".txt"))
					.forEach(filePath -> {
						try {
							String content = Files.readString(filePath);
							fileContents.add(content);
						} catch (IOException e) {
							System.err.println("Error al leer el archivo: " + filePath.getFileName());
						}
					});
		}

		return fileContents;
	}

	/**
	 * Método para procesar el contenido de los archivos y generar tokens utilizando el modelo.
	 *
	 * @param textData Lista de cadenas con el contenido de los archivos.
	 * @param modelPath Ruta al archivo del modelo de tokenización.
	 * @return Lista de tokens generados.
	 * @throws IOException Si ocurre un error al cargar el modelo.
	 */
	private static List<String> processTokens(List<String> textData, String modelPath) throws IOException {
		List<String> tokens = new ArrayList<>();
		try (InputStream modelStream = new FileInputStream(modelPath)) {
			TokenizerModel model = new TokenizerModel(modelStream);
			Tokenizer tokenizer = new TokenizerME(model);

			for (String text : textData) {
				String[] tokenizedText = tokenizer.tokenize(text);
				tokens.addAll(Arrays.asList(tokenizedText));
			}
		}
		return tokens;
	}

	/**
	 * Método para guardar los tokens en un archivo de salida.
	 *
	 * @param tokens Lista de tokens a guardar.
	 * @param outputFile Ruta del archivo de salida.
	 * @throws IOException Si ocurre un error al escribir en el archivo.
	 */
	private static void saveTokensToFile(List<String> tokens, String outputFile) throws IOException {
		File outputDir = new File(outputFile).getParentFile();
		if (!outputDir.exists() && !outputDir.mkdirs()) {
			throw new IOException("No se pudo crear el directorio de salida.");
		}
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
			for (String token : tokens) {
				writer.write(token);
				writer.newLine(); // Agregar un salto de línea después de cada token
			}
		}
	}
}