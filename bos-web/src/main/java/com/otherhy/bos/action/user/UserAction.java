package com.otherhy.bos.action.user;

import com.otherhy.bos.action.BaseAction;
import com.otherhy.bos.domain.user.User;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * Created by hao on 2017/8/7.
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("packageManager")
public class UserAction extends BaseAction<User> {
    @Action(value = "userAction_checkCode",results = {@Result(name = "checkCode",type = "json")})
    public String checkCode() {
        String checkCode = getParameter("checkCode");
        String  sessionCode = (String) getSession().getAttribute("key");
        if (sessionCode.equalsIgnoreCase(checkCode)) {
            push(true);
        } else {
            push(false);
        }
        return "checkCode";
    }
}
