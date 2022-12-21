package com.ssm.services;

import org.springframework.statemachine.StateMachine;
import com.ssm.domain.Payment;
import com.ssm.domain.PaymentEvent;
import com.ssm.domain.PaymentState;

public interface PaymentService {
		Payment newPayment(Payment payment);
		
		StateMachine<PaymentState,PaymentEvent> preAuth(Long paymentId);
		StateMachine<PaymentState,PaymentEvent> authorizePayment(Long paymentId);
		StateMachine<PaymentState,PaymentEvent> declineAuth(Long paymentId);
			
}
