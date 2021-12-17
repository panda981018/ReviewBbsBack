package com.back.review.service;

import com.back.review.domain.bbs.BbsEntity;
import com.back.review.dto.MemberDto;
import com.back.review.domain.reply.ReplyEntity;
import com.back.review.domain.bbs.BbsRepository;
import com.back.review.domain.reply.ReplyRepository;
import com.back.review.dto.ReplyDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BbsRepository bbsRepository;

    public void createReply(HttpSession session, ReplyDto replyDto) throws Exception {

        LocalDateTime createTime = LocalDateTime.now();
        String createTimeStr = createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        replyDto.setCreateDate(createTimeStr); // 생성날짜 setting

        MemberDto replyWriter = (MemberDto) session.getAttribute("memberInfo");
        BbsEntity bbsEntity = bbsRepository.findById(replyDto.getBbs())
                .orElseThrow(() -> new Exception("POST NOT EXIST"));
        ReplyEntity replyEntity = replyDto.toEntity();
        replyEntity.setWriter(replyWriter.toEntity());
        replyEntity.setBbs(bbsEntity);

        bbsEntity.addReply(replyEntity);
        replyRepository.saveAndFlush(replyEntity);
        bbsRepository.save(bbsEntity);
    }

    public List<ReplyDto> getReplies(Long bbsId) {
        BbsEntity bbsEntity = bbsRepository.findById(bbsId).isPresent() ? bbsRepository.findById(bbsId).get() : null;

        List<ReplyEntity> replies = bbsEntity.getReplies();
        List<ReplyDto> replyDtoList = new ArrayList<>();

        if (!replies.isEmpty()) {
            for (int i = 0; i < replies.size(); i++) {
                replyDtoList.add(replies.get(i).toDto());
            }
        }
        return replyDtoList;
    }

    public void updateReply(String contents, Long replyId, MemberDto memberDto) {

        Optional<ReplyEntity> reply = replyRepository.findById(replyId);

        if (reply.isPresent()) {
            ReplyEntity oldReply = reply.get(); // 저장되어 있던 reply
            ReplyDto replyDto = oldReply.toDto(); // updateDate와 contents 세팅을 위해서 dto로 만듦

            LocalDateTime updateNow = LocalDateTime.now();
            String updateTimeStr = updateNow.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            replyDto.setUpdateDate(updateTimeStr);
            replyDto.setContents(contents);

            ReplyEntity newEntity = replyDto.toEntity();
            Optional<BbsEntity> bbsEntity = bbsRepository.findById(replyDto.getBbs()); // writer 매핑을 위해
            newEntity.setBbs(bbsEntity.get());
            newEntity.setWriter(memberDto.toEntity());

            replyRepository.save(newEntity);
        }
    }

    public void removeReply(Long replyId, Long bbsId) {
        ReplyEntity reply = replyRepository.findById(replyId).orElse(null);
        BbsEntity bbs = bbsRepository.findById(bbsId).orElse(null);
        bbs.getReplies().remove(reply);
        replyRepository.deleteById(replyId);
    }

//    public List<ReplyDto> removeReply(Long replyId, Long bbsId) {
//        ReplyEntity reply = replyRepository.findById(replyId).orElse(null);
//        BbsEntity bbs = bbsRepository.findById(bbsId).orElse(null);
//        bbs.getReplies().remove(reply);
//        replyRepository.deleteById(replyId);
//        List<ReplyDto> replies = new ArrayList<>();
//
//        for (ReplyEntity entity : bbs.getReplies()) {
//            replies.add(entity.toDto());
//        }
//
//        return replies;
//    }
}
