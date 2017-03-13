package cn.com.pansky.demo.member.blo;

import cn.com.pansky.demo.member.Member;
import cn.com.pansky.demo.member.mapper.MemberMapper;
import org.mohrss.leaf.framework.domain.blo.impl.BLOImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Adi(adi@imeth.cn) on 2017/3/13.
 */
@Service
public class MemberBLOImpl extends BLOImpl implements MemberBLO {

    @Resource
    private MemberMapper memberMapper;

    @Override
    public Member get(String id) {
        return memberMapper.get(id);
    }

}
