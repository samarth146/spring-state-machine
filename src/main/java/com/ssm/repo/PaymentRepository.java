package com.ssm.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ssm.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
