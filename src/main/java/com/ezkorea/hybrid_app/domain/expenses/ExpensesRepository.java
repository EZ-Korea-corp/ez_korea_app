package com.ezkorea.hybrid_app.domain.expenses;

import com.ezkorea.hybrid_app.domain.user.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpensesRepository extends JpaRepository<Expenses, Long> {

    List<Expenses> findAllByMember(Member member);

}
