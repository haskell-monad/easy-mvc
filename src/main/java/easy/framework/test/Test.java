package easy.framework.test;

import easy.framework.orm.annotation.Entity;

/**
 * @author limengyu
 * @create 2017/10/23
 */
@Entity
public class Test {

    private Integer id;
    private Integer name;

    public Integer getName() {
        return name;
    }

    public void setName(Integer name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
