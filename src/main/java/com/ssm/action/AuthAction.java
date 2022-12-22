package com.ssm.action;

import java.util.Random;

import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.ssm.domain.PaymentEvent;
import com.ssm.domain.PaymentState;
import com.ssm.services.PaymentServiceImpl;

@Component
public class AuthAction implements Action<PaymentState, PaymentEvent> {

	@Override
	public void execute(StateContext<PaymentState, PaymentEvent> context) {
		
		if(new Random().nextInt(10)<8) {
			System.out.println("Auth Approved");
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(PaymentEvent.APPROVED)
                    .setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER, context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER))
                    .build());
		}else {
			 System.out.println("Auth Declined! No Credit!!!!!!");
	            context.getStateMachine().sendEvent(MessageBuilder.withPayload(PaymentEvent.AUTH_DECLINED)
	                    .setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER, context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER))
	                    .build());
		}
	}

	
}
