package com.qhieco.web.controller;

import com.qhieco.commonentity.Permission;
import com.qhieco.commonentity.Role;
import com.qhieco.commonentity.UserWeb;
import com.qhieco.constant.Status;
import com.qhieco.request.web.QueryPaged;
import com.qhieco.request.web.RoleRequest;
import com.qhieco.request.web.UserWebRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.AdminService;
import com.qhieco.webservice.PermissionService;
import com.qhieco.webservice.RoleService;
import com.qhieco.webservice.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/7 17:46
 * <p>
 * 类说明：
 * ${desription}
 */
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthWeb {

    @Autowired
    RoleService roleService;
    @Autowired
    AdminService adminService;
    @Autowired
    PermissionService permissionService;

    @RequestMapping(value = "unLogin")
    public Resp unLogin(){
        return RespUtil.errorResp(Status.WebErr.LOGIN_REQUIRE.getCode(),"尚未登录！无法访问");
    }

    @RequestMapping(value = "accessDenied")
    public Resp accessDenied(){
        return RespUtil.errorResp(Status.WebErr.ACCESS_DENIED.getCode(),Status.WebErr.ACCESS_DENIED.getMsg());
    }

    @PostMapping(value = "admin/save")
    public Resp adminSave(UserWeb userWeb){
        Resp paramResp = ParamCheck.check(userWeb, "username", "password");
        if(userWeb.getId()==null && !paramResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(paramResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return adminService.save(userWeb);
    }

    @PostMapping(value = "admin/pageable")
    public Resp adminAll( QueryPaged request){
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return adminService.all(request);
    }

    @PostMapping(value = "admin/bind")
    public Resp bindRole(@RequestParam("userId") Integer userId, @RequestParam("roleId") Integer[] roleIds){
        if (userId == null || roleIds == null || roleIds.length==0){
            return RespUtil.errorResp(Status.WebErr.EMPTY_INPUT_PARAM.getCode(),"绑定参数为空");
        }
        return adminService.bindRole(userId, roleIds);
    }

    @PostMapping(value = "admin/unbind")
    public Resp unbindRole(@RequestParam("userId") Integer userId, @RequestParam("roleId") Integer[] roleIds){
        if (userId == null || roleIds == null || roleIds.length==0){
            return RespUtil.errorResp(Status.WebErr.EMPTY_INPUT_PARAM.getCode(),"解绑参数为空");
        }
        return adminService.unbindRole(userId, roleIds);
    }

    @PostMapping(value = "admin/role")
    public Resp adminRole(Integer id){
        if(id == null){
            return RespUtil.errorResp(Status.WebErr.EMPTY_INPUT_PARAM.getCode(),"管理员id为空");
        }
        return adminService.findRole(id);
    }

    @PostMapping(value = "admin/permission")
    public Resp adminPermission(Integer id){
        if(id == null){
            return RespUtil.errorResp(Status.WebErr.EMPTY_INPUT_PARAM.getCode(),"管理员id为空");
        }
        return adminService.findPermission(id);
    }


    @RequestMapping(value = "role/excel")
    public void roleExcel(HttpServletResponse response,Integer level, String name, Long startCreateTime, Long endCreateTime) {
        if (ExcelUtil.paramCount(name, startCreateTime, endCreateTime) == 0) {
            return;
        }

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        // 下载文件的默认名称
        try {
            response.setHeader("Content-Disposition",
                    "attachment;filename="+ new String(("角色" + ".xls").getBytes(), "iso-8859-1"));
            roleService.excel(level, name, startCreateTime, endCreateTime, response.getOutputStream());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @PostMapping(value = "role/pageable")
    public Resp roleAll( RoleRequest roleRequest){
        Resp checkResp = ParamCheck.check(roleRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return roleService.all(roleRequest);
    }

    @PostMapping(value = "role/save")
    public Resp roleSave(Role role){
        Resp paramResp = ParamCheck.check(role, "name");
        if(role.getId()==null && !paramResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(paramResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return roleService.save(role);
    }

    @PostMapping(value = "role/permission")
    public Resp rolePermission(Integer id){
        if(id == null){
            return RespUtil.errorResp(Status.WebErr.EMPTY_INPUT_PARAM.getCode(),"角色id为空");
        }
        return roleService.findPermission(id);
    }


    @PostMapping(value = "role/noPermission")
    public Resp roleNoPermission(Integer id){
        if(id == null){
            return RespUtil.errorResp(Status.WebErr.EMPTY_INPUT_PARAM.getCode(),"角色id为空");
        }
        return roleService.noPermission(id);
    }

    @PostMapping(value = "role/bind")
    public Resp bindPermission(@RequestParam("roleId") Integer userId, @RequestParam("permissionId") Integer[] permissionIds){
        if (userId == null || permissionIds == null || permissionIds.length==0){
            return RespUtil.errorResp(Status.WebErr.EMPTY_INPUT_PARAM.getCode(),"绑定参数为空");
        }
        return roleService.bindPermission(userId, permissionIds);
    }

    @PostMapping(value = "role/unbind")
    public Resp unbindPermission(@RequestParam("roleId") Integer userId, @RequestParam("permissionId") Integer[] permissionIds){
        if (userId == null || permissionIds == null ||  permissionIds.length==0){
            return RespUtil.errorResp(Status.WebErr.EMPTY_INPUT_PARAM.getCode(),"解绑参数为空");
        }
        return roleService.unbindPermission(userId, permissionIds);
    }

    @PostMapping(value = "permission/save")
    public Resp permissionSave(Permission permission) {
        Resp paramResp = ParamCheck.check(permission, "name","url");
        if(permission.getId()==null && !paramResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(paramResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return permissionService.save(permission);
    }

    @PostMapping(value = "permission/pageable")
    public Resp permissionAll(QueryPaged request){
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return permissionService.all(request);
    }

    @PostMapping(value = "check")
    public Resp authCheck(HttpServletRequest request,HttpServletResponse response,@RequestParam("url") String url){
        if(url==null){
            return RespUtil.errorResp(Status.WebErr.EMPTY_INPUT_PARAM.getCode(),"url参数为空");
        }
        val admin = (UserWeb) request.getSession().getAttribute("user");
        if (admin == null ){
            return RespUtil.errorResp(Status.WebErr.LOGIN_REQUIRE.getCode(),"尚未登录");
        }
        if (admin.isSuper() || permissionService.check(admin, url)){
            return RespUtil.successResp();
        }
        return RespUtil.errorResp(Status.WebErr.ACCESS_DENIED.getCode(),"无权限访问该页面");
    }

    /**
     * @author ashang
     * @param request
     * @return
     */
    @PostMapping(value = "admin/account")
    public Resp account(HttpServletRequest request){
        return adminService.findAccount(request);
    }

    @PostMapping(value = "daddy/pageable")
    public Resp daddyAll( QueryPaged request){
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return adminService.daddyAll(request);
    }

    @PostMapping(value = "son/pageable")
    public  Resp sonAll(UserWebRequest request, HttpServletRequest httpServletRequest){
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return adminService.sonAll(request,httpServletRequest);
    }

}
