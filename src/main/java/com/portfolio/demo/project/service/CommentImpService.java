package com.portfolio.demo.project.service;

import com.portfolio.demo.project.entity.board.BoardImp;
import com.portfolio.demo.project.entity.comment.CommentImp;
import com.portfolio.demo.project.entity.member.Member;
import com.portfolio.demo.project.repository.BoardImpRepository;
import com.portfolio.demo.project.repository.CommentImpRepository;
import com.portfolio.demo.project.repository.MemberRepository;
import com.portfolio.demo.project.vo.CommentImpPagenationVO;
import com.portfolio.demo.project.vo.CommentImpVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CommentImpService {

    private final CommentImpRepository commentImpRepository;

    private final BoardImpRepository boardImpRepository;

    private final MemberRepository memberRepository;

    public CommentImpVO getCommentById(Long id) {
        CommentImp com = commentImpRepository.findById(id).orElse(null);

        CommentImpVO vo = null;

        if (com != null) {
            vo = CommentImpVO.create(com);

        } else {
            log.error("해당 아이디의 게시글 정보가 존재하지 않습니다.");
//            throw new IllegalStateException("해당 아이디의 게시글 정보가 존재하지 않습니다.");
        }

        return vo;
    }

    public CommentImpVO updateComment(CommentImpVO comment) {
        Member writer = memberRepository.findById(comment.getWriterId()).orElse(null);
        BoardImp board = boardImpRepository.findById(comment.getBoardId()).orElse(null);

        CommentImp result = commentImpRepository.save(
                CommentImp.builder()
                        .id(comment.getId())
                        .content(comment.getContent())
                        .board(board)
                        .writer(writer)
                        .build()
        );

        return CommentImpVO.create(result);
    }

    public void deleteCommentById(Long commentId) {
        Optional<CommentImp> comm = commentImpRepository.findById(commentId);
        comm.ifPresent(commentImpRepository::delete);
    }

    public CommentImpPagenationVO getCommentsByBoard(Long boardId, int page, int size) {
        BoardImp b = boardImpRepository.findById(boardId).orElse(null);

        List<CommentImpVO> vos = null;
        CommentImpPagenationVO result = null;
        if (b != null) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("regDate").descending());
            Page<CommentImp> pages = commentImpRepository.findAllByBoard(b, pageable);
            List<CommentImp> list = pages.getContent();

            log.info("조회된 댓글 수 : {}", list.size());

            vos = list.stream().map(CommentImpVO::create).toList();

            result = CommentImpPagenationVO.builder()
                    .commentImpsList(vos)
                    .totalPageCnt(pages.getTotalPages())
                    .build();
        } else {
            result = CommentImpPagenationVO.builder()
                    .commentImpsList(new ArrayList<>())
                    .totalPageCnt(0)
                    .build();

            log.error("해당 아이디의 게시글 정보가 존재하지 않습니다.");
//            throw new IllegalStateException("해당 아이디의 게시글 정보가 존재하지 않습니다.");
        }

        return result;
    }

    public List<CommentImpVO> getCommentsByMember(Long memNo, int page, int size) {
        Member mem = memberRepository.findById(memNo).orElse(null);

        List<CommentImpVO> vos = new ArrayList<>();
        if (mem != null) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("regDate").descending());
            Page<CommentImp> result = commentImpRepository.findAllByWriter(mem, pageable);
            List<CommentImp> list = result.getContent();

            vos = list.stream().map(CommentImpVO::create).toList();
        } else {
            log.error("해당 아이디의 회원 정보가 존재하지 않습니다.");
            throw new IllegalStateException("해당 아이디의 회원 정보가 존재하지 않습니다.");
        }

        return vos;
    }
}
