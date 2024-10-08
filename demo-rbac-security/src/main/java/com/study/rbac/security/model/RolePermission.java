package com.study.rbac.security.model;

import com.study.rbac.security.model.unionkey.RolePermissionKey;
import lombok.Data;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * <p>
 * 角色-权限
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-10 13:46
 */
@Data
@Entity
@Table(name = "sec_role_permission")
public class RolePermission {
    /**
     * 主键
     */
    @EmbeddedId
    private RolePermissionKey id;
}
