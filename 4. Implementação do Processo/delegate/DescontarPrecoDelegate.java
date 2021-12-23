package com.mycompany.myapp.delegate;

import com.mycompany.myapp.service.dto.TripPlanProcessDTO;
import com.mycompany.myapp.domain.TripPlan;
import com.mycompany.myapp.repository.TripPlanRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class DescontarPrecoDelegate implements JavaDelegate {

    @Autowired
    TripPlanRepository tripPlanRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        TripPlanProcessDTO tripPlanProcess = (TripPlanProcessDTO) delegateExecution.getVariable("processInstance");
	TripPlan tripPlan = tripPlanRepository.getOne(tripPlanProcess.getTripPlan().getId());
	String nomePlano = tripPlanProcess.getTripPlan().getNomePlano();
	Double precoTotal = tripPlanProcess.getTripPlan().getPrecoTotal();
	Double precoPromocional = 0.0;
	Double precoDescontado = 0.0;
	if(tripPlanProcess.getTripPlan().getTemPromocao()){
		precoPromocional = tripPlanProcess.getTripPlan().getPrecoPromocional();
		precoDescontado = precoPromocional - (precoPromocional*0.05);
	} else 
		precoDescontado = precoTotal - (precoTotal*0.05);
	delegateExecution.setVariable("precoDescontado", precoDescontado);
	tripPlan.setPrecoDescontado(precoDescontado);
	tripPlanRepository.save(tripPlan);

	System.out.println("PLANO: ###########################################");
        System.out.println("PLANO: ###########################################");
        System.out.println("PLANO: ###########################################");
	System.out.println("NOME PLANO:    " + nomePlano);
        System.out.println("PRECO TOTAL:    " + precoTotal);
	if(tripPlanProcess.getTripPlan().getTemPromocao())
		System.out.println("PRECO PROMOCAO:    " + precoPromocional);
	System.out.println("PRECO DESCONTADO:    " + precoDescontado);
        System.out.println("PLANO: ###########################################");
        System.out.println("PLANO: ###########################################");
        System.out.println("PLANO: ###########################################\n\n\n");
    }
}