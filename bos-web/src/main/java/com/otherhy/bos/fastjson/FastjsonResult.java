package com.otherhy.bos.fastjson;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.StrutsStatics;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Set;

/**
 * Created by hao on 2017/8/7.
 */
public class FastjsonResult implements Result {
    private String root;

    private Set<String> includeProperties = Collections.emptySet();
    private Set<String> excludeProperties = Collections.emptySet();

    public Set<String> getIncludeProperties() {
        return includeProperties;
    }

    public void setIncludeProperties(Set<String> includeProperties) {
        this.includeProperties = includeProperties;
    }

    public Set<String> getExcludeProperties() {
        return excludeProperties;
    }

    public void setExcludeProperties(Set<String> excludeProperties) {
        this.excludeProperties = excludeProperties;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    @Override
    public void execute(ActionInvocation invocation) throws Exception {
        //获取返回对象，设置返回参数
        HttpServletResponse response = (HttpServletResponse) invocation.getInvocationContext().get(StrutsStatics.HTTP_RESPONSE);
        response.setContentType("text/json;charset=utf-8");

        //获取需要序列化的对象
        Object rootObject = findRootObject(invocation);

        //获取序列化对象
        SimplePropertyPreFilter filter = new SimplePropertyPreFilter();

        //设置序列化条件
        if (includeProperties != null && includeProperties.size() != 0) {
            for (String property : includeProperties) {
                filter.getIncludes().add(property);
            }
        }
        if (excludeProperties != null && excludeProperties.size() != 0) {
            for (String property : excludeProperties) {
                filter.getExcludes().add(property);
            }
        }

        //执行序列化
        String jsonString = JSON.toJSONString(rootObject, filter, SerializerFeature.DisableCircularReferenceDetect);

        //写出
        response.getWriter().write(jsonString);
    }

    private Object findRootObject(ActionInvocation invocation) {
        ValueStack stack = invocation.getStack();
        Object rootObject;
        if (root != null) {
            rootObject = stack.findValue(root);
        } else {
            rootObject = stack.peek();
        }
        return rootObject;
    }
}
