package com.ssm.services;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;

import com.ssm.domain.Payment;
import com.ssm.domain.PaymentEvent;
import com.ssm.domain.PaymentState;
import com.ssm.repo.PaymentRepository;


@SpringBootTest
class PaymentServiceImplTest {

	@Autowired
	PaymentService paymentService;

	@Autowired
	PaymentRepository paymentRepository;

	Payment payment;

	@BeforeEach
	void setUp() {
		payment = Payment.builder().amount(new BigDecimal("133.99")).build();
	}

	@Transactional
	@Test
	void preAuth() {
		Payment savedpayment = paymentService.newPayment(payment);
		System.out.println("Shoud be New");
		System.out.println(savedpayment.getState());
		StateMachine<PaymentState,PaymentEvent> sm = paymentService.preAuth(savedpayment.getId());
		Payment preAuthedPayment = paymentRepository.getOne(savedpayment.getId());
		
		paymentService.preAuth(savedpayment.getId());
		System.out.println("Should be preAuth ");
		System.out.println(sm.getState().getId());
		System.out.println(preAuthedPayment);
	}
	
	@Transactional
	@Test
	void testAuth() {
		System.out.println("Inside testAuth method"
				+ "");
		Payment savedpayment = paymentService.newPayment(payment);
		StateMachine<PaymentState,PaymentEvent> preAuthsm = paymentService.preAuth(savedpayment.getId());
		
		
		if(preAuthsm.getState().getId()==PaymentState.PRE_AUTH) {
			System.out.println("Payment has been pre Authorized");
			
			StateMachine<PaymentState, PaymentEvent> authorize = paymentService.authorizePayment(savedpayment.getId());
			System.out.println("Result of auth is "+authorize.getState().getId());
		}else {
			System.out.println("Pre authorization has been failed");
		}
	}

}
