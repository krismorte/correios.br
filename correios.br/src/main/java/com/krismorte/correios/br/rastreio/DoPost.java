package br.com.SROCorreiosAPI.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class DoPost {

	public static URLConnection createURLConnection() throws IOException {
		URL url = new URL("http://webservice.correios.com.br/service/rastro");
		URLConnection conn = url.openConnection();
		conn.setDoOutput(true);
		configConnection(conn);
		return conn;
	}

	public static void configConnection(URLConnection conn) {
		conn.setRequestProperty("SOAPAction", "http://webservice.correios.com.br/service/rastro/Rastro.wsdl");
		conn.setRequestProperty("Type", "Request-Response");
		conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
		conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
		conn.setRequestProperty("User-Agent", "Jakarta Commons-HttpClient/3.1");
	}
	
	public static String doPost(URLConnection conn, OutputStreamWriter wr, String xml) throws IOException {
		String msgRetorno = "";
		wr.write(xml);
		wr.flush();
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		while (rd.ready()) {
			msgRetorno += rd.readLine();
		}
		wr.close();
		rd.close();
		conn.getInputStream().close();
		return msgRetorno;
	}
}
