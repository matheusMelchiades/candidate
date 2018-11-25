package br.edu.ulbra.election.candidate.repository;

import br.edu.ulbra.election.candidate.model.Candidate;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CandidateRepository extends CrudRepository<Candidate, Long> {
    Optional<Candidate> findByNumberElection(Long numberElection);
    Optional<Candidate> findByNumberElectionAndAndElectionId(Long numberElection, Long electionId);
}
