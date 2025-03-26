package me.hippy.prdsz.dao;

import me.hippy.prdsz.model.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishRepository extends JpaRepository<Wish, Integer> {

    @Query("SELECT w FROM Wish w " +
            "ORDER BY RANDOM() LIMIT 1")
    Wish findRandom();

    List<Wish> findAllByLinkToTelegramPhotoIsNull();
}
