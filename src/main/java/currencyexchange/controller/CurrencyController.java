package currencyexchange.controller;

import currencyexchange.dao.CurrencyDao;
import currencyexchange.model.Currency;
import currencyexchange.util.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;


public class CurrencyController extends HttpServlet {
    private final CurrencyDao currencyDao = new CurrencyDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo.equals("/currencies")) {
            try {
                JsonUtil.sendAsJson(response, currencyDao.getAllCurrencies());
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                JsonUtil.sendAsJson(response, Map.of("error", "Database error"));
            }
        } else {
            String code = pathInfo.substring(12);
            try {
                Currency currency = currencyDao.getCurrencyByCode(code);
                if (currency == null) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    JsonUtil.sendAsJson(response, new Error("Currency not found"));
                } else {
                    JsonUtil.sendAsJson(response, currency);
                }
            } catch (SQLException e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                JsonUtil.sendAsJson(response, new Error("Database error"));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String fullName = request.getParameter("name");
        String code = request.getParameter("code");
        String sign = request.getParameter("sign");

        try {
            Currency currency = new Currency();
            currency.setFullName(fullName);
            currency.setCurrencyCode(code);
            currency.setSign(sign);
            JsonUtil.sendAsJson(response, currencyDao.createCurrency(currency));
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            JsonUtil.sendAsJson(response, new Error("Database error: " + e.getMessage()));
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            JsonUtil.sendAsJson(response, new Error("Invalid request body"));
        }
    }
}
