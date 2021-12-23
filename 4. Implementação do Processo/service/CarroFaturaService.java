package com.mycompany.myapp.service;

import org.springframework.stereotype.Component;

@Component
public class CarroFaturaService {

    public void apresentarCarro(Boolean querCarro, String marcaCarro, String matriculaCarro, Double precoCarro) {
        System.out.println("CARRO: ###########################################");
        System.out.println("CARRO: ###########################################");
        System.out.println("CARRO: ###########################################");
	System.out.println("QUER CARRO:    " + querCarro);
	System.out.println("\nMARCA CARRO:    " + marcaCarro);
	System.out.println("MATRICULA CARRO:    " + matriculaCarro);
	System.out.println("PRECO CARRO:    " + precoCarro);
        System.out.println("CARRO: ###########################################");
        System.out.println("CARRO: ###########################################");
        System.out.println("CARRO: ###########################################\n\n\n");
    }

}