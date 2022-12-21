package com.ssm.services;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

import com.ssm.domain.Payment;
import com.ssm.domain.PaymentEvent;
import com.ssm.domain.PaymentState;
import com.ssm.repo.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
	public static final String PAYMENT_ID_HEADER="payment_id";

	private final PaymentRepository paymentRepository;
	private final StateMachineFactory<PaymentState, PaymentEvent> stateMachineFactory;
	private final PaymentStateChange paymentStateChange;
	
	@Transactional
	@Override
	public Payment newPayment(Payment payment) {
			payment.setState(PaymentState.New);
		return paymentRepository.save(payment);
	}

	@Transactional
	@Override
	public StateMachine<PaymentState, PaymentEvent> preAuth(Long paymentId) {
		StateMachine<PaymentState, PaymentEvent> sm =build(paymentId);
		
		sendEvent(paymentId, sm, PaymentEvent.PRE_AUTHORIZE);
		return sm;
	}
	@Transactional
	@Override
	public StateMachine<PaymentState, PaymentEvent> authorizePayment(Long paymentId) {
		StateMachine<PaymentState, PaymentEvent> sm =build(paymentId);
		sendEvent(paymentId, sm, PaymentEvent.PRE_AUHTORIZE_APPROVED);
		return null;
	}
	
	@Transactional
	@Override
	public StateMachine<PaymentState, PaymentEvent> declineAuth(Long paymentId) {
		StateMachine<PaymentState, PaymentEvent> sm =build(paymentId);
		sendEvent(paymentId, sm, PaymentEvent.AUTH_DECLINED);
		return sm;
	}
	
	private StateMachine<PaymentState, PaymentEvent> build(Long paymentId) {
		Optional<Payment> payment = paymentRepository.findById(paymentId);

		StateMachine<PaymentState, PaymentEvent> sm = stateMachineFactory
				.getStateMachine(Long.toString(payment.get().getId()));
		sm.stop();
		sm.getStateMachineAccessor().doWithAllRegions(sma -> {
			sma.addStateMachineInterceptor(paymentStateChange);
			sma.resetStateMachine(new DefaultStateMachineContext<PaymentState, PaymentEvent>
			(payment.get().getState(),
					null, null, null));
		});
		sm.start();
		return sm;
	}
	
	private void sendEvent(Long paymentId, StateMachine<PaymentState, PaymentEvent> sm, PaymentEvent event) {
		org.springframework.messaging.Message<PaymentEvent> msg = MessageBuilder.withPayload(event)
				.setHeader(PAYMENT_ID_HEADER,paymentId)
				.build();
				
		sm.sendEvent(msg);
	}
}
