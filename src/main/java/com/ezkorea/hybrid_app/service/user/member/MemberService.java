package com.ezkorea.hybrid_app.service.user.member;

import com.ezkorea.hybrid_app.domain.aws.S3Image;
import com.ezkorea.hybrid_app.domain.aws.S3ImageRepository;
import com.ezkorea.hybrid_app.domain.user.commute.CommuteTimeRepository;
import com.ezkorea.hybrid_app.domain.user.division.Division;
import com.ezkorea.hybrid_app.domain.user.division.DivisionRepository;
import com.ezkorea.hybrid_app.domain.user.member.*;
import com.ezkorea.hybrid_app.domain.user.team.Team;
import com.ezkorea.hybrid_app.domain.user.team.TeamRepository;
import com.ezkorea.hybrid_app.service.user.commute.CommuteService;
import com.ezkorea.hybrid_app.service.user.division.DivisionService;
import com.ezkorea.hybrid_app.web.dto.FindPasswordDto;
import com.ezkorea.hybrid_app.web.dto.ProfileDto;
import com.ezkorea.hybrid_app.web.dto.SignUpDto;
import com.ezkorea.hybrid_app.web.exception.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final CommuteTimeRepository ctRepository;
    private final S3ImageRepository s3Repository;
    private final SubAuthRepository saRepository;
    private final DivisionRepository divisionRepository;

    private final CommuteService commuteService;

    /**
     * 회원가입을 하기 위한 메소드
     * @param dto sign-up.html에서 받아온 정보
     */
    @Transactional
    public void saveNewMember(SignUpDto dto) {
        dto.setPassword(passwordEncode(dto.getPassword()));
        boolean postAuth = false;
        boolean inputAuth = false;
        if (dto.getUsername().equals("master") || dto.getUsername().equals("dev")) {
            dto.setMemberStatus(MemberStatus.FULL_TIME);
            postAuth = true;
            inputAuth = true;
        }
        Member savedMember = memberRepository.save(Member.builder()
                .username(dto.getUsername())
                .password(dto.getPassword())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .sex(dto.getSex())
                .name(dto.getName())
                .role(dto.getRole())
                .memberStatus(dto.getMemberStatus())
                .commuteTimeList(new ArrayList<>())
                .noticeList(new ArrayList<>())
                .build());
//        Member savedMember = memberRepository.save(mapper.map(dto, Member.class));
        savedMember.setSubAuth(SubAuth.builder()
                .member(savedMember)
                .postAuth(postAuth)
                .inputAuth(inputAuth)
                .build());

        savedMember.setS3Image(s3Repository.save(S3Image.builder()
                .fileName("profile-image.jpg")
                .member(savedMember)
                .fileRepo("images/static/")
                .filePath("https://ezkorea-bucket.s3.ap-northeast-2.amazonaws.com/images/static/profile-image.jpg")
                .build()));
    }

    /**
     * bcrypt로 인코딩하기 위한 메소드
     * @param password 인코딩 되지 않은 password
     * @return 인코딩 된 password
     */
    public String passwordEncode(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * username을 통해 정확한 Member를 찾기 위한 메소드
     * @param username 검색할 username
     * @exception UsernameNotFoundException 유저가 없을 시 발생
     * @return username에 맞는 Member 객체
     */
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow( () -> new UsernameNotFoundException("해당되는 유저가 없습니다."));
    }

    /**
     * email을 통해 정확한 Member를 찾기 위한 메소드
     * @param email 검색할 email
     * @exception UsernameNotFoundException 유저가 없을 시 발생
     * @return email에 맞는 Member 객체
     */
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow( () -> new UsernameNotFoundException("해당되는 유저가 없습니다."));
    }

    /**
     * id를 통해 Member를 찾는 메소드
     * @param id 검색할 id
     * @exception IdNotFoundException 유저가 없을 시 발생
     * @return id에 맞는 Member 객체
     * */
    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow( () -> new IdNotFoundException(id + "를 찾을 수 없습니다."));
    }

    /**
     * 회원 정보 갱신을 위한 메소드
     * @param member 현재 로그인된 Member
     * */
    @Transactional
    public void forceAuthentication(Member member) {
        member.setRoleChanged(false);
        SecurityUser securityUser = new SecurityUser(member, makeMemberAuthority(member));

        UsernamePasswordAuthenticationToken authentication =
                UsernamePasswordAuthenticationToken.authenticated(
                        securityUser,
                        null,
                        securityUser.getAuthorities()
                );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    @Transactional
    public List<GrantedAuthority> makeMemberAuthority(Member member) {
        Role memberRole = member.getRole();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (memberRole.equals(Role.ROLE_MASTER)) {
            authorities.add(new SimpleGrantedAuthority(Role.ROLE_MASTER.toString()));
            authorities.add(new SimpleGrantedAuthority(Role.ROLE_DIRECTOR.toString()));
            authorities.add(new SimpleGrantedAuthority(Role.ROLE_GM.toString()));
            authorities.add(new SimpleGrantedAuthority(Role.ROLE_MANAGER.toString()));
            authorities.add(new SimpleGrantedAuthority(Role.ROLE_LEADER.toString()));
        } else if (memberRole.equals(Role.ROLE_DIRECTOR)) {
            authorities.add(new SimpleGrantedAuthority(Role.ROLE_DIRECTOR.toString()));
            authorities.add(new SimpleGrantedAuthority(Role.ROLE_GM.toString()));
            authorities.add(new SimpleGrantedAuthority(Role.ROLE_MANAGER.toString()));
            authorities.add(new SimpleGrantedAuthority(Role.ROLE_LEADER.toString()));
        } else if (memberRole.equals(Role.ROLE_MANAGER)) {
            authorities.add(new SimpleGrantedAuthority(Role.ROLE_MANAGER.toString()));
        } else if (memberRole.equals(Role.ROLE_GM)) {
            authorities.add(new SimpleGrantedAuthority(Role.ROLE_GM.toString()));
        } else if (memberRole.equals(Role.ROLE_LEADER)) {
            authorities.add(new SimpleGrantedAuthority(Role.ROLE_LEADER.toString()));
        }
        authorities.add(new SimpleGrantedAuthority(Role.ROLE_EMPLOYEE.toString()));
        return authorities;
    }

    @Transactional
    public void updateMemberRole(String username, Role role, MemberStatus status) {
        Member currentMember = findByUsername(username);
        /*if (role.equals(Role.ROLE_LEADER) || role.equals(Role.ROLE_EMPLOYEE)) {
            memberTeamReset(currentMember, role);
        }*/
        currentMember.setRole(role);
        currentMember.setRoleChanged(true);
        currentMember.setMemberStatus(status);
        memberRepository.save(currentMember);
    }

    /*@Transactional
    public void memberTeamReset(Member member, Role role) {
        // 팀이 존재하는지 확인
        if (member.getTeam() != null) {
            Team currentTeam = teamRepository.findById(member.getTeam().getId()).get();

            // 변경될 권한이 리더일 경우
            if (role.equals(Role.ROLE_LEADER)) {
                // 현재 팀 멤버 리스트에 포함되어 있는지 확인
                if (currentTeam.getMemberList().contains(member)) {
                    log.info("꼴에 팀원");
                    List<Member> memberList = currentTeam.getMemberList();
                    memberList.remove(member);
                    currentTeam.setMemberList(memberList);
                    teamRepository.save(currentTeam);
                }
            } else if (role.equals(Role.ROLE_EMPLOYEE)) {
                // 변경될 권한이 사원일 경우 리더인지 확인
                if (teamRepository.existsByLeader(member)) {
                    log.info("꼴에 팀장");
                    Team memberLeaderTeam = teamRepository.findByLeader(member);
                    memberLeaderTeam.setLeader(null);
                    teamRepository.save(memberLeaderTeam);
                }
            }
        }
    }*/

    /**
     * 오늘 출근했는지 확인하는 메소드
     * @param member 현재 로그인한 멤버
     * @return 출근 여부
     */
    public boolean isOnTime(Member member) {
        return ctRepository.existsByDateAndMember(LocalDate.now(), member);
    }

    @Transactional
    public void setCommuteTime(Member member, String status, String currentLocation) {
        Member currentMember = findByUsername(member.getUsername());
        forceAuthentication(memberRepository
                .save(commuteService
                        .saveCommuteTime(currentMember, status, currentLocation)
                ));
    }

    public List<Member> findByRole(Role role) {
        return memberRepository.findAllByRole(role);
    }

    /* 권한에 맞는 회원 찾기 */

    public List<Member> findAllByRoleAndMemberStatus(Role role, MemberStatus status) {
        return memberRepository.findAllByRoleAndMemberStatus(role, status);
    }

    public List<Member> findByRoleAndDivisionIsNull(Role role, MemberStatus status) {
        return memberRepository.findAllByRoleAndMemberStatusAndDivisionIsNull(role, status);
    }

    public List<Member> findAllByRoleAndTeamIsNullOrTeam(Role role, Team team, MemberStatus status) {
        return memberRepository.findAllByRoleAndTeamIsNullOrTeam(role, team, status);
    }

    public List<Member> findByRoleAndStatus(Role role, MemberStatus status) {
        return memberRepository.findAllByRoleAndMemberStatus(role, status);
    }

    public List<Member> findByRoleAndStatusAndTeamIsNull(Role role, MemberStatus status) {
        return memberRepository.findAllByRoleAndMemberStatusAndTeamIsNull(role, status);
    }

    public List<Member> findAllMember() {
        return memberRepository.findAll();
    }

    public List<Member> findAllMemberExcludeStatus(MemberStatus status) {
        return memberRepository.findAllByMemberStatusNot(status);
    }

    public boolean updateMemberPassword(ProfileDto dto, Member member) {
        if (passwordEncoder.matches(dto.getOriginPassword(), findMemberById(member.getId()).getPassword())) {
            member.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            forceAuthentication(member);
            memberRepository.save(member);
            return true;
        }
        return false;
    }

    public boolean existsMemberByUsername(String checkUsername) {
        return memberRepository.existsByUsername(checkUsername);
    }

    public boolean existsMemberByEmailAndPhone(FindPasswordDto dto) {
        return memberRepository.existsByEmailAndPhone(dto.getEmail(), dto.getPhone());
    }

    public UUID makeNewUUID() {
        return UUID.randomUUID();
    }

    @Transactional
    public void sendTempPassword(FindPasswordDto dto) {
        Member currentMember = findByEmail(dto.getEmail());
//        String tempPassword = makeNewUUID().toString().substring(0, 8);
        String tempPassword = "1234";
        currentMember.setPassword(passwordEncoder.encode(tempPassword));
    }

    public List<Member> findAllMemberByStatus(MemberStatus status) {
        return memberRepository.findAllByMemberStatus(status);
    }

    @Transactional
    public void updateMemberStatus(Long id, MemberStatus status) {
        Member currentMember = findMemberById(id);
        currentMember.setMemberStatus(status);
    }

    @Transactional
    public void updateMemberSubAuth(String username, String memberPostAuth, String memberInputAuth) {
        Member currentMember = findByUsername(username);

        boolean postAuth = memberPostAuth != null;
        boolean inputAuth = memberInputAuth != null;

        SubAuth subAuth = saRepository.findByMember(currentMember);
        subAuth.setInputAuth(inputAuth);
        subAuth.setPostAuth(postAuth);
    }

    public List<Member> findByRoleAndDivisionAndDivisionNull(Role role, MemberStatus status, Division division) {
        return memberRepository.findByRoleAndDivisionAndDivisionAndDivisionNull(role, status, division);
    }

    @Transactional
    public void updateMemberInfo(Member member, ProfileDto dto) {
        Member contextMember = findByUsername(member.getUsername());
        contextMember.setMemberBasicInfo(
                dto.getName(), dto.getEmail(), dto.getPhone()
        );
    }

    public String makeDivisionName(Member currentMember) {
        StringBuilder name = new StringBuilder();
        switch (currentMember.getRole()) {
            case ROLE_MASTER, ROLE_DIRECTOR -> {
                name.append("경영진");
            }
            case ROLE_MANAGER -> {
                name.append("경리팀");
            }
            case ROLE_GM -> {
                List<Division> divisionList = divisionRepository.findAllByLeader(currentMember);
                for (Division division : divisionList) {
                    name.append(division.getDivisionName()).append(", ");
                }
                name.delete(name.length() - 2, name.length());
            }
            case ROLE_LEADER -> {
                if (teamRepository.existsByLeader(currentMember)) {
                    name.append(teamRepository.findByLeader(currentMember).getTeamName());
                } else {
                    name.append("소속 없음");
                }
            }
            case ROLE_EMPLOYEE -> {
                for (Team team : teamRepository.findAll()) {
                    if (team.getMemberList().contains(currentMember)) {
                        name.append(team.getTeamName());
                    }
                }
            }
        };
        return name.toString();
    }
}
