package com.krismorte.correios.br.ws;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Properties;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import javax.json.Json;
import javax.json.JsonObject;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;

public class CorreiosService {

    public static String sendRequest(Properties parametros) {
        String urlString = "http://ws.correios.com.br/calculador/CalcPrecoPrazo.aspx";

        @SuppressWarnings("rawtypes")
        Iterator i = parametros.keySet().iterator();
        int counter = 0;

        while (i.hasNext()) {
            String name = (String) i.next();
            String value = parametros.getProperty(name);
            urlString += (++counter == 1 ? "?" : "&") + name + "=" + value;
        }

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Request-Method", "GET");
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.connect();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer newData = new StringBuffer();
            String s = "";
            while (null != ((s = br.readLine()))) {
                newData.append(s);
            }
            br.close();
            return newData.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JsonObject sendRequest(String cep) {

        JsonObject responseJO = null;

        try {
            if (!validaCep(cep)) {
                throw new RuntimeException("Formato de CEP inválido");
            }
            HttpHost proxy = new HttpHost("172.31.3.254", 3128, "http");
            skipSSLCert();
            DefaultHttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            HttpGet httpGet = new HttpGet("https://viacep.com.br/ws/" + cep + "/json");
            HttpResponse response = httpclient.execute(httpGet);

            HttpEntity entity = response.getEntity();

            responseJO = Json.createReader(entity.getContent()).readObject();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return responseJO;
    }

    public static boolean validaCep(String cep) {
        if (!cep.matches("\\d{8}")) {
            return false;
        }

        return true;
    }

    public static Elements busca(String q) {
        String BUSCA_CEP_SERVICE_BASE_URL = "http://www.buscacep.correios.com.br/sistemas/buscacep/resultadoBuscaCepEndereco.cfm";
        try {

            Document doc = Jsoup.connect(BUSCA_CEP_SERVICE_BASE_URL)
                    .header("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
                    .data("relaxation", q)
                    .data("tipoCEP", "ALL")
                    .data("semelhante", "N")
                    .post();

            /*if (doc.title().equalsIgnoreCase("Error Occurred While Processing Request")) {
                throw new CorreiosException(-1, "Erro");
            }*/
            Elements rows = doc.select("table.tmptabela tr:not(:first-child)");
            for (Element row : rows) {
                System.out.println("r: " + row.toString());
            }
            return rows;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void skipSSLCert() throws Exception {
        /*
            *  fix for
            *    Exception in thread "main" javax.net.ssl.SSLHandshakeException:
            *       sun.security.validator.ValidatorException:
            *           PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException:
            *               unable to find valid certification path to requested target
         */
        TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

            }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        /*
     * end of the fix
         */
    }
}
