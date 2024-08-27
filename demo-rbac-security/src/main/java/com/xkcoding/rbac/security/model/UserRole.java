package com.xkcoding.rbac.security.model;

import com.xkcoding.rbac.security.model.unionkey.UserRoleKey;
import lombok.Data;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * <p>
 * 用户角色关联
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-10 11:18
 */
@Data
@Entity
@Table(name = "sec_user_role")
public class UserRole {
    /**
     * 主键
     */
    @EmbeddedId
    private UserRoleKey id;
}
