package com.ezkorea.hybrid_app.domain.user.member;

import com.ezkorea.hybrid_app.domain.aws.S3Image;
import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import com.ezkorea.hybrid_app.domain.expenses.Expenses;
import com.ezkorea.hybrid_app.domain.notice.Notice;
import com.ezkorea.hybrid_app.domain.user.commute.CommuteTime;
import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.web.dto.MemberDto;
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

    public void setMemberBasicInfo(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    @Setter
    @Column(name = "member_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Setter
    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    @Setter
    private boolean isRoleChanged;

    @Setter
    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL)
    private SubAuth subAuth;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<CommuteTime> commuteTimeList = new ArrayList<>();

    public void addCommuteTime(CommuteTime time) {
        commuteTimeList.add(time);
    }

    @ManyToOne(cascade = CascadeType.ALL)
    @Setter
    private Division division;

    @ManyToOne(cascade = CascadeType.ALL)
    @Setter
    private Team team;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL)
    private List<Notice> noticeList = new ArrayList<>();

    @ManyToOne
    @Setter
    private S3Image s3Image;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Expenses> expensesList = new ArrayList<>();

    public MemberDto of() {
        return MemberDto.builder()
                .id(getId())
                .username(username)
                .name(name)
                .phone(phone)
                .role(role.getViewName())
                .division(division != null ? division.getDivisionName() : "")
                .team(team != null ? team.getTeamName() : "")
                .profileImgUrl(s3Image.getFilePath())
                .build();
    }

}
