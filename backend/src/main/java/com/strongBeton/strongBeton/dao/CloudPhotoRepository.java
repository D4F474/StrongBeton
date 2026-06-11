package com.strongBeton.strongBeton.dao;

import com.strongBeton.strongBeton.entity.CloudPhoto;
import com.strongBeton.strongBeton.enums.PhotoType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CloudPhotoRepository extends JpaRepository<CloudPhoto, Integer> {

    @Query(
            value = "SELECT * FROM photos WHERE user_id = :userId AND photo_type = :photoType",
            nativeQuery = true
    )
    Optional<CloudPhoto> findByUserIdAndPhotoType(@Param("userId") int userId,
                                                    @Param("photoType") String photoType);
}
