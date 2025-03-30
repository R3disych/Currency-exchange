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
    private static final String SQL_REQUEST = "select * from currencies";

    public List<Currency> getCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        try (Connection connection = DBUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_REQUEST);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Currency currency = new Currency();
                currency.setId(resultSet.getInt("id"));
                currency.setCurrencyCode(resultSet.getString("code"));
                currency.setFullName(resultSet.getString("full_name"));
                currency.setSign(resultSet.getString("sign"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return currencies;
    }
}
