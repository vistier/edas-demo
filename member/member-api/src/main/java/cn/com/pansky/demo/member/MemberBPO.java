package cn.com.pansky.demo.member;

import org.mohrss.leaf.framework.domain.bpo.IBPO;

/**
 * Created by Adi(adi@imeth.cn) on 2017/3/9.
 */
public interface MemberBPO extends IBPO {

    Member get(String id);

}
