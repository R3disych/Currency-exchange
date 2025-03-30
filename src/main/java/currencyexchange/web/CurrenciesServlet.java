package currencyexchange.web;

import currencyexchange.dao.CurrencyDao;
import currencyexchange.model.Currency;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "Show currencies", urlPatterns = {"/showCurrencies"})
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyDao currencyDao = new CurrencyDao();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Currency> list = currencyDao.getCurrencies();
        request.setAttribute("currencies", list);
        request.getRequestDispatcher("/currencies.jsp").forward(request, response);
    }
}
