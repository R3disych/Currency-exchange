package currencyexchange.dao;

import currencyexchange.model.Currency;
import currencyexchange.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDao {
    private static final String GET_SQL_REQUEST = "select * from currencies";
    private static final String GET_BY_CODE_SQL_REQUEST = "select * from currencies where code = ?";
    private static final String GET_BY_ID_SQL_REQUEST = "select * from currencies where id = ?";
    private static final String POST_SQL_REQUEST = "insert into currencies (code, full_name, sign) values (?, ?, ?) returning id";

    public List<Currency> getAllCurrencies() throws SQLException {
        List<Currency> currencies = new ArrayList<>();
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_SQL_REQUEST);
             ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {
                Currency currency = new Currency();
                currency.setId(resultSet.getInt("id"));
                currency.setCurrencyCode(resultSet.getString("code"));
                currency.setFullName(resultSet.getString("full_name"));
                currency.setSign(resultSet.getString("sign"));
                currencies.add(currency);
            }
        }
        return currencies;
    }

    public Currency getCurrencyByCode(String code) throws SQLException {
        try (Connection connection = DBUtil.getConnection();//лучше сделать через optional везде где я беру один элемент
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_CODE_SQL_REQUEST)) {

            pstmt.setString(1, code);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Currency currency = new Currency();
                    currency.setId(rs.getInt("id"));
                    currency.setCurrencyCode(rs.getString("code"));
                    currency.setFullName(rs.getString("full_name"));
                    currency.setSign(rs.getString("sign"));
                    return currency;
                }
            }
        }
        throw new SQLException("This currency doesn't exists"); //лучше не создавать ошибки из не ошибки
    }

    public Currency getCurrencyById(int id) throws SQLException {
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_BY_ID_SQL_REQUEST)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Currency currency = new Currency();
                    currency.setId(rs.getInt("id"));
                    currency.setCurrencyCode(rs.getString("code"));
                    currency.setFullName(rs.getString("full_name"));
                    currency.setSign(rs.getString("sign"));
                    return currency;
                }
            }
        }
        throw new SQLException("This currency doesn't exists");
    }

    public Currency createCurrency(Currency currency) throws SQLException {
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(POST_SQL_REQUEST)) {

            pstmt.setString(1, currency.getCurrencyCode());
            pstmt.setString(2, currency.getFullName());
            pstmt.setString(3, currency.getSign());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return currency;
                }
            }
        }
        throw new SQLException("Creating currancy failed, no ID obtained.");
    }
}
