package ssafy.journeymate.categoryservice.categoryservice.service;

import java.util.List;
import ssafy.journeymate.categoryservice.categoryservice.dto.request.CategoryRegistPostReq;
import ssafy.journeymate.categoryservice.categoryservice.dto.request.CategoryModifyPutReq;
import ssafy.journeymate.categoryservice.categoryservice.dto.request.ItemModifyPutReq;
import ssafy.journeymate.categoryservice.categoryservice.dto.request.ItemRegistPostReq;
import ssafy.journeymate.categoryservice.categoryservice.dto.response.CategoryGetRes;
import ssafy.journeymate.categoryservice.categoryservice.dto.response.ItemGetRes;

public interface CategoryService {

    CategoryGetRes findCategoryById(Long id);

    CategoryGetRes findCategoryByName(String name);

    CategoryGetRes registCategory(CategoryRegistPostReq categoryRegistPostReq);

    boolean modifyCategory(CategoryModifyPutReq categoryUpdatePutReq);

    ItemGetRes findItemById(Long id);

    ItemGetRes registItem(ItemRegistPostReq itemRegistPostReq);

    boolean modifyItem(ItemModifyPutReq itemModifyPutReq);

    List<ItemGetRes> findItemsByCategory(Long categoryId);


}
