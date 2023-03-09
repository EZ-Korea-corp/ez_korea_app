package com.ezkorea.hybrid_app.service.etc;

import com.ezkorea.hybrid_app.domain.etc.msgToCeo.MsgToCeo;
import com.ezkorea.hybrid_app.domain.etc.msgToCeo.MsgToCeoRepository;
import com.ezkorea.hybrid_app.domain.myBatis.SaleMbRepository;
import com.ezkorea.hybrid_app.domain.user.member.Member;
import com.ezkorea.hybrid_app.domain.wiper.Wiper;
import com.ezkorea.hybrid_app.web.exception.IdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class EtcService {

    private final MsgToCeoRepository msgToCeoRepository;

    @Transactional
    public void saveMsgToCeo(Map<String, String> data, Member member) {

        MsgToCeo msgToCeo = MsgToCeo.builder()
                .title(data.get("title"))
                .content(data.get("content"))
                .member(member)
                .isAnonymous(Boolean.parseBoolean(data.get("isAnonymous")))
                .build();

        msgToCeoRepository.save(msgToCeo);
    }

    public Page<MsgToCeo> msgToCeoList(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        return msgToCeoRepository.findAllByOrderByCreateDateDesc(pageable);
    }

    public void deleteMsgToCeo(Long id) {
        msgToCeoRepository.deleteById(id);
    }

    public MsgToCeo findMsgToCeoReg(Long id) {
        return msgToCeoRepository.findById(id).orElseThrow(() -> new IdNotFoundException("해당글은 삭제된 글입니다"));
    }
}
