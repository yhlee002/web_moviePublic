package com.portfolio.demo.project.controller;

import com.portfolio.demo.project.service.CommentImpService;
import com.portfolio.demo.project.service.CommentMovService;
import com.portfolio.demo.project.dto.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentApi {

    private final CommentMovService commentMovService;

    private final CommentImpService commentImpService;

    /**
     * 댓글을 작성일자 내림차순으로 조회
     *
     * @param page
     * @param movieNo
     * @return
     */
    @GetMapping("/comment/movie")
    public ResponseEntity<CommentMovPagenationParam> getCommentMovs(
            @RequestParam("movieNo") Long movieNo,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        CommentMovPagenationParam vos = commentMovService.getCommentsByMovie(movieNo, page, size);
        return new ResponseEntity<>(vos, HttpStatus.OK);
    }

    /**
     * 댓글 작성
     *
     * @param request
     */
    @PostMapping("/comment/movie")
    public ResponseEntity<String> createCommentMov(CreateCommentMovRequest request) {
        commentMovService.updateComment(
                CommentMovParam.builder()
                        .movieNo(request.getMovieNo())
                        .content(request.getContent())
                        .writerId(request.getWriterId())
                        .rating(request.getRating())
                        .build()
        );

        return new ResponseEntity<>("success", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 댓글 수정
     *
     * @param request
     */
    @PatchMapping("/comment/movie")
    public ResponseEntity<String> updateCommentMov(UpdateCommentMovRequest request) {
        CommentMovParam commentParam = CommentMovParam.builder()
                .id(request.getCommentId())
                .content(request.getContent())
                .rating(request.getRating())
                .build();

        commentMovService.updateComment(commentParam);

        return new ResponseEntity<>("success", HttpStatus.OK);
    }


    /**
     * 댓글 삭제
     *
     * @param commentId
     * @return
     */
    @DeleteMapping("/comment/movie")
    public String deleteCommentMov(Long commentId) {
        commentMovService.deleteCommentById(commentId);

        return "success";
    }

    /**
     * @param memNo
     * @deprecated 추후 Vue.js 등으로 변경시 프론트에서 모두 처리하도록 변경시 삭제 예정
     * 사용자 식별번호를 이용해 사용자가 입력한 댓글을 식별하기 위한 메서드
     */
    @GetMapping("/comment/movie/checkMemNo")
    public ResponseEntity<List<CommentMovParam>> getCommentMovsByMemNo(
            @RequestParam(name = "memNo") Long memNo,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        List<CommentMovParam> commList = commentMovService.getCommentsByMember(memNo, page, size);

        log.info("조회된 댓글 수 : {}", commList);

        return new ResponseEntity<>(commList, HttpStatus.OK);
    }

    // ------------ 영화 감상 후기 게시판 댓글 ------------ //

    /**
     * 해당 글에 대한 전체 댓글 조회
     *
     * @param boardId
     * @param page
     */
    @GetMapping("/comments/imp")
    public ResponseEntity<CommentImpPagenationParam> getCommentImpsByBoard(
            @RequestParam(name = "boardId") Long boardId,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        CommentImpPagenationParam vo = commentImpService.getCommentsByBoard(boardId, page, size);
        return new ResponseEntity<>(vo, HttpStatus.OK);
    }

    /**
     * 댓글 작성
     *
     * @param request
     */
    @PostMapping("/comment/imp")
    public void writeCommentImp(CreateCommentImpRequest request) {
        commentImpService.updateComment(
                CommentImpParam.builder()
                        .content(request.getContent())
                        .boardId(request.getBoardId())
                        .writerId(request.getWriterId())
                        .build());
    }

    /**
     * 댓글 수정
     *
     * @param request
     */
    @PatchMapping("/comment/imp")
    public void updateCommentImp(@RequestBody UpdateCommentImpRequest request) {
        CommentImpParam comm = CommentImpParam.builder()
                .id(request.getCommentId())
                .writerId(request.getWriterId())
                .content(request.getContent())
                .boardId(request.getBoardId())
                .build();
        commentImpService.updateComment(comm);
    }

    /**
     * 댓글 삭제
     *
     * @param commentId
     */
    @DeleteMapping("/comment/imp")
    public String deleteCommentImp(Long commentId) {
        commentImpService.deleteCommentById(commentId);

        return "success";
    }

    @GetMapping("/comment/imp/checkMemNo")
    public List<CommentImpParam> getCommentListByMemNo(
            @RequestParam(name = "memNo") Long memNo,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size
    ) {
        return commentImpService.getCommentsByMember(memNo, page, size);
    }

    @Data
    static class CreateCommentMovRequest {
        private Long writerId;
        private Long movieNo;
        private String content;
        private Integer rating;
    }

    @Data
    static class UpdateCommentMovRequest {
        private Long writerId;
        private Long movieNo;
        private Long commentId;
        private String content;
        private Integer rating;
    }

    @Data
    static class CreateCommentImpRequest {
        private Long writerId;
        private Long boardId;
        private String content;
    }

    @Data
    static class UpdateCommentImpRequest {
        private Long writerId;
        private Long boardId;
        private Long commentId;
        private String content;

    }
}
