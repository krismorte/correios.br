package com.krismorte.correios.br.frete.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.krismorte.correios.br.frete.enums.Servico;

public class ServicoAdapter extends XmlAdapter<String, Servico>{

	@Override
	public Servico unmarshal(String v) throws Exception {
		return Servico.getServico(v);
	}

	@Override
	public String marshal(Servico v) throws Exception {
		return v.toString();
	}
}