package com.mycompany.myapp.delegate;

import com.mycompany.myapp.service.dto.TripPlanProcessDTO;
import com.mycompany.myapp.service.dto.CarroDTO;
import com.mycompany.myapp.service.dto.AviaoDTO;
import com.mycompany.myapp.service.dto.HotelDTO;
import com.mycompany.myapp.domain.TripPlan;
import com.mycompany.myapp.domain.Carro;
import com.mycompany.myapp.domain.Hotel;
import com.mycompany.myapp.domain.Aviao;
import com.mycompany.myapp.repository.TripPlanRepository;
import com.mycompany.myapp.repository.CarroRepository;
import com.mycompany.myapp.repository.HotelRepository;
import com.mycompany.myapp.repository.AviaoRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.mycompany.myapp.service.CarroFaturaService;
import com.mycompany.myapp.service.HotelFaturaService;
import com.mycompany.myapp.service.AviaoFaturaService;
import com.mycompany.myapp.service.ApresentarPlanoService;

@Component
public class ApresentarPlanoDelegate implements JavaDelegate {

    @Autowired
    TripPlanRepository tripPlanRepository;

    @Autowired
    CarroRepository carroRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    AviaoRepository aviaoRepository;

    @Autowired
    ApresentarPlanoService apresentarPlanoService;

    @Autowired
    AviaoFaturaService aviaoFaturaService;

    @Autowired
    CarroFaturaService carroFaturaService;

    @Autowired
    HotelFaturaService hotelFaturaService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        TripPlanProcessDTO tripPlanProcess = (TripPlanProcessDTO) delegateExecution.getVariable("processInstance");
	TripPlan tripPlan = tripPlanRepository.getOne(tripPlanProcess.getTripPlan().getId());
	Aviao aviao = aviaoRepository.getOne(tripPlanProcess.getTripPlan().getAviao().getId());
	Hotel hotel = hotelRepository.getOne(tripPlanProcess.getTripPlan().getHotel().getId());	

	String marcaCarro = " ";
	String matriculaCarro = " ";

	String nomeCompanhiaAerea = aviao.getNomeCompanhiaAerea();
	int numeroLugar = tripPlanProcess.getTripPlan().getNumeroLugarAviao();

	String nomeHotel = hotel.getNomeHotel();
	int numeroQuarto = tripPlanProcess.getTripPlan().getNumeroQuarto();

	String nomePlano = tripPlanProcess.getTripPlan().getNomePlano();
	Boolean querCarro = tripPlanProcess.getTripPlan().getQuerCarro();
	Double precoHotel = hotel.getPrecoHotel();
	Double precoAviao = aviao.getPrecoAviao();
	Double precoTotal;
	Double precoCarro = 0.0;
	if(querCarro){
		Carro carro = carroRepository.getOne(tripPlanProcess.getTripPlan().getCarro().getId());
		marcaCarro = carro.getMarcaCarro();
		matriculaCarro = carro.getMatriculaCarro();
		precoCarro = carro.getPrecoCarro();
		precoTotal = precoCarro + precoHotel + precoAviao;
	} else {
		precoTotal = precoHotel + precoAviao;
	}
	delegateExecution.setVariable("precoTotal", precoTotal);
	tripPlan.setPrecoTotal(precoTotal);
	tripPlanRepository.save(tripPlan);

	apresentarPlanoService.apresentarPlano(querCarro, nomePlano, precoTotal, precoAviao, precoHotel, precoCarro);
	carroFaturaService.apresentarCarro(querCarro, marcaCarro, matriculaCarro, precoCarro);
	aviaoFaturaService.apresentarVoo(nomeCompanhiaAerea, numeroLugar, precoAviao);
	hotelFaturaService.apresentarHotel(nomeHotel, numeroQuarto, precoHotel);
    }
}