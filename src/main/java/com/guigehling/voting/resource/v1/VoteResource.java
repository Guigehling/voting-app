package com.guigehling.voting.resource.v1;

import com.guigehling.voting.dto.VoteDTO;
import com.guigehling.voting.exception.BusinessException;
import com.guigehling.voting.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@Validated
@RestController
@RequestMapping("/v1/votes")
@RequiredArgsConstructor
public class VoteResource {

    private final VoteService voteService;

    @PostMapping
    @ResponseStatus(CREATED)
    public void registerVote(@RequestBody(required = true) @Valid VoteDTO voteDTO) throws BusinessException {
        voteService.registerVote(voteDTO);
    }

}


