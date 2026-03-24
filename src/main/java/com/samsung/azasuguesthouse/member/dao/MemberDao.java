package com.samsung.azasuguesthouse.member.dao;

import com.samsung.azasuguesthouse.entity.member.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface MemberDao {
    int countByLoginId(String loginId);

    Member selectByLoginId(String loginId);

    void insert(Member member);

    void updatePassword(Member member);
    void updateTry(Member member);

    void log(Map<String, Object> data);
}
