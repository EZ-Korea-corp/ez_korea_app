package com.ezkorea.hybrid_app.domain.member;

import com.ezkorea.hybrid_app.domain.base.BaseEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class Member extends BaseEntity {

    @Setter
    private String username;

    @Setter
    private String password;

    @Setter
    private String sex;

    @Setter
    private String name;

}
