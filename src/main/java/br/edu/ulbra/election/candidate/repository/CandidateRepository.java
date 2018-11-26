package br.edu.ulbra.election.candidate.repository;

import br.edu.ulbra.election.candidate.model.Candidate;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends CrudRepository<Candidate, Long> {
    List<Candidate> findAllByNumberElection(Long numberElection);
    Optional<Candidate> findByNumberElectionAndAndElectionId(Long numberElection, Long electionId);
}
