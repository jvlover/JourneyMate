package ssafy.journeymate.categoryservice.categoryservice.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ssafy.journeymate.categoryservice.categoryservice.entity.Category;
import ssafy.journeymate.categoryservice.categoryservice.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findById(Long id);

    List<Item> findByCategory(Category category);

    List<Item> findByCategory_Id(Long categoryId);

}
