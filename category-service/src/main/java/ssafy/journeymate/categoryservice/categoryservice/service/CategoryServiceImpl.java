package ssafy.journeymate.categoryservice.categoryservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ssafy.journeymate.categoryservice.categoryservice.repository.CategoryRepository;
import ssafy.journeymate.categoryservice.categoryservice.repository.ItemRepository;


@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService{

    CategoryRepository categoryRepository;
    ItemRepository itemRepository;



}
