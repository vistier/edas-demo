package cn.com.pansky.demo.member;

import cn.com.pansky.demo.member.blo.MemberBLO;
import org.mohrss.leaf.framework.domain.bpo.impl.BPOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Adi(adi@imeth.cn) on 2017/3/9.
 */
@Service("memberBPOImpl")
public class MemberBPOImpl extends BPOImpl implements MemberBPO {

    @Autowired
    private MemberBLO memberBLO;

    public Member get(String id) {

        System.out.println("我是服务端我接收到一个id: " + id);

        return memberBLO.get(id);
    }
}
