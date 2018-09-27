package org.danielmkraus.delivery.integration;

import org.danielmkraus.delivery.DeliveryApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

@ContextConfiguration(classes = DeliveryApplication.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT) 
@Transactional
public class SpringIntegrationTest {

	


}