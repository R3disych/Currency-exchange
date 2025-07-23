package currencyexchange.dao;

import currencyexchange.model.ExchangeRate;
import currencyexchange.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDao {
    private static final String GET_ALL_SQL_REQUEST = "select * from exchange_rates";
    private static final String GET_BY_CODES_SQL_REQUEST = "select * from exchange_rates " +
            "where base_currency_id = ?, target_currency_id = ?";
    private static final String POST_SQL_REQUEST = "insert into exchange_rates (base_currency_id, " +
            "target_currency_id, rate) values (?, ?, ?)";
    private CurrencyDao currencyDao = new CurrencyDao();

    public List<ExchangeRate> getAllExchangeRates() throws SQLException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_ALL_SQL_REQUEST);
             ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {
                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setBaseCurrencyCode(currencyDao
                        .getCurrencyById(resultSet.getInt("base_currency_id")));
                exchangeRate.setTargetCurrencyCode(currencyDao
                        .getCurrencyById(resultSet.getInt("target_currency_id")));
                exchangeRate.setRate(resultSet.getInt("rate"));
                exchangeRates.add(exchangeRate);
            }
        }
        return exchangeRates;
    }

    public ExchangeRate createExchangeRate(ExchangeRate exchangeRate) throws SQLException {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(POST_SQL_REQUEST)) {

            pstmt.setInt(1, exchangeRate.getBaseCurrencyCode().getId());
            pstmt.setInt(2, exchangeRate.getTargetCurrencyCode().getId());
            pstmt.setDouble(3, exchangeRate.getRate());

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return exchangeRate;
                }
            }
        }
        throw new SQLException("Creating currancy failed, no ID obtained");
    }
}
