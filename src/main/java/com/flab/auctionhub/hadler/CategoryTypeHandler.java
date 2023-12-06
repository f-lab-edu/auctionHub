package com.flab.auctionhub.hadler;

import com.flab.auctionhub.category.domain.CategoryType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

@MappedTypes(CategoryType.class)
public class CategoryTypeHandler implements TypeHandler<CategoryType> {

    @Override
    public void setParameter(PreparedStatement ps, int i, CategoryType categoryType,
        JdbcType jdbcType)
        throws SQLException {
        ps.setString(i, categoryType.getValue());
    }

    @Override
    public CategoryType getResult(ResultSet rs, String columnName) throws SQLException {
        return getCategoryType(rs.getString(columnName));
    }

    @Override
    public CategoryType getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getCategoryType(rs.getString(columnIndex));
    }

    @Override
    public CategoryType getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getCategoryType(cs.getString(columnIndex));
    }

    private CategoryType getCategoryType(String value) {
        for (CategoryType categoryType : CategoryType.values()) {
            if (categoryType.getValue().equals(value)) {
                return categoryType;
            }
        }
        return null;
    }
}
