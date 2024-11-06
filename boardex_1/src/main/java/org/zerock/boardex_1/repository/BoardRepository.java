package org.zerock.boardex_1.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.boardex_1.domain.Board;
import org.zerock.boardex_1.repository.search.BoardSearch;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {
    @Query(value = "select now()", nativeQuery = true)
    String getTime();
}
