package com.ssm.action;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

import com.ssm.domain.PaymentEvent;
import com.ssm.domain.PaymentState;


@Component
public class PreAuthApprovedAction implements Action<PaymentState, PaymentEvent> {
    @Override
    public void execute(StateContext<PaymentState, PaymentEvent> context) {
        System.out.println("Sending Notification of PreAuth Approved");
    }
}
