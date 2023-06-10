package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    // ItemRepository 경우 싱글톤으로 생성되기 때문에
    // 실무에서는 멀티스레드 일때 'store' 값 여러개 동시에 접근시 Race condition 발생된다.
    // ConcurrentHashMap 을 사용해야 한다!
    private static final Map<Long, Item> store = new HashMap<>(); // static

    // 이것도! Atomic 으로 대체 하자!
    private static long sequence = 0L; // static

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);

        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    // 정성적으로 하자면 2번째 파라미터 Item 보다 ItemDto 를 만들어서 관리하자!
    public void update(Long itemId, Item updateParam) {

        Item findItem = findById(itemId);

        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }
}
