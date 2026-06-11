package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.social.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
