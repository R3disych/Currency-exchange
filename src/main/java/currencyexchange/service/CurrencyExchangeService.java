package currencyexchange.service;

import currencyexchange.dao.ExchangeRateDao;
import currencyexchange.dto.CurrencyExchangeDto;
import currencyexchange.model.Currency;
import currencyexchange.model.ExchangeRate;

import java.sql.SQLException;

public class CurrencyExchangeService {
    private static final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    public static CurrencyExchangeDto transferCurrency(Currency baseCurrency,
                                                       Currency targetCurrency,
                                                       double amount) throws SQLException {
        CurrencyExchangeDto currencyExchangeDto = new CurrencyExchangeDto();
        currencyExchangeDto.setBaseCurrency(baseCurrency);
        currencyExchangeDto.setTargetCurrency(targetCurrency);

        ExchangeRate exchangeRate;

        exchangeRate = exchangeRateDao.getExchangeRateByCodes(baseCurrency.getCurrencyCode(),
                targetCurrency.getCurrencyCode()); //лучше сделать через котракт(дао должен возвращать null, если не нашел)
        if (exchangeRate.getRate() != 0) {
            currencyExchangeDto.setRate(exchangeRate.getRate());
            currencyExchangeDto.setAmount(amount);
            currencyExchangeDto.setConvertedAmount(amount * Math.round(exchangeRate.getRate() * 100.0) / 100.0);
            return currencyExchangeDto;

        } else {
            exchangeRate = exchangeRateDao.getExchangeRateByCodes(targetCurrency.getCurrencyCode(),
                    baseCurrency.getCurrencyCode());

            if (exchangeRate.getRate() != 0) {
                double newRate = 1 / exchangeRate.getRate();
                newRate = Math.round(newRate * 100.0) / 100.0;
                currencyExchangeDto.setRate(newRate);
                currencyExchangeDto.setAmount(amount);
                currencyExchangeDto.setConvertedAmount(amount * newRate);
                return currencyExchangeDto;

            } else {
                ExchangeRate rateUSD1 = exchangeRateDao.getExchangeRateByCodes(targetCurrency.getCurrencyCode(), "USD");
                ExchangeRate rateUSD2 = exchangeRateDao.getExchangeRateByCodes(baseCurrency.getCurrencyCode(), "USD");

                double newRate = rateUSD2.getRate() / rateUSD1.getRate();
                newRate = Math.round(newRate * 100.0) / 100.0;
                currencyExchangeDto.setRate(newRate);
                currencyExchangeDto.setAmount(amount);
                currencyExchangeDto.setConvertedAmount(amount * newRate);
                return currencyExchangeDto;
            }
        }
    }
}
