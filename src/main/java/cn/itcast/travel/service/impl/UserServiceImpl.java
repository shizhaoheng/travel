package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();
    /**
     * 注册用户
     * @param user
     * @return
     */

    @Override
    public boolean regist(User user) {
        //根据用户名查询用户
        User u = userDao.findByUsername(user.getUsername());
        if (u != null) {
            //用户存在,注册失败！
            return false;
        }

        //保存用户
        userDao.saveUser(user);
        return false;
    }
}
