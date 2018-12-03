package br.edu.ulbra.election.candidate.api.v1;

import br.edu.ulbra.election.candidate.input.v1.CandidateInput;
import br.edu.ulbra.election.candidate.output.v1.CandidateOutput;
import br.edu.ulbra.election.candidate.output.v1.GenericOutput;
import br.edu.ulbra.election.candidate.service.CandidateService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/v1/candidate")
public class CandidateApi {

    private final CandidateService candidateService;

    @Autowired
    public CandidateApi(CandidateService candidateService) { this.candidateService = candidateService; }

    @GetMapping("/")
    @ApiOperation(value = "Get candidates List")
    public List<CandidateOutput> getAll(){
        return candidateService.getAll();
    }

    @GetMapping("/{candidateId}")
    @ApiOperation(value = "Get candidate by Id")
    public CandidateOutput getById(@PathVariable Long candidateId){
        return candidateService.getById(candidateId);
    }

    @GetMapping("/numberElection/{numberElection}")
    @ApiOperation(value = "Get number Election")
    public List<CandidateOutput> getAllByNumberElection(@PathVariable Long numberElection) { return  candidateService.getAllByNumberElection(numberElection); }

    @GetMapping("/numberElection/{numberElection}/election/{electionId}")
    @ApiOperation(value = "Get number Election by Election id")
    public CandidateOutput getAllByNumberElection(@PathVariable Long numberElection, @PathVariable Long electionId) {
        return  candidateService.getCandidateByNumberElectionAndElectionId(numberElection, electionId);
    }

    @PostMapping("/")
    @ApiOperation(value = "Create new candidate")
    public CandidateOutput create(@RequestBody CandidateInput candidateInput){
        return candidateService.create(candidateInput);
    }

    @PutMapping("/{candidateId}")
    @ApiOperation(value = "Update candidate")
    public CandidateOutput update(@PathVariable Long candidateId, @RequestBody CandidateInput candidateInput){
        return candidateService.update(candidateId, candidateInput);
    }

    @DeleteMapping("/{candidateId}")
    @ApiOperation(value = "Delete candidate")
    public GenericOutput delete(@PathVariable Long candidateId){
        return candidateService.delete(candidateId);
    }
}
