package ru.otus;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ValuesSetter {
    private final PreparedStatement pst;

    public ValuesSetter(PreparedStatement preparedStatement) {
        this.pst = preparedStatement;
    }

    public void setValue(Object value, int parameterIndex) throws SQLException {
        if (value.getClass().equals(String.class)) {
            pst.setString(parameterIndex, (String) value);
        }
        if (value.getClass().equals(Integer.class)) {
            pst.setInt(parameterIndex, (Integer) value);
        }
        if (value.getClass().equals(Long.class)) {
            pst.setLong(parameterIndex, (Long) value);
        }
        if (value.getClass().equals(Double.class)) {
            pst.setDouble(parameterIndex, (Double) value);
        }
        if (value.getClass().equals(BigDecimal.class)) {
            pst.setBigDecimal(parameterIndex, (BigDecimal) value);
        }
    }


}
