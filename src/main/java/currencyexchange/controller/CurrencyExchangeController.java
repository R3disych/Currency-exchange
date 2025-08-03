package currencyexchange.controller;

import currencyexchange.dao.CurrencyDao;
import currencyexchange.dao.ExchangeRateDao;
import currencyexchange.dto.CurrencyExchangeDto;
import currencyexchange.dto.MyError;
import currencyexchange.model.Currency;
import currencyexchange.model.ExchangeRate;
import currencyexchange.service.CurrencyExchangeService;
import currencyexchange.util.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

public class CurrencyExchangeController extends HttpServlet {
    private final CurrencyDao currencyDao = new CurrencyDao();
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String baseCurrencyCode = request.getParameter("from");
        String targetCurrencyCode = request.getParameter("to");
        double amount = Double.parseDouble(request.getParameter("amount"));

        ExchangeRate exchangeRate;
        try {
            exchangeRate = exchangeRateDao.getExchangeRateByCodes(baseCurrencyCode, targetCurrencyCode);
        } catch (SQLException e) {
            try {
                exchangeRate = exchangeRateDao.getExchangeRateByCodes(targetCurrencyCode, baseCurrencyCode);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
        try {
            Currency baseCurrency = currencyDao.getCurrencyByCode(baseCurrencyCode);
            Currency targetCurrency = currencyDao.getCurrencyByCode(targetCurrencyCode);
            if (baseCurrency == null || targetCurrency == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                JsonUtil.sendAsJson(response, new MyError("Currencies not found"));
            } else {
                JsonUtil.sendAsJson(response, CurrencyExchangeService.transferCurrency(baseCurrency, targetCurrency,
                        amount));
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonUtil.sendAsJson(response, new MyError("Database error: " + e.getMessage()));
        }
    }
}
