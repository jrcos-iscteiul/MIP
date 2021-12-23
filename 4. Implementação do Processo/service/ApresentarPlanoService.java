package com.mycompany.myapp.service;

import org.springframework.stereotype.Component;

@Component
public class ApresentarPlanoService {

    public void apresentarPlano(Boolean querCarro, String nomePlano, Double precoTotal, Double precoVoo, Double precoHotel, Double precoCarro) {
        System.out.println("PLANO: ###########################################");
        System.out.println("PLANO: ###########################################");
        System.out.println("PLANO: ###########################################");
	System.out.println("NOME PLANO:    " + nomePlano);
	if(querCarro)
        	System.out.println("PRECO CARRO:    " + precoCarro);
	System.out.println("PRECO HOTEL:    " + precoHotel);
	System.out.println("PRECO VOO:    " + precoVoo);
        System.out.println("\nPRECO TOTAL:    " + precoTotal);
        System.out.println("PLANO: ###########################################");
        System.out.println("PLANO: ###########################################");
        System.out.println("PLANO: ###########################################\n\n\n");
    }

}