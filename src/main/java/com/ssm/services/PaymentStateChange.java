package com.ssm.services;

import java.util.Optional;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;
import org.springframework.stereotype.Component;

import com.ssm.domain.Payment;
import com.ssm.domain.PaymentEvent;
import com.ssm.domain.PaymentState;
import com.ssm.repo.PaymentRepository;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class PaymentStateChange extends StateMachineInterceptorAdapter<PaymentState, PaymentEvent>{
	public final PaymentRepository paymentrepo;
	
@Override
	public void preStateChange(State<PaymentState, PaymentEvent> state, Message<PaymentEvent> message,
			Transition<PaymentState, PaymentEvent> transition, StateMachine<PaymentState, PaymentEvent> stateMachine,
			StateMachine<PaymentState, PaymentEvent> rootStateMachine) {
	
	Optional.ofNullable(message)
	.ifPresent(msg->{
		Optional.ofNullable(Long.class.cast(message.getHeaders().getOrDefault(PaymentServiceImpl.PAYMENT_ID_HEADER, -1L)))
		.ifPresent(paymentId->{
			Payment payment = paymentrepo.getOne(paymentId);
			payment.setState(state.getId());
			paymentrepo.save(payment);
		});
	
	});
	

}



}
