package cn.com.pansky.demo.member;

/**
 * Created by Adi(adi@imeth.cn) on 2017/3/9.
 */
public class MemberBpoImpl implements MemberBpo {

    public Member get(String id) {

        System.out.println("我是服务端我接收到一个id: " + id);

        return new Member(id, "名称" + id);
    }
}
