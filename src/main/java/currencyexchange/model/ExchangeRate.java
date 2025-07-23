package currencyexchange.model;

public class ExchangeRate {
    private Currency baseCurrencyCode;
    private Currency targetCurrencyCode;
    private double rate;

    public ExchangeRate() {

    }

    public ExchangeRate(Currency baseCurrencyCode, Currency targetCurrencyCode, double rate) {
        this.baseCurrencyCode = baseCurrencyCode;
        this.targetCurrencyCode = targetCurrencyCode;
        this.rate = rate;
    }

    public Currency getBaseCurrencyCode() {
        return baseCurrencyCode;
    }

    public void setBaseCurrencyCode(Currency baseCurrencyCode) {
        this.baseCurrencyCode = baseCurrencyCode;
    }

    public Currency getTargetCurrencyCode() {
        return targetCurrencyCode;
    }

    public void setTargetCurrencyCode(Currency targetCurrencyCode) {
        this.targetCurrencyCode = targetCurrencyCode;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
