package org.superboot.common.pub;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.superboot.base.AuthRoles;
import org.superboot.base.BaseConstants;
import org.superboot.base.BaseException;
import org.superboot.base.StatusCode;
import org.superboot.config.RequestMappingHandlerConfig;
import org.superboot.dao.jpa.ModuleDAO;
import org.superboot.dao.jpa.PermissionsDAO;
import org.superboot.dao.jpa.PermissionsResourceDAO;
import org.superboot.dao.jpa.ResourceDAO;
import org.superboot.entity.jpa.SuperbootModule;
import org.superboot.entity.jpa.SuperbootPermissions;
import org.superboot.entity.jpa.SuperbootPermissionsResource;
import org.superboot.entity.jpa.SuperbootResource;

import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

/**
 * <b> 通用数据库操作工具类 </b>
 * <p>
 * 功能描述:
 * </p>
 */
@Component
public class Pub_DBUtils {

    @Value("${spring.application.name}")
    private String module_id;

    @Value("${eureka.instance.appname}")
    private String module_name;

    @Autowired
    private ModuleDAO moduleDAO;

    @Autowired
    private ResourceDAO resourceDAO;

    @Autowired
    private PermissionsDAO permissionsDAO;

    @Autowired
    private PermissionsResourceDAO permissionsResourceDAO;


    @Autowired
    private RequestMappingHandlerConfig requestMappingHandlerConfig;

    @Autowired
    private Pub_Tools pubTools;

    /**
     * 自动添加服务器的接口API到数据库
     */
    @Transactional(rollbackFor = BaseException.class)
    public void addApiToDB() {
        RequestMappingHandlerMapping requestMappingHandlerMapping = requestMappingHandlerConfig.requestMappingHandlerMapping();
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        Set<RequestMappingInfo> mappings = map.keySet();

        //添加模块信息
        SuperbootModule module = moduleDAO.findByModuleIdAndDr(module_id, BaseConstants.DATA_STATUS_OK);
        if (null == module) {
            module = new SuperbootModule();
        }
        module.setModuleId(module_id);
        module.setModuleName(module_name);
        moduleDAO.save(module);

        //构建API及权限分组信息
        for (RequestMappingInfo info : mappings) {
            HandlerMethod method = map.get(info);
            String moduleStr = method.toString();
            if (0 < moduleStr.split("\\(").length) {
                moduleStr = moduleStr.split("\\(")[0];
            }

            if (2 < moduleStr.split(" ").length) {
                moduleStr = moduleStr.split(" ")[2];
            }
            int i = moduleStr.lastIndexOf(".");
            moduleStr = moduleStr.substring(0, i);
            String urlparm = info.getPatternsCondition().toString();
            String url = urlparm.substring(1, urlparm.length() - 1);
            String methodName = method.getMethod().getName();
            String description = null;
            //获取对象信息
            Object o = null;


            //只有项目用到的资源才需要添加
            if (moduleStr.startsWith(BaseConstants.CONTROLLER_BASE_PATH)) {
                //获取对象信息
                try {
                    o = Class.forName(moduleStr).newInstance();
                } catch (InstantiationException e) {
                    throw new BaseException(StatusCode.EXCEPTION, e);
                } catch (IllegalAccessException e) {
                    throw new BaseException(StatusCode.EXCEPTION, e);
                } catch (ClassNotFoundException e) {
                    throw new BaseException(StatusCode.EXCEPTION, e);
                }

                //获取API信息
                SuperbootResource impResource = resourceDAO.findByModuleIdAndMethodPathAndMethodName(module_id, moduleStr, methodName);
                if (null == impResource) {
                    impResource = new SuperbootResource();
                }

                impResource.setUrl(url);
                impResource.setMethodPath(moduleStr);
                impResource.setModuleId(module_id);
                impResource.setModuleName(module_name);
                //获取字段的注解信息，此处用到的swagger配置的注解信息
                impResource.setMethodName(methodName);

                //映射模块路径
                RequestMapping requestMapping = o.getClass().getAnnotation(RequestMapping.class);
                if (null != requestMapping) {
                    impResource.setModulePath(StringUtils.join(requestMapping.value(), ","));
                }
                //设置服务名称,没写注解的默认不进行数据库注册
                Api api_annotation = o.getClass().getAnnotation(Api.class);
                if (null != api_annotation) {
                    impResource.setServiceName(StringUtils.join(api_annotation.tags(), ","));
                    //获取类功能注释，用于标记权限服务使用
                    description = api_annotation.description();
                    //根据API注解设置API名称及备注信息
                    Annotation operation_annotation = pubTools.getMothodAnnotationByAnnotation(o, ApiOperation.class, methodName);
                    if (null != operation_annotation) {
                        impResource.setApiName(((ApiOperation) operation_annotation).value());
                        impResource.setRemark(((ApiOperation) operation_annotation).notes());
                    }
                    impResource = resourceDAO.save(impResource);
                }


                //生成授权信息
                boolean genPermissions = true;
                AuthRoles authRoles = (AuthRoles) pubTools.getMothodAnnotationByAnnotation(o, AuthRoles.class, methodName);
                //如果方法上包含授权注解
                if (null == authRoles) {
                    //如果类上有不需要验证的注解则忽略创建资源权限
                    authRoles = o.getClass().getAnnotation(AuthRoles.class);
                    if (null != authRoles) {
                        String[] rols = authRoles.roles();
                        for (String role : rols) {
                            if (BaseConstants.ALL_USER_NAME.equals(role)) {
                                genPermissions = false;
                            }
                        }
                    }
                } else {
                    String[] rols = authRoles.roles();
                    for (String role : rols) {
                        if (BaseConstants.ALL_USER_NAME.equals(role)) {
                            genPermissions = false;
                        }
                    }
                }

                //如果接口没有添加权限注解并且注解的类型不是全用户访问，则生成授权信息
                if (genPermissions && null != impResource.getPkResource()) {
                    String mocode = impResource.getModuleId().replace("-", "_");
                    String code = mocode + "_" + impResource.getModulePath().substring(1).replace("/", "_");
                    String name = impResource.getServiceName();

                    SuperbootPermissions impPermissions = permissionsDAO.findByPermissionsCodeAndDr(code, BaseConstants.DATA_STATUS_OK);
                    if (null == impPermissions) {
                        impPermissions = new SuperbootPermissions();
                    }
                    impPermissions.setPermissionsCode(code);
                    impPermissions.setPermissionsName(name);
                    impPermissions.setPkGroup(-1L);
                    impPermissions.setPermissionsInfo(description);
                    impPermissions = permissionsDAO.save(impPermissions);

                    //构建权限资源信息
                    if (null != impPermissions) {
                        SuperbootPermissionsResource permissionsResource = permissionsResourceDAO.
                                findByPkPermissionsAndPkResourceAndDr
                                        (impPermissions.getPkPermissions(), impResource.getPkResource(), BaseConstants.DATA_STATUS_OK);
                        if (null == permissionsResource) {
                            permissionsResource = new SuperbootPermissionsResource();
                            permissionsResource.setPkPermissions(impPermissions.getPkPermissions());
                            permissionsResource.setPkResource(impResource.getPkResource());
                            permissionsResourceDAO.save(permissionsResource);
                        }
                    }

                }

            }
        }
    }
}