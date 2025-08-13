package currencyexchange.dao;

import currencyexchange.model.Currency;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyDaoTest {

    @Test
    void getCurrencies() {
        CurrencyDao dao = new CurrencyDao();

        List<Currency> currencies = dao.getCurrencies();
        for(Currency currency : currencies) {
            System.out.println(currency);
        }
    }
}