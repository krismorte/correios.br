package br.com.SROCorreiosAPI.util;

public class PostBuilder {
	
	
	public static String buildStringPostSingleObject(String objeto) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("<soapenv:Envelope\r\n");
		stringBuilder.append("xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"\r\n");
		stringBuilder.append("xmlns:res=\"http://resource.webservice.correios.com.br/\">\r\n");
		stringBuilder.append(" <soapenv:Header/>\r\n");
		stringBuilder.append(" <soapenv:Body>\r\n");
		stringBuilder.append(" <res:buscaEventos>\r\n");
		stringBuilder.append(" <usuario>ECT</usuario>\r\n");
		stringBuilder.append(" <senha>SRO</senha>\r\n");
		stringBuilder.append(" <tipo>L</tipo>\r\n");
		stringBuilder.append(" <resultado>T</resultado>\r\n");
		stringBuilder.append(" <lingua>101</lingua>\r\n");
		stringBuilder.append(" <objetos>" + objeto + "</objetos>\r\n");
		stringBuilder.append(" </res:buscaEventos>\r\n");
		stringBuilder.append(" </soapenv:Body>\r\n");
		stringBuilder.append("</soapenv:Envelope>");
		String xml = stringBuilder.toString();
		return xml;
	}

}
