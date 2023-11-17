package ssafy.journeymate.categoryservice.categoryservice.exception;

public class ItemNotFoundException extends BaseException {

    public ItemNotFoundException() {
        super(ErrorCode.ITEM_NOT_FOUND);
    }
}
