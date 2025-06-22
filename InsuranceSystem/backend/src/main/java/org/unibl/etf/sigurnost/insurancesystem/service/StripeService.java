package org.unibl.etf.sigurnost.insurancesystem.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.unibl.etf.sigurnost.insurancesystem.model.Policy;
import jakarta.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
public class StripeService {

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public String createCheckoutSession(Policy policy, String successUrl, String cancelUrl, String customerEmail) throws StripeException {
        if (policy.getAmount() == null || policy.getAmount() <= 0 || policy.getName() == null) {
            throw new IllegalArgumentException("Invalid policy data for Stripe session.");
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(successUrl + "?payment=success")
                .setCancelUrl(cancelUrl)
                .setClientReferenceId(String.valueOf(policy.getId()))
                .setCustomerEmail(customerEmail)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount((long) (policy.getAmount() * 100))
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(policy.getName())
                                                                .build())
                                                .build())
                                .build())
                .build();

        Session session = Session.create(params);
        System.out.println("Created Stripe session with ID: " + session.getId());
        return session.getUrl();
    }
}
