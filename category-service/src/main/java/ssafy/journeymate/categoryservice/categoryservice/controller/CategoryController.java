package ssafy.journeymate.categoryservice.categoryservice.controller;


import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssafy.journeymate.categoryservice.categoryservice.dto.ResponseDto;
import ssafy.journeymate.categoryservice.categoryservice.dto.request.CategoryModifyPutReq;
import ssafy.journeymate.categoryservice.categoryservice.dto.request.CategoryRegistPostReq;
import ssafy.journeymate.categoryservice.categoryservice.dto.request.ItemModifyPutReq;
import ssafy.journeymate.categoryservice.categoryservice.dto.request.ItemRegistPostReq;
import ssafy.journeymate.categoryservice.categoryservice.dto.response.CategoryGetRes;
import ssafy.journeymate.categoryservice.categoryservice.dto.response.ItemGetRes;
import ssafy.journeymate.categoryservice.categoryservice.service.CategoryService;

@Slf4j
@RestController
@RequestMapping("/category-service")
public class CategoryController {

    private Environment env;
    private CategoryService categoryService;

    @Autowired
    public CategoryController(Environment env, CategoryService categoryService) {
        this.env = env;
        this.categoryService = categoryService;
    }

    /* 테스트용 */
    @GetMapping("/health_check")
    public String status() {

        return "health_check";
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseDto> getCategoryById(@PathVariable Long categoryId) {

        log.info("CategoryController_getCategoryById_start: " + categoryId);

        CategoryGetRes categoryGetRes = categoryService.findCategoryById(categoryId);

        log.info("CategoryController_getCategoryById_end: " + categoryGetRes.toString());

        return new ResponseEntity<>(new ResponseDto("카테고리 정보 조회 완료", categoryGetRes), HttpStatus.OK);
    }

    @GetMapping("/findByName/{categoryName}")
    public ResponseEntity<ResponseDto> getCategoryByName(@PathVariable String categoryName) {

        log.info("CategoryController_getCategoryByName_start: " + categoryName);

        CategoryGetRes categoryGetRes = categoryService.findCategoryByName(categoryName);

        log.info("CategoryController_getCategoryById_end: " + categoryGetRes.toString());

        return new ResponseEntity<>(new ResponseDto("카테고리 정보 조회 완료", categoryGetRes), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/items")
    public ResponseEntity<ResponseDto> getCategoryItems(@PathVariable Long categoryId) {

        log.info("CategoryController_getCategoryItems_start: " + categoryId);

        List<ItemGetRes> items = categoryService.findItemsByCategory(categoryId);

        log.info("CategoryController_getCategoryItems_end: " + items.toString());

        return new ResponseEntity<>(new ResponseDto("카테고리 내 아이템 목록 조회 완료", items), HttpStatus.OK);
    }

    @PostMapping("/regist")
    public ResponseEntity<ResponseDto> registCategory(@RequestBody CategoryRegistPostReq categoryRegistPostReq) {

        log.info("CategoryController_getCategoryItems_start: " + categoryRegistPostReq.toString());

        CategoryGetRes categoryGetRes = categoryService.registCategory(categoryRegistPostReq);

        log.info("CategoryController_getCategoryItems_end: " + categoryGetRes.toString());

        return new ResponseEntity<>(new ResponseDto("카테고리 등록 완료", categoryGetRes), HttpStatus.OK);
    }

    @PostMapping("/additems")
    public ResponseEntity<ResponseDto> registCategoryItem(@RequestBody ItemRegistPostReq itemRegistPostReq) {

        log.info("CategoryController_registCategoryItem_start: " + itemRegistPostReq.toString());

        ItemGetRes itemGetRes = categoryService.registItem(itemRegistPostReq);

        log.info("CategoryController_registCategoryItem_end: " + itemGetRes.toString());

        return new ResponseEntity<>(new ResponseDto("카테고리 아이템 등록 완료", itemGetRes), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCategory(@RequestBody CategoryModifyPutReq categoryModifyPutReq) {

        log.info("CategoryController_updateCategory_start: " + categoryModifyPutReq.toString());

        Boolean check = categoryService.modifyCategory(categoryModifyPutReq);

        log.info("CategoryController_updateCategory_end: " + check);

        return new ResponseEntity<>(new ResponseDto("카테고리 업데이트 완료", check), HttpStatus.OK);
    }

    @PutMapping("/updateitem")
    public ResponseEntity<ResponseDto> updateCategoryItem(@RequestBody ItemModifyPutReq itemModifyPutReq) {

        log.info("CategoryController_updateCategoryItem_start: " + itemModifyPutReq.toString());

        Boolean check = categoryService.modifyItem(itemModifyPutReq);

        log.info("CategoryController_updateCategoryItem_start: " + check);

        return new ResponseEntity<>(new ResponseDto("카테고리 아이템 업데이트 완료", check), HttpStatus.OK);
    }

}
