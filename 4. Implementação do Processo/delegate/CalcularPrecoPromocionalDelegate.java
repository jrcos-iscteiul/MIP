package com.mycompany.myapp.delegate;

import com.mycompany.myapp.service.dto.TripPlanProcessDTO;
import com.mycompany.myapp.domain.TripPlan;
import com.mycompany.myapp.repository.TripPlanRepository;
import com.mycompany.myapp.service.dto.PromocaoDTO;
import com.mycompany.myapp.domain.Promocao;
import com.mycompany.myapp.repository.PromocaoRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class CalcularPrecoPromocionalDelegate implements JavaDelegate {

    @Autowired
    TripPlanRepository tripPlanRepository;

    @Autowired
    PromocaoRepository promocaoRepository;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {

        TripPlanProcessDTO tripPlanProcess = (TripPlanProcessDTO) delegateExecution.getVariable("processInstance");
	TripPlan tripPlan = tripPlanRepository.getOne(tripPlanProcess.getTripPlan().getId());
	Promocao promocao = promocaoRepository.getOne(tripPlanProcess.getTripPlan().getPromocao().getId());

	Double valorPromocao = (promocao.getValorPromocao());
	Double precoTotal = tripPlanProcess.getTripPlan().getPrecoTotal();
	Double precoPromocional = precoTotal - (precoTotal*valorPromocao);

	delegateExecution.setVariable("precoPromocional", precoPromocional);
	tripPlan.setPrecoPromocional(precoPromocional);
	tripPlanRepository.save(tripPlan);
    }
}