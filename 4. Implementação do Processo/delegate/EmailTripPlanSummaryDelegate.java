package com.mycompany.myapp.delegate;

import com.mycompany.myapp.service.MailService;
import com.mycompany.myapp.service.dto.TripPlanDTO;
import com.mycompany.myapp.service.dto.CarroDTO;
import com.mycompany.myapp.service.dto.AviaoDTO;
import com.mycompany.myapp.service.dto.HotelDTO;
import com.mycompany.myapp.domain.Carro;
import com.mycompany.myapp.domain.Hotel;
import com.mycompany.myapp.domain.Aviao;
import com.mycompany.myapp.repository.CarroRepository;
import com.mycompany.myapp.repository.HotelRepository;
import com.mycompany.myapp.repository.AviaoRepository;
import com.mycompany.myapp.service.dto.TripPlanProcessDTO;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.Locale;

@Component
public class EmailTripPlanSummaryDelegate implements JavaDelegate {

    @Autowired
    CarroRepository carroRepository;

    @Autowired
    HotelRepository hotelRepository;

    @Autowired
    AviaoRepository aviaoRepository;

    @Autowired
    MailService mailService;

    @Autowired
    SpringTemplateEngine templateEngine;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        TripPlanProcessDTO tripPlanProcess = (TripPlanProcessDTO) delegateExecution.getVariable("processInstance");
        TripPlanDTO tripPlan = tripPlanProcess.getTripPlan();
	
	Aviao aviao = aviaoRepository.getOne(tripPlanProcess.getTripPlan().getAviao().getId());
	Hotel hotel = hotelRepository.getOne(tripPlanProcess.getTripPlan().getHotel().getId());

        String to = tripPlan.getEmailCliente();
	String subject = "Plano " + hotel.getNomeHotel();

	Boolean temPromocao = tripPlan.getTemPromocao();
	Boolean querCartao = tripPlan.getQuerCartao();
	Double precoTotal = tripPlan.getPrecoTotal();

	String valorDesconto = "#NA";
	String valorPromocao = "#NA";
	
	if(temPromocao)
		valorPromocao = Double.toString((precoTotal - tripPlan.getPrecoPromocional()));

	if(temPromocao && querCartao)
		valorDesconto = Double.toString((tripPlan.getPrecoPromocional() - tripPlan.getPrecoDescontado()));
	if(querCartao)
		valorDesconto = Double.toString((precoTotal - tripPlan.getPrecoDescontado()));

	Double precoFinal = precoTotal;
	
	if(temPromocao)
		precoFinal = tripPlan.getPrecoPromocional();
	if(querCartao)
		precoFinal = tripPlan.getPrecoDescontado();

	String ccBoolean = "NO";
	Boolean card = tripPlan.getQuerCartao();
	if(card)
		ccBoolean = "YES";
	String cardOwner = "#NA";
	String cardNumber = "#NA";
	String cardCVV = "#NA#";
	if(card){
		cardOwner = tripPlan.getTitularCartao();
		cardNumber = Integer.toString(tripPlan.getNumeroCartao());
		cardCVV = Integer.toString(tripPlan.getNumeroCVV());
	}

	String nomeHotel = hotel.getNomeHotel();
	Integer numeroQuarto = tripPlan.getNumeroQuarto();
	Double precoHotel = hotel.getPrecoHotel();

	String nomeCompanhiaAerea = aviao.getNomeCompanhiaAerea();
	Double precoAviao = aviao.getPrecoAviao();
	Integer numeroLugarAviao = tripPlan.getNumeroLugarAviao();

	String precoCarro = "#NA";
	String carroMatricula = "#NA";
	String carroMarca = "#NA";

	Context context = new Context(Locale.getDefault());
	if(tripPlan.getQuerCarro()){
		Carro carro = carroRepository.getOne(tripPlanProcess.getTripPlan().getCarro().getId());
		Boolean car = tripPlan.getQuerCarro();

		if(car){
			precoCarro = Double.toString(carro.getPrecoCarro());
			carroMatricula = carro.getMatriculaCarro();
			carroMarca = carro.getMarcaCarro();
		}
	}
	context.setVariable("tripPlan", tripPlan);
	context.setVariable("ccBoolean", ccBoolean);
	context.setVariable("cardOwner", cardOwner);
	context.setVariable("cardNumber", cardNumber);
	context.setVariable("cardCVV", cardCVV);
	context.setVariable("precoCarro", precoCarro);
	context.setVariable("carroMatricula", carroMatricula);
	context.setVariable("carroMarca", carroMarca);
	context.setVariable("precoFinal", precoFinal);
	context.setVariable("precoTotal", precoTotal);
	context.setVariable("valorDesconto", valorDesconto);
	context.setVariable("valorPromocao", valorPromocao);
	context.setVariable("nomeHotel", nomeHotel);
	context.setVariable("numeroQuarto", numeroQuarto);
	context.setVariable("precoHotel", precoHotel);
	context.setVariable("nomeCompanhiaAerea", nomeCompanhiaAerea);
	context.setVariable("precoAviao", precoAviao);
	context.setVariable("numeroLugarAviao", numeroLugarAviao);

	String content = templateEngine.process("tripPlanProcess/fatura", context);
    	mailService.sendEmail(to, subject, content, false, true);
    }
}