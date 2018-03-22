package br.com.SROCorreiosAPI.util;

import java.io.IOException;
import java.util.UUID;

public class XmlBuilder {
	/**
	 * 
	 * @param xml <a> em formato String <a>
	 * @param path <a> local on salvar o xml <a>
	 * @throws IOException
	 * 
	 */
	@SuppressWarnings("unused")
	private static void createXmlFile(String xml, String path) throws IOException {
		java.io.FileWriter fw = new java.io.FileWriter(
				path + UUID.randomUUID().toString().toCharArray()[1] + ".xml");
		fw.write(xml);
		fw.close();
	}
}
