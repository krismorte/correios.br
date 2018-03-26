/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import br.com.SROCorreiosAPI.main.SROCorreiosAPI;
import com.krismorte.correios.br.exception.CorreiosException;
import com.krismorte.correios.br.frete.CalculoFreteCorreio;
import com.krismorte.correios.br.frete.entity.Encomenda;
import com.krismorte.correios.br.frete.entity.Frete;
import com.krismorte.correios.br.frete.enums.Servico;
import com.krismorte.correios.br.logradouro.BuscaEndereco;
import com.krismorte.correios.br.logradouro.entity.Endereco;

/**
 *
 * @author krisnamourtscf
 */
public class Teste {

    public static void testeCaelum() {
        try {
            Encomenda encomenda = new Encomenda().doCep("20021180")
                    .paraOCep("01205-000")
                    .comAltura("10")
                    .comComprimento("20")
                    .comLargura("20")
                    .comPeso(0.3)
                    .comAvisoDeRecebimento()
                    .semMaoPropria();

            Frete frete = CalculoFreteCorreio.calcularFrete(encomenda, Servico.SEDEX);
            System.out.println(String.format("Entregará por %s reais em até %s %s .",
                    frete.getValor(), frete.getPrazoEntrega(), frete.getPrazoEntrega() <= 1 ? "dia útil" : "dias úteis"));
        } catch (CorreiosException e) {
            System.out.println("Código do erro: " + e.getCodigo());
            System.out.println("Motivo        : " + e.getMensagem());
        }

    }

    public static void testeBuscaCep() {

        Endereco end = BuscaEndereco.getEnderecoPorCep("13084440");//buscarPorCEP("13084440");
        System.out.println("CEP " + end.getBairro() + " " + end.getUf());
        //CEP cep = buscaCEP.obtemPorNumeroCEP("13084440");
        /*List<CEP> ceps = buscaCEP.obtemPorEndereco("CAUCAIA-CE");
        for (CEP cep : ceps) {
            System.out.println("CEP " + cep.getBairro() + " " + cep.getUf());
        }*/
    }

    public static void rastreio() {
        SROCorreiosAPI correiosAPI = new SROCorreiosAPI();
        String searchObject = correiosAPI.SearchObject("DW620569438BR");
        System.out.println(searchObject);
        /*CorreiosAPI correios = new CorreiosAPI("DW620569438BR");
        List<HashMap<String, String>> statusList = correios.getStatusList();

        for (HashMap<String, String> status : statusList) {
            System.out.print("Data: " + status.get("date") + "\t");
            System.out.print("Local: " + status.get("location") + "\t");
            System.out.print("Situa‹o: " + status.get("situation") + "\t");
            if (status.get("description") != null) {
                System.out.print("Descri‹o: " + status.get("description"));
            }
            System.out.println();
        }*/
    }

    public static void main(String[] args) {
        
        System.setProperty("http.proxyHost", "172.31.3.254");
        System.setProperty("http.proxyPort", "3128");
        System.setProperty("https.proxyHost", "172.31.3.254");
        System.setProperty("https.proxyPort", "3128");
        Teste.testeBuscaCep();
        Teste.testeCaelum();
        rastreio();

    }

}
