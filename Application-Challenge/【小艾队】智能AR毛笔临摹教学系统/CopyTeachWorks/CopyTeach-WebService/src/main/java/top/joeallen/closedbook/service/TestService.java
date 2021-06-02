package top.joeallen.closedbook.service;

import org.springframework.stereotype.Service;
import top.joeallen.closedbook.dao.TestMapper;
import top.joeallen.closedbook.entity.TestEntity;

import javax.annotation.Resource;

/**
 * @author JoeAllen_Li
 * @Date 2021/3/22 13:57
 * @describe 类描述
 */
@Service
public class TestService implements TestMapper {
    @Resource
    TestMapper testMapper;


    @Override
    public int insert(TestEntity record) {
        return 0;
    }

    @Override
    public int insertSelective(TestEntity record) {
        return 0;
    }

    @Override
    public int update(TestEntity record) {
        return 0;
    }

    @Override
    public int updateSelective(TestEntity record) {
        return 0;
    }

    @Override
    public TestEntity queryOne(Integer id) {
        return null;
    }

    @Override
    public int delete(Integer id) {
        return 0;
    }
}
