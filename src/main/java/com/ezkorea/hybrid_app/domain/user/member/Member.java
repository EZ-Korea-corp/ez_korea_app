package com.ezkorea.hybrid_app.domain.user.member;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.task.DailyTask;
import com.ezkorea.hybrid_app.domain.user.commute.CommuteTime;
import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true, exclude = "commuteTimeList")
public class Member extends BaseEntity {

    @Setter
    @Column(unique = true)
    private String username;

    @Setter
    private String password;

    @Setter
    private String phone;

    @Setter
    private String email;

    @Setter
    private String sex;

    @Setter
    private String name;

    @Setter
    @Column(name = "member_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Setter
    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Setter
    private boolean isRoleChanged;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<CommuteTime> commuteTimeList = new ArrayList<>();

    public void addCommuteTime(CommuteTime time) {
        commuteTimeList.add(time);
    }

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<DailyTask> taskList = new ArrayList<>();

    @ManyToOne
    @Setter
    private Division division;

    @ManyToOne
    @Setter
    private Team team;

}
