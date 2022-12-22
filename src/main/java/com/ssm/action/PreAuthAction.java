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
public class PreAuthAction implements Action<PaymentState, PaymentEvent>{

    @Override
    public void execute(StateContext<PaymentState, PaymentEvent> context) {
        System.out.println("PreAuth was called!!!");

        if (new Random().nextInt(10) < 8) {
            System.out.println("Pre Auth Approved");
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(PaymentEvent.PRE_AUHTORIZE_APPROVED)
                    .setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER, context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER))
                    .build());

        } else {
            System.out.println("Per Auth Declined! No Credit!!!!!!");
            context.getStateMachine().sendEvent(MessageBuilder.withPayload(PaymentEvent.PRE_AUTH_DECLINE)
                    .setHeader(PaymentServiceImpl.PAYMENT_ID_HEADER, context.getMessageHeader(PaymentServiceImpl.PAYMENT_ID_HEADER))
                    .build());
        }
    }
}
