package currencyexchange.controller;

import currencyexchange.dao.CurrencyDao;
import currencyexchange.dao.ExchangeRateDao;
import currencyexchange.model.ExchangeRate;
import currencyexchange.util.JsonUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

public class ExchangeRateController extends HttpServlet {
    private final ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
    private final CurrencyDao currencyDao = new CurrencyDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo.equals("/exchangeRates")) {
            try {
                JsonUtil.sendAsJson(response, exchangeRateDao.getAllExchangeRates());
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                JsonUtil.sendAsJson(response, new Error("Database error"));
            }
        } else {

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //String pathInfo = request.getPathInfo();
        //System.out.println(pathInfo);
        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        double rate = Double.parseDouble(request.getParameter("rate"));
        System.out.println(rate);

        try {
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setBaseCurrencyCode(currencyDao.getCurrencyByCode(baseCurrencyCode));
            exchangeRate.setTargetCurrencyCode(currencyDao.getCurrencyByCode(targetCurrencyCode));
            exchangeRate.setRate(rate);
            JsonUtil.sendAsJson(response, exchangeRateDao.createExchangeRate(exchangeRate));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonUtil.sendAsJson(response, new Error("Database error: " + e.getMessage()));
        }
    }
}
