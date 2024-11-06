package org.zerock.boardex_1.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.boardex_1.domain.Board;
import org.zerock.boardex_1.domain.Reply;
import org.zerock.boardex_1.dto.BoardListReplyCountDTO;
//import org.zerock.boardex_1.dto.BoardListReplyCountDTO;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testInsert() {

        Long bno = 99L;

        Board board = Board.builder().bno(bno).build();

        Reply reply = Reply.builder()
                .board(board)
                .replyText("댓글....")
                .replyer("replyer1")
                .build();

        replyRepository.save(reply);

    }

    @Test
    public void testBoardReplies() {
        Long bno = 99L;

        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());

        Page<Reply> result = replyRepository.listOfBoard(bno, pageable);

        result.getContent().forEach(reply -> log.info(reply));
    }

    @Test
    public void testSearchReplyCount() {
        String[] types = {"t", "c", "w"};

        String keyword = "9";

        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<BoardListReplyCountDTO> result = boardRepository.searchWithReplyCount(types, keyword, pageable);

        log.info(result.getTotalPages());
        log.info(result.getSize());
        log.info(result.getNumber());
        log.info(result.hasPrevious()+ ": " + result.hasNext());

        result.getContent().forEach(board -> log.info(board));
    }

}
