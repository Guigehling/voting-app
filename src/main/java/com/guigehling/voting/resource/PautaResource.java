package com.guigehling.voting.resource;

import com.guigehling.voting.dto.PautaDTO;
import com.guigehling.voting.service.PautaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1")
public class PautaResource {

    private PautaService pautaService;

    @GetMapping
    public Page<PautaDTO> query(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") @Min(10) @Max(25) Integer size) {
        return pautaService.getPautas(PageRequest.of(page, size));
    }

}
