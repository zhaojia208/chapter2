/*
package org.smart4j.chapter2.controller;

import org.apache.jasper.tagplugins.jstl.core.Param;
import org.smart4j.chapter2.model.Customer;
import org.smart4j.chapter2.service.CustomerService;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.swing.text.View;
import javax.xml.ws.Action;
import java.util.List;

*/
/*
* 处理客户管理相关请求
* *//*

@Controller
public class CustomerController {

    @Inject
    private CustomerService customerService;

    */
/*
    * 进入 客户列表 页面
    * *//*

    @Action("get:/customer")
    public View index(Param param){
        List<Customer> customerList = customerService.getCustomerList();
        return new View("customer.jsp")
    }
}
*/
