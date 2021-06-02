package top.joeallen.closedbook.dao;

import org.springframework.stereotype.Repository;
import top.joeallen.closedbook.entity.TestEntity;

/**
 * @author MybatisHelperPro
 * @date 2021/03/29
 * @see <a href="https://gitee.com/lsl-gitee/LDevKit/tree/master/MybatisHelperPro">MybatisHelperPro</a>
 */
@Repository
public interface TestMapper {

    int insert(TestEntity record);

    int insertSelective(TestEntity record);

    int update(TestEntity record);

    int updateSelective(TestEntity record);

    TestEntity queryOne(Integer id);

    int delete(Integer id);
}