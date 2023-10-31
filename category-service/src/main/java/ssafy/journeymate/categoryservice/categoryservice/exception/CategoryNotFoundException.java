package ssafy.journeymate.categoryservice.categoryservice.exception;

public class CategoryNotFoundException extends BaseException {

    public CategoryNotFoundException() {
        super(ErrorCode.CATEGORY_NOT_FOUND);
    }
}
