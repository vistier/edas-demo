package cn.com.pansky.demo.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Adi(adi@imeth.cn) on 2017/3/9.
 */
@Controller
public class MemberController {

    @Autowired
    private MemberBpo memberBpo;

    @ResponseBody
    @RequestMapping("/member/{id}")
    public Member get(@PathVariable String id) {
        return memberBpo.get(id);
    }

}
