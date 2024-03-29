package com.portfolio.demo.project.controller;

import com.portfolio.demo.project.entity.comment.CommentMov;
import com.portfolio.demo.project.service.CommentImpService;
import com.portfolio.demo.project.service.CommentMovService;
import com.portfolio.demo.project.vo.CommentImpVO;
import com.portfolio.demo.project.vo.CommentMovVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class CommentController {

    @Autowired
    CommentMovService commentMovService;

    @Autowired
    CommentImpService commentImpService;

    @RequestMapping("/movieInfo/comment/write")
    public String writeCommentMovieInfo(String commentContent, Long memNo, Long movieNo, int rating) {
        CommentMov comment = commentMovService.saveComment(memNo, commentContent, movieNo, rating);
        if (comment != null) {
            return "writeSuccess";
        }

        return "writerFail";
    }

    @RequestMapping("/movieInfo/comment/regDt") // getCommentListOrderByRegDt
    public Map<String, Object> getCommentList(@RequestParam(name = "p") int pageNum, Long movieCd) {
        Map<String, Object> map = commentMovService.getCommentListVOOrderByRegDate(pageNum, movieCd);

        return map;
    }

    @RequestMapping("/movieInfo/comment/checkMemNo")
    public List<CommentMovVO> getMovieCommentListByMemNo(Long memNo) {
        log.info("들어온 memNo : " + memNo);
        List<CommentMovVO> commList = commentMovService.getCommentVOListByMemNo(memNo);
        log.info("반환될 commList : " + commList);

        return commList;
    }

    // 댓글 수정
    @RequestMapping("/movieInfo/comment/update")
    public String updateCommentMov(String content, Long commentId) {
        if (commentMovService.updateMovComment(commentId, content) != null) {
            return "success";
        }
        return "false";
    }


    // 댓글 삭제
    @RequestMapping("/movieInfo/comment/delete")
    public String deleteCommentMov(Long commentId) {
        commentMovService.deleteMovComment(commentId);

        return "success";
    }

    /**
     * 영화 감상 후기 게시판 댓글
     */
    // 댓글 작성
    @RequestMapping("/imp/comment/write")
    public void writeCommentImp(String content, Long boardId, Long memNo) {
        commentImpService.saveComment(content, boardId, memNo);
    }

    // 댓글 수정
    @RequestMapping("/imp/comment/update")
    public String updateCommentImp(String content, Long commentId) {
        if (commentImpService.updateComment(commentId, content) != null) {
            return "success";
        }
        return "false";
    }

    @RequestMapping("/imp/comment/delete")
    public String deleteCommentImp(Long commentId) {
        commentImpService.deleteComment(commentId);

        return "success";
    }


    // 해당 글에 대한 전체 댓글 조회
    @RequestMapping("/imp/comment/list")
    public List<CommentImpVO> getCommentList(Long boardId) {
        List<CommentImpVO> impList = commentImpService.getCommentVOList(boardId);

        return impList;
    }

    @RequestMapping("/imp/comment/checkMemNo")
    public List<CommentImpVO> getCommentListByMemNo(Long memNo) {
        List<CommentImpVO> impList = commentImpService.getCommentVOListByMemNo(memNo);

        return impList;
    }


}
