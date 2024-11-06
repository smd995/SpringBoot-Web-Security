package org.zerock.boardex_1.service;

import org.zerock.boardex_1.dto.BoardDTO;
//import org.zerock.boardex_1.dto.BoardListReplyCountDTO;
import org.zerock.boardex_1.dto.BoardListReplyCountDTO;
import org.zerock.boardex_1.dto.PageRequestDTO;
import org.zerock.boardex_1.dto.PageResponseDTO;

public interface BoardService {

    Long register(BoardDTO boardDTO);

    BoardDTO readOne(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);

    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);

    PageResponseDTO<BoardListReplyCountDTO> listWithReplyCount(PageRequestDTO pageRequestDTO);
}
