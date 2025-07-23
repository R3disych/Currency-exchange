package currencyexchange.model;

public class Currency {
    private int id;
    private String currencyCode;
    private String fullName;
    private String sign;

    public Currency() {
    }

    public Currency(int id, String currencyCode, String fullName, String sign) {
        this.id = id;
        this.currencyCode = currencyCode;
        this.fullName = fullName;
        this.sign = sign;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "Currency [id=" + id + ", currencyCode=" + currencyCode + ", fullName=" + fullName + ", sign=" + sign + "]";
    }
}
