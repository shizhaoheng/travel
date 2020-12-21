package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    //声明UserService业务对象
    private UserService service = new UserServiceImpl();

    /**
     * 注册功能
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取前台提供的验证码
        String check = request.getParameter("check");
        //从session中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        //比较
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            //验证码错误
            ResultInfo info = new ResultInfo();
            info.setFlag(false);
            info.setErrorMsg("验证码错误!");

            //将info对象序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            //将json数据写回客户端
            //设置content-type
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);
            return;


        }


        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();

        //2.封装对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用service完成注册
//        UserService service = new UserServiceImpl();
        boolean flag = service.regist(user);

        //4.响应结果
        ResultInfo info = new ResultInfo();
        if (flag) {
            //注册成功
            info.setFlag(true);
        } else {
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败!");
        }

        //5.将info对象序列化为json
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(info);
        String json = writeValueAsString(info);

        //6.将json数据写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);

    }

    /**
     * 登录功能
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.获取用户名和密码数据
        Map<String, String[]> map = request.getParameterMap();

        //2.封装user对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //3.调用service查询
//        UserService service = new UserServiceImpl();
        User u = service.login(user);

        ResultInfo info = new ResultInfo();
        //4.判断用户对象是否为null
        if (u == null) {
            //用户名密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");
        }

        //5.判断用户是否激活
        if (u != null && !"Y".equals(u.getStatus())) {
            //用户尚未激活
            info.setFlag(false);
            info.setErrorMsg("您尚未激活，请激活！");
        }

        //5.判断登录成功
        if (u != null && "Y".equals(u.getStatus())) {
            //登录成功
            info.setFlag(true);
            info.setErrorMsg("登录成功!");
            request.getSession().setAttribute("user", u);//登录成功标记
        }

        //响应数据
//        ObjectMapper mapper = new ObjectMapper();
//        response.setContentType("application/json;charset=utf-8");
//        mapper.writeValue(response.getOutputStream(), info);
        writeValue(info, response);


    }


    /**
     * 查询单个用户
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session中获取登录用户
        Object user = request.getSession().getAttribute("user");
        //将user写回客户端

//        ObjectMapper mapper = new ObjectMapper();
//        response.setContentType("application/json;charset=utf-8");
//        mapper.writeValue(response.getOutputStream(), user);
        writeValue(user, response);

    }


    /**
     * 退出功能
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */

    public void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.销毁session
        request.getSession().invalidate();

        //2.跳转登录页面
        response.sendRedirect(request.getContextPath() + "/login.html");

    }


    /**
     * 激活功能
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //1.获取激活码
        String code = request.getParameter("code");
        if (code != null) {
            //2.调用service完成激活
//            UserService service = new UserServiceImpl();
            boolean flag = service.active(code);

            //3.判断激活是否成功
            String msg = null;
            if (flag) {
                msg = "激活成功,清<a href='login.html'>登录</a>";
            } else {
                msg = "激活失败，请联系管理员！";
            }

            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);

        }

    }
}
