package cn.com.pansky.demo.member;

import java.io.Serializable;

/**
 * Created by Adi(adi@imeth.cn) on 2017/3/9.
 */
public class Member implements Serializable {

    private String id;
    private String name;

    public Member() {
    }

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
