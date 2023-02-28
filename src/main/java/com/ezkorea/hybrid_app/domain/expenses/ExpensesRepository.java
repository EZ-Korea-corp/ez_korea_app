package com.ezkorea.hybrid_app.domain.expenses;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ExpensesRepository extends JpaRepository<Expenses, Long> {

    Page<Expenses> findAllByMemberOrderByPayDateDesc(Member member, Pageable pageable);

    @Query("SELECT e FROM Expenses e ORDER BY e.payDate DESC, e.isManagerCheck ASC")
    Page<Expenses> findAllByOrderByPayDateDescManagerCheckAsc(Pageable pageable);
}
