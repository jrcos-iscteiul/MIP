package com.mycompany.myapp.service;

import org.springframework.stereotype.Component;

@Component
public class AviaoFaturaService {

    public void apresentarVoo(String nomeCompanhiaAerea, int numeroLugar, Double precoAviao) {
        System.out.println("AVIAO: ###########################################");
        System.out.println("AVIAO: ###########################################");
        System.out.println("AVIAO: ###########################################");
	System.out.println("NOME COMPANHIA AEREA:    " + nomeCompanhiaAerea);
	System.out.println("NUMERO LUGAR:    " + numeroLugar);
	System.out.println("PRECO AVIAO:    " + precoAviao);
        System.out.println("AVIAO: ###########################################");
        System.out.println("AVIAO: ###########################################");
        System.out.println("AVIAO: ###########################################\n\n\n");
    }

}