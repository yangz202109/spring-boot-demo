package com.xkcoding.rbac.security.model.unionkey;

import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

/**
 * <p>
 * 角色-权限联合主键
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-10 13:47
 */
@Data
@Embeddable
public class RolePermissionKey implements Serializable {
    private static final long serialVersionUID = 6850974328279713855L;

    /**
     * 角色id
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 权限id
     */
    @Column(name = "permission_id")
    private Long permissionId;
}
