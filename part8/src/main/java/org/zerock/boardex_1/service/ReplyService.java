package org.zerock.boardex_1.service;

import org.zerock.boardex_1.dto.PageRequestDTO;
import org.zerock.boardex_1.dto.PageResponseDTO;
import org.zerock.boardex_1.dto.ReplyDTO;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);

    ReplyDTO read(Long rno);

    void modify(ReplyDTO replyDTO);

    void remove(Long rno);

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequest);

}
