package com.beehyv.backend.repositories;

import com.beehyv.backend.models.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaptchaRepo extends JpaRepository<Captcha, Integer> {
}
