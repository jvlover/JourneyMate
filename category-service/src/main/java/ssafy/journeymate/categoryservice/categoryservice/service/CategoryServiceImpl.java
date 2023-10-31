package ssafy.journeymate.categoryservice.categoryservice.service;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ssafy.journeymate.categoryservice.categoryservice.dto.request.CategoryModifyPutReq;
import ssafy.journeymate.categoryservice.categoryservice.dto.request.CategoryRegistPostReq;
import ssafy.journeymate.categoryservice.categoryservice.dto.request.ItemModifyPutReq;
import ssafy.journeymate.categoryservice.categoryservice.dto.request.ItemRegistPostReq;
import ssafy.journeymate.categoryservice.categoryservice.dto.response.CategoryGetRes;
import ssafy.journeymate.categoryservice.categoryservice.dto.response.ItemGetRes;
import ssafy.journeymate.categoryservice.categoryservice.entity.Category;
import ssafy.journeymate.categoryservice.categoryservice.entity.Item;
import ssafy.journeymate.categoryservice.categoryservice.exception.CategoryNotFoundException;
import ssafy.journeymate.categoryservice.categoryservice.exception.ItemNotFoundException;
import ssafy.journeymate.categoryservice.categoryservice.repository.CategoryRepository;
import ssafy.journeymate.categoryservice.categoryservice.repository.ItemRepository;


@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ItemRepository itemRepository) {
        this.categoryRepository = categoryRepository;
        this.itemRepository = itemRepository;
    }


    @Override
    public CategoryGetRes findCategoryById(Long categoryId) {

        log.info("CategoryService_findCategoryById_start: " + categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(CategoryNotFoundException::new);

        CategoryGetRes categoryGetRes = new ModelMapper().map(category, CategoryGetRes.class);

        log.info("CategoryService_findCategoryById_end");

        return categoryGetRes;
    }

    @Override
    public CategoryGetRes findCategoryByName(String categoryName) {

        log.info("CategoryService_findCategoryName_start: " + categoryName);

        Category category = categoryRepository.findByName(categoryName)
                .orElseThrow(CategoryNotFoundException::new);

        CategoryGetRes categoryGetRes = new ModelMapper().map(category, CategoryGetRes.class);

        log.info("CategoryService_findCategoryName_end");

        return categoryGetRes;
    }

    @Override
    public CategoryGetRes registCategory(CategoryRegistPostReq categoryRegistPostReq) {

        log.info("CategoryService_registCategory_start: " + categoryRegistPostReq.toString());

        Category category = Category.builder()
                .name(categoryRegistPostReq.getName())
                .icon(categoryRegistPostReq.getIcon()).build();

        categoryRepository.save(category);
        CategoryGetRes categoryGetRes = new ModelMapper().map(category, CategoryGetRes.class);

        log.info("CategoryService_registCategory_end: " + categoryGetRes.toString());

        return categoryGetRes;
    }

    @Override
    public boolean modifyCategory(CategoryModifyPutReq categoryUpdatePutReq) throws CategoryNotFoundException {

        log.info("CategoryService_modifyCategory_start: " + categoryUpdatePutReq.toString());

        Category category = categoryRepository.findById(categoryUpdatePutReq.getId()).orElseThrow();
        category.modifyCategory(categoryUpdatePutReq);
        categoryRepository.save(category);
        log.info("CategoryService_modifyCategory_end: " + categoryUpdatePutReq.toString());

        return true;
    }


    @Override
    public ItemGetRes findItemById(Long id) {

        log.info("CategoryService_findItemById_start: " + id);

        Item item = itemRepository.findById(id)
                .orElseThrow(ItemNotFoundException::new);

        ItemGetRes itemGetRes = new ModelMapper().map(item, ItemGetRes.class);

        log.info("CategoryService_findItemById_end");

        return itemGetRes;
    }

    @Override
    public List<ItemGetRes> findItemsByCategory(Long categoryId) {

        log.info("CategoryService_findItemsByCategory_start: " + categoryId);

        List<Item> items = itemRepository.findByCategory_Id(categoryId);

        List<ItemGetRes> itemGetResponses = new ArrayList<>();

        for (Item item : items) {
            ItemGetRes itemGetRes = new ModelMapper().map(item, ItemGetRes.class);
            itemGetResponses.add(itemGetRes);
        }

        log.info("CategoryService_findItemsByCategory_end");

        return itemGetResponses;
    }


    @Override
    public ItemGetRes registItem(ItemRegistPostReq itemRegistPostReq) {

        log.info("CategoryService_registItem_start: " + itemRegistPostReq.toString());

        Category category = categoryRepository.findById(itemRegistPostReq.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        Item item = Item.builder()
                .category(category)
                .name(itemRegistPostReq.getName())
                .num(itemRegistPostReq.getNum()).build();

        itemRepository.save(item);

        ItemGetRes itemGetRes = new ModelMapper().map(item, ItemGetRes.class);

        log.info("CategoryService_registItem_end: " + itemGetRes.toString());

        return itemGetRes;
    }

    @Override
    public boolean modifyItem(ItemModifyPutReq itemModifyPutReq) {

        log.info("CategoryService_modifyItem_start: " + itemModifyPutReq.toString());

        Item item = itemRepository.findById(itemModifyPutReq.getId()).orElseThrow(ItemNotFoundException::new);
        Category category = categoryRepository.findById(itemModifyPutReq.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);

        item.modify(category, itemModifyPutReq.getName(), itemModifyPutReq.getNum());
        itemRepository.save(item);

        log.info("CategoryService_modifyItem_end: " + itemModifyPutReq.toString());

        return true;
    }


}
