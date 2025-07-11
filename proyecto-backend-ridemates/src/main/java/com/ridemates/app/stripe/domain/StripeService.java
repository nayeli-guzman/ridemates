package com.ridemates.app.stripe.domain;

import com.ridemates.app.payment.domain.PaymentService;
import com.ridemates.app.payment.dto.PaymentRequestDto;
import com.ridemates.app.stripe.dto.StripeIntentRequest;
import com.ridemates.app.stripe.dto.StripeIntentResponse;
import com.ridemates.app.stripe.dto.StripeRequest;
import com.ridemates.app.stripe.dto.StripeResponse;
import com.ridemates.app.user.domain.User;
import com.ridemates.app.user.domain.UserService;
import com.ridemates.app.utils.AuthUtils;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.EphemeralKey;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentIntentCollection;
import com.stripe.param.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class StripeService {
    public static final String STRIPE_VERSION = "2024-06-20";

    @Value("${stripe.publishable_key}")
    private String publishableKey;

    @Value("${stripe.secret_key}")
    private String secretKey;

    @Autowired
    private UserService userService;
    @Autowired
    private PaymentService paymentService;

    private Customer loadCustomer(StripeRequest request) throws StripeException {
        User sender = userService.findMeUser();
        return sender.getStripeClientUserId() == null
                ? Customer.create(CustomerCreateParams.builder().setEmail(request.getEmail()).build())
                : Customer.retrieve(sender.getStripeClientUserId());
    }

    public StripeIntentResponse intentPayment(StripeIntentRequest intentRequest) throws StripeException {
        Stripe.apiKey = secretKey;
        PaymentIntentCreateParams paymentIntentParams = PaymentIntentCreateParams.builder()
                .setAmount(intentRequest.amountInCents())
                .setCurrency("PEN")
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentParams);
        return new StripeIntentResponse(paymentIntent.getClientSecret());
    }

    public Page<PaymentIntent> listPayments(int page, int size) throws StripeException {
        Stripe.apiKey = secretKey;

        PaymentIntentListParams listParams = PaymentIntentListParams.builder()
                .setLimit(3L)
                .build();
        PaymentIntentCollection paymentIntents = PaymentIntent.list(listParams);
        List<PaymentIntent> data = paymentIntents.getData();
        return new PageImpl<>(data, Pageable.ofSize(size).withPage(page), data.size());
    }

    public StripeResponse generatePayment(StripeRequest request) throws Exception {
        Stripe.apiKey = secretKey;
        Customer customer = loadCustomer(request);

        EphemeralKeyCreateParams ephemeralKeyParams = EphemeralKeyCreateParams.builder()
                .setStripeVersion(STRIPE_VERSION)
                .setCustomer(customer.getId())
                .build();

        EphemeralKey ephemeralKey = EphemeralKey.create(ephemeralKeyParams);
        PaymentIntentCreateParams paymentIntentParams = PaymentIntentCreateParams.builder()
                .setAmount(request.amountInCents())
                .setCurrency("PEN")
                .setCustomer(customer.getId())
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentParams);

        // Pushing stripe payment as generic payment
        PaymentRequestDto dto = new PaymentRequestDto();
        dto.setAmount(request.getAmount());
        dto.setMethod(paymentIntent.getPaymentMethod());
        paymentService.createPayment(request.getTravelId(), dto, paymentIntent.getId());

        return new StripeResponse(paymentIntent.getClientSecret(), ephemeralKey.getSecret(), customer.getId(), publishableKey,
                paymentIntent.getPaymentMethod());
    }
}
