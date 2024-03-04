package com.flab.auctionhub.hadler;

import com.flab.auctionhub.common.exception.UserRoleTypeNotFoundException;
import com.flab.auctionhub.user.domain.UserRoleType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

@MappedTypes(UserRoleType.class) // MyBatis에서 사용되는 어노테이션으로, 자바 타입과 MyBatis의 타입 핸들러를 매핑할 때 사용된다.
public class UserRoleTypeHandler implements TypeHandler<UserRoleType> {

    @Override
    public void setParameter(PreparedStatement ps, int i, UserRoleType userRoleType, JdbcType jdbcType)
        throws SQLException {
        ps.setString(i, userRoleType.getValue());
    }

    @Override
    public UserRoleType getResult(ResultSet rs, String columnName) throws SQLException {
        return getRoleName(rs.getString(columnName));
    }

    @Override
    public UserRoleType getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getRoleName(rs.getString(columnIndex));
    }

    @Override
    public UserRoleType getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return getRoleName(cs.getString(columnIndex));
    }

    private UserRoleType getRoleName(String value) {

        for (UserRoleType userRoleType : UserRoleType.values()) {
            if (userRoleType.getValue().equals(value))
                return userRoleType;
        }
        throw new UserRoleTypeNotFoundException("UserRoleType을 찾을 수 없습니다.");
    }
}
