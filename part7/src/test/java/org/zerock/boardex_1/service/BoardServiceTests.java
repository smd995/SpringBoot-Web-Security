package org.zerock.boardex_1.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.boardex_1.domain.Board;
import org.zerock.boardex_1.dto.BoardDTO;
import org.zerock.boardex_1.dto.BoardListReplyCountDTO;
import org.zerock.boardex_1.dto.PageRequestDTO;
import org.zerock.boardex_1.dto.PageResponseDTO;

import java.util.Arrays;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister() {
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Sample Title")
                .content("Sample Content")
                .writer("user00")
                .build();

        Long bno = boardService.register(boardDTO);

        log.info("bno :" + bno);
    }

    @Test
    public void testRegisterWithImages() {
        log.info(boardService.getClass().getName());

        BoardDTO boardDTO = BoardDTO.builder()
                .title("File.....Sample Title....")
                .content("Sample Content")
                .writer("user00")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID()+"_aaa.jpg",
                        UUID.randomUUID()+"_bbb.jpg",
                        UUID.randomUUID()+"_ccc.jpg"
                ));

        Long bno = boardService.register(boardDTO);

        log.info("bno :" + bno);
    }

    @Test
    public void testModify() {

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(101L)
                .title("Updated 101")
                .content("Updated Content 101")
                .build();

        boardService.modify(boardDTO);
    }

    @Test
    public void testList() {

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .type("tcw")
                .keyword("1")
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardDTO> pageResponseDTO = boardService.list(pageRequestDTO);

        log.info(pageResponseDTO);
    }

    @Test
    public void testListWithReplyCount() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tcw")
                .keyword("9")
                .link(null)
                .build();

        PageResponseDTO<BoardListReplyCountDTO> result = boardService.listWithReplyCount(pageRequestDTO);

        log.info(result);

    }
}
