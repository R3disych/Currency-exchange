package currencyexchange.dao;

import currencyexchange.model.Currency;
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
    private static final String GET_BY_CODES_SQL_REQUEST = "select rate from exchange_rates where base_currency_id = ? and target_currency_id = ?";
    private static final String POST_SQL_REQUEST = "insert into exchange_rates (base_currency_id, target_currency_id, rate) values (?, ?, ?)";
    private static final String PATCH_SQL_REQUEST = "update exchange_rates set rate = ? where base_currency_id = ? and target_currency_id = ? returning *";
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
                exchangeRate.setRate(resultSet.getDouble("rate"));
                exchangeRates.add(exchangeRate);
            }
        }
        return exchangeRates;
    }

    public ExchangeRate getExchangeRateByCodes(String base, String target) throws SQLException {
        ExchangeRate exchangeRate = new ExchangeRate(); //лучше сделать join || !!!сделать отдельный сервис аля сборщик бизнес модели
        //лучше собирать объекты в сервисах, а дао сделать проще, который не будет ходить в таблички
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_CODES_SQL_REQUEST)) {

            Currency baseCurrency = currencyDao.getCurrencyByCode(base);
            Currency targetCurrency = currencyDao.getCurrencyByCode(target);

            pstmt.setInt(1, baseCurrency.getId());
            pstmt.setInt(2, targetCurrency.getId());

            ResultSet resultSet = pstmt.executeQuery();
            while (resultSet.next()) {
                exchangeRate.setBaseCurrencyCode(baseCurrency);
                exchangeRate.setTargetCurrencyCode(targetCurrency);
                exchangeRate.setRate(resultSet.getDouble("rate"));
            }
        }
        return exchangeRate;
    }

    public ExchangeRate createExchangeRate(ExchangeRate exchangeRate) throws SQLException {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(POST_SQL_REQUEST)) {

            pstmt.setInt(1, exchangeRate.getBaseCurrencyCode().getId());
            pstmt.setInt(2, exchangeRate.getTargetCurrencyCode().getId());
            pstmt.setDouble(3, exchangeRate.getRate());

            pstmt.executeUpdate();
            return exchangeRate;
        }
    }

    public ExchangeRate updateExchangeRate(String base, String target, double rate) throws SQLException {
        ExchangeRate exchangeRate = new ExchangeRate();
        Currency baseCurrency = currencyDao.getCurrencyByCode(base);
        Currency targetCurrency = currencyDao.getCurrencyByCode(target);

        exchangeRate.setBaseCurrencyCode(baseCurrency);
        exchangeRate.setTargetCurrencyCode(targetCurrency);
        exchangeRate.setRate(rate);


        try (Connection connection = DBUtil.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(PATCH_SQL_REQUEST)) {

            pstmt.setDouble(1, rate);
            pstmt.setInt(2, baseCurrency.getId());
            pstmt.setInt(3, targetCurrency.getId());

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return exchangeRate;
                }
            }
        }
        throw new SQLException("Update exchange rate failed");
    }
}
