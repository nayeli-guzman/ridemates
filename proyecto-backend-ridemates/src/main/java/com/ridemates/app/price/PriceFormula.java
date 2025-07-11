package com.ridemates.app.price;

import java.math.BigDecimal;

public interface PriceFormula {

    BigDecimal calculatePrice(String origin, String destination);
}
