package br.edu.ulbra.election.candidate.model;

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

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }
}
