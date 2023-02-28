package com.ezkorea.hybrid_app.domain.expenses;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ExpensesRepository extends JpaRepository<Expenses, Long> {

    Page<Expenses> findAllByMemberOrderByPayDateDesc(Member member, Pageable pageable);

    Page<Expenses> findAllByOrderByPayDateDesc(Pageable pageable);
}
