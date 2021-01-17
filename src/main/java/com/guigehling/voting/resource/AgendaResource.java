package com.guigehling.voting.resource;

import com.guigehling.voting.dto.AgendaDTO;
import com.guigehling.voting.dto.AgendaDetailsDTO;
import com.guigehling.voting.dto.SessionDTO;
import com.guigehling.voting.service.AgendaService;
import com.guigehling.voting.service.SessionService;
import com.guigehling.voting.service.VoteService;
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
    private final SessionService sessionService;
    private final VoteService voteService;

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

    @GetMapping("/{idAgenda}/details")
    public AgendaDetailsDTO openVotingSession(@PathVariable("idAgenda") @Positive Long idAgenda) {
        return agendaService.getAgendaDetails(idAgenda);
    }

    @PostMapping("/{idAgenda}/open-session")
    public SessionDTO openVotingSession(@PathVariable("idAgenda") @Positive Long idAgenda,
                                        @RequestParam(defaultValue = "60", required = false) @Positive Long minutesLong) {
        return sessionService.openVotingSession(idAgenda, minutesLong);
    }

}


