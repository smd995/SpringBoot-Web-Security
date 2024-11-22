package org.zerock.boardex_1.service;

import org.zerock.boardex_1.dto.MemberJoinDTO;

public interface MemberService {

    static class MidExistException extends Exception {

    }

    void join (MemberJoinDTO memberJoinDTO) throws MidExistException;

}
