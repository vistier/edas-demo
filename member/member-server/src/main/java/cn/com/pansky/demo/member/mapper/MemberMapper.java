package cn.com.pansky.demo.member.mapper;

import cn.com.pansky.demo.member.Member;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

/**
 * Created by Adi(adi@imeth.cn) on 2017/3/12.
 */
public interface MemberMapper {

    @ResultMap("memberResultMap")
    @Select("select * from t_member where id=#{id}")
    Member get(String id);

}
