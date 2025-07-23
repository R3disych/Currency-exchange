package currencyexchange.web;

import currencyexchange.controller.CurrencyController;
import currencyexchange.controller.ExchangeRateController;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(value = "/api/*")
public class ApplicationServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getRequestURI().substring(req.getContextPath().length());
        //System.out.println(path);

        if (path.contains("/api/currencies")) {
            new CurrencyController().service(req, resp);
        } else if (path.contains("/api/exchangeRates")) {
            new ExchangeRateController().service(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
