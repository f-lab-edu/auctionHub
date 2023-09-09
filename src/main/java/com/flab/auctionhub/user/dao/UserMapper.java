package com.flab.auctionhub.user.dao;

import com.flab.auctionhub.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;

@Mapper // MyBatis의 매퍼 등록을 위한 어노테이션
public interface UserMapper {

    void save(User user);

    boolean isExistUserId(String userId);

    List<User> findAll();

    Optional<User> findById(Long id);
}
