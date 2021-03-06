package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Hashtable;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.text.Font;

public class ResourceLoader {
		
	private static final Hashtable<String, File> TEMP_FILES = new Hashtable<>();
	
	
	public static String getCSSPath(String filename) throws IOException {
		URL url = ResourceLoader.class.getClassLoader().getResource("css/" + filename);
		if(url == null)
			throw new FileNotFoundException("The file \"" + filename + "\" cannot be found in the css folder");
		return url.toExternalForm();
	}
	
	public static File getYAMLFile(String filename) throws IOException {
		URL url = ResourceLoader.class.getClassLoader().getResource("yaml/" + filename);
		if(url == null)
			throw new FileNotFoundException("The file \"" + filename + "\" cannot be found in the yaml folder");
		try {
			return new File(url.toURI());
		} catch (URISyntaxException e) {
			throw new FileNotFoundException("The file \"" + filename + "\" cannot be found in the yaml folder");
		}
	}
	
	public static Image getImage(String iconName) throws IOException{
		InputStream stream = ResourceLoader.class.getClassLoader().getResourceAsStream("images/" + iconName);
		if(stream == null)
			throw new FileNotFoundException("The image \"" + iconName + "\" cannot be found in images");
		return new Image(stream);
 	}
	
	public static Node loadFXML(String filename) throws IOException {
		return loadFXMLLoader(filename).load();
	}
		
	public static FXMLLoader loadFXMLLoader(String filename) {
		return new FXMLLoader(ResourceLoader.class.getClassLoader().getResource("fxml/" + filename));
	}
	
	public static Font loadFont(String fileName, int size) {
		InputStream stream = ResourceLoader.class.getClassLoader().getResourceAsStream("fonts/" + fileName);
		return Font.loadFont(stream, size);
	}
	
	/**
	 * 
	 * Returns the Temporary file with the name "fileName"
	 * <p>
	 * Note that the file with the name "fileName " <br>
	 * must be created under the addTempFile() method first
	 * 
	 * @param fileName
	 * @return Temporary File
	 */
	public static File getTempFile(String fileName) {
		return TEMP_FILES.get(fileName);
	}
	
	/**
	 * 
	 * Creates a Temporary File under the name "fileName" that
	 * deletes itself after program execution
	 * <p>
	 * If file with "fileName" already exists, then the file will be replaced
	 * with a new File
	 * 
	 * 
	 * @param fileName
	 * @return Temporary File
	 */
	public static File addTempFile(String fileName) {
		String[] parts = getPrefixSuffix(fileName);
		
		if(TEMP_FILES.containsKey(fileName)) {
			File file = TEMP_FILES.get(fileName);
			file.delete();
		}
		
		File tempFile = null;
		try {
			tempFile = File.createTempFile(parts[0], parts[1]);
			tempFile.deleteOnExit();
			TEMP_FILES.put(fileName, tempFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tempFile;
	}
	
	/**
	 * Removes Temporary File with the name "fileName"
	 * <p>
	 * Note that the file with the name "fileName " <br>
	 * must be created under the addTempFile() method first
	 * @param fileName
	 */
	public static void removeTempFile(String fileName) {
		File file = TEMP_FILES.get(fileName);
		file.delete();
		TEMP_FILES.remove(fileName);
	}
		
	private static String[] getPrefixSuffix(String fileName) {
		String[] parts = fileName.split(".");
		if(parts.length > 1) {
			String prefix = parts[0];
			for(int i = 0; i < parts.length - 1; i++)
				prefix += "." + parts[i];
			String suffix = "." + parts[parts.length - 1];
			parts = new String[]{prefix, suffix};
		}else {
			parts = new String[] {fileName, ""};
		}
		return parts;
	}
		
}
