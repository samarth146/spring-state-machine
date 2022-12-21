package com.ssm.test;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;

import com.ssm.domain.PaymentEvent;
import com.ssm.domain.PaymentState;

@SpringBootTest
public class StateMachineConfigTest {
	
	@Autowired
	StateMachineFactory<PaymentState, PaymentEvent> factory;
	
	@Test
	void testNewState() {
		StateMachine<PaymentState, PaymentEvent> sm =  factory.getStateMachine(UUID.randomUUID());
		sm.start();
		System.out.println("value of state is "+sm.getState().toString());
		
		sm.sendEvent(PaymentEvent.PRE_AUTHORIZE);
		System.out.println(sm.getState().toString());
	}
}
