package com.guigehling.voting.integration.user;

import com.guigehling.voting.integration.user.dto.UserStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "UserIntegration", url = "${api.path.user.host}${api.path.user.basePath}")
public interface UserIntegration {

    @GetMapping("${api.path.user.isAble}")
    UserStatus isAble(@PathVariable("cpf") String Cpf);

}
