package com.flab.auctionhub.hadler;

import com.flab.auctionhub.product.domain.ProductSellingType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

@MappedTypes(ProductSellingType.class)
public class ProductSellingTypeHandler implements TypeHandler<ProductSellingType> {

    @Override
    public void setParameter(PreparedStatement ps, int i, ProductSellingType productSellingType,
        JdbcType jdbcType)
        throws SQLException {
        ps.setString(i, productSellingType.getValue());
    }


    @Override
    public ProductSellingType getResult(ResultSet rs, String columnName) throws SQLException {
        return getProductSellingType(rs.getString(columnName));
    }

    @Override
    public ProductSellingType getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getProductSellingType(rs.getString(columnIndex));
    }

    @Override
    public ProductSellingType getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getProductSellingType(cs.getString(columnIndex));
    }

    private ProductSellingType getProductSellingType(String value) {

        for (ProductSellingType productSellingType : ProductSellingType.values()) {
            if (productSellingType.getValue().equals(value)) {
                return productSellingType;
            }
        }
        return null;
    }
}
