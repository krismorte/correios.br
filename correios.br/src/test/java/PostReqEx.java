/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class PostReqEx {

    public void sendReq(String url, String objeto) {
        //HttpClient httpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair("acao", "track"));
        params.add(new BasicNameValuePair("objetos", objeto));
        params.add(new BasicNameValuePair("btnPesq", "Buscar"));

        CloseableHttpResponse response = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = response.getEntity();
                String resp = EntityUtils.toString(httpEntity);
                EntityUtils.consume(httpEntity);
                System.out.println("" + resp);
            } else {
                //...postMethod.getStatusLine();
            }
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
