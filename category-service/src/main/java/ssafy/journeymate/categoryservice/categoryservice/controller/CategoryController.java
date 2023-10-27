package ssafy.journeymate.categoryservice.categoryservice.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ssafy.journeymate.categoryservice.categoryservice.dto.ResponseDto;
import ssafy.journeymate.categoryservice.categoryservice.service.CategoryService;

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
    public String status(){

        return String.format("It's Working in User Service"
                +", port(local.sever.port)=" + env.getProperty("local.server.port"))
                +", port(sever.port)=" + env.getProperty("server.port")
                +", token secret=" + env.getProperty("token.secret")
                +", token expiration time=" + env.getProperty("token.expiration_time");
    }


    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseDto> getCategory(@PathVariable Long categoryId) {

        return new ResponseEntity<>(new ResponseDto("카테고리 정보 조회 완료", null), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}/items")
    public ResponseEntity<ResponseDto> getCategoryItems(@PathVariable Long categoryId) {

        return new ResponseEntity<>(new ResponseDto("카테고리 내 아이템 목록 조회 완료", null), HttpStatus.OK);
    }

    @PostMapping("/regist")
    public ResponseEntity<ResponseDto> registCategory() {


        return new ResponseEntity<>(new ResponseDto("카테고리 등록 완료", null), HttpStatus.OK);
    }

    @PostMapping("/additems")
    public ResponseEntity<ResponseDto> registCategoryItem() {


        return new ResponseEntity<>(new ResponseDto("카테고리 아이템 등록 완료", null), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateCategory() {


        return new ResponseEntity<>(new ResponseDto("카테고리 업데이트 완료", null), HttpStatus.OK);
    }

    @PutMapping("/updateitems")
    public ResponseEntity<ResponseDto> updateCategoryItem() {


        return new ResponseEntity<>(new ResponseDto("카테고리 아이템 업데이트 완료", null), HttpStatus.OK);
    }





}
