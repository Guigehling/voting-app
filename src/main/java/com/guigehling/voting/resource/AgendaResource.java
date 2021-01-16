package com.guigehling.voting.resource;

import com.guigehling.voting.dto.AgendaDTO;
import com.guigehling.voting.dto.SessionDTO;
import com.guigehling.voting.service.AgendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import static org.springframework.http.HttpStatus.CREATED;

@Validated
@RestController
@RequestMapping("/v1/agendas")
@RequiredArgsConstructor
public class AgendaResource {

    private final AgendaService agendaService;

    @PostMapping
    @ResponseStatus(CREATED)
    public AgendaDTO create(@RequestBody @Valid AgendaDTO agendaDTO) {
        return agendaService.create(agendaDTO);
    }

    @GetMapping
    public Page<AgendaDTO> query(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") @Min(10) @Max(25) Integer size) {
        return agendaService.query(PageRequest.of(page, size));
    }

    @PostMapping("/{idAgenda}/session")
    public SessionDTO openVotingSession(@PathVariable("idAgenda") @Positive Long personId,
                                        @RequestParam(defaultValue = "60", required = false) @Positive Long duration) {
        return null;
    }

}


