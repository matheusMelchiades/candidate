package br.edu.ulbra.election.candidate.service;

import br.edu.ulbra.election.candidate.client.ElectionClientService;
import br.edu.ulbra.election.candidate.client.PartyClientService;
import br.edu.ulbra.election.candidate.exception.GenericOutputException;
import br.edu.ulbra.election.candidate.input.v1.CandidateInput;
import br.edu.ulbra.election.candidate.model.Candidate;
import br.edu.ulbra.election.candidate.output.v1.CandidateOutput;
import br.edu.ulbra.election.candidate.output.v1.ElectionOutput;
import br.edu.ulbra.election.candidate.output.v1.GenericOutput;
import br.edu.ulbra.election.candidate.output.v1.PartyOutput;
import br.edu.ulbra.election.candidate.repository.CandidateRepository;
import feign.FeignException;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidateService {
    private final CandidateRepository candidateRepository;

    private final ModelMapper modelMapper;
    private final ElectionClientService electionClientService;
    private final PartyClientService partyClientService;

    private static final String MESSAGE_INVALID_ID = "Invalid id";
    private static final String MESSAGE_INVALID_ELECTION_ID = "Invalid Election Id";
    private static final String MESSAGE_INVALID_NUMBER_ELECTION = "Invalid Number Election";
    private static final String MESSAGE_CANDIDATE_NOT_FOUND = "Candidate not found";

    @Autowired
    public CandidateService(CandidateRepository candidateRepository, ModelMapper modelMapper, ElectionClientService electionClientService, PartyClientService partyClientService) {
        this.candidateRepository = candidateRepository;
        this.modelMapper = modelMapper;
        this.electionClientService = electionClientService;
        this.partyClientService = partyClientService;
    }

    public List<CandidateOutput> getAll(){
        List<Candidate> candidateList = (List<Candidate>)candidateRepository.findAll();
        return candidateList.stream().map(this::toCandidateOutput).collect(Collectors.toList());
    }

    public CandidateOutput create(CandidateInput candidateInput) {
        validateInput(candidateInput);

        Candidate candidate = modelMapper.map(candidateInput, Candidate.class);

        candidate = candidateRepository.save(candidate);

        return toCandidateOutput(candidate);
    }

    public CandidateOutput getById(Long candidateId){
        if (candidateId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);

        if (candidate == null){
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
        }

        return toCandidateOutput(candidate);
    }

    public List<CandidateOutput> getAllByNumberElection(Long numberElection){
        if(numberElection == null) {
            throw  new GenericOutputException(MESSAGE_INVALID_NUMBER_ELECTION);
        }

        List<Candidate> candidateList = (List<Candidate>) candidateRepository.findAllByNumberElection(numberElection);

        System.out.println("teste" + candidateList);

        if (candidateList == null) {
            throw new GenericOutputException(MESSAGE_INVALID_NUMBER_ELECTION);
        }

        return candidateList.stream().map(this::toCandidateOutput).collect(Collectors.toList());
    }

    public CandidateOutput getCandidateByNumberElectionAndElectionId(Long numberElection,Long electionId) {
        if(numberElection == null) {
            throw  new GenericOutputException(MESSAGE_INVALID_NUMBER_ELECTION);
        }

        if(electionId == null) {
            throw  new GenericOutputException(MESSAGE_INVALID_ELECTION_ID);
        }

        Candidate candidate = candidateRepository.findByNumberElectionAndElectionId(numberElection, electionId).orElse(null);

        if(candidate == null) {
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
        }

        return toCandidateOutput(candidate);
    }

    public CandidateOutput update(Long candidateId, CandidateInput candidateInput) {
        if (candidateId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }
        validateInput(candidateInput);

        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);

        if (candidate == null){
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
        }

        candidate.setName(candidateInput.getName());
        candidate.setPartyId(candidateInput.getPartyId());
        candidate.setNumberElection(candidateInput.getNumberElection());
        candidate.setElectionId(candidateInput.getElectionId());

        candidate = candidateRepository.save(candidate);

        return toCandidateOutput(candidate);
    }

    public GenericOutput delete(Long candidateId) {
        if (candidateId == null){
            throw new GenericOutputException(MESSAGE_INVALID_ID);
        }

        Candidate candidate = candidateRepository.findById(candidateId).orElse(null);

        if (candidate == null){
            throw new GenericOutputException(MESSAGE_CANDIDATE_NOT_FOUND);
        }

        candidateRepository.delete(candidate);
        return new GenericOutput("Candidate deleted");
    }

    public CandidateOutput toCandidateOutput(Candidate candidate) {
        CandidateOutput candidateOutput = modelMapper.map(candidate, CandidateOutput.class);

        ElectionOutput electionOutput = electionClientService.getById(candidate.getElectionId());
        candidateOutput.setElectionOutput(electionOutput);

        PartyOutput partyOutput = partyClientService.getById(candidate.getPartyId());
        candidateOutput.setPartyOutput(partyOutput);

        return candidateOutput;
    }

    private void validateInput(CandidateInput candidateInput){
        if (StringUtils.isBlank(candidateInput.getName()) ||
                (candidateInput.getName().trim().split(" ").length < 2) ||
                (candidateInput.getName().trim().replace(" ", "").length() < 5))
        {
            throw new GenericOutputException("Invalid name");
        }
        if (StringUtils.isBlank("" + candidateInput.getPartyId())){
            throw new GenericOutputException("Invalid party Id");
        }
        if (StringUtils.isBlank("" + candidateInput.getNumberElection())){
            throw new GenericOutputException("Invalid Number election");
        }
        if (StringUtils.isBlank("" + candidateInput.getElectionId())){
            throw new GenericOutputException("Invalid Election Id");
        }

        if ( candidateRepository.findByNumberElectionAndElectionId(candidateInput.getNumberElection(), candidateInput.getElectionId()).orElse(null) != null) {
            throw new GenericOutputException("Invalid Number election, this number election already used in this election");
        }

        //VALIDATIONS REQUESTS
        try{
            PartyOutput partyOutput = partyClientService.getById(candidateInput.getPartyId());
            if (!candidateInput.getNumberElection().toString().startsWith(partyOutput.getNumber().toString())){
                throw new GenericOutputException("Number doesn't belong to party");
            }
        } catch (FeignException e){
            if (e.status() == 500) {
                throw new GenericOutputException("Invalid Party");
            }
        }

        try {
            electionClientService.getById(candidateInput.getElectionId());
        } catch (FeignException e){
            if (e.status() == 500) {
                throw new GenericOutputException(MESSAGE_INVALID_ELECTION_ID);
            }
        }
    }

}
