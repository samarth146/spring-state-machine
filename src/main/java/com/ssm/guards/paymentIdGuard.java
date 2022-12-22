package com.ssm.guards;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.guard.Guard;

import com.ssm.domain.PaymentEvent;
import com.ssm.domain.PaymentState;
import com.ssm.services.PaymentServiceImpl;

public class paymentIdGuard implements Guard<PaymentState, PaymentEvent>{

	@Override
	public boolean evaluate(StateContext<PaymentState, PaymentEvent> context) {
		return context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER) != null;
	}

}
