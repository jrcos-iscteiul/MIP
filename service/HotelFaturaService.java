package com.mycompany.myapp.service;

import org.springframework.stereotype.Component;

@Component
public class HotelFaturaService {

    public void apresentarHotel(String nomeHotel, int numeroQuartoHotel, Double precoHotel) {
        System.out.println("HOTEL: ###########################################");
        System.out.println("HOTEL: ###########################################");
        System.out.println("HOTEL: ###########################################");
	System.out.println("NOME HOTEL:    " + nomeHotel);
	System.out.println("NUMERO QUARTO HOTEL:    " + numeroQuartoHotel);
	System.out.println("PRECO HOTEL:    " + precoHotel);
        System.out.println("HOTEL: ###########################################");
        System.out.println("HOTEL: ###########################################");
        System.out.println("HOTEL: ###########################################\n\n\n");
    }

}