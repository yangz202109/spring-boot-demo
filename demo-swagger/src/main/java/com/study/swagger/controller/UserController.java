package com.study.swagger.controller;

import com.study.swagger.common.ApiResponse;
import com.study.swagger.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * <p>
 * User Controller
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-11-29 11:30
 */
@RestController
@RequestMapping("/user")
@Tag(name = "UserControllerApi", description = "用户管理")
@Slf4j
public class UserController {
    
    @GetMapping
    @Operation(description = "根据用户名称查询", parameters =
            {@Parameter(name = "username", description = "用户名",required = true)}
    )
    public ApiResponse<User> getByUserName(String username) {
        log.info("多个参数用  @ApiImplicitParams");
        return ApiResponse.<User>builder().code(200).message("操作成功").data(new User(1, username, "JAVA")).build();
    }

    @GetMapping("/{id}")
    @Operation(description = "根据用户id查询",parameters = {@Parameter(name = "id", description = "用户id",required = true)})
    public ApiResponse<User> get(@PathVariable Integer id) {
        log.info("单个参数用  @ApiImplicitParam");
        return ApiResponse.<User>builder().code(200).message("操作成功").data(new User(id, "u1", "p1")).build();
    }

    @DeleteMapping("/{id}")
    @Operation(description = "删除用户",parameters = {@Parameter(name = "id", description = "用户id",required = true)})
    public void delete(@PathVariable Integer id) {
        log.info("单个参数用 ApiImplicitParam");
    }

    @PostMapping
    @Operation(description = "添加用户")
    public User save(@RequestBody User user) {
        log.info("如果是 POST PUT 这种带 @RequestBody 的可以不用写 @ApiImplicitParam");
        return user;
    }

    @PostMapping("/batchSave")
    @Operation(description = "批量添加用户")
    public List<User> batchSave(@RequestBody List<User> user) {
        log.info("如果是 POST PUT 这种带 @RequestBody 的可以不用写 @ApiImplicitParam");
        return user;
    }

    @PostMapping("/batchSaveByArray")
    @Operation(description = "批量添加用户")
    public User[] batchSaveByArray(@RequestBody User[] user) {
        log.info("如果是 POST PUT 这种带 @RequestBody 的可以不用写 @ApiImplicitParam");
        return user;
    }

    @PutMapping("/{id}")
    @Operation(description = "修改用户")
    public void put(@PathVariable Long id, @RequestBody User user) {
        log.info("如果你不想写 @ApiImplicitParam 那么 swagger 也会使用默认的参数名作为描述信息 ");
    }

    @PostMapping("/{id}/file")
    @Operation(description = "文件上传（DONE）")
    public String file(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        log.info(file.getContentType());
        log.info(file.getName());
        log.info(file.getOriginalFilename());
        return file.getOriginalFilename();
    }
}
