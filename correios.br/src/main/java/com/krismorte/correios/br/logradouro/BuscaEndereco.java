/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.krismorte.correios.br.logradouro;

import com.krismorte.correios.br.logradouro.entity.Endereco;
import com.krismorte.correios.br.ws.CorreiosService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.json.JsonObject;
import javax.json.JsonValue;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author krisnamourtscf
 */
public class BuscaEndereco {

    private static final Set<String> CAMPOS = new HashSet<String>(Arrays.asList(
            "cep",
            "logradouro",
            "complemento",
            "bairro",
            "localidade",
            "uf",
            "unidade",
            "ibge",
            "gia"
    ));

    private static final String EMPTY = "";
    private static final String NBSP = "\u00a0";
    private static final int NUM_INDEX = 3;
    private static final int LOGRADOURO_INDEX = 0;
    private static final int BAIRRO_INDEX = 1;
    private static final int LOCALIDADE_INDEX = 2;
    private static final int UF_INDEX = 2;
    private static final String HIFEN = "-";
    private static final String SLASH = "/";
    private static Random random;

    /**
     * Recupera objeto Endereco pelo CEP
     *
     * @param cep String no formato 00000000
     * @return instancia de br.com.viacep.Endereco
     */
    public static Endereco getEnderecoPorCep(String cep) {

        JsonObject jsonObject = CorreiosService.sendRequest(cep);
        Endereco endereco = null;

        JsonValue erro = jsonObject.get("erro");

        if (erro == null) {

            endereco = new Endereco()
                    .setCep(jsonObject.getString("cep"))
                    .setLogradouro(jsonObject.getString("logradouro"))
                    .setComplemento(jsonObject.getString("complemento"))
                    .setBairro(jsonObject.getString("bairro"))
                    .setLocalidade(jsonObject.getString("localidade"))
                    .setUf(jsonObject.getString("uf"))
                    .setUnidade(jsonObject.getString("unidade"))
                    .setIbge(jsonObject.getString("ibge"))
                    .setGia(jsonObject.getString("gia"));

        }

        return endereco;
    }

    /**
     * Recupera Map<String,String> pelo CEP
     *
     * @param cep String no formato 00000000
     * @return instancia de Map<String,String>
     *
    public static Map<String, String> getMapPorCep(String cep) {

        JsonObject jsonObject = getCepResponse(cep);

        JsonValue erro = jsonObject.get("erro");

        Map<String, String> mapa = null;
        if (erro == null) {
            mapa = new HashMap<String, String>();

            for (Iterator<Map.Entry<String, JsonValue>> it = jsonObject.entrySet().iterator(); it.hasNext();) {
                Map.Entry<String, JsonValue> entry = it.next();
                mapa.put(entry.getKey(), entry.getValue().toString());
            }
        }

        return mapa;
    }*/

    /*public static Endereco buscarPorCEP(String numeroCEP) {
        Elements rows = busca(numeroCEP, 3);
        for (Element row : rows) {
            Endereco cep = toEndereco(row);
            return cep;
        }
        return null;
    }*/

    private static Elements busca(String q, int maxTries) {
        int low = 1;
        int high = 1000;

        // Attempt to execute our main action, retrying up to 4 times
        // if an exception is thrown
        for (int n = 0; n <= maxTries; n++) {
            try {

                // The main action you want to execute goes here
                // If this does not come in the form of a return
                // statement (i.e., code continues below the loop)
                // then you must insert a break statement after the
                // action is complete.
                return CorreiosService.busca(q);

            } catch (Exception e) {

                // If we've exhausted our retries, throw the exception
                if (n == maxTries) {
                    throw e;
                }

                // Wait an indeterminate amount of time (range determined by n)
                try {
                    Thread.sleep(((int) Math.round(Math.pow(2, n)) * 1000)
                            + (random.nextInt(high - low) + low));
                } catch (InterruptedException ignored) {
                    // Ignoring interruptions in the Thread sleep so that
                    // retries continue
                }
            }
        }
        return null;
    }

   /* private static Endereco toEndereco(Element row) {
        Elements cols = row.getElementsByTag("td");
        Endereco cep = new Endereco(numero(cols), logradouro(cols), bairro(cols),
                localidade(cols), uf(cols));
        return cep;
    }*/

    private static String uf(Elements cols) {
        String cidadeEstado = cols.get(UF_INDEX).text().replace(NBSP, EMPTY);
        return cidadeEstado.split(SLASH)[1];
    }

    private static String localidade(Elements cols) {
        String cidadeEstado = cols.get(LOCALIDADE_INDEX).text().replace(NBSP, EMPTY);
        return cidadeEstado.split(SLASH)[0];
    }

    private static String bairro(Elements cols) {
        return cols.get(BAIRRO_INDEX).text().replace(NBSP, EMPTY);
    }

    private static String logradouro(Elements cols) {
        return cols.get(LOGRADOURO_INDEX).text().replace(NBSP, EMPTY);
    }

    private static String numero(Elements cols) {
        String num = cols.get(NUM_INDEX).text().replace(NBSP, EMPTY);
        return num.contains(HIFEN) ? num.replace(HIFEN, EMPTY) : num;
    }

}
