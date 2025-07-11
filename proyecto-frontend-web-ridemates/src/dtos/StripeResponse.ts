export interface StripeResponse {
    paymentIntent: string;
    ephemeralKey: string;
    customer: string;
    publishableKey: string;
    paymentMethod: string;
}
