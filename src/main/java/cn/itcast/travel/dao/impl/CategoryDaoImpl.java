package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao{

    private JdbcTemplate jdbcTemplate = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 查询所有
     * @return
     */
    @Override
    public List<Category> findAll() {
        String sql = "SELECT * FROM tab_category order by cid";
//        String sql = "SELECT * FROM tab_category";
        return jdbcTemplate.query(sql,new BeanPropertyRowMapper<Category>(Category.class));
    }
}
