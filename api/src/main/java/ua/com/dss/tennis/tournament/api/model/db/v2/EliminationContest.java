package ua.com.dss.tennis.tournament.api.model.db.v2;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "contest_id")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class EliminationContest extends Contest {

    private Integer firstParentContestId;
    private Integer secondParentContestId;

    @Basic
    @Column(name = "first_parent_contest_id", nullable = false)
    public Integer getFirstParentContestId() {
        return firstParentContestId;
    }

    public void setFirstParentContestId(Integer firstParentContestId) {
        this.firstParentContestId = firstParentContestId;
    }

    @Basic
    @Column(name = "second_parent_contest_id", nullable = false)
    public Integer getSecondParentContestId() {
        return secondParentContestId;
    }

    public void setSecondParentContestId(Integer secondParentContestId) {
        this.secondParentContestId = secondParentContestId;
    }

    @Override
    public String toString() {
        return "SingleContest{" +
                super.toString() +
                "firstParentContestId=" + firstParentContestId +
                ", secondParentContestId=" + secondParentContestId +
                '}';
    }
}
