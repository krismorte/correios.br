package br.com.SROCorreiosAPI.main;

import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import br.com.SROCorreiosAPI.net.DoPost;
import br.com.SROCorreiosAPI.util.PostBuilder;

/**
 * @author = Gabriell Huver
 * @category = API
 * @since = 2/22/2018
 * @version = 1 Api em SOAP desenvolvido com base no manual do Web Service do
 *          Correios
 * @see <a> https://github.com/gabriellhuver/SROCorreiosAPI <a>
 *      {@link} https://github.com/gabriellhuver/SROCorreiosAPI
 */

public class SROCorreiosAPI {


	/**
	 * @param objecto String
	 * 			Codigo
	 *            de rastreamento
	 * @return Xml contendo os dados em forma de String
	 */
	public String SearchObject(String objecto) {
		String response = null;
		try {
			URLConnection conn = DoPost.createURLConnection();
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			String xml = PostBuilder.buildStringPostSingleObject(objecto);
			response = DoPost.doPost(conn, wr, xml);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * 
	 * @param objecto
	 * 			
	 * @return org.w3c.dom.Document
	 */
	public Document searchObjectTomlDocument(String objecto) {
		String response = null;
		Document document = null;
		try {
			URLConnection conn = DoPost.createURLConnection();
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			String xml = PostBuilder.buildStringPostSingleObject(objecto);
			response = DoPost.doPost(conn, wr, xml);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
		DocumentBuilder builder;  
		try {  
		    builder = factory.newDocumentBuilder();  
		    document = builder.parse(new InputSource(new StringReader(response)));  
		} catch (Exception e) {  
		    e.printStackTrace();  
		}
		return document; 
	}
	
	
	public static void main(String[] args) {
		System.out.println("Created By Gabriell H.");
		System.out.println("22/2/2018");
		System.out.println("Donate bitcoins: 1BseU69CoQnSMDWc24Qnzq31s4qMh348v6");
	}

}
