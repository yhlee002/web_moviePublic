package com.portfolio.demo.project.repository;

import com.portfolio.demo.project.entity.board.BoardImp;
import com.portfolio.demo.project.entity.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardImpRepository extends JpaRepository<BoardImp, Long> {

    /**
     * 게시판 식별번호로 조회
     * @param id
     */
    BoardImp findBoardImpById(Long id);

    /**
     * 이전글
     * @param id
     * @return
     */
    @Query(value = "select b.* from board_imp b join Member m on b.writer_no = m.mem_no where b.id = " +
            "(select b.id from board_imp b where b.id < :id order by b.id desc limit 1)"
            , nativeQuery = true)
    BoardImp findPrevBoardImpByBoardId(Long id);

    /**
     * 다음글
     * @param id
     */
    @Query(value = "select b.* from board_imp b join Member m on b.writer_no = m.mem_no where b.id = " +
            "(select b.id from board_imp b where b.id > :id order by b.id asc limit 1)"
            , nativeQuery = true)
    BoardImp findNextBoardImpByBoardId(Long id);

    /**
     * 인기 게시글 top 5 조회
     */
    @Query(value = "select b.*, m.name from board_imp b join member m on b.writer_no=m.mem_no order by b.views desc limit 5", nativeQuery = true)
    List<BoardImp> findTop5ByOrderByViewsDesc();

    /**
     * 작성자명으로 검색 결과 조회
     * @param writerName
     * @param pageable
     * @return
     */

    @Query(value = "select b.* from BoardImp b join Member m on b.writer_no = m.mem_no where m.name like %:writerName%"
        , nativeQuery = true) //  order by b.id desc limit ?2, ?3
    Page<BoardImp> findAllByWriterNameOrderByRegDateDesc(String writerName, Pageable pageable);

    /**
     * 제목 또는 내용으로 검색 결과 조회
     */
    Page<BoardImp> findAllByTitleOrContentContainingOrderByRegDate(String title, String content, Pageable pageable);

    /**
     * 유저가 작성한 게시글 조회
     * ex. 마이페이지 > 자신이 작성한 글 조회
     * @param member
     * @return
     */
    int countBoardImpsByWriter(Member member);

    Page<BoardImp> findAllByWriter(Member member, Pageable pageable);

    /**
     * 자신이 작성한 글 최신순 5개(마이페이지)
     * @param memNo
     */
    List<BoardImp> findTop5ByWriter_MemNoOrderByRegDateDesc(Long memNo);

    /**
     * @deprecated
     * 감상평 게시글 조회
     * @param startRow
     * @param boardCntPerPage
     */
    @Query(value = "select b.* from board_imp b join Member m on b.writer_no = m.mem_no order by b.id desc limit ?1, ?2"
            , nativeQuery = true)
    List<BoardImp> findBoardImpListView(int startRow, int boardCntPerPage);

    /**
     * 게시글 전체 조회
     * @param pageable
     * @return
     */
    Page<BoardImp> findAllByOrderByIdDesc(Pageable pageable);
}
