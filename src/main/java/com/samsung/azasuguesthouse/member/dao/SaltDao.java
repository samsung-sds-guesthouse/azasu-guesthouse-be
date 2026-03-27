package com.samsung.azasuguesthouse.member.dao;

import com.samsung.azasuguesthouse.entity.member.Salt;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SaltDao {
    Salt selectByMemberId(long memberId);

    void insert(Salt salt);

    void deleteByMemberId(long memberId);
}
