package br.edu.ulbra.election.candidate.model;

import br.edu.ulbra.election.candidate.output.v1.CandidateOutput;
import br.edu.ulbra.election.candidate.output.v1.ElectionOutput;
import br.edu.ulbra.election.candidate.output.v1.PartyOutput;

import javax.persistence.*;

@Entity
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "party_id")
    private Long partyId;

    @Column(nullable = false, name = "number")
    private Long numberElection;

    @Column(nullable = false, name = "election_id")
    private Long electionId;

    public Long getId() {
        return id;
    }

    public void setId(Long ids) {
        this.id = ids;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public Long getNumberElection() {
        return numberElection;
    }

    public void setNumberElection(Long numberElection) {
        this.numberElection = numberElection;
    }

    public Long getElectionId() {
        return electionId;
    }

    public static CandidateOutput toCandidateOutput(Candidate candidate) {
        ElectionOutput el = new ElectionOutput();
        PartyOutput pa = new PartyOutput();

        el.setId(candidate.getId());
        pa.setId(candidate.getId());

        CandidateOutput candidateOutput = new CandidateOutput();

        candidateOutput.setId(candidate.getId());
        candidateOutput.setName(candidate.getName());
        candidateOutput.setNumberElection(candidate.getNumberElection());
        candidateOutput.setElectionOutput(el);
        candidateOutput.setPartyOutput(pa);

        return candidateOutput;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }
}
