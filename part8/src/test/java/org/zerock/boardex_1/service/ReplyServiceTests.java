package org.zerock.boardex_1.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.boardex_1.dto.PageRequestDTO;
import org.zerock.boardex_1.dto.PageResponseDTO;
import org.zerock.boardex_1.dto.ReplyDTO;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;

    @Test
    public void testRegister() {

        ReplyDTO replyDTO = ReplyDTO.builder()
                .replyText("ReplyDTO Text")
                .replyer("replyer")
                .bno(99L)
                .build();

        log.info(replyService.register(replyDTO));
    }

    @Test
    public void testGetByRno() {
        Long rno = 2L;

        ReplyDTO replyDTO = replyService.read(rno);

        log.info(replyDTO);
    }

    @Test
    public void testUpdate() {
        Long rno = 2L;

        ReplyDTO replyDTO = ReplyDTO.builder()
                .rno(rno)
                .replyText("Update Reply Text")
                .build();

        replyService.modify(replyDTO);

    }

    @Test
    public void testGetList() {

        Long bno = 99L;
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .link(null)
                .build();

        PageResponseDTO<ReplyDTO> listOfBoard = replyService.getListOfBoard(bno, pageRequestDTO);

        log.info(listOfBoard);
    }

}
