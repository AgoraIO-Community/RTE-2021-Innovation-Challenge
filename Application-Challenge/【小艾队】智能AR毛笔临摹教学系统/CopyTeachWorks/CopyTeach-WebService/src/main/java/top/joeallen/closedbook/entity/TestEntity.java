package top.joeallen.closedbook.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author MybatisHelperPro
 * @date 2021/03/29
 * @see <a href="https://gitee.com/lsl-gitee/LDevKit/tree/master/MybatisHelperPro">MybatisHelperPro</a>
 */
public class TestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String pwd;

    public TestEntity() {
    }

    public TestEntity(Integer id, String name, String pwd) {
        this.id = id;
        this.name = name;
        this.pwd = pwd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "TestEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TestEntity o1 = (TestEntity) o;
        return Objects.equals(id, o1.id) &&
                Objects.equals(name, o1.name) &&
                Objects.equals(pwd, o1.pwd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, pwd);
    }
}