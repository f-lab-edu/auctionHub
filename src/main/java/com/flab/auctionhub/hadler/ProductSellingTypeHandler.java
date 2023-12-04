package com.flab.auctionhub.hadler;

import com.flab.auctionhub.product.domain.ProductSellingStatus;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

@MappedTypes(ProductSellingStatus.class)
public class ProductSellingTypeHandler implements TypeHandler<ProductSellingStatus> {

    @Override
    public void setParameter(PreparedStatement ps, int i, ProductSellingStatus productSellingStatus,
        JdbcType jdbcType)
        throws SQLException {
        ps.setString(i, productSellingStatus.getValue());
    }


    @Override
    public ProductSellingStatus getResult(ResultSet rs, String columnName) throws SQLException {
        return getProductSellingType(rs.getString(columnName));
    }

    @Override
    public ProductSellingStatus getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getProductSellingType(rs.getString(columnIndex));
    }

    @Override
    public ProductSellingStatus getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getProductSellingType(cs.getString(columnIndex));
    }

    private ProductSellingStatus getProductSellingType(String value) {

        for (ProductSellingStatus productSellingStatus : ProductSellingStatus.values()) {
            if (productSellingStatus.getValue().equals(value)) {
                return productSellingStatus;
            }
        }
        return null;
    }
}
