package cn.com.pansky.demo.member.blo;

import cn.com.pansky.demo.member.Member;
import org.mohrss.leaf.framework.domain.blo.IBLO;

/**
 * Created by Adi(adi@imeth.cn) on 2017/3/13.
 */
public interface MemberBLO extends IBLO {

    Member get(String id);

}
