package com.otherhy.bos.action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.otherhy.bos.service.FacadeService;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hao on 2017/8/6.
 */
public class BaseAction<T> extends ActionSupport implements ModelDriven<T> {
    @Autowired
    protected FacadeService facadeService;

    protected T model;

    @Override
    public T getModel() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0].getClass();
        try {
            model = modelClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    //分页封装
    protected int page;
    protected int rows;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    private Map<String, Object> map;

    public void setToStack(Page<T> page) {
        map = new HashMap<>();
        map.put("total", page.getTotalElements());
        map.put("rows", page.getContent());
    }

    public Map<String, Object> getMap() {
        return map;
    }

    //servlet相关API
    public HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    public HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

    public HttpSession getSession() {
        return ServletActionContext.getRequest().getSession();
    }

    //操作值栈
    public void setToStack(String key, Object value) {
        ActionContext.getContext().getValueStack().set(key, value);
    }

    public void pushToStack(Object value) {
        ActionContext.getContext().getValueStack().push(value);
    }

    public void putToMap(String key, Object value) {
        ActionContext.getContext().put(key, value);
    }
}
