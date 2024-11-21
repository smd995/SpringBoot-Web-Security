package org.zerock.boardex_1.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.boardex_1.domain.Board;
import org.zerock.boardex_1.dto.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Log4j2
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

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
                .bno(99L)
                .title("Updated 101")
                .content("Updated Content 101")
                .build();

        boardDTO.setFileNames(Arrays.asList(UUID.randomUUID()+"_zzz.jpg"));

        boardService.modify(boardDTO);
    }

    @Test
    public void testReadAll() {

        Long bno = 99L;

        BoardDTO boardDTO = boardService.readOne(bno);

        log.info(boardDTO);

        for(String fileName : boardDTO.getFileNames()) {
            log.info(fileName);
        }

    }

    @Test
    public void testRemoveAll() {

        Long bno = 99L;

        boardService.remove(bno);

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
    public void testListWithAll() {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<BoardListAllDTO> responseDTO = boardService.listWithAll(pageRequestDTO);

        List<BoardListAllDTO> dtoList = responseDTO.getDtoList();

        dtoList.forEach(boardListAllDTO -> {
            log.info(boardListAllDTO.getBno() + ":" + boardListAllDTO.getTitle());

            if(boardListAllDTO.getBoardImages() != null) {
                for (BoardImageDTO boardImageDTO : boardListAllDTO.getBoardImages() ){
                    log.info(boardImageDTO);
                }
            }

            log.info("--------------------------------------");
        });
    }


}
