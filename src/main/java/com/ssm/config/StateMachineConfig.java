package com.ssm.config;

import java.util.EnumSet;
import java.util.Random;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.guard.Guard;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import com.ssm.domain.PaymentEvent;
import com.ssm.domain.PaymentState;
import com.ssm.services.PaymentServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableStateMachineFactory
@Configuration
@RequiredArgsConstructor
public class StateMachineConfig extends StateMachineConfigurerAdapter<PaymentState, PaymentEvent> {

	 private final Action<PaymentState, PaymentEvent> preAuthAction;
	    private final Action<PaymentState, PaymentEvent> authAction;
	    private final Guard<PaymentState, PaymentEvent> paymentIdGuard;
	    private final Action<PaymentState, PaymentEvent> preAuthApprovedAction;
	    private final Action<PaymentState, PaymentEvent> preAuthDeclinedAction;
	    private final Action<PaymentState, PaymentEvent> authApprovedAction;
	    private final Action<PaymentState, PaymentEvent> authDeclinedAction;
	
	@Override
	public void configure(StateMachineStateConfigurer<PaymentState, PaymentEvent> states) throws Exception {
		states.withStates().initial(PaymentState.New).states(EnumSet.allOf(PaymentState.class)).end(PaymentState.AUTH)
				.end(PaymentState.PRE_AUTH).end(PaymentState.PRE_AUTH_ERROR);
	}

	@Override
	public void configure(StateMachineConfigurationConfigurer<PaymentState, PaymentEvent> config) throws Exception {
		StateMachineListenerAdapter<PaymentState, PaymentEvent> adapter = new StateMachineListenerAdapter() {

			@Override
			public void stateChanged(State from, State to) {
				log.info(String.format("State changed from " + from, to));
			}
		};
		config.withConfiguration().listener(adapter);
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<PaymentState, PaymentEvent> transistion) throws Exception {
		transistion.withExternal().source(PaymentState.New).target(PaymentState.New).event(PaymentEvent.PRE_AUTHORIZE)
         .action(preAuthAction).guard(paymentIdGuard)
     .and()
     .withExternal().source(PaymentState.New).target(PaymentState.PRE_AUTH).event(PaymentEvent.PRE_AUHTORIZE_APPROVED)
         .action(preAuthApprovedAction)
     .and()
     .withExternal().source(PaymentState.New).target(PaymentState.PRE_AUTH_ERROR).event(PaymentEvent.PRE_AUTH_DECLINE)
         .action(preAuthDeclinedAction)
     .and()
     .withExternal().source(PaymentState.PRE_AUTH).target(PaymentState.PRE_AUTH).event(PaymentEvent.AUTHORIZE)
         .action(authAction)
     .and()
     .withExternal().source(PaymentState.PRE_AUTH).target(PaymentState.AUTH).event(PaymentEvent.APPROVED)
         .action(authApprovedAction)
     .and()
     .withExternal().source(PaymentState.PRE_AUTH).target(PaymentState.AUTH_ERROR).event(PaymentEvent.AUTH_DECLINED)
         .action(authDeclinedAction);
	}


}
