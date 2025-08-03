package currencyexchange.controller;

import currencyexchange.dao.CurrencyDao;
import currencyexchange.dao.ExchangeRateDao;
import currencyexchange.dto.MyError;
import currencyexchange.model.ExchangeRate;
import currencyexchange.util.JsonUtil;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.postgresql.gss.GSSOutputStream;

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
                JsonUtil.sendAsJson(response, new MyError("Database error: " + e.getMessage()));
            }
        } else {
            String baseCode = pathInfo.substring(15, 18).toUpperCase();
            String targetCode = pathInfo.substring(18, 21).toUpperCase();

            try {
                ExchangeRate exchangeRate = exchangeRateDao
                        .getExchangeRateByCodes(baseCode, targetCode);
                if (exchangeRate == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    JsonUtil.sendAsJson(response, new MyError("Exchange rate not found"));
                } else {
                    JsonUtil.sendAsJson(response, exchangeRate);
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                JsonUtil.sendAsJson(response, new MyError("Database error: " + e.getMessage()));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        String baseCurrencyCode = request.getParameter("baseCurrencyCode");
        String targetCurrencyCode = request.getParameter("targetCurrencyCode");
        double rate = Double.parseDouble(request.getParameter("rate"));

        try {
            ExchangeRate exchangeRate = new ExchangeRate();
            exchangeRate.setBaseCurrencyCode(currencyDao.getCurrencyByCode(baseCurrencyCode));
            exchangeRate.setTargetCurrencyCode(currencyDao.getCurrencyByCode(targetCurrencyCode));
            exchangeRate.setRate(rate);
            JsonUtil.sendAsJson(response, exchangeRateDao.createExchangeRate(exchangeRate));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonUtil.sendAsJson(response, new MyError("Database error: " + e.getMessage()));
            e.printStackTrace();
        }
    }

    protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        double rate = Double.parseDouble(request.getParameter("rate"));

        String baseCode = pathInfo.substring(15, 18).toUpperCase();
        String targetCode = pathInfo.substring(18, 21).toUpperCase();

        try {
            JsonUtil.sendAsJson(response, exchangeRateDao.updateExchangeRate(baseCode, targetCode, rate));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonUtil.sendAsJson(response, new MyError("Database error: " + e.getMessage()));
        }
    }
}
